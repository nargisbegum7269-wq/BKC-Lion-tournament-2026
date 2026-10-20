package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String,
    val ffUid: String,
    val password: String,
    val balance: Double = 0.0,
    val totalKills: Int = 0,
    val totalMatches: Int = 0,
    val totalWinnings: Double = 0.0,
    val isAdmin: Boolean = false
)

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // "SOLO", "DUO", "SQUAD"
    val map: String, // "Bermuda", "Purgatory", "Kalahari", "Nexterra"
    val entryFee: Int,
    val perKill: Int,
    val prizePool: Int,
    val matchTime: String,
    val maxSlots: Int = 48,
    val joinedSlots: Int = 0,
    val roomId: String = "",
    val roomPassword: String = "",
    val status: String = "UPCOMING" // "UPCOMING", "ONGOING", "COMPLETED"
)

@Entity(tableName = "match_registrations")
data class MatchRegistrationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val matchId: Long,
    val userId: Long,
    val playerName: String,
    val playerFfUid: String,
    val slotNumber: Int,
    val registeredAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val type: String, // "DEPOSIT", "WITHDRAW", "ENTRY_FEE", "WINNING_PRIZE"
    val method: String, // "NAGAD", "ROCKET", "WALLET"
    val accountNumber: String,
    val trxId: String = "",
    val amount: Double,
    val status: String = "PENDING", // "PENDING", "APPROVED", "REJECTED"
    val createdAt: Long = System.currentTimeMillis(),
    val note: String = ""
)

@Entity(tableName = "proofs")
data class ProofEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val matchId: Long,
    val matchTitle: String,
    val userId: Long,
    val userName: String,
    val userFfUid: String,
    val kills: Int,
    val proofUrl: String,
    val status: String = "PENDING", // "PENDING", "APPROVED", "REJECTED"
    val rewardAmount: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis(),
    val adminNote: String = ""
)

@Entity(tableName = "admin_config")
data class AdminConfigEntity(
    @PrimaryKey val id: Int = 1,
    val nagadNumber: String = "01789123456",
    val rocketNumber: String = "01987654321",
    val adminPasscode: String = "1234"
)
