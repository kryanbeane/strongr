package com.strongr.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.strongr.controllers.FirebaseController
import com.strongr.databinding.ActivityLoginBinding
import com.strongr.main.MainApp
import com.strongr.models.TraineeModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlin.math.sign

class LoginActivity: AppCompatActivity() {
    // Used to bind this logic to the layout - activity_trainee.xml
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var dbController: FirebaseController
    private lateinit var app: MainApp
    private val tag = "LOGIN_ACTIVITY"


    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize view
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase stuff
        Firebase.initialize(this)
        auth = Firebase.auth
        db = Firebase.firestore
        dbController = FirebaseController()

        app = application as MainApp

        binding.signUp.setOnClickListener {
            // Sign up user with email and password
            completeSignUpFlow(binding.emailAddress.text.toString())

            // Switch to the workout activity
            startActivity(Intent(this, WorkoutListActivity::class.java))
        }

        binding.signIn.setOnClickListener {
            signIn(binding.emailAddress.text.toString(), binding.password.text.toString())
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            // Open workout activity
            val intent = Intent(this, WorkoutListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { result ->
                if (result.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(tag, "signInWithEmail:success")
                    val switchActivityIntent = Intent(this, WorkoutActivity::class.java)
                    startActivity(switchActivityIntent)
                }
                else {
                    Toast.makeText(this, result.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private suspend fun signUp(email: String, password: String): AuthResult {
        // Return the auth result but use await() in coroutine to wait for successful creation
        return auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { result ->
                if (result.isSuccessful) {
                    Toast.makeText(this, "Welcome $email!", Toast.LENGTH_SHORT).show()
                } else {
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
                            Log.d(tag, "createUserWithEmail:failure", result.exception)
                        }
                    }
                }
            }.await()
    }

    private fun completeSignUpFlow(emailAddress: String): Any = runBlocking {
        val user = signUp(emailAddress, binding.password.text.toString()).user

        if (user != null) {
            val returnPair = dbController.createTraineeRevised(emailAddress, user.uid)

            if (returnPair.first != null) {
                app.trainee = returnPair.first!!
            } else {
                Log.d(tag, "failed to create user ${returnPair.second}")

            }

        } else {
            Log.d(tag, "user is null")
        }
    }

}