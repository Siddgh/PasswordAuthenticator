package com.example.cs683.passwordauthenticator.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserManagementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(userManagement: UserManagement)

    @Query("SELECT salt FROM user_management where username = :userName")
    fun getSalt(userName: String): String?

    @Query("SELECT passwordHash FROM user_management where username = :userName")
    fun getSavedHash(userName: String): String?

}