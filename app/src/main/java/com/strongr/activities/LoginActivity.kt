package com.strongr.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.strongr.utils.parcelizeIntent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

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
            completeSignUpFlow(binding.emailAddress.text.toString(), binding.password.text.toString())
        }

        binding.signIn.setOnClickListener {
            signIn(binding.emailAddress.text.toString(), binding.password.text.toString())
        }
    }

    public override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null && currentUser.uid.isNotEmpty()) {

            val trainee = dbController.getTrainee(currentUser.uid)
            if (trainee != null) {
                app.trainee = trainee
                Toast.makeText(baseContext, "Welcome back ${trainee.fullName}", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, WorkoutListActivity::class.java))
                //startActivity(parcelizeIntent(this@LoginActivity, WorkoutListActivity(), "trainee", trainee))
                finish()

            } else {
                Toast.makeText(baseContext, "NOT welcome back ${currentUser.email}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(baseContext, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }


    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { result ->
                if (result.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Welcome back $email!", Toast.LENGTH_SHORT).show()

                    val trainee = dbController.getTrainee(auth.currentUser!!.uid)
                    if (trainee != null) {
                        app.trainee = trainee
                        startActivity(parcelizeIntent(this, WorkoutListActivity(), "trainee", trainee))
                        finish()
                    }
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
                            Toast.makeText(this@LoginActivity, "User already exists, try singing in!", Toast.LENGTH_SHORT).show()
                        }
                        // If the user enters a weak password
                        FirebaseAuthWeakPasswordException::class.java -> {
                            Toast.makeText(this@LoginActivity, "Password is too weak, try a stronger password!", Toast.LENGTH_SHORT).show()
                        }
                        // If the user enters an invalid email address
                        FirebaseAuthInvalidCredentialsException::class.java -> {
                            Toast.makeText(this@LoginActivity, "Invalid email address, try again!", Toast.LENGTH_SHORT).show()
                        }
                        // Some other arbitrary exception
                        else -> {
                            Toast.makeText(this@LoginActivity, result.exception!!.message, Toast.LENGTH_SHORT).show()
                            Log.d(tag, "createUserWithEmail:failure", result.exception)
                        }
                    }
                }
            }.await()
    }

    private fun completeSignUpFlow(emailAddress: String, password: String) = runBlocking {
        val user = signUp(emailAddress, password).user

        if (user != null) {
            val trainee = TraineeModel(emailAddress=emailAddress, id=user.uid)
            val exception = dbController.createTraineeRevised(trainee)

            if (exception == null) {
                app.trainee = trainee
                startActivity(parcelizeIntent(this@LoginActivity, WorkoutListActivity(), "trainee", trainee))
                finish()
            } else {
                // TODO Clean up any already made accounts to start again - delete auth account if db account wasn't created
                Toast.makeText(this@LoginActivity, "Error creating trainee $exception", Toast.LENGTH_LONG).show()
            }
        } else {
            Log.d(tag, "user is null, signup was unsuccessful")
        }
    }

}