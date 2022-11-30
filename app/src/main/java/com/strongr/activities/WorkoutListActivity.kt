package com.strongr.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.strongr.R
import com.strongr.adapters.WorkoutAdapter
import com.strongr.controllers.FirebaseController
import com.strongr.databinding.ActivityWorkoutListBinding
import com.strongr.main.MainApp
import com.strongr.models.trainee.TraineeModel
import com.strongr.utils.parcelizeIntent

class WorkoutListActivity: AppCompatActivity() {

    private lateinit var app: MainApp
    private lateinit var binding: ActivityWorkoutListBinding
    private lateinit var firebaseController: FirebaseController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        app = application as MainApp
        firebaseController = FirebaseController()


        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = WorkoutAdapter(app.firestore.currentTrainee.workouts)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.workout_add -> {
                getResult.launch(parcelizeIntent(this, WorkoutActivity(), "trainee", app.firestore.currentTrainee))
            }
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                finish()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.notifyItemRangeChanged(0, app.firestore.currentTrainee.workouts.size)
            }
        }
}
