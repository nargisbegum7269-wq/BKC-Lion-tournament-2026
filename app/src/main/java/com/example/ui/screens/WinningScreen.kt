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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MatchEntity
import com.example.data.ProofEntity
import com.example.ui.theme.GamingAccentRed
import com.example.ui.theme.GamingCard
import com.example.ui.theme.GamingCardElevated
import com.example.ui.theme.GamingEmerald
import com.example.ui.theme.GamingGold
import com.example.ui.theme.GamingSurfaceBorder
import com.example.ui.theme.RocketPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WinningScreen(
    matches: List<MatchEntity>,
    userProofs: List<ProofEntity>,
    preSelectedMatchId: Long?,
    onSubmitProof: (matchId: Long, matchTitle: String, kills: Int, proofUrl: String) -> Unit,
    onShowToast: (String) -> Unit
) {
    var selectedMatch by remember {
        mutableStateOf<MatchEntity?>(
            if (preSelectedMatchId != null) {
                matches.find { it.id == preSelectedMatchId } ?: matches.firstOrNull()
            } else {
                matches.firstOrNull()
            }
        )
    }

    LaunchedEffect(preSelectedMatchId, matches) {
        if (preSelectedMatchId != null) {
            val found = matches.find { it.id == preSelectedMatchId }
            if (found != null) {
                selectedMatch = found
            }
        }
    }

    var dropdownExpanded by remember { mutableStateOf(false) }
    var killsText by remember { mutableStateOf("") }
    var proofUrlText by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // Title Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(20.dp)
                        .background(GamingEmerald, RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "উইন ও প্রুফ সাবমিশন",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Submission Form Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("proof_submission_form"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GamingCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(GamingSurfaceBorder), width = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "ম্যাচে জয়ী হলে বা Booyah পেলে স্ক্রিনশট লিংক এবং কিল সংখ্যা দিয়ে পুরষ্কারের জন্য রিকোয়েস্ট পাঠান।",
                        color = TextMuted,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )

                    // Match Selector Dropdown
                    Text(text = "ম্যাচ নির্বাচন করুন", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(GamingCardElevated)
                            .border(1.dp, GamingSurfaceBorder, RoundedCornerShape(10.dp))
                            .clickable { dropdownExpanded = true }
                            .padding(12.dp)
                            .testTag("match_selector_dropdown")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedMatch?.title ?: "ম্যাচ সিলেক্ট করুন",
                                color = if (selectedMatch != null) TextPrimary else TextMuted,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Select",
                                tint = TextSecondary
                            )
                        }

                        DropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false },
                            modifier = Modifier.background(GamingCardElevated)
                        ) {
                            matches.forEach { match ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "${match.title} (${match.category})",
                                            color = TextPrimary,
                                            fontSize = 12.sp
                                        )
                                    },
                                    onClick = {
                                        selectedMatch = match
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Total Kills Input
                    Text(text = "মোট কিল (Total Kills)", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = killsText,
                        onValueChange = {
                            killsText = it.filter { char -> char.isDigit() }
                            errorMsg = null
                        },
                        placeholder = { Text("উদাহরণ: 5", color = TextMuted, fontSize = 12.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_proof_kills"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GamingEmerald,
                            unfocusedBorderColor = GamingSurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = GamingCardElevated,
                            unfocusedContainerColor = GamingCardElevated
                        )
                    )

                    // Screenshot URL Input
                    Text(text = "স্ক্রিনশট প্রুফ লিংক (Imgur/Drive Link)", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = proofUrlText,
                        onValueChange = { proofUrlText = it; errorMsg = null },
                        placeholder = { Text("https://imgur.com/... বা Google Drive লিংক", color = TextMuted, fontSize = 12.sp) },
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Link, contentDescription = "URL", tint = TextMuted, modifier = Modifier.size(16.dp))
                        },
                        modifier = Modifier.fillMaxWidth().testTag("input_proof_url"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GamingEmerald,
                            unfocusedBorderColor = GamingSurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = GamingCardElevated,
                            unfocusedContainerColor = GamingCardElevated
                        )
                    )

                    errorMsg?.let {
                        Text(text = it, color = GamingAccentRed, fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            val match = selectedMatch
                            if (match == null) {
                                errorMsg = "দয়া করে একটি ম্যাচ নির্বাচন করুন"
                                return@Button
                            }
                            val kills = killsText.toIntOrNull()
                            if (kills == null || kills < 0) {
                                errorMsg = "সঠিক কিল সংখ্যা লিখুন (ন্যূনতম 0)"
                                return@Button
                            }
                            if (proofUrlText.isBlank()) {
                                errorMsg = "স্ক্রিনশট প্রুফ লিংক দিন"
                                return@Button
                            }

                            onSubmitProof(match.id, match.title, kills, proofUrlText)
                            killsText = ""
                            proofUrlText = ""
                            errorMsg = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .testTag("btn_submit_proof"),
                        colors = ButtonDefaults.buttonColors(containerColor = GamingEmerald),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Submit", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "প্রুফ সাবমিট করুন", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Previous Proof Submissions
        item {
            Text(
                text = "আমার সাবমিট করা প্রুফসমূহ (${userProofs.size})",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (userProofs.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "এখনো কোনো প্রুফ সাবমিট করা হয়নি।",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            items(userProofs, key = { it.id }) { proof ->
                ProofHistoryCard(proof = proof)
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ProofHistoryCard(proof: ProofEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = GamingCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(GamingSurfaceBorder), width = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = proof.matchTitle,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                val (badgeColor, statusText) = when (proof.status) {
                    "APPROVED" -> Pair(GamingEmerald, "অনুমোদিত (৳${proof.rewardAmount.toInt()})")
                    "REJECTED" -> Pair(GamingAccentRed, "বাতিল")
                    else -> Pair(GamingGold, "পেন্ডিং (Pending)")
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = statusText,
                        color = badgeColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "মোট কিল: ${proof.kills} Kills",
                    color = GamingAccentRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )

                val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(proof.createdAt))
                Text(
                    text = dateStr,
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "প্রুফ লিংক: ${proof.proofUrl}",
                color = TextMuted,
                fontSize = 10.sp,
                maxLines = 1
            )

            if (proof.adminNote.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "এডমিন নোট: ${proof.adminNote}",
                    color = if (proof.status == "APPROVED") GamingEmerald else GamingAccentRed,
                    fontSize = 10.sp
                )
            }
        }
    }
}
