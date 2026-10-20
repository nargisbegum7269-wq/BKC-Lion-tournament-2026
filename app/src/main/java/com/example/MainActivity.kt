package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppTab
import com.example.ui.EsportsViewModel
import com.example.ui.components.BottomNavBar
import com.example.ui.components.TopHeader
import com.example.ui.modals.AdminPanelScreen
import com.example.ui.modals.AdminPasscodeDialog
import com.example.ui.modals.AuthScreenOverlay
import com.example.ui.modals.DepositModal
import com.example.ui.modals.WithdrawModal
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.MatchesScreen
import com.example.ui.screens.MyMatchesScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.WinningScreen
import com.example.ui.theme.FFEsportsTheme
import com.example.ui.theme.GamingBg
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FFEsportsTheme {
                FFEsportsApp()
            }
        }
    }
}

@Composable
fun FFEsportsApp(
    viewModel: EsportsViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allMatches by viewModel.allMatches.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val leaderboard by viewModel.leaderboard.collectAsStateWithLifecycle()
    val adminConfig by viewModel.adminConfig.collectAsStateWithLifecycle()
    val userRegistrations by viewModel.userRegistrations.collectAsStateWithLifecycle()
    val userTransactions by viewModel.userTransactions.collectAsStateWithLifecycle()
    val userProofs by viewModel.userProofs.collectAsStateWithLifecycle()
    val pendingDeposits by viewModel.pendingDeposits.collectAsStateWithLifecycle()
    val pendingWithdrawals by viewModel.pendingWithdrawals.collectAsStateWithLifecycle()
    val allProofs by viewModel.allProofs.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()
    val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsStateWithLifecycle()

    // Modals state
    var showDepositModal by remember { mutableStateOf(false) }
    var showWithdrawModal by remember { mutableStateOf(false) }
    var showAdminPasscodeDialog by remember { mutableStateOf(false) }
    var showAdminPanel by remember { mutableStateOf(false) }
    var showAuthModal by remember { mutableStateOf(false) }
    var winningPreSelectedMatchId by remember { mutableStateOf<Long?>(null) }

    // Listen for toast messages
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
                viewModel.clearToast()
            }
        }
    }

    // Back handling
    BackHandler(enabled = showAdminPanel || showDepositModal || showWithdrawModal || showAuthModal || currentTab != AppTab.MATCHES) {
        when {
            showAdminPanel -> showAdminPanel = false
            showDepositModal -> showDepositModal = false
            showWithdrawModal -> showWithdrawModal = false
            showAuthModal -> showAuthModal = false
            currentTab != AppTab.MATCHES -> viewModel.setTab(AppTab.MATCHES)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(GamingBg)) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = GamingBg,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopHeader(
                    balance = currentUser?.balance ?: 0.0,
                    onBalanceClick = {
                        if (currentUser == null) {
                            showAuthModal = true
                        } else {
                            showDepositModal = true
                        }
                    },
                    onAdminClick = {
                        if (isAdminLoggedIn) {
                            showAdminPanel = true
                        } else {
                            showAdminPasscodeDialog = true
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavBar(
                    currentTab = currentTab,
                    onTabSelected = { viewModel.setTab(it) }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    AppTab.MATCHES -> {
                        MatchesScreen(
                            matches = allMatches,
                            selectedCategory = selectedCategory,
                            onCategorySelected = { viewModel.setCategory(it) },
                            currentUser = currentUser,
                            userRegistrations = userRegistrations,
                            onJoinMatch = { match, ign, uid ->
                                viewModel.joinMatch(match, ign, uid) {}
                            },
                            onRequireLogin = { showAuthModal = true },
                            onShowToast = { viewModel.showToast(it) }
                        )
                    }
                    AppTab.MY_MATCHES -> {
                        MyMatchesScreen(
                            matches = allMatches,
                            userRegistrations = userRegistrations,
                            onNavigateToMatches = { viewModel.setTab(AppTab.MATCHES) },
                            onNavigateToWinning = { matchId ->
                                winningPreSelectedMatchId = matchId
                                viewModel.setTab(AppTab.WINNING)
                            },
                            onShowToast = { viewModel.showToast(it) }
                        )
                    }
                    AppTab.WINNING -> {
                        WinningScreen(
                            matches = allMatches,
                            userProofs = userProofs,
                            preSelectedMatchId = winningPreSelectedMatchId,
                            onSubmitProof = { matchId, title, kills, url ->
                                viewModel.submitProof(matchId, title, kills, url) {
                                    winningPreSelectedMatchId = null
                                }
                            },
                            onShowToast = { viewModel.showToast(it) }
                        )
                    }
                    AppTab.LEADERBOARD -> {
                        LeaderboardScreen(
                            leaderboard = leaderboard
                        )
                    }
                    AppTab.PROFILE -> {
                        ProfileScreen(
                            currentUser = currentUser,
                            transactions = userTransactions,
                            onOpenDeposit = { showDepositModal = true },
                            onOpenWithdraw = { showWithdrawModal = true },
                            onLogout = { viewModel.logout() },
                            onOpenAuth = { showAuthModal = true }
                        )
                    }
                }
            }
        }

        // Modals & Overlays
        if (showDepositModal) {
            DepositModal(
                adminConfig = adminConfig,
                onDismiss = { showDepositModal = false },
                onSubmitDeposit = { method, amount, trxId ->
                    viewModel.submitDeposit(method, amount, trxId) {}
                },
                onShowToast = { viewModel.showToast(it) }
            )
        }

        if (showWithdrawModal) {
            WithdrawModal(
                userBalance = currentUser?.balance ?: 0.0,
                userPhone = currentUser?.phone ?: "",
                onDismiss = { showWithdrawModal = false },
                onSubmitWithdraw = { method, phone, amount ->
                    viewModel.submitWithdrawal(method, phone, amount) {}
                }
            )
        }

        if (showAdminPasscodeDialog) {
            AdminPasscodeDialog(
                onDismiss = { showAdminPasscodeDialog = false },
                onVerifyPasscode = { code -> viewModel.verifyAdminPasscode(code) },
                onSuccess = {
                    showAdminPasscodeDialog = false
                    showAdminPanel = true
                }
            )
        }

        if (showAdminPanel) {
            AdminPanelScreen(
                adminConfig = adminConfig,
                matches = allMatches,
                pendingDeposits = pendingDeposits,
                pendingWithdrawals = pendingWithdrawals,
                allProofs = allProofs,
                onClose = { showAdminPanel = false },
                onCreateMatch = { title, cat, map, fee, kill, pool, time, slots ->
                    viewModel.createMatch(title, cat, map, fee, kill, pool, time, slots) {}
                },
                onUpdateRoom = { matchId, roomId, pass ->
                    viewModel.updateMatchRoom(matchId, roomId, pass) {}
                },
                onUpdateStatus = { matchId, status ->
                    viewModel.updateMatchStatus(matchId, status)
                },
                onDeleteMatch = { matchId ->
                    viewModel.deleteMatch(matchId)
                },
                onApproveDeposit = { trx ->
                    viewModel.approveDeposit(trx)
                },
                onRejectDeposit = { trx, reason ->
                    viewModel.rejectDeposit(trx, reason)
                },
                onApproveWithdraw = { trx ->
                    viewModel.approveWithdrawal(trx)
                },
                onRejectWithdraw = { trx, reason ->
                    viewModel.rejectWithdrawal(trx, reason)
                },
                onApproveProof = { proof, reward ->
                    viewModel.approveProof(proof, reward)
                },
                onRejectProof = { proof, reason ->
                    viewModel.rejectProof(proof, reason)
                },
                onUpdateSettings = { nagad, rocket, pass ->
                    viewModel.updateAdminConfig(nagad, rocket, pass) {}
                }
            )
        }

        if (showAuthModal) {
            AuthScreenOverlay(
                onDismiss = { showAuthModal = false },
                onLogin = { phone, pass ->
                    viewModel.login(phone, pass) {
                        showAuthModal = false
                    }
                },
                onRegister = { name, phone, ffUid, pass ->
                    viewModel.register(name, phone, ffUid, pass) {
                        showAuthModal = false
                    }
                }
            )
        }
    }
}
