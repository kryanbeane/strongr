package com.strongr.activities.workouts

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.strongr.R
import com.strongr.activities.dashboard.DashboardActivity
import com.strongr.activities.login.LoginActivity
import com.strongr.activities.settings.SettingsActivity
import com.strongr.activities.workouts.fragments.EditWorkoutFragment
import com.strongr.activities.workouts.fragments.WorkoutDetailsFragment
import com.strongr.databinding.ActivityViewWorkoutBinding
import com.strongr.exercises.ExerciseActivity
import com.strongr.exercises.adapters.ExerciseAdapter
import com.strongr.exercises.adapters.ExerciseListener
import com.strongr.main.MainApp
import com.strongr.models.exercise.ExerciseModel
import com.strongr.models.workout.WorkoutModel
import com.strongr.utils.RearrangeCardHelperExercise
import com.strongr.utils.parcelizeExerciseIntent
import com.strongr.utils.parcelizeWorkoutIntent
import kotlinx.coroutines.runBlocking

class ViewWorkoutActivity : AppCompatActivity(), ExerciseListener {
    lateinit var app: MainApp
    private lateinit var binding: ActivityViewWorkoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)
        binding.toolbarTitle.setOnClickListener {
            finish()
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val adapter = ExerciseAdapter(app.workoutFS.currentWorkout.exercises, this)
        binding.recyclerView.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(RearrangeCardHelperExercise(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        val workoutDetailsFragment = WorkoutDetailsFragment()
        supportFragmentManager.beginTransaction().add(R.id.workout_fragment_container, workoutDetailsFragment).commit()

        binding.fab.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Deleting Workout")
            builder.setMessage("Are you sure you want to delete this workout?")
            builder.setPositiveButton("Yes") { dialog, which ->
                deleteWorkout()
                finish()
                startActivity(Intent(this, WorkoutListActivity::class.java))
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

    }

    fun updateWorkout(workout: WorkoutModel) = runBlocking {
        app.workoutFS.update(workout, app.traineeFS.currentTrainee)
        app.traineeFS.currentTrainee.workouts[workout.id] = workout
    }

    fun launchEditWorkoutFragment() {
        val editWorkoutFragment = EditWorkoutFragment.newInstance(app.workoutFS.currentWorkout)
        supportFragmentManager.beginTransaction()
            .replace(R.id.workout_fragment_container, editWorkoutFragment).commit()
    }

    fun launchWorkoutDetailsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.workout_fragment_container, WorkoutDetailsFragment()).commit()
    }

    private fun deleteWorkout() = runBlocking {
        app.workoutFS.delete(app.workoutFS.currentWorkout, app.traineeFS.currentTrainee)
        app.traineeFS.currentTrainee.workouts.remove(app.workoutFS.currentWorkout.id)
    }

    fun launchExerciseActivity() {
        return getResult.launch(parcelizeWorkoutIntent(this, ExerciseActivity(), "workout", app.workoutFS.currentWorkout))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_workout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.return_button -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        startActivity(Intent(this, WorkoutListActivity::class.java))
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            (binding.recyclerView.adapter)?.notifyItemRangeChanged(0, app.workoutFS.currentWorkout.exercises.size)
        }
    }

    private val getClickResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.notifyItemRangeChanged(0,app.workoutFS.currentWorkout.exercises.size)
            }
        }

    override fun onExerciseClick(exercise: ExerciseModel) {
        app.exerciseFS.currentExercise = exercise
        val launcherIntent = parcelizeExerciseIntent(this, ExerciseActivity(), "exercise", exercise)
        getClickResult.launch(launcherIntent)
    }
}
