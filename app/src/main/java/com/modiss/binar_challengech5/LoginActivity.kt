package com.modiss.binar_challengech5

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.modiss.binar_challengech5.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty()) {
                binding.etUsername.error = "Email tidak boleh kosong"
                binding.etUsername.requestFocus()
                return@setOnClickListener
            } else if (password.isEmpty()) {
                binding.etPassword.error = "Password tidak boleh kosong"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser

                        val editor = sharedPreferences.edit()
                        editor.putBoolean("isLoggedIn", true)
                        editor.apply()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val errorMessage = task.exception?.message ?: "Terjadi kesalahan saat otentikasi."
                        showError(errorMessage)
                    }
                }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showError(errorMessage: String) {
        val snackbar = Snackbar.make(
            binding.root,
            errorMessage,
            Snackbar.LENGTH_LONG
        )
        snackbar.show()
    }
}
