package com.strongr.activities.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.strongr.R
import com.strongr.activities.dashboard.DashboardActivity
import com.strongr.databinding.ActivityLoginBinding
import com.strongr.main.MainApp
import com.strongr.models.trainee.TraineeModel
import com.strongr.utils.parcelizeTraineeIntent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class LoginActivity: AppCompatActivity() {
    // Used to bind this logic to the layout - activity_trainee.xml
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var app: MainApp
    private val tag = "LOGIN_ACTIVITY"
    var gsoClient = MutableLiveData<GoogleSignInClient>()
    private lateinit var startForResult : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Firebase.initialize(this)
        auth = Firebase.auth
        db = Firebase.firestore

        app = application as MainApp

        binding.signUp.setOnClickListener {
            completeSignUpFlow(binding.emailAddress.text.toString(), binding.password.text.toString())
        }

        binding.signIn.setOnClickListener {
            signIn(binding.emailAddress.text.toString(), binding.password.text.toString())
        }

        configGoogleSignIn()
        setupGoogleSignInCallback()
        binding.googleSignInButton.setOnClickListener {
            googleSignIn()
        }

    }

    private fun googleSignIn() {
        val signInIntent = gsoClient.value!!.signInIntent

        startForResult.launch(signInIntent)
    }

    private fun setupGoogleSignInCallback() {
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            val account = task.getResult(ApiException::class.java)
                            firebaseAuthWithGoogle(account!!)
                        } catch (e: ApiException) {
                            // Google Sign In failed
                            Toast.makeText(this, "Google Sign In failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    RESULT_CANCELED -> {
                    } else -> { }
                }
            }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(application!!.mainExecutor) { task ->
                if (task.isSuccessful) {
                    val trainee = auth.currentUser?.email?.let { auth.currentUser?.let { it1 -> TraineeModel(emailAddress= it, id= it1.uid) } }
                    if (trainee != null) {
                        createTrainee(trainee)
                        app.traineeFS.currentTrainee = trainee

                        finish()
                        startActivity(parcelizeTraineeIntent(this@LoginActivity, TraineeDetailsActivity(), "trainee", trainee))
                    }

                }
            }
    }

    private fun createTrainee(traineeModel: TraineeModel) = runBlocking {
        app.traineeFS.create(traineeModel)
    }

    private fun configGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(application!!.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        gsoClient.value = GoogleSignIn.getClient(application!!.applicationContext,gso)
    }

    public override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null && currentUser.uid.isNotEmpty()) {
            val trainee = app.traineeFS.get(currentUser.uid)
            if (trainee != null) {
                app.traineeFS.currentTrainee = trainee
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()

            }
        }
    }


    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { result ->
                if (result.isSuccessful) {
                    val trainee = app.traineeFS.get(auth.currentUser!!.uid)
                    if (trainee != null) {
                        app.traineeFS.currentTrainee = trainee
                        startActivity(parcelizeTraineeIntent(this, DashboardActivity(), "trainee", trainee))
                        finish()
                    }
                }
                else {
                    Toast.makeText(this, result.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private suspend fun signUp(email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }

    private fun completeSignUpFlow(emailAddress: String, password: String) = runBlocking {
        val user = signUp(emailAddress, password).user

        if (user != null) {
            val trainee = TraineeModel(emailAddress=emailAddress, id=user.uid)
            app.traineeFS.create(trainee)

            app.traineeFS.currentTrainee = trainee
            finish()
            startActivity(parcelizeTraineeIntent(this@LoginActivity, TraineeDetailsActivity(), "trainee", trainee))
        } else {
            Log.d(tag, "Database Error D':")
        }
    }

}