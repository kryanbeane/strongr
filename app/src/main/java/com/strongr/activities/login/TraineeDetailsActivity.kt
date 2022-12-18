package com.strongr.activities.login

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.strongr.R
import com.strongr.activities.dashboard.DashboardActivity
import com.strongr.activities.workouts.WorkoutListActivity
import com.strongr.databinding.ActivityTraineeDetailsBinding
import com.strongr.main.MainApp
import com.strongr.models.trainee.TraineeModel
import kotlinx.coroutines.runBlocking
import java.util.*

class TraineeDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTraineeDetailsBinding
    private lateinit var app: MainApp
    private lateinit var trainee: TraineeModel
    private lateinit var dateButton: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTraineeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp

        trainee = app.traineeFS.currentTrainee
        setSupportActionBar(binding.toolbar)
        dateButton = binding.datePicker

        val picker = initDatePicker()
        dateButton.setOnClickListener {
            picker.show()
            picker.setOnDateSetListener { _, year, month, day ->
                trainee.dob = Date(year, month, day)
                binding.datePicker.text = "$day/$month/$year"
            }
        }

        binding.buttonSave.setOnClickListener {
            trainee.fullName = binding.editFullName.text.toString()

            val radioGroup: RadioGroup = findViewById(R.id.radio_group_sex)
            val checkedRadioButtonId = radioGroup.checkedRadioButtonId
            val radioButton: RadioButton = findViewById(checkedRadioButtonId)

            trainee.sex = radioButton.text.toString()
            trainee.workoutsPerWeek = binding.editWorkoutsPerWeek.selectedItem.toString().toInt()
            trainee.activityLevel = binding.spinnerActivityLevel.selectedItem.toString()
            trainee.height = binding.editHeight.text.toString().toFloat()
            updateTrainee()
            app.traineeFS.currentTrainee = trainee

            finish()
            startActivity(Intent(this, DashboardActivity::class.java))
        }

    }

    private fun updateTrainee() = runBlocking {
        app.traineeFS.update(trainee)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                finish()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initDatePicker(): DatePickerDialog {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            var m = month
            m += 1
            val date = "$day/$month/$year"
            dateButton.text = date
        }
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        val style: Int = AlertDialog.THEME_HOLO_LIGHT
        return DatePickerDialog(this, style, dateSetListener, year, month, day)
    }
}