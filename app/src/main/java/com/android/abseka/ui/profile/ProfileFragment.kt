package com.android.abseka.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.abseka.data.Absensi
import com.android.abseka.databinding.FragmentProfileBinding
import com.android.abseka.ui.language.LanguageActivity
import com.android.abseka.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth

        databaseReference = FirebaseDatabase.getInstance().getReference("absensi")

        showUsername()

        binding.apply {
            logout.setOnClickListener {
                auth.signOut()

                Intent(context, LoginActivity::class.java).also {
                    startActivity(it)
                    requireActivity().finish()
                }
            }

            changeLanguage.setOnClickListener {
                Intent(context, LanguageActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    private fun showUsername() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userEmail = currentUser.email

            if (!userEmail.isNullOrBlank()) {
                binding.tvUsername.text = userEmail
            } else {
                binding.tvUsername.text = "-"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}