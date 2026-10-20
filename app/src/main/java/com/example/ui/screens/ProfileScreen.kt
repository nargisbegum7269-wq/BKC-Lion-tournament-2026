package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TransactionEntity
import com.example.data.UserEntity
import com.example.ui.theme.GamingAccentOrange
import com.example.ui.theme.GamingAccentRed
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
fun ProfileScreen(
    currentUser: UserEntity?,
    transactions: List<TransactionEntity>,
    onOpenDeposit: () -> Unit,
    onOpenWithdraw: () -> Unit,
    onLogout: () -> Unit,
    onOpenAuth: () -> Unit
) {
    if (currentUser == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User",
                    tint = TextMuted,
                    modifier = Modifier.size(54.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "আপনি এখনো লগইন করেননি",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "টুর্নামেন্টে জয়েন করতে ও প্রোফাইল দেখতে লগইন করুন।",
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onOpenAuth,
                    colors = ButtonDefaults.buttonColors(containerColor = GamingAccentRed),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_profile_login")
                ) {
                    Text("লগইন / রেজিস্টার করুন", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // Profile Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("user_profile_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GamingCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(GamingSurfaceBorder),
                    width = 1.dp
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar Icon
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(GamingAccentRed.copy(alpha = 0.15f))
                                .border(1.5.dp, GamingAccentRed.copy(alpha = 0.5f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Avatar",
                                tint = GamingAccentRed,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currentUser.name,
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (currentUser.isAdmin) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(RocketPurple.copy(alpha = 0.2f))
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text(text = "ADMIN", color = RocketPurple, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            Text(
                                text = "মোবাইল: ${currentUser.phone}",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "Free Fire UID: ${currentUser.ffUid}",
                                color = GamingGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stats Grid
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(GamingCardElevated)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "মোট ম্যাচ", color = TextMuted, fontSize = 10.sp)
                            Text(text = "${currentUser.totalMatches}", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(modifier = Modifier.height(24.dp).width(1.dp).background(GamingSurfaceBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "মোট কিল", color = TextMuted, fontSize = 10.sp)
                            Text(text = "${currentUser.totalKills}", color = GamingAccentOrange, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(modifier = Modifier.height(24.dp).width(1.dp).background(GamingSurfaceBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "মোট উইনিং", color = TextMuted, fontSize = 10.sp)
                            Text(text = "৳${currentUser.totalWinnings.toInt()}", color = GamingGold, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Wallet Balance & Actions
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("wallet_balance_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GamingCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(GamingGold.copy(alpha = 0.4f), GamingAccentRed.copy(alpha = 0.4f))
                    ),
                    width = 1.dp
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "ওয়ালেট ব্যালেন্স (Wallet Balance)", color = TextMuted, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "৳",
                            color = GamingGold,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format(Locale.US, "%.2f", currentUser.balance),
                            color = GamingGold,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 2 Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Deposit Button
                        Button(
                            onClick = onOpenDeposit,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("btn_open_deposit"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NagadRed)
                        ) {
                            Icon(imageVector = Icons.Default.AccountBalanceWallet, contentDescription = "Add", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "ডিপোজিট", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        // Withdraw Button
                        Button(
                            onClick = onOpenWithdraw,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("btn_open_withdraw"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RocketPurple)
                        ) {
                            Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = "Withdraw", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "উইথড্র", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Transaction History Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.History, contentDescription = "History", tint = TextMuted, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "লেনদেন হিস্টোরি (${transactions.size})",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (transactions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "কোনো লেনদেনের তথ্য পাওয়া যায়নি।", color = TextMuted, fontSize = 12.sp)
                }
            }
        } else {
            items(transactions, key = { it.id }) { trx ->
                TransactionItemRow(trx = trx)
            }
        }

        // Logout Button
        item {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .testTag("btn_logout"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = GamingAccentRed),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(GamingAccentRed.copy(alpha = 0.5f))
                )
            ) {
                Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Logout", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "লগআউট করুন", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun TransactionItemRow(trx: TransactionEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = GamingCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(GamingSurfaceBorder),
            width = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val (icon, iconColor) = when (trx.type) {
                    "DEPOSIT" -> Pair(Icons.Default.ArrowDownward, GamingEmerald)
                    "WITHDRAW" -> Pair(Icons.Default.ArrowUpward, RocketPurple)
                    "WINNING_PRIZE" -> Pair(Icons.Default.WorkspacePremium, GamingGold)
                    else -> Pair(Icons.Default.MonetizationOn, GamingAccentRed)
                }

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = trx.type, tint = iconColor, modifier = Modifier.size(18.dp))
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = when (trx.type) {
                            "DEPOSIT" -> "ডিপোজিট (${trx.method})"
                            "WITHDRAW" -> "উইথড্র (${trx.method})"
                            "WINNING_PRIZE" -> "উইনিং পুরষ্কার"
                            else -> "ম্যাচ ফি"
                        },
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (trx.trxId.isNotBlank()) "Trx: ${trx.trxId}" else trx.accountNumber,
                        color = TextMuted,
                        fontSize = 9.sp
                    )
                    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(trx.createdAt))
                    Text(text = dateStr, color = TextMuted, fontSize = 9.sp)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                val isPlus = trx.type == "DEPOSIT" || trx.type == "WINNING_PRIZE"
                Text(
                    text = "${if (isPlus) "+" else "-"}৳${trx.amount.toInt()}",
                    color = if (isPlus) GamingEmerald else GamingAccentRed,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                val (statusColor, statusLabel) = when (trx.status) {
                    "APPROVED" -> Pair(GamingEmerald, "সম্পন্ন")
                    "REJECTED" -> Pair(GamingAccentRed, "বাতিল")
                    else -> Pair(GamingGold, "পেন্ডিং")
                }

                Text(
                    text = statusLabel,
                    color = statusColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
