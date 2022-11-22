package com.strongr.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.ajalt.timberkt.Timber
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.strongr.controllers.FirebaseController
import com.strongr.databinding.ActivityLoginBinding
import timber.log.Timber.i

class LoginActivity: AppCompatActivity() {
    // Used to bind this logic to the layout - activity_trainee.xml
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var dbController: FirebaseController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.initialize(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = Firebase.firestore
        dbController = FirebaseController()

        Timber.plant(Timber.DebugTree())
        i("Trainee Activity Started Successfully...")


        binding.signUp.setOnClickListener() {
            val emailAddress = binding.emailAddress.text.toString()
            Toast.makeText(this, "Signing Up... with email $emailAddress", Toast.LENGTH_SHORT).show()
            dbController.addTrainee(emailAddress)
            signUp(binding.emailAddress.text.toString(), binding.password.text.toString())
        }

        binding.signIn.setOnClickListener() {
            signIn(binding.emailAddress.text.toString(), binding.password.text.toString())
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            // Open workout activity
            val intent = Intent(this, WorkoutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { result ->
                if (result.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    i("Welcome to Strongr ${auth.currentUser?.email}")
                    val user = auth.currentUser
                    val switchActivityIntent = Intent(this, WorkoutActivity::class.java)
                    startActivity(switchActivityIntent)
                }
                else {
                    Toast.makeText(this, result.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { result ->
                if (result.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    i("Welcome to Strongr ${auth.currentUser?.email}")
                    val user = auth.currentUser
                    val switchActivityIntent = Intent(this, WorkoutActivity::class.java)
                    startActivity(switchActivityIntent)
                }
                else {
                    when (result.exception!!.javaClass) {
                        // If the user enters an email address that is already in use
                        FirebaseAuthUserCollisionException::class.java -> {
                            Toast.makeText(this, "User already exists, try singing in!", Toast.LENGTH_SHORT).show()
                        }
                        // If the user enters a weak password
                        FirebaseAuthWeakPasswordException::class.java -> {
                            Toast.makeText(this, "Password is too weak, try a stronger password!", Toast.LENGTH_SHORT).show()
                        }
                        // If the user enters an invalid email address
                        FirebaseAuthInvalidCredentialsException::class.java -> {
                            Toast.makeText(this, "Invalid email address, try again!", Toast.LENGTH_SHORT).show()
                        }
                        // Some other arbitrary exception
                        else -> {
                            Toast.makeText(this, result.exception!!.message, Toast.LENGTH_SHORT).show()
                            i(result.exception)
                        }
                    }
                }
            }
    }

}