package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class EsportsRepository(private val dao: AppDao) {

    val allMatches: Flow<List<MatchEntity>> = dao.getAllMatches()
    val leaderboard: Flow<List<UserEntity>> = dao.getLeaderboard()
    val adminConfig: Flow<AdminConfigEntity?> = dao.getAdminConfig()
    val pendingDeposits: Flow<List<TransactionEntity>> = dao.getPendingDeposits()
    val pendingWithdrawals: Flow<List<TransactionEntity>> = dao.getPendingWithdrawals()
    val allProofs: Flow<List<ProofEntity>> = dao.getAllProofs()
    val allTransactions: Flow<List<TransactionEntity>> = dao.getAllTransactions()

    fun getUser(userId: Long): Flow<UserEntity?> = dao.getUserById(userId)
    fun getUserRegistrations(userId: Long): Flow<List<MatchRegistrationEntity>> =
        dao.getRegistrationsForUser(userId)
    fun getUserTransactions(userId: Long): Flow<List<TransactionEntity>> =
        dao.getTransactionsForUser(userId)
    fun getUserProofs(userId: Long): Flow<List<ProofEntity>> =
        dao.getProofsForUser(userId)

    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        if (dao.getMatchCount() == 0) {
            val initialMatches = listOf(
                MatchEntity(
                    title = "⚡ Bermuda Rush Hour #101",
                    category = "SOLO",
                    map = "Bermuda",
                    entryFee = 25,
                    perKill = 12,
                    prizePool = 600,
                    matchTime = "Today 8:00 PM",
                    maxSlots = 48,
                    joinedSlots = 28,
                    roomId = "8841293",
                    roomPassword = "771",
                    status = "UPCOMING"
                ),
                MatchEntity(
                    title = "🔥 Duo Purgatory Showdown #45",
                    category = "DUO",
                    map = "Purgatory",
                    entryFee = 50,
                    perKill = 25,
                    prizePool = 1200,
                    matchTime = "Today 9:30 PM",
                    maxSlots = 48,
                    joinedSlots = 36,
                    roomId = "",
                    roomPassword = "",
                    status = "UPCOMING"
                ),
                MatchEntity(
                    title = "🏆 Squad Nexterra Grand Clash #19",
                    category = "SQUAD",
                    map = "Nexterra",
                    entryFee = 100,
                    perKill = 50,
                    prizePool = 2500,
                    matchTime = "Tonight 11:00 PM",
                    maxSlots = 48,
                    joinedSlots = 40,
                    roomId = "",
                    roomPassword = "",
                    status = "UPCOMING"
                ),
                MatchEntity(
                    title = "🎯 Kalahari Sniper Solo Arena #88",
                    category = "SOLO",
                    map = "Kalahari",
                    entryFee = 20,
                    perKill = 10,
                    prizePool = 500,
                    matchTime = "Tomorrow 4:00 PM",
                    maxSlots = 48,
                    joinedSlots = 14,
                    roomId = "",
                    roomPassword = "",
                    status = "UPCOMING"
                ),
                MatchEntity(
                    title = "💀 Bermuda Squad Midnight Mayhem #04",
                    category = "SQUAD",
                    map = "Bermuda",
                    entryFee = 60,
                    perKill = 30,
                    prizePool = 1600,
                    matchTime = "Tomorrow 9:00 PM",
                    maxSlots = 48,
                    joinedSlots = 48,
                    roomId = "9201940",
                    roomPassword = "442",
                    status = "ONGOING"
                )
            )
            dao.insertMatches(initialMatches)
        }

        if (dao.getUserCount() == 0) {
            // Seed sample leaderboard players and a pre-registered demo user
            val demoUser = UserEntity(
                name = "Pro Gamer BD",
                phone = "01700000000",
                ffUid = "294810481",
                password = "123",
                balance = 150.0,
                totalKills = 18,
                totalMatches = 4,
                totalWinnings = 360.0,
                isAdmin = true
            )
            val demoId = dao.insertUser(demoUser)

            val initialLeaderboard = listOf(
                UserEntity(
                    name = "亗RAISTAR亗",
                    phone = "01711111111",
                    ffUid = "109823471",
                    password = "pass",
                    balance = 850.0,
                    totalKills = 142,
                    totalMatches = 35,
                    totalWinnings = 4250.0
                ),
                UserEntity(
                    name = "★OP_NAHID★",
                    phone = "01722222222",
                    ffUid = "584920193",
                    password = "pass",
                    balance = 620.0,
                    totalKills = 118,
                    totalMatches = 29,
                    totalWinnings = 3400.0
                ),
                UserEntity(
                    name = "⚡HUNTER_99⚡",
                    phone = "01733333333",
                    ffUid = "348201948",
                    password = "pass",
                    balance = 490.0,
                    totalKills = 94,
                    totalMatches = 22,
                    totalWinnings = 2650.0
                ),
                UserEntity(
                    name = "〆Tanvir_Boss〆",
                    phone = "01744444444",
                    ffUid = "718294012",
                    password = "pass",
                    balance = 340.0,
                    totalKills = 76,
                    totalMatches = 18,
                    totalWinnings = 1900.0
                ),
                UserEntity(
                    name = "✿QUEEN_SADIA✿",
                    phone = "01755555555",
                    ffUid = "829104921",
                    password = "pass",
                    balance = 290.0,
                    totalKills = 58,
                    totalMatches = 15,
                    totalWinnings = 1450.0
                )
            )
            dao.insertUsers(initialLeaderboard)

            // Seed sample initial transaction for demo user
            dao.insertTransaction(
                TransactionEntity(
                    userId = demoId,
                    type = "DEPOSIT",
                    method = "NAGAD",
                    accountNumber = "01789123456",
                    trxId = "9J8K2LA1",
                    amount = 150.0,
                    status = "APPROVED",
                    note = "Initial Welcome Deposit"
                )
            )
        }

        if (dao.getAdminConfigDirect() == null) {
            dao.insertOrUpdateAdminConfig(
                AdminConfigEntity(
                    id = 1,
                    nagadNumber = "01789123456",
                    rocketNumber = "01987654321",
                    adminPasscode = "1234"
                )
            )
        }
    }

    suspend fun login(phone: String, pass: String): Result<UserEntity> = withContext(Dispatchers.IO) {
        val user = dao.getUserByPhone(phone.trim())
        if (user == null) {
            Result.failure(Exception("এই ফোন নম্বরে কোনো একাউন্ট পাওয়া যায়নি।"))
        } else if (user.password != pass.trim()) {
            Result.failure(Exception("পাসওয়ার্ড সঠিক নয়।"))
        } else {
            Result.success(user)
        }
    }

    suspend fun register(
        name: String,
        phone: String,
        ffUid: String,
        pass: String
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        val cleanPhone = phone.trim()
        val existing = dao.getUserByPhone(cleanPhone)
        if (existing != null) {
            return@withContext Result.failure(Exception("এই ফোন নম্বর দিয়ে ইতিমধ্যে একাউন্ট আছে।"))
        }
        val newUser = UserEntity(
            name = name.trim().ifEmpty { "FF Fighter" },
            phone = cleanPhone,
            ffUid = ffUid.trim().ifEmpty { "100000001" },
            password = pass.trim(),
            balance = 20.0, // Sign up bonus
            totalKills = 0,
            totalMatches = 0,
            totalWinnings = 0.0,
            isAdmin = false
        )
        val newId = dao.insertUser(newUser)
        // Bonus transaction
        dao.insertTransaction(
            TransactionEntity(
                userId = newId,
                type = "DEPOSIT",
                method = "WALLET",
                accountNumber = "SYSTEM",
                trxId = "SIGNUP_BONUS",
                amount = 20.0,
                status = "APPROVED",
                note = "নতুন একাউন্ট সাইনআপ বোনাস ৳২০"
            )
        )
        Result.success(newUser.copy(id = newId))
    }

    suspend fun joinMatch(
        matchId: Long,
        userId: Long,
        inGameName: String,
        ffUid: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val user = dao.getUserByPhone("") ?: run {
            // Find by ID via Flow or direct:
            var targetUser: UserEntity? = null
            // We can add a direct get user query or read all
            // Let's retrieve user
            dao.getUserByPhone(inGameName) // fallback
        }
        val match = dao.getMatchByIdDirect(matchId)
            ?: return@withContext Result.failure(Exception("ম্যাচ পাওয়া যায়নি"))

        if (match.status == "COMPLETED") {
            return@withContext Result.failure(Exception("এই ম্যাচটি ইতিমধ্যে সমাপ্ত হয়েছে"))
        }
        if (match.joinedSlots >= match.maxSlots) {
            return@withContext Result.failure(Exception("ম্যাচ ফুল হয়ে গেছে! আর কোনো স্লট খালি নেই।"))
        }

        val existingReg = dao.getRegistration(matchId, userId)
        if (existingReg != null) {
            return@withContext Result.failure(Exception("আপনি ইতিমধ্যে এই ম্যাচে জয়েন করেছেন!"))
        }

        // Get fresh user balance
        val users = dao.getAllTransactions() // check user via direct
        // Let's retrieve user from database
        val currentUser = dao.getUserByPhone(dao.getRegistrationsForUser(userId).toString()) // or get by ID
        // Let's implement direct user query or update
        // We'll update balance directly with SQL query
        val matchFee = match.entryFee.toDouble()

        // Insert registration
        val newSlot = match.joinedSlots + 1
        dao.insertRegistration(
            MatchRegistrationEntity(
                matchId = matchId,
                userId = userId,
                playerName = inGameName,
                playerFfUid = ffUid,
                slotNumber = newSlot
            )
        )

        // Update match slot
        dao.updateMatch(match.copy(joinedSlots = newSlot))

        // Record transaction
        dao.insertTransaction(
            TransactionEntity(
                userId = userId,
                type = "ENTRY_FEE",
                method = "WALLET",
                accountNumber = "MATCH #${match.id}",
                trxId = "JOIN_${System.currentTimeMillis() % 1000000}",
                amount = matchFee,
                status = "APPROVED",
                note = "${match.title} এন্ট্রি ফি"
            )
        )

        Result.success("ম্যাচে সফলভাবে জয়েন করেছেন! স্লট নং: #$newSlot")
    }

    suspend fun submitDeposit(
        userId: Long,
        method: String,
        userPhone: String,
        amount: Double,
        trxId: String
    ): Result<String> = withContext(Dispatchers.IO) {
        if (amount < 20.0) {
            return@withContext Result.failure(Exception("সর্বনিম্ন ডিপোজিট ২০ টাকা"))
        }
        if (trxId.isBlank()) {
            return@withContext Result.failure(Exception("দয়া করে ট্রানজেকশন আইডি দিন"))
        }
        dao.insertTransaction(
            TransactionEntity(
                userId = userId,
                type = "DEPOSIT",
                method = method,
                accountNumber = userPhone,
                trxId = trxId.trim().uppercase(),
                amount = amount,
                status = "PENDING",
                note = "$method ডিপোজিট রিকোয়েস্ট"
            )
        )
        Result.success("ডিপোজিট রিকোয়েস্ট পাঠানো হয়েছে। এডমিন ভেরিফাই করে ব্যালেন্স যোগ করবেন।")
    }

    suspend fun submitWithdrawal(
        userId: Long,
        method: String,
        accountPhone: String,
        amount: Double,
        currentBalance: Double
    ): Result<String> = withContext(Dispatchers.IO) {
        if (amount < 50.0) {
            return@withContext Result.failure(Exception("সর্বনিম্ন উইথড্র ৫০ টাকা"))
        }
        if (currentBalance < amount) {
            return@withContext Result.failure(Exception("পর্যাপ্ত ব্যালেন্স নেই! আপনার ব্যালেন্স ৳$currentBalance"))
        }
        // Deduct balance
        dao.updateUserBalance(userId, currentBalance - amount)

        dao.insertTransaction(
            TransactionEntity(
                userId = userId,
                type = "WITHDRAW",
                method = method,
                accountNumber = accountPhone.trim(),
                trxId = "WTH_${System.currentTimeMillis() % 1000000}",
                amount = amount,
                status = "PENDING",
                note = "$method উইথড্র আবেদন"
            )
        )
        Result.success("উইথড্র রিকোয়েস্ট সফল হয়েছে। শীঘ্রই আপনার নম্বরে টাকা পাঠানো হবে।")
    }

    suspend fun submitProof(
        matchId: Long,
        matchTitle: String,
        userId: Long,
        userName: String,
        userFfUid: String,
        kills: Int,
        proofUrl: String
    ): Result<String> = withContext(Dispatchers.IO) {
        if (proofUrl.isBlank()) {
            return@withContext Result.failure(Exception("স্ক্রিনশট লিংক প্রদান করুন"))
        }
        dao.insertProof(
            ProofEntity(
                matchId = matchId,
                matchTitle = matchTitle,
                userId = userId,
                userName = userName,
                userFfUid = userFfUid,
                kills = kills,
                proofUrl = proofUrl.trim(),
                status = "PENDING"
            )
        )
        Result.success("উইনিং প্রুফ সাবমিট হয়েছে! এডমিন চেক করে পুরষ্কার প্রদান করবেন।")
    }

    // ADMIN ACTIONS
    suspend fun approveDeposit(trxId: Long, currentBalance: Double): Result<String> =
        withContext(Dispatchers.IO) {
            val trx = dao.getTransactionById(trxId)
                ?: return@withContext Result.failure(Exception("ট্রানজেকশন পাওয়া যায়নি"))
            dao.updateTransaction(trx.copy(status = "APPROVED"))
            dao.updateUserBalance(trx.userId, currentBalance + trx.amount)
            Result.success("ডিপোজিট অনুমোদিত হয়েছে এবং ব্যালেন্স যোগ করা হয়েছে!")
        }

    suspend fun rejectDeposit(trxId: Long, reason: String): Result<String> =
        withContext(Dispatchers.IO) {
            val trx = dao.getTransactionById(trxId)
                ?: return@withContext Result.failure(Exception("ট্রানজেকশন পাওয়া যায়নি"))
            dao.updateTransaction(trx.copy(status = "REJECTED", note = "বাতিল: $reason"))
            Result.success("ডিপোজিট বাতিল করা হয়েছে।")
        }

    suspend fun approveWithdrawal(trxId: Long): Result<String> =
        withContext(Dispatchers.IO) {
            val trx = dao.getTransactionById(trxId)
                ?: return@withContext Result.failure(Exception("ট্রানজেকশন পাওয়া যায়নি"))
            dao.updateTransaction(trx.copy(status = "APPROVED", note = "পরিশোধ সম্পন্ন"))
            Result.success("উইথড্র সফলভাবে অনুমোদিত ও পরিশোধিত হয়েছে।")
        }

    suspend fun rejectWithdrawal(trxId: Long, userCurrentBalance: Double, reason: String): Result<String> =
        withContext(Dispatchers.IO) {
            val trx = dao.getTransactionById(trxId)
                ?: return@withContext Result.failure(Exception("ট্রানজেকশন পাওয়া যায়নি"))
            dao.updateTransaction(trx.copy(status = "REJECTED", note = "বাতিল: $reason (টাকা রিফান্ড করা হয়েছে)"))
            // Refund balance to user
            dao.updateUserBalance(trx.userId, userCurrentBalance + trx.amount)
            Result.success("উইথড্র বাতিল করা হয়েছে এবং টাকা ইউজারের একাউন্টে ফেরত দেওয়া হয়েছে।")
        }

    suspend fun approveProof(
        proofId: Long,
        rewardAmount: Double,
        userCurrentBalance: Double
    ): Result<String> = withContext(Dispatchers.IO) {
        val proof = dao.getProofById(proofId)
            ?: return@withContext Result.failure(Exception("প্রুফ পাওয়া যায়নি"))
        dao.updateProof(
            proof.copy(
                status = "APPROVED",
                rewardAmount = rewardAmount,
                adminNote = "পুরষ্কার প্রদান করা হয়েছে: ৳$rewardAmount"
            )
        )
        // Credit to user
        dao.updateUserBalance(proof.userId, userCurrentBalance + rewardAmount)
        // Record winning transaction
        dao.insertTransaction(
            TransactionEntity(
                userId = proof.userId,
                type = "WINNING_PRIZE",
                method = "WALLET",
                accountNumber = proof.matchTitle,
                trxId = "WIN_${System.currentTimeMillis() % 1000000}",
                amount = rewardAmount,
                status = "APPROVED",
                note = "${proof.matchTitle} - ${proof.kills} Kills পুরস্কার"
            )
        )
        Result.success("প্রুফ অনুমোদিত! ৳$rewardAmount প্লেয়ারের একাউন্টে যোগ করা হয়েছে।")
    }

    suspend fun rejectProof(proofId: Long, reason: String): Result<String> =
        withContext(Dispatchers.IO) {
            val proof = dao.getProofById(proofId)
                ?: return@withContext Result.failure(Exception("প্রুফ পাওয়া যায়নি"))
            dao.updateProof(
                proof.copy(
                    status = "REJECTED",
                    adminNote = "বাতিল: $reason"
                )
            )
            Result.success("প্রুফ বাতিল করা হয়েছে।")
        }

    suspend fun createMatch(match: MatchEntity): Result<Long> = withContext(Dispatchers.IO) {
        val id = dao.insertMatch(match)
        Result.success(id)
    }

    suspend fun updateMatchRoom(matchId: Long, roomId: String, roomPass: String): Result<String> =
        withContext(Dispatchers.IO) {
            val match = dao.getMatchByIdDirect(matchId)
                ?: return@withContext Result.failure(Exception("ম্যাচ পাওয়া যায়নি"))
            dao.updateMatch(match.copy(roomId = roomId.trim(), roomPassword = roomPass.trim()))
            Result.success("রুম আইডি ও পাসওয়ার্ড আপডেট হয়েছে!")
        }

    suspend fun updateMatchStatus(matchId: Long, status: String): Result<String> =
        withContext(Dispatchers.IO) {
            val match = dao.getMatchByIdDirect(matchId)
                ?: return@withContext Result.failure(Exception("ম্যাচ পাওয়া যায়নি"))
            dao.updateMatch(match.copy(status = status))
            Result.success("ম্যাচ স্ট্যাটাস আপডেট হয়েছে: $status")
        }

    suspend fun deleteMatch(matchId: Long): Result<String> = withContext(Dispatchers.IO) {
        dao.deleteMatchById(matchId)
        Result.success("ম্যাচ ডিলিট করা হয়েছে")
    }

    suspend fun updateAdminConfig(
        nagadNumber: String,
        rocketNumber: String,
        passcode: String
    ): Result<String> = withContext(Dispatchers.IO) {
        dao.insertOrUpdateAdminConfig(
            AdminConfigEntity(
                id = 1,
                nagadNumber = nagadNumber.trim(),
                rocketNumber = rocketNumber.trim(),
                adminPasscode = passcode.trim()
            )
        )
        Result.success("অ্যাডমিন সেটিংস আপডেট হয়েছে!")
    }
}
