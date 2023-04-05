package com.example.cs683.passwordauthenticator.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserManagement::class, SessionManagement::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userManagementDao(): UserManagementDao
    abstract fun sessionManagementDao(): SessionManagementDao
}