package com.example.cs683.passwordauthenticator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.cs683.passwordauthenticator.database.UserManagement
import com.example.cs683.passwordauthenticator.databinding.ActivitySignupBinding
import com.example.cs683.passwordauthenticator.security.PasswordHasher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Signup : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding;

    // Checks if username has only letters and numbers
    private val onlyLettersAndNumbersRegex = Regex("^[a-zA-Z0-9]+$")

    // Checks if password has atleast 1 Uppercase, Lowercase, Number and Special Character
    private val uppercaseRegex = Regex(".*[A-Z].*")
    private val lowercaseRegex = Regex(".*[a-z].*")
    private val numberRegex = Regex(".*\\d.*")
    private val specialCharRegex = Regex(".*[@\$!%*?&].*")

    //Check if the password is atleast 16 characters in length
    private val lengthRegex = Regex(".{16,}")

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userManagementDao = (application as PasswordApplication).appDatabase.userManagementDao()

        val usernameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (onlyLettersAndNumbersRegex.matches(s.toString())) {
                    binding.tvUsernameIndicatorSignup.text =
                        "✅ Username can only contain letters and numbers"
                } else {
                    binding.tvUsernameIndicatorSignup.text =
                        "❌ Username can only contain letters and numbers"
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }


        val passwordTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val messageToDisplay = ""
                val stringBuilder = StringBuilder(messageToDisplay)

                if (lengthRegex.matches(s.toString())) {
                    stringBuilder.append("✅ Password should be minimum 16 character long\n\n")
                } else {
                    stringBuilder.append("❌ Password should be minimum 16 character long\n\n")
                }

                stringBuilder.append("Password Should Contain:\n")
                if (uppercaseRegex.matches(s.toString())) {
                    stringBuilder.append("✅ - 1 upper case letter\n")
                } else {
                    stringBuilder.append("❌ - 1 upper case letter\n")
                }

                if (lowercaseRegex.matches(s.toString())) {
                    stringBuilder.append("✅ - 1 lower case letter\n")
                } else {
                    stringBuilder.append("❌ - 1 lower case letter\n")
                }

                if (numberRegex.matches(s.toString())) {
                    stringBuilder.append("✅ - 1 number\n")
                } else {
                    stringBuilder.append("❌ - 1 number\n")
                }

                if (specialCharRegex.matches(s.toString())) {
                    stringBuilder.append("✅ - 1 special character\n")
                } else {
                    stringBuilder.append("❌ - 1 special character\n")
                }

                binding.tvPasswordIndicatorSignup.text = stringBuilder.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        val repasswordTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.contentEquals(binding.etPasswordSignup.text)) {
                    binding.tvRepasswordIndicator.text = "✅ Password Matches"
                } else {
                    binding.tvRepasswordIndicator.text = "❌ Password Doesn't Match"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }

        binding.etUsernameSignup.addTextChangedListener(usernameTextWatcher)
        binding.etPasswordSignup.addTextChangedListener(passwordTextWatcher)
        binding.etRepasswordSignup.addTextChangedListener(repasswordTextWatcher)

        binding.btnSignupSignup.setOnClickListener {
            if (canSignUp()) {
                val hashedPassword =
                    PasswordHasher.hashPassword(binding.etPasswordSignup.text.toString())

                scope.launch {
                    userManagementDao.addUser(
                        UserManagement(
                            id = 1,
                            username = binding.etUsernameSignup.text.toString(),
                            passwordHash = hashedPassword.first.encodedOutputAsString(),
                            salt = hashedPassword.second
                        )
                    )
                }

                Toast.makeText(baseContext, "Sign Up Successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(baseContext, MainActivity::class.java))
                finish()

            } else {
                Toast.makeText(
                    baseContext,
                    "Can't signup. Please check the username and password details and try again",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun canSignUp(): Boolean {
        return (isRePasswordValid() && isPasswordValid() && isUsernameValid())
    }

    private fun isRePasswordValid(): Boolean {
        return binding.etRepasswordSignup.text.toString()
            .contentEquals(binding.etPasswordSignup.text.toString())
    }

    private fun isPasswordValid(): Boolean {
        return (uppercaseRegex.matches(binding.etPasswordSignup.text.toString()) && lowercaseRegex.matches(
            binding.etPasswordSignup.text.toString()
        ) && numberRegex.matches(
            binding.etPasswordSignup.text.toString()
        ) && specialCharRegex.matches(binding.etPasswordSignup.text.toString()) && lengthRegex.matches(
            binding.etPasswordSignup.text.toString()
        ))
    }

    private fun isUsernameValid(): Boolean {
        val username = binding.etUsernameSignup.text.toString()
        return onlyLettersAndNumbersRegex.matches(username)
    }

}