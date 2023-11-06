package com.modiss.binar_challengech5.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.modiss.binar_challengech5.LoginActivity
import com.modiss.binar_challengech5.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val usersReference = databaseReference.child("users")
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.btEdit.setOnClickListener {
            if (isEditMode) {
                saveUserData()
                isEditMode = false
                binding.btEdit.text = "Edit Profile"
                disableEditing()
            } else {
                isEditMode = true
                binding.btEdit.text = "Simpan Profile"
                enableEditing()
            }
        }

        binding.btLogout.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            clearLoginSharedPreferences()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        getUserData()

        return binding.root
    }

    private fun clearLoginSharedPreferences() {
        val sharedPrefs: SharedPreferences = requireContext().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPrefs.edit()

        editor.clear()
        editor.apply()
    }

    private fun enableEditing() {
        binding.usernameEditText.isEnabled = true
        binding.emailEditText.isEnabled = true
        binding.phoneEditText.isEnabled = true
    }

    private fun disableEditing() {
        binding.usernameEditText.isEnabled = false
        binding.emailEditText.isEnabled = false
        binding.phoneEditText.isEnabled = false
    }

    private fun saveUserData() {
        val uid = currentUser?.uid

        if (uid != null) {
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val notlp = binding.phoneEditText.text.toString()

            val userUpdates = HashMap<String, Any>()
            userUpdates["username"] = username
            userUpdates["email"] = email
            userUpdates["notif"] = notlp

            usersReference.child(uid).updateChildren(userUpdates)
        }
    }

    private fun getUserData() {
        val uid = currentUser?.uid

        if (uid != null) {
            usersReference.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val username = snapshot.child("username").getValue(String::class.java)
                        val email = snapshot.child("email").getValue(String::class.java)
                        val notlp = snapshot.child("phonenumber").getValue(String::class.java)

                        binding.usernameEditText.setText(username)
                        binding.emailEditText.setText(email)
                        binding.phoneEditText.setText(notlp)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle kesalahan ketika membaca data
                }
            })
        }
    }
}
