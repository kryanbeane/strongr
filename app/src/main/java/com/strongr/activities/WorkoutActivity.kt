package com.strongr.activities

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.strongr.R
import com.strongr.controllers.FirebaseController
import com.strongr.databinding.ActivityWorkoutBinding
import java.util.*
import kotlin.collections.ArrayList


class WorkoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkoutBinding
    private lateinit var dbController: FirebaseController
    private val tag = "WORKOUT ACTIVITY"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbController = FirebaseController()


        binding.createWorkout.setOnClickListener {
            Snackbar.make(binding.root, "Workout created ${FirebaseAuth.getInstance().currentUser.toString()}", Snackbar.LENGTH_LONG).show()
            dbController.addWorkout(binding.workoutName.text.toString())
            Log.d(tag, "Workout added")
        }



//        binding.logOut.setOnClickListener {
//            Firebase.auth.signOut()
//
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            Timber.tag("Workout Activity").d("User logged out")
//        }

    }
}
