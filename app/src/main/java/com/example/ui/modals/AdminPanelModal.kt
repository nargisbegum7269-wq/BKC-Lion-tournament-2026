package com.example.ui.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AdminConfigEntity
import com.example.data.MatchEntity
import com.example.data.ProofEntity
import com.example.data.TransactionEntity
import com.example.ui.theme.GamingAccentOrange
import com.example.ui.theme.GamingAccentRed
import com.example.ui.theme.GamingBg
import com.example.ui.theme.GamingCard
import com.example.ui.theme.GamingCardElevated
import com.example.ui.theme.GamingEmerald
import com.example.ui.theme.GamingGold
import com.example.ui.theme.GamingSurfaceBorder
import com.example.ui.theme.NagadRed
import com.example.ui.theme.RocketPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminPasscodeDialog(
    onDismiss: () -> Unit,
    onVerifyPasscode: (String) -> Boolean,
    onSuccess: () -> Unit
) {
    var code by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GamingCard,
        modifier = Modifier.testTag("admin_passcode_dialog"),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AdminPanelSettings,
                    contentDescription = "Admin",
                    tint = RocketPurple,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "এডমিন অ্যাক্সেস",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "এডমিন প্যানেলে প্রবেশ করতে ৪ ডিজিটের পাসকোড লিখুন।",
                    color = TextMuted,
                    fontSize = 11.sp
                )

                OutlinedTextField(
                    value = code,
                    onValueChange = {
                        code = it.filter { c -> c.isDigit() }
                        errorMsg = null
                    },
                    label = { Text("এডমিন পাসকোড", fontSize = 11.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_admin_passcode"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RocketPurple,
                        unfocusedBorderColor = GamingSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(GamingCardElevated)
                        .padding(6.dp)
                ) {
                    Text(
                        text = "ডিফল্ট অ্যাডমিন পাসকোড: 1234",
                        color = GamingGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                errorMsg?.let {
                    Text(text = it, color = GamingAccentRed, fontSize = 11.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (onVerifyPasscode(code)) {
                        onSuccess()
                    } else {
                        errorMsg = "পাসকোড সঠিক নয়! পুনরায় চেষ্টা করুন।"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RocketPurple),
                modifier = Modifier.testTag("btn_verify_admin_code")
            ) {
                Text(text = "প্রবেশ করুন", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("বাতিল", color = TextMuted)
            }
        }
    )
}

enum class AdminTab(val title: String) {
    MATCHES("ম্যাচ"),
    DEPOSITS("ডিপোজিট"),
    WITHDRAWALS("উইথড্র"),
    PROOFS("প্রুফ"),
    SETTINGS("সেটিংস")
}

@Composable
fun AdminPanelScreen(
    adminConfig: AdminConfigEntity?,
    matches: List<MatchEntity>,
    pendingDeposits: List<TransactionEntity>,
    pendingWithdrawals: List<TransactionEntity>,
    allProofs: List<ProofEntity>,
    onClose: () -> Unit,
    onCreateMatch: (title: String, cat: String, map: String, fee: Int, kill: Int, pool: Int, time: String, slots: Int) -> Unit,
    onUpdateRoom: (matchId: Long, roomId: String, pass: String) -> Unit,
    onUpdateStatus: (matchId: Long, status: String) -> Unit,
    onDeleteMatch: (matchId: Long) -> Unit,
    onApproveDeposit: (TransactionEntity) -> Unit,
    onRejectDeposit: (TransactionEntity, String) -> Unit,
    onApproveWithdraw: (TransactionEntity) -> Unit,
    onRejectWithdraw: (TransactionEntity, String) -> Unit,
    onApproveProof: (ProofEntity, Double) -> Unit,
    onRejectProof: (ProofEntity, String) -> Unit,
    onUpdateSettings: (nagad: String, rocket: String, pass: String) -> Unit
) {
    var selectedAdminTab by remember { mutableStateOf(AdminTab.MATCHES) }
    var showCreateMatchDialog by remember { mutableStateOf(false) }
    var editingMatchForRoom by remember { mutableStateOf<MatchEntity?>(null) }
    var approvingProof by remember { mutableStateOf<ProofEntity?>(null) }

    Surface(
        color = GamingBg,
        modifier = Modifier.fillMaxSize().testTag("admin_panel_container")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Admin Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GamingCard)
                    .border(1.dp, RocketPurple.copy(alpha = 0.3f))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(RocketPurple.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = "Admin", tint = RocketPurple, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = "এডমিন কন্ট্রোল প্যানেল", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text(text = "ম্যাচ ও ওয়ালেট ম্যানেজমেন্ট", color = TextMuted, fontSize = 9.sp)
                    }
                }

                IconButton(onClick = onClose, modifier = Modifier.testTag("btn_close_admin")) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextPrimary)
                }
            }

            // Tab Buttons Row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GamingCard)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(AdminTab.values()) { tab ->
                    val isSelected = selectedAdminTab == tab
                    val badgeCount = when (tab) {
                        AdminTab.DEPOSITS -> pendingDeposits.size
                        AdminTab.WITHDRAWALS -> pendingWithdrawals.size
                        AdminTab.PROOFS -> allProofs.count { it.status == "PENDING" }
                        else -> 0
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) RocketPurple else GamingCardElevated)
                            .clickable { selectedAdminTab = tab }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("admin_tab_${tab.name.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = tab.title,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (badgeCount > 0) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(GamingAccentRed),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "$badgeCount", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }
                    }
                }
            }

            // Content per tab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                when (selectedAdminTab) {
                    AdminTab.MATCHES -> {
                        AdminMatchesTab(
                            matches = matches,
                            onOpenCreateMatch = { showCreateMatchDialog = true },
                            onEditRoom = { editingMatchForRoom = it },
                            onUpdateStatus = onUpdateStatus,
                            onDelete = onDeleteMatch
                        )
                    }
                    AdminTab.DEPOSITS -> {
                        AdminDepositsTab(
                            deposits = pendingDeposits,
                            onApprove = onApproveDeposit,
                            onReject = onRejectDeposit
                        )
                    }
                    AdminTab.WITHDRAWALS -> {
                        AdminWithdrawalsTab(
                            withdrawals = pendingWithdrawals,
                            onApprove = onApproveWithdraw,
                            onReject = onRejectWithdraw
                        )
                    }
                    AdminTab.PROOFS -> {
                        AdminProofsTab(
                            proofs = allProofs,
                            onApproveClick = { approvingProof = it },
                            onRejectClick = onRejectProof
                        )
                    }
                    AdminTab.SETTINGS -> {
                        AdminSettingsTab(
                            adminConfig = adminConfig,
                            onSave = onUpdateSettings
                        )
                    }
                }
            }
        }
    }

    // Create Match Dialog
    if (showCreateMatchDialog) {
        CreateMatchDialog(
            onDismiss = { showCreateMatchDialog = false },
            onCreate = { title, cat, map, fee, kill, pool, time, slots ->
                onCreateMatch(title, cat, map, fee, kill, pool, time, slots)
                showCreateMatchDialog = false
            }
        )
    }

    // Edit Room ID & Password Dialog
    editingMatchForRoom?.let { match ->
        EditRoomDialog(
            match = match,
            onDismiss = { editingMatchForRoom = null },
            onSave = { roomId, pass ->
                onUpdateRoom(match.id, roomId, pass)
                editingMatchForRoom = null
            }
        )
    }

    // Approve Proof & Reward Dialog
    approvingProof?.let { proof ->
        ApproveProofDialog(
            proof = proof,
            onDismiss = { approvingProof = null },
            onConfirm = { reward ->
                onApproveProof(proof, reward)
                approvingProof = null
            }
        )
    }
}

