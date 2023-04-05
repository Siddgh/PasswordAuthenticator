package com.example.cs683.passwordauthenticator

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cs683.passwordauthenticator.database.AppDatabase
import com.example.cs683.passwordauthenticator.database.UserManagement
import com.example.cs683.passwordauthenticator.database.UserManagementDao

class PasswordApplication : Application() {
    lateinit var appDatabase: AppDatabase

    override fun onCreate() {
        super.onCreate()
        appDatabase =
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "users-db")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                }).fallbackToDestructiveMigration()
                .build()
    }
}