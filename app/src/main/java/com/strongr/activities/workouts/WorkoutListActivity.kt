package com.strongr.activities.workouts

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.strongr.R
import com.strongr.activities.login.LoginActivity
import com.strongr.activities.settings.SettingsActivity
import com.strongr.activities.workouts.adapters.WorkoutAdapter
import com.strongr.activities.workouts.adapters.WorkoutListener
import com.strongr.databinding.ActivityWorkoutListBinding
import com.strongr.main.MainApp
import com.strongr.models.workout.WorkoutModel
import com.strongr.utils.RearrangeCardHelper
import com.strongr.utils.parcelizeWorkoutIntent

class WorkoutListActivity: AppCompatActivity(), WorkoutListener {

    private lateinit var app: MainApp
    private lateinit var binding: ActivityWorkoutListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        app = application as MainApp

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener {
            getResult.launch(parcelizeWorkoutIntent(this, WorkoutActivity(), "workout", app.workoutFS.currentWorkout))
        }

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val adapter = WorkoutAdapter(app.traineeFS.currentTrainee.workouts,this)
        binding.recyclerView.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(RearrangeCardHelper(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
                (binding.recyclerView.adapter)?.notifyItemRangeChanged(0, app.traineeFS.currentTrainee.workouts.size)
            }
        }

    override fun onWorkoutClick(workout: WorkoutModel) {
        app.workoutFS.currentWorkout = workout

        finish()
        startActivity(parcelizeWorkoutIntent(this, Workout2Activity(), "workout", workout))
    }
}
