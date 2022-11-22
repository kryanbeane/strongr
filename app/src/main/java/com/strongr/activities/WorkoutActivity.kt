package com.strongr.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.strongr.R
import com.strongr.databinding.ActivityWorkoutBinding

class WorkoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        binding.logOut.setOnClickListener {
            Firebase.auth.signOut()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Log.d("Workout Activity", "User logged out")
        }

    }
}
