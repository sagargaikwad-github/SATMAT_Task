package com.example.satmattask.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.satmattask.view.activities.DashboardActivity
import com.example.satmattask.databinding.ActivitySignInBinding
import com.example.satmattask.databinding.DialogForgotPasswordBinding
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

            //ForgotPassword BTN
            signInForgotPassTV.setOnClickListener {
                val dialog = DialogForgotPasswordBinding.inflate(LayoutInflater.from(this@SignInActivity))
                var alertDialog = AlertDialog.Builder(this@SignInActivity)
                    .setView(dialog.root)
                    .create()
                alertDialog.show()
                dialog.forgotPasswordBTN.setOnClickListener {
                    val email = dialog.forgotPasswordEmailET.text.toString()
                    if(email.isNotEmpty())
                    {
                        resetPassword(email,alertDialog)
                    }else{
                        Utils.showToast(this@SignInActivity,"Email is required")
                    }
                }

                dialog.forgotPasswordBackTV.setOnClickListener {
                    alertDialog.dismiss()
                }
            }

        }
    }

    private fun loginUser(email: String, password: String) {
        Utils.showDialog(this)
        val firebaseAuth = FirebaseAuth.getInstance()

        lifecycleScope.launch {
            try {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val currentUser = firebaseAuth.currentUser?.uid
                            if (currentUser != null) {
                                val isEmailVerified =
                                    FirebaseAuth.getInstance().currentUser?.isEmailVerified
                                if (isEmailVerified == true) {
                                    FirebaseDatabase.getInstance().getReference("Users")
                                        .child(currentUser)
                                        .addListenerForSingleValueEvent(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {

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
                                else {
                                    Utils.hideDialog()
                                    Utils.showToast(
                                        this@SignInActivity,
                                        "Please verify email first"
                                    )
                                }
                            }
                        } else {
                            Utils.hideDialog()
                            Utils.showToast(this@SignInActivity,task.exception.toString())
                        }
                    }
            }catch (e: Exception)
            {
                Utils.hideDialog()
                Utils.showToast(this@SignInActivity,"An error occured!!")
            }
        }

    }

    private fun resetPassword(email: String, alertDialog: AlertDialog) {
        lifecycleScope.launch {
            try {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
                alertDialog.dismiss()
                Utils.showToast(this@SignInActivity,"Please check your email and reset the password")
            }catch (e: Exception)
            {
                Utils.showToast(this@SignInActivity,e.message.toString())
            }
        }

    }
}