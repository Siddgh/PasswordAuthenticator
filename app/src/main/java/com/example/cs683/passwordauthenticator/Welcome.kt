package com.example.cs683.passwordauthenticator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cs683.passwordauthenticator.database.SessionManagement
import com.example.cs683.passwordauthenticator.database.SessionManagementDao
import com.example.cs683.passwordauthenticator.databinding.ActivityMainBinding
import com.example.cs683.passwordauthenticator.databinding.ActivityWelcomeBinding
import com.example.cs683.passwordauthenticator.security.PasswordHasher
import com.example.cs683.passwordauthenticator.security.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Welcome : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val passwordHasher = PasswordHasher()
        val sessionManagement =
            (application as PasswordApplication).appDatabase.sessionManagementDao()

        SessionManager.startSession(sessionManagement, passwordHasher, scope)
        Toast.makeText(baseContext, "Session Started", Toast.LENGTH_LONG).show()

        val username: String = intent.getStringExtra("userName").toString()
        binding.tvWelcome.text = "Welcome ${username}"


        binding.btnLogout.setOnClickListener {
            scope.launch {
                sessionManagement.deleteSession(passwordHasher.sessionId)
            }
            finish()
            startActivity(Intent(baseContext, MainActivity::class.java))
            Toast.makeText(baseContext, "Session Ended", Toast.LENGTH_LONG).show()
        }

    }
}