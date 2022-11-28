package com.strongr.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strongr.R
import com.strongr.adapters.WorkoutAdapter
import com.strongr.controllers.FirebaseController
import com.strongr.databinding.ActivityWorkoutListBinding
import com.strongr.databinding.CardWorkoutBinding
import com.strongr.main.MainApp
import com.strongr.models.TraineeModel
import com.strongr.models.WorkoutModel
import com.strongr.utils.parcelizeIntent

class WorkoutListActivity: AppCompatActivity() {

    private lateinit var app: MainApp
    private lateinit var binding: ActivityWorkoutListBinding
    private lateinit var firebaseController: FirebaseController
    var trainee = TraineeModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        app = application as MainApp
        firebaseController = FirebaseController()

        trainee = app.trainee

//        if (intent.hasExtra("trainee")) {
//            trainee = intent.extras?.getParcelable("trainee")!!
//        }

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)


        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = WorkoutAdapter(trainee.workouts)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.workout_add -> {
                getResult.launch(parcelizeIntent(this, WorkoutActivity(), "trainee", trainee))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.notifyItemRangeChanged(0, trainee.workouts.size)
                trainee = it.data?.extras?.getParcelable("trainee")!!
            }
        }
}
