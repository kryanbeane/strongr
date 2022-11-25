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
import com.strongr.main.MainApp
import com.strongr.models.WorkoutModel
import java.util.*
import kotlin.collections.ArrayList


class WorkoutActivity: AppCompatActivity() {
    private lateinit var binding: ActivityWorkoutBinding
    private lateinit var dbController: FirebaseController
    private lateinit var app: MainApp
    private var workout = WorkoutModel("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbController = FirebaseController()

        app = application as MainApp


        binding.createWorkout.setOnClickListener() {
            workout.name = binding.workoutName.text.toString()
            if (workout.name.isNotEmpty()) {
                dbController.addWorkout(binding.workoutName.text.toString(), app.trainee)

                setResult(RESULT_OK)
                finish()
            } else {
                Snackbar.make(it,"Please Enter a name", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    companion object {
        private const val tag = "WORKOUT_ACTIVITY"
    }
}
