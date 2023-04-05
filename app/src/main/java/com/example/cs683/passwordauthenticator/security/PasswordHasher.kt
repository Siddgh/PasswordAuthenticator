package com.example.cs683.passwordauthenticator.security

import android.os.Build
import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2KtResult
import com.lambdapioneer.argon2kt.Argon2Mode
import java.security.SecureRandom
import java.util.Base64

class PasswordHasher {
    lateinit var sessionId: String
    lateinit var timestamp: String

    fun generateSessionId(): Pair<String, String> {
        this.timestamp = System.currentTimeMillis().toString()
        val random = SecureRandom()
        val sessionId = ByteArray(20)
        random.nextBytes(sessionId)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.sessionId = Base64.getEncoder().encodeToString(sessionId)
            return Pair(this.sessionId, this.timestamp)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    companion object {
        private fun generateSalt(): ByteArray {
            val random = SecureRandom()
            val salt = ByteArray(16)
            random.nextBytes(salt)

            return salt
        }

        fun hashPassword(password: String): Pair<Argon2KtResult, String> {
            val argon2Kt = Argon2Kt()

            val salt = generateSalt();
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Pair(
                    argon2Kt.hash(
                        mode = Argon2Mode.ARGON2_I,
                        password = password.toByteArray(),
                        salt = salt,
                        tCostInIterations = 5,
                        mCostInKibibyte = 65536
                    ), Base64.getEncoder().encodeToString(salt)
                )
            } else {
                TODO("VERSION.SDK_INT < O")
            }

        }

        fun hashPassword(password: String, salt: ByteArray): Pair<Argon2KtResult, String> {
            val argon2Kt = Argon2Kt()

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Pair(
                    argon2Kt.hash(
                        mode = Argon2Mode.ARGON2_I,
                        password = password.toByteArray(),
                        salt = salt,
                        tCostInIterations = 5,
                        mCostInKibibyte = 65536
                    ), Base64.getEncoder().encodeToString(salt)
                )
            } else {
                TODO("VERSION.SDK_INT < O")
            }

        }
    }
}