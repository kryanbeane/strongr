package com.strongr.exercises

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.strongr.R
import com.strongr.activities.login.LoginActivity
import com.strongr.activities.settings.SettingsActivity
import com.strongr.databinding.ActivityExerciseListBinding
import com.strongr.exercises.adapters.ExerciseListener
import com.strongr.main.MainApp
import com.strongr.models.exercise.ExerciseModel
import com.strongr.models.workout.WorkoutModel
import com.strongr.utils.parcelizeExerciseIntent

class ExerciseListActivity: AppCompatActivity(), ExerciseListener {

    private lateinit var app: MainApp
    private lateinit var binding: ActivityExerciseListBinding
    private lateinit var workout: WorkoutModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        workout = intent.getParcelableExtra("current_workout")!!

        // binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

//        val layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.layoutManager = layoutManager
//        val adapter = ExerciseAdapter(app.firestore.currentTrainee.workouts,this)
//        binding.recyclerView.adapter = adapter
//        val itemTouchHelper = ItemTouchHelper(RearrangeCardHelper(adapter))
//        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.workout_add -> {
                getResult.launch(parcelizeExerciseIntent(this, ExerciseActivity(), "exercise", app.exerciseFS.currentExercise))
            }
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                finish()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            (binding.recyclerView.adapter)?.notifyItemRangeChanged(0, app.workoutFS.currentWorkout.exercises.size)
        }
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.workoutFS.currentWorkout.exercises.size)
            }
        }

    override fun onExerciseClick(exercise: ExerciseModel) {
        val launcherIntent = Intent(this, ExerciseActivity::class.java)
        getClickResult.launch(launcherIntent)
    }
}
