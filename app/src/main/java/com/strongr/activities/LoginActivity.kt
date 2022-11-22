package com.strongr.activities

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.ajalt.timberkt.Timber
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.strongr.BuildConfig
import com.strongr.R
import com.strongr.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity() {
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private companion object {
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 9001
        private const val RC_ONE_TAP = 9002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.API_KEY)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // Initialize firebase auth
        auth = Firebase.auth
        checkUser()

        // Button click to start
        binding.logo.setOnClickListener {
            Toast.makeText(this, "Signing in...", Toast.LENGTH_SHORT).show()
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }


    private fun checkUser() {

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN) {
            Toast.makeText(this, "Signing in...", Toast.LENGTH_SHORT).show()
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = accountTask.getResult(Exception::class.java)
                firebaseAuthWithGoogleAccount(account)
            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Toast.makeText(this, "Signing in...", Toast.LENGTH_SHORT).show()

        val credentials = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credentials)
            .addOnSuccessListener { authResult ->
                Toast.makeText(this, "Signed in successfully", Toast.LENGTH_SHORT).show()
                val firebaseUser = auth.currentUser
                val uid = firebaseUser?.uid
                val email = firebaseUser?.email
                Toast.makeText(this, "User: $uid, $email", Toast.LENGTH_SHORT).show()

                if (authResult.additionalUserInfo!!.isNewUser) {
                    // User is new, show them a fancy intro screen!
                    Toast.makeText(this@LoginActivity, "New user", Toast.LENGTH_SHORT).show()
                } else {
                    // User is old, show them a welcome back screen.
                    Toast.makeText(this, "Old user", Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

}

