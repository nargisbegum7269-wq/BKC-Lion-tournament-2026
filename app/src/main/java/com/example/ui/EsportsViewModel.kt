package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AdminConfigEntity
import com.example.data.AppDatabase
import com.example.data.EsportsRepository
import com.example.data.MatchEntity
import com.example.data.MatchRegistrationEntity
import com.example.data.ProofEntity
import com.example.data.TransactionEntity
import com.example.data.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val label: String) {
    MATCHES("ম্যাচ"),
    MY_MATCHES("মাই ম্যাচ"),
    WINNING("উইনিং"),
    LEADERBOARD("লিডারবোর্ড"),
    PROFILE("প্রোফাইল")
}

class EsportsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EsportsRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = EsportsRepository(db.appDao())
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    // Active Tab
    private val _currentTab = MutableStateFlow(AppTab.MATCHES)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    fun setTab(tab: AppTab) {
        _currentTab.value = tab
    }

    // User State
    private val _currentUserId = MutableStateFlow<Long?>(null)
    val currentUserId: StateFlow<Long?> = _currentUserId.asStateFlow()

    val currentUser: StateFlow<UserEntity?> = _currentUserId
        .flatMapLatest { id ->
            if (id == null) flowOf(null) else repository.getUser(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Matches
    val allMatches: StateFlow<List<MatchEntity>> = repository.allMatches
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Category Filter
    private val _selectedCategory = MutableStateFlow("ALL")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    // Leaderboard
    val leaderboard: StateFlow<List<UserEntity>> = repository.leaderboard
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin Config
    val adminConfig: StateFlow<AdminConfigEntity?> = repository.adminConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // User Registrations & Transactions
    val userRegistrations: StateFlow<List<MatchRegistrationEntity>> = _currentUserId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else repository.getUserRegistrations(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userTransactions: StateFlow<List<TransactionEntity>> = _currentUserId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else repository.getUserTransactions(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProofs: StateFlow<List<ProofEntity>> = _currentUserId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else repository.getUserProofs(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin Lists
    val pendingDeposits: StateFlow<List<TransactionEntity>> = repository.pendingDeposits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingWithdrawals: StateFlow<List<TransactionEntity>> = repository.pendingWithdrawals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProofs: StateFlow<List<ProofEntity>> = repository.allProofs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI Feedback
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun clearToast() {
        _toastMessage.value = null
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    // Admin login state
    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    fun setAdminLoggedIn(loggedIn: Boolean) {
        _isAdminLoggedIn.value = loggedIn
    }

    // Auto-login demo user on first start if needed
    init {
        viewModelScope.launch {
            // Check if there is an existing user or demo
            val result = repository.login("01700000000", "123")
            result.onSuccess {
                _currentUserId.value = it.id
            }
        }
    }

    // Auth actions
    fun login(phone: String, pass: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val res = repository.login(phone, pass)
            res.fold(
                onSuccess = {
                    _currentUserId.value = it.id
                    _toastMessage.value = "লগইন সফল হয়েছে! স্বাগতম ${it.name}"
                    onSuccess()
                },
                onFailure = {
                    _toastMessage.value = it.message ?: "লগইন ব্যর্থ হয়েছে"
                }
            )
        }
    }

    fun register(name: String, phone: String, ffUid: String, pass: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val res = repository.register(name, phone, ffUid, pass)
            res.fold(
                onSuccess = {
                    _currentUserId.value = it.id
                    _toastMessage.value = "রেজিস্ট্রেশন সফল হয়েছে! স্বাগতম ${it.name}"
                    onSuccess()
                },
                onFailure = {
                    _toastMessage.value = it.message ?: "রেজিস্ট্রেশন ব্যর্থ হয়েছে"
                }
            )
        }
    }

    fun logout() {
        _currentUserId.value = null
        _toastMessage.value = "লগআউট করা হয়েছে"
    }

    // Join Match
    fun joinMatch(match: MatchEntity, inGameName: String, ffUid: String, onSuccess: () -> Unit) {
        val user = currentUser.value
        if (user == null) {
            _toastMessage.value = "দয়া করে প্রথমে লগইন করুন"
            return
        }
        if (user.balance < match.entryFee) {
            _toastMessage.value = "পর্যাপ্ত ব্যালেন্স নেই! প্রয়োজন ৳${match.entryFee}, আপনার আছে ৳${user.balance.toInt()}"
            return
        }

        viewModelScope.launch {
            val res = repository.joinMatch(match.id, user.id, inGameName, ffUid)
            res.fold(
                onSuccess = { msg ->
                    _toastMessage.value = msg
                    onSuccess()
                },
                onFailure = { err ->
                    _toastMessage.value = err.message ?: "জয়েন ব্যর্থ হয়েছে"
                }
            )
        }
    }

    // Submit Deposit
    fun submitDeposit(method: String, amount: Double, trxId: String, onSuccess: () -> Unit) {
        val user = currentUser.value
        if (user == null) {
            _toastMessage.value = "প্রথমে লগইন করুন"
            return
        }
        viewModelScope.launch {
            val res = repository.submitDeposit(user.id, method, user.phone, amount, trxId)
            res.fold(
                onSuccess = { msg ->
                    _toastMessage.value = msg
                    onSuccess()
                },
                onFailure = { err ->
                    _toastMessage.value = err.message ?: "ডিপোজিট ব্যর্থ"
                }
            )
        }
    }

    // Submit Withdrawal
    fun submitWithdrawal(method: String, withdrawPhone: String, amount: Double, onSuccess: () -> Unit) {
        val user = currentUser.value
        if (user == null) {
            _toastMessage.value = "প্রথমে লগইন করুন"
            return
        }
        viewModelScope.launch {
            val res = repository.submitWithdrawal(user.id, method, withdrawPhone, amount, user.balance)
            res.fold(
                onSuccess = { msg ->
                    _toastMessage.value = msg
                    onSuccess()
                },
                onFailure = { err ->
                    _toastMessage.value = err.message ?: "উইথড্র ব্যর্থ"
                }
            )
        }
    }

    // Submit Proof
    fun submitProof(matchId: Long, matchTitle: String, kills: Int, proofUrl: String, onSuccess: () -> Unit) {
        val user = currentUser.value
        if (user == null) {
            _toastMessage.value = "প্রথমে লগইন করুন"
            return
        }
        viewModelScope.launch {
            val res = repository.submitProof(matchId, matchTitle, user.id, user.name, user.ffUid, kills, proofUrl)
            res.fold(
                onSuccess = { msg ->
                    _toastMessage.value = msg
                    onSuccess()
                },
                onFailure = { err ->
                    _toastMessage.value = err.message ?: "প্রুফ সাবমিট ব্যর্থ"
                }
            )
        }
    }

    // Admin Operations
    fun verifyAdminPasscode(code: String): Boolean {
        val expected = adminConfig.value?.adminPasscode ?: "1234"
        val matched = code.trim() == expected.trim()
        if (matched) {
            _isAdminLoggedIn.value = true
        }
        return matched
    }

    fun approveDeposit(trx: TransactionEntity) {
        viewModelScope.launch {
            val user = currentUser.value
            val currentBal = if (user?.id == trx.userId) user.balance else 0.0
            val res = repository.approveDeposit(trx.id, currentBal)
            res.fold(
                onSuccess = { _toastMessage.value = it },
                onFailure = { _toastMessage.value = it.message }
            )
        }
    }

    fun rejectDeposit(trx: TransactionEntity, reason: String = "ভুল TrxID বা টাকা আসেনি") {
        viewModelScope.launch {
            val res = repository.rejectDeposit(trx.id, reason)
            res.fold(
                onSuccess = { _toastMessage.value = it },
                onFailure = { _toastMessage.value = it.message }
            )
        }
    }

    fun approveWithdrawal(trx: TransactionEntity) {
        viewModelScope.launch {
            val res = repository.approveWithdrawal(trx.id)
            res.fold(
                onSuccess = { _toastMessage.value = it },
                onFailure = { _toastMessage.value = it.message }
            )
        }
    }

    fun rejectWithdrawal(trx: TransactionEntity, reason: String = "ভুল অ্যাকাউন্ট নম্বর") {
        viewModelScope.launch {
            val user = currentUser.value
            val currentBal = if (user?.id == trx.userId) user.balance else 0.0
            val res = repository.rejectWithdrawal(trx.id, currentBal, reason)
            res.fold(
                onSuccess = { _toastMessage.value = it },
                onFailure = { _toastMessage.value = it.message }
            )
        }
    }

    fun approveProof(proof: ProofEntity, reward: Double) {
        viewModelScope.launch {
            val user = currentUser.value
            val currentBal = if (user?.id == proof.userId) user.balance else 0.0
            val res = repository.approveProof(proof.id, reward, currentBal)
            res.fold(
                onSuccess = { _toastMessage.value = it },
                onFailure = { _toastMessage.value = it.message }
            )
        }
    }

    fun rejectProof(proof: ProofEntity, reason: String = "সঠিক স্ক্রিনশট নয়") {
        viewModelScope.launch {
            val res = repository.rejectProof(proof.id, reason)
            res.fold(
                onSuccess = { _toastMessage.value = it },
                onFailure = { _toastMessage.value = it.message }
            )
        }
    }

    fun createMatch(
        title: String,
        category: String,
        map: String,
        entryFee: Int,
        perKill: Int,
        prizePool: Int,
        matchTime: String,
        maxSlots: Int,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val match = MatchEntity(
                title = title.trim(),
                category = category,
                map = map,
                entryFee = entryFee,
                perKill = perKill,
                prizePool = prizePool,
                matchTime = matchTime.trim(),
                maxSlots = maxSlots,
                joinedSlots = 0,
                status = "UPCOMING"
            )
            repository.createMatch(match).fold(
                onSuccess = {
                    _toastMessage.value = "নতুন ম্যাচ তৈরি করা হয়েছে!"
                    onSuccess()
                },
                onFailure = { _toastMessage.value = it.message }
            )
        }
    }

    fun updateMatchRoom(matchId: Long, roomId: String, roomPass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.updateMatchRoom(matchId, roomId, roomPass).fold(
                onSuccess = {
                    _toastMessage.value = it
                    onSuccess()
                },
                onFailure = { _toastMessage.value = it.message }
            )
        }
    }

    fun updateMatchStatus(matchId: Long, status: String) {
        viewModelScope.launch {
            repository.updateMatchStatus(matchId, status).fold(
                onSuccess = { _toastMessage.value = it },
                onFailure = { _toastMessage.value = it.message }
            )
        }
    }

    fun deleteMatch(matchId: Long) {
        viewModelScope.launch {
            repository.deleteMatch(matchId).fold(
                onSuccess = { _toastMessage.value = it },
                onFailure = { _toastMessage.value = it.message }
            )
        }
    }

    fun updateAdminConfig(nagad: String, rocket: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.updateAdminConfig(nagad, rocket, pass).fold(
                onSuccess = {
                    _toastMessage.value = it
                    onSuccess()
                },
                onFailure = { _toastMessage.value = it.message }
            )
        }
    }
}
