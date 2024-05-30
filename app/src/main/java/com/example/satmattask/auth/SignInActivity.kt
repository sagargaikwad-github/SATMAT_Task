package com.example.satmattask.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.satmattask.DashboardActivity
import com.example.satmattask.R
import com.example.satmattask.databinding.ActivitySignInBinding
import com.example.satmattask.databinding.ActivitySignUpBinding
import com.example.satmattask.model.Users
import com.example.satmattask.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            signInLoginBTN.setOnClickListener {
                val email = signInEmailET.text.toString()
                val password = signInPasswordET.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    loginUser(email, password)
                }
            }

            signInRegisterTV.setOnClickListener {
                startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
                finish()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        Utils.showDialog(this)
        val firebaseAuth = FirebaseAuth.getInstance()

        lifecycleScope.launch {
            try {
                val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val currentUser = authResult.user!!.uid

                if (currentUser != null) {
                    FirebaseDatabase.getInstance().getReference("Users").child(currentUser)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val user = snapshot.getValue(Users::class.java)

                                startActivity(
                                    Intent(
                                        this@SignInActivity,
                                        DashboardActivity::class.java
                                    )
                                )
                                finish()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Utils.hideDialog()
                                Utils.showToast(this@SignInActivity, error.message)
                            }

                        })
                }
            } catch (e: Exception) {
                Utils.hideDialog()
                Utils.showToast(this@SignInActivity, e.message!!)
            }
        }

    }
}