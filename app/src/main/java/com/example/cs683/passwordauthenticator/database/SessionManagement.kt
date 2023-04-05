package com.example.cs683.passwordauthenticator.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_management")
data class SessionManagement(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "sessionId")
    var sessionId: String,

    @ColumnInfo(name = "timestamp")
    var timestamp: String
)