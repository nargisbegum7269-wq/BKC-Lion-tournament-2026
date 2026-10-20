package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // USERS
    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Long): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE phone = :phone LIMIT 1")
    suspend fun getUserByPhone(phone: String): UserEntity?

    @Query("SELECT * FROM users ORDER BY totalWinnings DESC, totalKills DESC LIMIT 50")
    fun getLeaderboard(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET balance = :newBalance WHERE id = :userId")
    suspend fun updateUserBalance(userId: Long, newBalance: Double)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int

    // MATCHES
    @Query("SELECT * FROM matches ORDER BY id DESC")
    fun getAllMatches(): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE id = :id")
    fun getMatchById(id: Long): Flow<MatchEntity?>

    @Query("SELECT * FROM matches WHERE id = :id")
    suspend fun getMatchByIdDirect(id: Long): MatchEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMatches(matches: List<MatchEntity>)

    @Update
    suspend fun updateMatch(match: MatchEntity)

    @Query("DELETE FROM matches WHERE id = :id")
    suspend fun deleteMatchById(id: Long)

    @Query("SELECT COUNT(*) FROM matches")
    suspend fun getMatchCount(): Int

    // REGISTRATIONS
    @Query("SELECT * FROM match_registrations WHERE userId = :userId ORDER BY id DESC")
    fun getRegistrationsForUser(userId: Long): Flow<List<MatchRegistrationEntity>>

    @Query("SELECT * FROM match_registrations WHERE matchId = :matchId AND userId = :userId LIMIT 1")
    suspend fun getRegistration(matchId: Long, userId: Long): MatchRegistrationEntity?

    @Query("SELECT * FROM match_registrations WHERE matchId = :matchId")
    fun getRegistrationsForMatch(matchId: Long): Flow<List<MatchRegistrationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistration(registration: MatchRegistrationEntity): Long

    // TRANSACTIONS
    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY createdAt DESC")
    fun getTransactionsForUser(userId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY createdAt DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = 'DEPOSIT' AND status = 'PENDING' ORDER BY createdAt DESC")
    fun getPendingDeposits(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = 'WITHDRAW' AND status = 'PENDING' ORDER BY createdAt DESC")
    fun getPendingWithdrawals(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(trx: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(trx: TransactionEntity)

    // PROOFS
    @Query("SELECT * FROM proofs ORDER BY createdAt DESC")
    fun getAllProofs(): Flow<List<ProofEntity>>

    @Query("SELECT * FROM proofs WHERE userId = :userId ORDER BY createdAt DESC")
    fun getProofsForUser(userId: Long): Flow<List<ProofEntity>>

    @Query("SELECT * FROM proofs WHERE id = :id")
    suspend fun getProofById(id: Long): ProofEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProof(proof: ProofEntity): Long

    @Update
    suspend fun updateProof(proof: ProofEntity)

    // ADMIN CONFIG
    @Query("SELECT * FROM admin_config WHERE id = 1 LIMIT 1")
    fun getAdminConfig(): Flow<AdminConfigEntity?>

    @Query("SELECT * FROM admin_config WHERE id = 1 LIMIT 1")
    suspend fun getAdminConfigDirect(): AdminConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAdminConfig(config: AdminConfigEntity)
}
