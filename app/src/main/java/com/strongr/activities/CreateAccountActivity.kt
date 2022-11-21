package com.strongr.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.AuthUI.IdpConfig.EmailBuilder
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.strongr.R
import com.strongr.databinding.ActivityAccountCreateBinding
import com.strongr.models.TraineeModel
import timber.log.Timber
import timber.log.Timber.i
import java.util.*


class CreateAccountActivity: AppCompatActivity() {

    val providers = arrayListOf(
        EmailBuilder().build(),
        IdpConfig.GoogleBuilder().build(),
    )
    // Used to bind this logic to the layout - activity_trainee.xml
    private lateinit var binding: ActivityAccountCreateBinding
    private lateinit var auth: FirebaseAuth
    private var trainees = ArrayList<TraineeModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Firebase.initialize(this)
        auth = Firebase.auth

        Timber.plant(Timber.DebugTree())
        i("Trainee Activity Started Successfully...")


        // Use the binding to add an on click listener to the add trainee button
        binding.createAccount.setOnClickListener() {
            createAccount(binding.emailAddress.text.toString(), binding.password.text.toString())
        }

    }

    private fun createGoogleSignInThing() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
    }


    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            // TODO(): Will want to push the user to the dashboard screen here as they are already logged in
            i("User is already logged in")
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    i("Welcome to Strongr ${auth.currentUser?.email}")
                    val user = auth.currentUser
                    // TODO(): updateUI(user)
                    val switchActivityIntent = Intent(this, LoginActivity::class.java)
                    startActivity(switchActivityIntent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, task.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}

