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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Whatshot
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GamingAccentOrange
import com.example.ui.theme.GamingAccentRed
import com.example.ui.theme.GamingBg
import com.example.ui.theme.GamingCard
import com.example.ui.theme.GamingCardElevated
import com.example.ui.theme.GamingGold
import com.example.ui.theme.GamingSurfaceBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AuthScreenOverlay(
    onDismiss: () -> Unit,
    onLogin: (phone: String, pass: String) -> Unit,
    onRegister: (name: String, phone: String, ffUid: String, pass: String) -> Unit
) {
    var isLoginTab by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var ffUid by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clickable(enabled = false) {}
                .testTag("auth_screen_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GamingCard),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(GamingAccentRed.copy(alpha = 0.5f)),
                width = 1.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Spacer(modifier = Modifier.size(24.dp))
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(GamingAccentRed.copy(alpha = 0.15f))
                            .border(1.dp, GamingAccentRed.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = "Gamepad",
                            tint = GamingAccentRed,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "একাউন্টে প্রবেশ করুন",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "টুর্নামেন্টে যোগ দিতে লগইন বা রেজিস্টার করুন",
                    color = TextMuted,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Login / Register Tab Pill
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(GamingCardElevated)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isLoginTab) GamingAccentRed else Color.Transparent)
                            .clickable { isLoginTab = true; errorMessage = null }
                            .padding(vertical = 8.dp)
                            .testTag("auth_tab_login"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "লগইন",
                            color = if (isLoginTab) Color.White else TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (!isLoginTab) GamingAccentRed else Color.Transparent)
                            .clickable { isLoginTab = false; errorMessage = null }
                            .padding(vertical = 8.dp)
                            .testTag("auth_tab_register"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "রেজিস্টার",
                            color = if (!isLoginTab) Color.White else TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Input Fields
                if (!isLoginTab) {
                    // Name Field
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; errorMessage = null },
                        label = { Text("আপনার নাম", fontSize = 11.sp) },
                        placeholder = { Text("Ex: Pro Gamer", fontSize = 11.sp, color = TextMuted) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "Name", tint = TextMuted, modifier = Modifier.size(16.dp))
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("auth_input_name"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GamingAccentRed,
                            unfocusedBorderColor = GamingSurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = GamingCardElevated,
                            unfocusedContainerColor = GamingCardElevated
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Phone Field
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it; errorMessage = null },
                    label = { Text("মোবাইল নম্বর", fontSize = 11.sp) },
                    placeholder = { Text("017XXXXXXXX", fontSize = 11.sp, color = TextMuted) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = "Phone", tint = TextMuted, modifier = Modifier.size(16.dp))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("auth_input_phone"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GamingAccentRed,
                        unfocusedBorderColor = GamingSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = GamingCardElevated,
                        unfocusedContainerColor = GamingCardElevated
                    )
                )

                if (!isLoginTab) {
                    Spacer(modifier = Modifier.height(8.dp))
                    // FF UID
                    OutlinedTextField(
                        value = ffUid,
                        onValueChange = { ffUid = it.filter { c -> c.isDigit() }; errorMessage = null },
                        label = { Text("Free Fire UID", fontSize = 11.sp) },
                        placeholder = { Text("123456789", fontSize = 11.sp, color = TextMuted) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.SportsEsports, contentDescription = "UID", tint = TextMuted, modifier = Modifier.size(16.dp))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("auth_input_uid"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GamingAccentRed,
                            unfocusedBorderColor = GamingSurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = GamingCardElevated,
                            unfocusedContainerColor = GamingCardElevated
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Password Field
                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it; errorMessage = null },
                    label = { Text("পাসওয়ার্ড", fontSize = 11.sp) },
                    placeholder = { Text("******", fontSize = 11.sp, color = TextMuted) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Password", tint = TextMuted, modifier = Modifier.size(16.dp))
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password",
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("auth_input_pass"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GamingAccentRed,
                        unfocusedBorderColor = GamingSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = GamingCardElevated,
                        unfocusedContainerColor = GamingCardElevated
                    )
                )

                errorMessage?.let {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = it, color = GamingAccentRed, fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (phone.isBlank()) {
                            errorMessage = "মোবাইল নম্বর লিখুন"
                            return@Button
                        }
                        if (pass.isBlank()) {
                            errorMessage = "পাসওয়ার্ড লিখুন"
                            return@Button
                        }
                        if (isLoginTab) {
                            onLogin(phone, pass)
                        } else {
                            if (name.isBlank()) {
                                errorMessage = "আপনার নাম লিখুন"
                                return@Button
                            }
                            if (ffUid.isBlank()) {
                                errorMessage = "ফ্রি ফায়ার UID লিখুন"
                                return@Button
                            }
                            onRegister(name, phone, ffUid, pass)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("btn_auth_submit"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GamingAccentRed)
                ) {
                    Text(
                        text = if (isLoginTab) "লগইন করুন" else "রেজিস্ট্রেশন করুন",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Demo Login Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(GamingCardElevated)
                        .border(1.dp, GamingGold.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .clickable {
                            onLogin("01700000000", "123")
                        }
                        .padding(vertical = 8.dp, horizontal = 10.dp)
                        .testTag("btn_quick_demo_login"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚡ ডেমো একাউন্টে দ্রুত প্রবেশ করুন (01700000000)",
                        color = GamingGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
