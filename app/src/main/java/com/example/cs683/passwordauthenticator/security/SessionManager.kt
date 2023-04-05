package com.example.cs683.passwordauthenticator.security

import android.content.Context
import com.example.cs683.passwordauthenticator.database.SessionManagement
import com.example.cs683.passwordauthenticator.database.SessionManagementDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SessionManager {

    companion object {
        fun startSession(
            sessionManagement: SessionManagementDao,
            passwordHasher: PasswordHasher,
            scope: CoroutineScope
        ) {
            scope.launch {
                val results = passwordHasher.generateSessionId()
                sessionManagement.addSession(SessionManagement(1, results.first, results.second))
            }
        }

    }
}