package com.example.cs683.passwordauthenticator.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_management")
data class UserManagement(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "username")
    var username: String,

    @ColumnInfo(name = "passwordHash")
    var passwordHash: String,

    @ColumnInfo(name = "salt")
    var salt: String
)