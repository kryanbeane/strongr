package com.strongr.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.strongr.databinding.ActivityAccountCreateBinding
import com.strongr.models.TraineeModel
import timber.log.Timber
import timber.log.Timber.i
import java.util.*

class CreateAccountActivity: AppCompatActivity() {

    // Used to bind this logic to the layout - activity_trainee.xml
    private lateinit var binding: ActivityAccountCreateBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var trainee: TraineeModel

    private var trainees = ArrayList<TraineeModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.initialize(this)
        binding = ActivityAccountCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        Timber.plant(Timber.DebugTree())
        i("Trainee Activity Started Successfully...")



        // Use the binding to add an on click listener to the add trainee button
        binding.createAccount.setOnClickListener() {
            val emailAddress = binding.emailAddress.text.toString()
            when {
                isValidEmail(emailAddress) -> i("Email Address: $emailAddress")
                else -> Snackbar.make(it, "Please enter an email address", Snackbar.LENGTH_LONG).show()
            }


        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            // TODO(): Will want to push the user to the dashboard screen here as they are already logged in
            i("User is already logged in")
        }
    }


    private fun isValidEmail(email: String): Boolean {
        if (email.isNotEmpty()) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
        return false
    }

}

