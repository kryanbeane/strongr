package com.strongr.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.strongr.R
import com.strongr.controllers.FirebaseController
import com.strongr.databinding.ActivityWorkoutBinding
import com.strongr.main.MainApp
import com.strongr.models.TraineeModel
import com.strongr.models.WorkoutModel
import com.strongr.utils.parcelizeIntent

class WorkoutActivity: AppCompatActivity() {
    private lateinit var binding: ActivityWorkoutBinding
    private lateinit var dbController: FirebaseController
    private lateinit var app: MainApp
    private var workout = WorkoutModel("")
    var trainee = TraineeModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbController = FirebaseController()

        app = application as MainApp
        trainee = app.trainee

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        binding.createWorkout.setOnClickListener {

            if (binding.workoutName.text.toString().isNotEmpty()) {
                workout.name = binding.workoutName.text.toString()
                dbController.addWorkout(binding.workoutName.text.toString())

                trainee.workouts.add(workout)

                setResult(RESULT_OK)
                finish()

            } else {
                Snackbar.make(it,"Please Enter a name", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_workout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val tag = "WORKOUT_ACTIVITY"
    }
}
