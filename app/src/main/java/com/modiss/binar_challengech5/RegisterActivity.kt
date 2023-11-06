package com.modiss.binar_challengech5

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.modiss.binar_challengech5.databinding.ActivityRegisterBinding
import com.modiss.binar_challengech5.items.UserData

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val phoneNumber = binding.etPhoneNumber.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
                showError("Semua kolom harus diisi")
                return@setOnClickListener
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        if (user != null) {
                            saveUserDataToDatabase(user, username, phoneNumber)
                        } else {
                            showError("Gagal mendapatkan data pengguna")
                        }
                    } else {
                        showError(task.exception?.message ?: "Pendaftaran gagal")
                    }
                }
        }
    }

    private fun saveUserDataToDatabase(user: FirebaseUser, username: String, phoneNumber: String) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        val userId = user.uid

        val userData = UserData(username, user.email, phoneNumber)

        databaseReference.child(userId).setValue(userData)
            .addOnSuccessListener {
                showSuccess("Pendaftaran berhasil")
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                showError("Gagal menyimpan data pengguna ke database")
            }
    }

    private fun showError(errorMessage: String) {
        showSnackbar(errorMessage)
    }

    private fun showSuccess(message: String) {
        showSnackbar(message)
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        )
        snackbar.show()
    }
}

