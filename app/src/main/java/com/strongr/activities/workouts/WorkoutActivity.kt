package com.strongr.activities.workouts

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.anurag.multiselectionspinner.MultiSelectionSpinnerDialog
import com.google.android.material.snackbar.Snackbar
import com.strongr.R
import com.strongr.databinding.ActivityWorkoutBinding
import com.strongr.main.MainApp
import com.strongr.models.trainee.TraineeModel
import com.strongr.models.workout.WorkoutModel
import kotlinx.coroutines.runBlocking

class WorkoutActivity: AppCompatActivity(),
    MultiSelectionSpinnerDialog.OnMultiSpinnerSelectionListener {
    private lateinit var binding: ActivityWorkoutBinding
    private lateinit var app: MainApp
    private var workout = WorkoutModel("")
    var trainee = TraineeModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        trainee = app.firestore.currentTrainee

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        val muscleGroupList: MutableList<String> = mutableListOf(
            " Chest", " Upper Back", " Lats", " Rear Delts", " Side Delts",
            " Front Delts", " Triceps", " Biceps", " Forearms", " Abs",
            " Quads", " Hamstrings", " Calves", " Glutes"
        )

        binding.multiSpinner.setAdapterWithOutImage(this, muscleGroupList, this)
        binding.multiSpinner.initMultiSpinner(this, binding.multiSpinner)

        binding.createWorkout.setOnClickListener {
            if (binding.workoutName.text.toString().isNotEmpty()) {
                workout.name = binding.workoutName.text.toString()
                createWorkout(workout)

                trainee.workouts.add(workout)

                setResult(RESULT_OK)
                finish()

            } else {
                Snackbar.make(it,"Please Enter a name", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun OnMultiSpinnerItemSelected(chosenItems: MutableList<String>?) {
        workout.targetMuscleGroups= chosenItems as ArrayList<String>
    }

    private fun createWorkout(workout: WorkoutModel) = runBlocking {
        app.firestore.workouts.create(workout, app.firestore.currentTrainee)
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
