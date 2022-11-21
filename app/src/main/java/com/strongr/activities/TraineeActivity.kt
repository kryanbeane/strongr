package com.strongr.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.strongr.databinding.ActivityTraineeBinding
import com.strongr.models.Sex
import com.strongr.models.TraineeModel
import timber.log.Timber
import timber.log.Timber.i
import java.util.*

class TraineeActivity: AppCompatActivity() {

    // Used to bind this logic to the layout - activity_trainee.xml
    private lateinit var binding: ActivityTraineeBinding
    private lateinit var trainee: TraineeModel
    private var trainees = ArrayList<TraineeModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTraineeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())
        i("Trainee Activity Started Successfully...")



        // Use the binding to add an on click listener to the add trainee button
        binding.createAccount.setOnClickListener() {
            val fullName = binding.fullName.text.toString()
            when {
                fullName.isNotEmpty() -> i("Full Name: $fullName")
                else -> Snackbar.make(it, "Please enter a full name", Snackbar.LENGTH_LONG).show()
            }

            val phoneNumber = binding.phoneNumber.text.toString()
            when {
                isValidNumber(phoneNumber) -> i("Phone Number: $phoneNumber")
                else -> Snackbar.make(it, "Please enter a phone number", Snackbar.LENGTH_LONG).show()
            }

            val emailAddress = binding.emailAddress.text.toString()
            when {
                isValidEmail(emailAddress) -> i("Email Address: $emailAddress")
                else -> Snackbar.make(it, "Please enter an email address", Snackbar.LENGTH_LONG).show()
            }

            trainee = TraineeModel(
                fullName = fullName,
                phoneNumber=phoneNumber,
                emailAddress=emailAddress,
                dob = Date(),
                sex = Sex.Male,
                workouts = arrayListOf()
            )

            trainees.add(trainee)

        }
    }

    private fun isValidEmail(email: String): Boolean {
        if (email.isNotEmpty()) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
        return false
    }

    private fun isValidNumber(num: String): Boolean {
        if (num.isNotEmpty()) {
            return android.util.Patterns.PHONE.matcher(num).matches()
        }
        return false
    }
}

