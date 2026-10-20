package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MatchEntity
import com.example.data.MatchRegistrationEntity
import com.example.data.UserEntity
import com.example.ui.theme.GamingAccentOrange
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

@Composable
fun MatchesScreen(
    matches: List<MatchEntity>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    currentUser: UserEntity?,
    userRegistrations: List<MatchRegistrationEntity>,
    onJoinMatch: (match: MatchEntity, inGameName: String, ffUid: String) -> Unit,
    onRequireLogin: () -> Unit,
    onShowToast: (String) -> Unit
) {
    var matchToJoin by remember { mutableStateOf<MatchEntity?>(null) }
    var matchForRoomInfo by remember { mutableStateOf<MatchEntity?>(null) }

    val filteredMatches = if (selectedCategory == "ALL") {
        matches
    } else {
        matches.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    val joinedMatchIds = remember(userRegistrations) {
        userRegistrations.map { it.matchId }.toSet()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Category Filter Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(GamingCard)
                .border(1.dp, GamingSurfaceBorder, RoundedCornerShape(12.dp))
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "ক্যাটাগরি:",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf("ALL", "SOLO", "DUO", "SQUAD").forEach { category ->
                    val isSelected = selectedCategory.equals(category, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) GamingAccentRed else GamingCardElevated
                            )
                            .clickable { onCategorySelected(category) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("filter_$category"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredMatches.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Gamepad,
                        contentDescription = "No Matches",
                        tint = TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "এই ক্যাটাগরিতে বর্তমানে কোনো ম্যাচ নেই",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredMatches, key = { it.id }) { match ->
                    val isJoined = joinedMatchIds.contains(match.id)
                    MatchCardItem(
                        match = match,
                        isJoined = isJoined,
                        onJoinClick = {
                            if (currentUser == null) {
                                onRequireLogin()
                            } else {
                                matchToJoin = match
                            }
                        },
                        onViewRoomClick = {
                            matchForRoomInfo = match
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    // Join Match Confirmation Dialog
    matchToJoin?.let { match ->
        JoinMatchDialog(
            match = match,
            currentUser = currentUser,
            onDismiss = { matchToJoin = null },
            onConfirmJoin = { ign, uid ->
                onJoinMatch(match, ign, uid)
                matchToJoin = null
            }
        )
    }

    // Room ID & Password Modal
    matchForRoomInfo?.let { match ->
        RoomCredentialsDialog(
            match = match,
            onDismiss = { matchForRoomInfo = null },
            onShowToast = onShowToast
        )
    }
}

@Composable
fun MatchCardItem(
    match: MatchEntity,
    isJoined: Boolean,
    onJoinClick: () -> Unit,
    onViewRoomClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("match_card_${match.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GamingCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.verticalGradient(
                listOf(
                    if (isJoined) GamingEmerald.copy(alpha = 0.6f) else GamingSurfaceBorder,
                    GamingSurfaceBorder
                )
            ),
            width = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            // Header Row: Map, Category, Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Category Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(GamingAccentRed.copy(alpha = 0.15f))
                            .border(1.dp, GamingAccentRed.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = match.category,
                            color = GamingAccentRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Map Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(GamingCardElevated)
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = "Map",
                                tint = TextMuted,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = match.map,
                                color = TextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Status Tag
                val statusColor = when (match.status) {
                    "UPCOMING" -> RocketPurple
                    "ONGOING" -> GamingAccentRed
                    else -> GamingEmerald
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = when (match.status) {
                            "UPCOMING" -> "আসন্ন (Upcoming)"
                            "ONGOING" -> "চলমান (Live)"
                            else -> "সমাপ্ত"
                        },
                        color = statusColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Match Title
            Text(
                text = match.title,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            // Match Time
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Time",
                    tint = TextMuted,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = match.matchTime,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Metrics Grid: Entry Fee, Per Kill, Prize Pool
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(GamingCardElevated)
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MetricColumn(title = "এন্ট্রি ফি", value = "৳${match.entryFee}", valueColor = GamingGold)
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp)
                        .background(GamingSurfaceBorder)
                )
                MetricColumn(title = "প্রতি কিল", value = "৳${match.perKill}", valueColor = GamingAccentOrange)
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp)
                        .background(GamingSurfaceBorder)
                )
                MetricColumn(title = "মোট পুরষ্কার", value = "৳${match.prizePool}", valueColor = GamingEmerald)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Slots Progress Bar
            val progress = (match.joinedSlots.toFloat() / match.maxSlots.toFloat()).coerceIn(0f, 1f)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "স্লট বুকিং (${match.joinedSlots}/${match.maxSlots})",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${((progress * 100).toInt())}% পূর্ণ",
                    color = if (progress >= 1f) GamingAccentRed else TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (progress >= 1f) GamingAccentRed else GamingAccentOrange,
                trackColor = GamingCardElevated
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action Button Row
            if (isJoined) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(GamingEmerald.copy(alpha = 0.15f))
                            .border(1.dp, GamingEmerald.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Joined",
                                tint = GamingEmerald,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "জয়েন করেছেন",
                                color = GamingEmerald,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = onViewRoomClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .testTag("btn_room_info_${match.id}"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RocketPurple)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = "Room Key",
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "রুম কোড", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                val isFull = match.joinedSlots >= match.maxSlots
                Button(
                    onClick = onJoinClick,
                    enabled = !isFull && match.status != "COMPLETED",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("btn_join_match_${match.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GamingAccentRed,
                        disabledContainerColor = GamingCardElevated
                    )
                ) {
                    Text(
                        text = when {
                            match.status == "COMPLETED" -> "ম্যাচ সমাপ্ত"
                            isFull -> "স্লট পূর্ণ (FULL)"
                            else -> "ম্যাচে জয়েন করুন (৳${match.entryFee})"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isFull || match.status == "COMPLETED") TextMuted else Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun MetricColumn(title: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            color = TextMuted,
            fontSize = 10.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            color = valueColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun JoinMatchDialog(
    match: MatchEntity,
    currentUser: UserEntity?,
    onDismiss: () -> Unit,
    onConfirmJoin: (ign: String, uid: String) -> Unit
) {
    var ign by remember { mutableStateOf(currentUser?.name ?: "") }
    var uid by remember { mutableStateOf(currentUser?.ffUid ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GamingCard,
        title = {
            Text(
                text = "ম্যাচে জয়েন কনফার্ম করুন",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "${match.title} (${match.category} - ${match.map})",
                    color = GamingAccentOrange,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(GamingCardElevated)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "এন্ট্রি ফি:", color = TextMuted, fontSize = 12.sp)
                    Text(text = "৳${match.entryFee}", color = GamingGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(GamingCardElevated)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "আপনার ব্যালেন্স:", color = TextMuted, fontSize = 12.sp)
                    Text(
                        text = "৳${currentUser?.balance?.toInt() ?: 0}",
                        color = if ((currentUser?.balance ?: 0.0) >= match.entryFee) GamingEmerald else GamingAccentRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedTextField(
                    value = ign,
                    onValueChange = { ign = it; errorMessage = null },
                    label = { Text("ফ্রি ফায়ার ইন-গেম নাম (IGN)", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_join_ign"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GamingAccentRed,
                        unfocusedBorderColor = GamingSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                OutlinedTextField(
                    value = uid,
                    onValueChange = { uid = it; errorMessage = null },
                    label = { Text("ফ্রি ফায়ার প্লেয়ার UID", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_join_uid"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GamingAccentRed,
                        unfocusedBorderColor = GamingSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                errorMessage?.let {
                    Text(text = it, color = GamingAccentRed, fontSize = 11.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (ign.isBlank()) {
                        errorMessage = "ইন-গেম নাম লিখুন"
                        return@Button
                    }
                    if (uid.isBlank()) {
                        errorMessage = "ফ্রি ফায়ার UID লিখুন"
                        return@Button
                    }
                    if ((currentUser?.balance ?: 0.0) < match.entryFee) {
                        errorMessage = "ব্যালেন্স অপর্যাপ্ত! ওয়ালেটে টাকা অ্যাড করুন।"
                        return@Button
                    }
                    onConfirmJoin(ign, uid)
                },
                colors = ButtonDefaults.buttonColors(containerColor = GamingAccentRed),
                modifier = Modifier.testTag("btn_confirm_join")
            ) {
                Text("জয়েন করুন", color = Color.White, fontWeight = FontWeight.Bold)
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
fun RoomCredentialsDialog(
    match: MatchEntity,
    onDismiss: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GamingCard,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Key,
                    contentDescription = "Room Key",
                    tint = RocketPurple,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "কাস্টম রুম ডিটেইলস",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = match.title,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (match.roomId.isNotBlank()) {
                    // Room ID Card
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(GamingCardElevated)
                            .border(1.dp, GamingSurfaceBorder, RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "রুম আইডি (Room ID):", color = TextMuted, fontSize = 10.sp)
                            Text(
                                text = match.roomId,
                                color = GamingGold,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        Button(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(match.roomId))
                                onShowToast("রুম আইডি কপি করা হয়েছে!")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GamingCard),
                            modifier = Modifier.height(34.dp).testTag("copy_room_id")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = GamingGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "কপি", color = GamingGold, fontSize = 11.sp)
                        }
                    }

                    // Room Password Card
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(GamingCardElevated)
                            .border(1.dp, GamingSurfaceBorder, RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "পাসওয়ার্ড (Password):", color = TextMuted, fontSize = 10.sp)
                            Text(
                                text = match.roomPassword.ifBlank { "পাসওয়ার্ড নেই" },
                                color = GamingAccentRed,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        if (match.roomPassword.isNotBlank()) {
                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(match.roomPassword))
                                    onShowToast("পাসওয়ার্ড কপি করা হয়েছে!")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GamingCard),
                                modifier = Modifier.height(34.dp).testTag("copy_room_pass")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = GamingAccentRed,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "কপি", color = GamingAccentRed, fontSize = 11.sp)
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(GamingCardElevated)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                tint = TextMuted,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "রুম আইডি এখনো প্রকাশ করা হয়নি",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "ম্যাচ শুরুর ১০-১৫ মিনিট আগে এখানে রুম আইডি ও পাসওয়ার্ড দেওয়া হবে।",
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = RocketPurple)
            ) {
                Text("ঠিক আছে", color = Color.White)
            }
        }
    )
}
