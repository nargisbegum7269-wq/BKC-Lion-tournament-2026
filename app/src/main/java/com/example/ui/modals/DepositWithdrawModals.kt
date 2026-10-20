package com.example.ui.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AdminConfigEntity
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

@Composable
fun DepositModal(
    adminConfig: AdminConfigEntity?,
    onDismiss: () -> Unit,
    onSubmitDeposit: (method: String, amount: Double, trxId: String) -> Unit,
    onShowToast: (String) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    var selectedMethod by remember { mutableStateOf("NAGAD") }
    var amountText by remember { mutableStateOf("") }
    var trxIdText by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val adminNumber = if (selectedMethod == "NAGAD") {
        adminConfig?.nagadNumber ?: "01789123456"
    } else {
        adminConfig?.rocketNumber ?: "01987654321"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GamingCard,
        modifier = Modifier.testTag("deposit_dialog"),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "Wallet",
                        tint = GamingAccentRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ডিপোজিট করুন",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Method Selector: Nagad vs Rocket
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Nagad Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selectedMethod == "NAGAD") NagadRed.copy(alpha = 0.2f) else GamingCardElevated
                            )
                            .border(
                                1.dp,
                                if (selectedMethod == "NAGAD") NagadRed else GamingSurfaceBorder,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedMethod = "NAGAD" }
                            .padding(10.dp)
                            .testTag("deposit_method_nagad"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "নগদ (Nagad)", color = NagadRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Send Money", color = TextMuted, fontSize = 9.sp)
                        }
                    }

                    // Rocket Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selectedMethod == "ROCKET") RocketPurple.copy(alpha = 0.2f) else GamingCardElevated
                            )
                            .border(
                                1.dp,
                                if (selectedMethod == "ROCKET") RocketPurple else GamingSurfaceBorder,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedMethod = "ROCKET" }
                            .padding(10.dp)
                            .testTag("deposit_method_rocket"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "রকেট (Rocket)", color = RocketPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Send Money", color = TextMuted, fontSize = 9.sp)
                        }
                    }
                }

                // Admin Number Display Box with Copy
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(GamingCardElevated)
                        .border(1.dp, GamingSurfaceBorder, RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (selectedMethod == "NAGAD") "নগদ অ্যাডমিন নম্বর (Send Money):" else "রকেট অ্যাডমিন নম্বর (Send Money):",
                            color = TextMuted,
                            fontSize = 9.sp
                        )
                        Text(
                            text = adminNumber,
                            color = GamingGold,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(adminNumber))
                            onShowToast("অ্যাডমিন নম্বর কপি করা হয়েছে: $adminNumber")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GamingCard),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(32.dp).testTag("copy_admin_deposit_num")
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = GamingGold, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "কপি", color = GamingGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "প্রথমে উপরের নম্বরে Send Money করুন। এরপর নিচের বক্সে টাকার পরিমাণ ও ট্রানজেকশন আইডি দিন।",
                    color = TextMuted,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )

                // Amount
                OutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it.filter { c -> c.isDigit() || c == '.' }
                        errorMsg = null
                    },
                    label = { Text("টাকার পরিমাণ (সর্বনিম্ন ৳২০)", fontSize = 11.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_dep_amount"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GamingAccentRed,
                        unfocusedBorderColor = GamingSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                // TrxID
                OutlinedTextField(
                    value = trxIdText,
                    onValueChange = {
                        trxIdText = it.uppercase()
                        errorMsg = null
                    },
                    label = { Text("ট্রানজেকশন আইডি (TrxID)", fontSize = 11.sp) },
                    placeholder = { Text("যেমন: 9J7X8K2L", fontSize = 11.sp, color = TextMuted) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_dep_trx_id"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GamingAccentRed,
                        unfocusedBorderColor = GamingSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                errorMsg?.let {
                    Text(text = it, color = GamingAccentRed, fontSize = 11.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull()
                    if (amount == null || amount < 20.0) {
                        errorMsg = "সর্বনিম্ন ডিপোজিট ২০ টাকা"
                        return@Button
                    }
                    if (trxIdText.trim().length < 4) {
                        errorMsg = "সঠিক ট্রানজেকশন আইডি (TrxID) লিখুন"
                        return@Button
                    }
                    onSubmitDeposit(selectedMethod, amount, trxIdText.trim())
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = GamingAccentRed),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().testTag("btn_submit_deposit")
            ) {
                Text(text = "রিকোয়েস্ট পাঠান", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun WithdrawModal(
    userBalance: Double,
    userPhone: String,
    onDismiss: () -> Unit,
    onSubmitWithdraw: (method: String, withdrawPhone: String, amount: Double) -> Unit
) {
    var selectedMethod by remember { mutableStateOf("NAGAD") }
    var phoneText by remember { mutableStateOf(userPhone) }
    var amountText by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GamingCard,
        modifier = Modifier.testTag("withdraw_dialog"),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "Withdraw",
                        tint = RocketPurple,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "টাকা তুলুন (Withdraw)",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Method Selector: Nagad vs Rocket
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Nagad Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selectedMethod == "NAGAD") NagadRed.copy(alpha = 0.2f) else GamingCardElevated
                            )
                            .border(
                                1.dp,
                                if (selectedMethod == "NAGAD") NagadRed else GamingSurfaceBorder,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedMethod = "NAGAD" }
                            .padding(10.dp)
                            .testTag("withdraw_method_nagad"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "নগদ (Nagad)", color = NagadRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    // Rocket Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selectedMethod == "ROCKET") RocketPurple.copy(alpha = 0.2f) else GamingCardElevated
                            )
                            .border(
                                1.dp,
                                if (selectedMethod == "ROCKET") RocketPurple else GamingSurfaceBorder,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedMethod = "ROCKET" }
                            .padding(10.dp)
                            .testTag("withdraw_method_rocket"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "রকেট (Rocket)", color = RocketPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Balance Info
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(GamingCardElevated)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "উত্তোলনযোগ্য ব্যালেন্স:", color = TextMuted, fontSize = 11.sp)
                    Text(text = "৳${userBalance.toInt()}", color = GamingGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Phone Input
                OutlinedTextField(
                    value = phoneText,
                    onValueChange = { phoneText = it; errorMsg = null },
                    label = { Text("আপনার নগদ/রকেট নম্বর", fontSize = 11.sp) },
                    placeholder = { Text("017XXXXXXXX", fontSize = 11.sp, color = TextMuted) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_wit_phone"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RocketPurple,
                        unfocusedBorderColor = GamingSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                // Amount Input
                OutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it.filter { c -> c.isDigit() || c == '.' }
                        errorMsg = null
                    },
                    label = { Text("উইথড্র পরিমাণ (সর্বনিম্ন ৳৫০)", fontSize = 11.sp) },
                    placeholder = { Text("যেমন: 100", fontSize = 11.sp, color = TextMuted) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_wit_amount"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RocketPurple,
                        unfocusedBorderColor = GamingSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                errorMsg?.let {
                    Text(text = it, color = GamingAccentRed, fontSize = 11.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull()
                    if (amount == null || amount < 50.0) {
                        errorMsg = "সর্বনিম্ন উইথড্র ৫০ টাকা"
                        return@Button
                    }
                    if (amount > userBalance) {
                        errorMsg = "পর্যাপ্ত ব্যালেন্স নেই!"
                        return@Button
                    }
                    if (phoneText.trim().length < 11) {
                        errorMsg = "সঠিক ১১ ডিজিটের মোবাইল নম্বর দিন"
                        return@Button
                    }
                    onSubmitWithdraw(selectedMethod, phoneText.trim(), amount)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = RocketPurple),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().testTag("btn_submit_withdraw")
            ) {
                Text(text = "উইথড্র রিকোয়েস্ট পাঠান", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    )
}