@Composable
fun AdminMatchesTab(
    matches: List<MatchEntity>,
    onOpenCreateMatch: () -> Unit,
    onEditRoom: (MatchEntity) -> Unit,
    onUpdateStatus: (Long, String) -> Unit,
    onDelete: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onOpenCreateMatch,
            modifier = Modifier.fillMaxWidth().height(42.dp).testTag("btn_create_new_match"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GamingAccentRed)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "নতুন ম্যাচ তৈরি করুন", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
            items(matches, key = { it.id }) { match ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = GamingCard),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(GamingSurfaceBorder),
                        width = 1.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = match.title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(text = "ID: #${match.id}", color = TextMuted, fontSize = 10.sp)
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${match.category} • ${match.map} • ফি: ৳${match.entryFee} • কিল: ৳${match.perKill} • স্লট: ${match.joinedSlots}/${match.maxSlots}",
                            color = TextSecondary,
                            fontSize = 10.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(GamingCardElevated)
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "রুম: ${match.roomId.ifBlank { "দেওয়া হয়নি" }} | পাস: ${match.roomPassword.ifBlank { "নেই" }}",
                                color = GamingGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Button(
                                onClick = { onEditRoom(match) },
                                modifier = Modifier.height(28.dp),
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = RocketPurple)
                            ) {
                                Text("রুম এডিট", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Status Buttons & Delete
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                listOf("UPCOMING", "ONGOING", "COMPLETED").forEach { st ->
                                    val isCur = match.status == st
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (isCur) GamingEmerald.copy(alpha = 0.25f) else GamingCardElevated)
                                            .clickable { onUpdateStatus(match.id, st) }
                                            .padding(horizontal = 6.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = when(st) { "UPCOMING" -> "আসন্ন"; "ONGOING" -> "চলমান"; else -> "সমাপ্ত" },
                                            color = if (isCur) GamingEmerald else TextMuted,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            IconButton(onClick = { onDelete(match.id) }, modifier = Modifier.size(24.dp)) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = GamingAccentRed, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminDepositsTab(
    deposits: List<TransactionEntity>,
    onApprove: (TransactionEntity) -> Unit,
    onReject: (TransactionEntity, String) -> Unit
) {
    if (deposits.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "কোনো পেন্ডিং ডিপোজিট রিকোয়েস্ট নেই", color = TextMuted, fontSize = 12.sp)
        }
        return
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
        items(deposits, key = { it.id }) { trx ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = GamingCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(GamingSurfaceBorder),
                    width = 1.dp
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "${trx.method} ডিপোজিট", color = if (trx.method == "NAGAD") NagadRed else RocketPurple, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(text = "৳${trx.amount.toInt()}", color = GamingGold, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "ব্যবহারকারী আইডি: #${trx.userId} | নম্বর: ${trx.accountNumber}", color = TextSecondary, fontSize = 11.sp)
                    Text(text = "TrxID: ${trx.trxId}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onApprove(trx) },
                            modifier = Modifier.weight(1f).height(36.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GamingEmerald)
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = "Approve", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "অনুমোদন করুন", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onReject(trx, "ভুল TrxID বা টাকা পাওয়া যায়নি") },
                            modifier = Modifier.weight(1f).height(36.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GamingAccentRed)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Reject", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "বাতিল", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminWithdrawalsTab(
    withdrawals: List<TransactionEntity>,
    onApprove: (TransactionEntity) -> Unit,
    onReject: (TransactionEntity, String) -> Unit
) {
    if (withdrawals.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "কোনো পেন্ডিং উইথড্র রিকোয়েস্ট নেই", color = TextMuted, fontSize = 12.sp)
        }
        return
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
        items(withdrawals, key = { it.id }) { trx ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = GamingCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(GamingSurfaceBorder),
                    width = 1.dp
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "${trx.method} উইথড্র", color = RocketPurple, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(text = "৳${trx.amount.toInt()}", color = GamingGold, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "টাকা পাঠানোর নম্বর: ${trx.accountNumber}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = "ইউজার আইডি: #${trx.userId}", color = TextMuted, fontSize = 10.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onApprove(trx) },
                            modifier = Modifier.weight(1f).height(36.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GamingEmerald)
                        ) {
                            Text(text = "পরিশোধ সম্পন্ন", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onReject(trx, "ভুল নম্বর বা সমস্যা") },
                            modifier = Modifier.weight(1f).height(36.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GamingAccentRed)
                        ) {
                            Text(text = "বাতিল ও রিফান্ড", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminProofsTab(
    proofs: List<ProofEntity>,
    onApproveClick: (ProofEntity) -> Unit,
    onRejectClick: (ProofEntity, String) -> Unit
) {
    if (proofs.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "কোনো প্রুফ সাবমিশন নেই", color = TextMuted, fontSize = 12.sp)
        }
        return
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
        items(proofs, key = { it.id }) { proof ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = GamingCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(GamingSurfaceBorder),
                    width = 1.dp
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = proof.matchTitle, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = proof.status,
                            color = when(proof.status) { "APPROVED" -> GamingEmerald; "REJECTED" -> GamingAccentRed; else -> GamingGold },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "প্লেয়ার: ${proof.userName} (UID: ${proof.userFfUid})", color = TextSecondary, fontSize = 11.sp)
                    Text(text = "দাবিকৃত কিল: ${proof.kills} Kills", color = GamingAccentRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = "স্ক্রিনশট লিংক: ${proof.proofUrl}", color = TextMuted, fontSize = 10.sp)

                    if (proof.status == "PENDING") {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onApproveClick(proof) },
                                modifier = Modifier.weight(1f).height(34.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GamingEmerald)
                            ) {
                                Text("অনুমোদন ও পুরষ্কার দিন", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = { onRejectClick(proof, "সঠিক প্রুফ নয়") },
                                modifier = Modifier.weight(1f).height(34.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GamingAccentRed)
                            ) {
                                Text("বাতিল", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminSettingsTab(
    adminConfig: AdminConfigEntity?,
    onSave: (nagad: String, rocket: String, pass: String) -> Unit
) {
    var nagad by remember { mutableStateOf(adminConfig?.nagadNumber ?: "01789123456") }
    var rocket by remember { mutableStateOf(adminConfig?.rocketNumber ?: "01987654321") }
    var pass by remember { mutableStateOf(adminConfig?.adminPasscode ?: "1234") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = GamingCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(GamingSurfaceBorder),
            width = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = "পেমেন্ট নম্বর ও পাসকোড সেটিংস", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = nagad,
                onValueChange = { nagad = it },
                label = { Text("নগদ অ্যাডমিন নম্বর (Nagad)", fontSize = 11.sp) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NagadRed,
                    unfocusedBorderColor = GamingSurfaceBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            OutlinedTextField(
                value = rocket,
                onValueChange = { rocket = it },
                label = { Text("রকেট অ্যাডমিন নম্বর (Rocket)", fontSize = 11.sp) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RocketPurple,
                    unfocusedBorderColor = GamingSurfaceBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("অ্যাডমিন পাসকোড", fontSize = 11.sp) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GamingGold,
                    unfocusedBorderColor = GamingSurfaceBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            Button(
                onClick = { onSave(nagad, rocket, pass) },
                modifier = Modifier.fillMaxWidth().height(42.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RocketPurple)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("সেটিংস সংরক্ষণ করুন", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CreateMatchDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, cat: String, map: String, fee: Int, kill: Int, pool: Int, time: String, slots: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("SOLO") }
    var map by remember { mutableStateOf("Bermuda") }
    var feeText by remember { mutableStateOf("25") }
    var killText by remember { mutableStateOf("12") }
    var poolText by remember { mutableStateOf("600") }
    var timeText by remember { mutableStateOf("Today 9:00 PM") }
    var slotsText by remember { mutableStateOf("48") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GamingCard,
        title = {
            Text(text = "নতুন ম্যাচ তৈরি করুন", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("ম্যাচের নাম", fontSize = 11.sp) },
                    placeholder = { Text("যেমন: Bermuda Rush Hour #105", fontSize = 10.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GamingAccentRed,
                        unfocusedBorderColor = GamingSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                // Category selector
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("SOLO", "DUO", "SQUAD").forEach { cat ->
                        val isSel = category == cat
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSel) GamingAccentRed else GamingCardElevated)
                                .clickable { category = cat }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = cat, color = if (isSel) Color.White else TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Map selector
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("Bermuda", "Purgatory", "Kalahari", "Nexterra").forEach { m ->
                        val isSel = map == m
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSel) RocketPurple else GamingCardElevated)
                                .clickable { map = m }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = m, color = if (isSel) Color.White else TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(
                        value = feeText,
                        onValueChange = { feeText = it.filter { c -> c.isDigit() } },
                        label = { Text("এন্ট্রি ফি", fontSize = 10.sp) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = killText,
                        onValueChange = { killText = it.filter { c -> c.isDigit() } },
                        label = { Text("প্রতি কিল", fontSize = 10.sp) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = poolText,
                        onValueChange = { poolText = it.filter { c -> c.isDigit() } },
                        label = { Text("পুরষ্কার", fontSize = 10.sp) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = timeText,
                    onValueChange = { timeText = it },
                    label = { Text("ম্যাচ টাইম", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalTitle = title.ifBlank { "FF $map $category Match" }
                    val fee = feeText.toIntOrNull() ?: 20
                    val kill = killText.toIntOrNull() ?: 10
                    val pool = poolText.toIntOrNull() ?: 500
                    val slots = slotsText.toIntOrNull() ?: 48
                    onCreate(finalTitle, category, map, fee, kill, pool, timeText, slots)
                },
                colors = ButtonDefaults.buttonColors(containerColor = GamingAccentRed)
            ) {
                Text("তৈরি করুন", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("বাতিল", color = TextMuted)
            }
        }
    )
}

@Composable
fun EditRoomDialog(
    match: MatchEntity,
    onDismiss: () -> Unit,
    onSave: (roomId: String, pass: String) -> Unit
) {
    var roomId by remember { mutableStateOf(match.roomId) }
    var pass by remember { mutableStateOf(match.roomPassword) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GamingCard,
        title = {
            Text(text = "রুম আইডি ও পাসওয়ার্ড আপডেট", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = match.title, color = TextSecondary, fontSize = 11.sp)

                OutlinedTextField(
                    value = roomId,
                    onValueChange = { roomId = it },
                    label = { Text("রুম আইডি (Room ID)", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = { Text("রুম পাসওয়ার্ড (Password)", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(roomId, pass) },
                colors = ButtonDefaults.buttonColors(containerColor = RocketPurple)
            ) {
                Text("সেভ করুন", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("বাতিল", color = TextMuted)
            }
        }
    )
}

@Composable
fun ApproveProofDialog(
    proof: ProofEntity,
    onDismiss: () -> Unit,
    onConfirm: (rewardAmount: Double) -> Unit
) {
    var rewardText by remember { mutableStateOf("100") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GamingCard,
        title = {
            Text(text = "প্রুফ অনুমোদন ও পুরষ্কার", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "প্লেয়ার: ${proof.userName} (কিল: ${proof.kills})", color = TextSecondary, fontSize = 12.sp)
                Text(text = "ম্যাচ: ${proof.matchTitle}", color = TextMuted, fontSize = 11.sp)

                OutlinedTextField(
                    value = rewardText,
                    onValueChange = { rewardText = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("পুরষ্কারের টাকা (BDT ৳)", fontSize = 11.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val reward = rewardText.toDoubleOrNull() ?: 50.0
                    onConfirm(reward)
                },
                colors = ButtonDefaults.buttonColors(containerColor = GamingEmerald)
            ) {
                Text("অনুমোদন করুন", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("বাতিল", color = TextMuted)
            }
        }
    )
}
