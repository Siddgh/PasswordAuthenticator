package com.example.cs683.passwordauthenticator

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.cs683.passwordauthenticator.databinding.ActivityMainBinding
import com.example.cs683.passwordauthenticator.security.PasswordHasher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.Base64.getDecoder
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val scope = CoroutineScope(Dispatchers.IO)
    private var loginCount = 3;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userManagementDao = (application as PasswordApplication).appDatabase.userManagementDao()

        binding.btnSignupMain.setOnClickListener {
            startActivity(Intent(baseContext, Signup::class.java))
            finish()
        }

        binding.btnLoginMain.setOnClickListener {
            val username = binding.etUsernameMain.text.toString()
            var password = binding.etPasswordMain.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                scope.launch {
                    val salt = userManagementDao.getSalt(username) ?: "nan"
                    val savedHash = userManagementDao.getSavedHash(username) ?: "nan"

                    val saltByteArray: ByteArray
                    if (!salt.contentEquals("nan") && !savedHash.contentEquals("nan")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            saltByteArray = Base64.getDecoder().decode(salt)

                            performLogin(
                                verifyHashedPassword(
                                    PasswordHasher.hashPassword(
                                        password,
                                        saltByteArray
                                    ).first.encodedOutputAsString(), savedHash
                                )
                            )
                        }
                    } else {
                        performLogin(false)
                    }
                    password = "00000"
                }
            }
        }
    }

    private fun verifyHashedPassword(inputHash: String, savedHash: String): Boolean {
        return inputHash.contentEquals(savedHash)
    }

    private fun performLogin(loginStatus: Boolean) {
        if (!loginStatus) {

            val timeTaken = measureTimeMillis {
                //run a dummy check through password hasher that mimicks password match
                verifyHashedPassword(
                    PasswordHasher.hashPassword("*Test12345678910*").first.encodedOutputAsString(),
                    PasswordHasher.hashPassword("*Test12345678910*").first.encodedOutputAsString()
                )

                loginCount -= 1
                runOnUiThread {
                    binding.tvTimer.text = "You have ${loginCount} Login Attempts"
                }

                if (loginCount == 0) {
                    runOnUiThread {
                        disableLogin()
                    }
                }
            }

            Log.d("SIDD", "Time Taken when Password isn't Matched :- $timeTaken")
        } else {
            val timeTaken = measureTimeMillis {

                verifyHashedPassword(
                    PasswordHasher.hashPassword("*Test12345678910*").first.encodedOutputAsString(),
                    PasswordHasher.hashPassword("*Test12345678910*").first.encodedOutputAsString()
                )
            }
            Log.d("SIDD", "Time Taken when Password Matched :- $timeTaken")
            runOnUiThread {
                val intent = Intent(baseContext, Welcome::class.java)
                intent.putExtra("userName", binding.etUsernameMain.text.toString())
                startActivity(intent)
                finish()
            }
        }
    }

    private fun disableLogin() {
        binding.btnLoginMain.isClickable = false
        binding.btnSignupMain.isClickable = false
        val countDownTimer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                val timeLeftFormatted = String.format("%02d:%02d", minutes, seconds)
                binding.tvTimer.text =
                    "Login Functionality is disabled since you exceeded password attempts.\n\nTime Left : ${timeLeftFormatted}"
            }

            override fun onFinish() {
                binding.btnLoginMain.isClickable = true
                binding.btnSignupMain.isClickable = true
                loginCount = 3
                binding.tvTimer.text = "You have ${loginCount} Login Attempts"
            }
        }

        countDownTimer.start()
    }


}