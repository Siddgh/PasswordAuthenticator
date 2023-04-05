package com.example.cs683.passwordauthenticator.database

import androidx.room.*

@Dao
interface SessionManagementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSession(sessionManagement: SessionManagement)

    @Query("DELETE FROM session_management WHERE sessionId = :sessionId")
    fun deleteSession(sessionId: String)
}