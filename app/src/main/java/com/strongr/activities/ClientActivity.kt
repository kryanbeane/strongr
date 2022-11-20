package com.strongr.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.strongr.databinding.ActivityClientBinding
import com.strongr.models.ClientModel
import com.strongr.models.Sex
import timber.log.Timber
import timber.log.Timber.i
import java.util.*

class ClientActivity: AppCompatActivity() {

    // Used to bind this logic to the layout - activity_client.xml
    private lateinit var binding: ActivityClientBinding
    private lateinit var client: ClientModel
    private var clients = ArrayList<ClientModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())
        i("Client Activity Started Successfully...")



        // Use the binding to add an on click listener to the add client button
        binding.addClient.setOnClickListener() {
            val firstName = binding.clientFirstName.text.toString()
            when {
                firstName.isNotEmpty() -> i("First Name: $firstName")
                else -> Snackbar.make(it, "Please enter a first name", Snackbar.LENGTH_LONG).show()
            }

            val lastName = binding.clientLastName.text.toString()

            when {
                lastName.isNotEmpty() -> i("Last Name: $lastName")
                else -> Snackbar.make(it, "Please enter a last name", Snackbar.LENGTH_LONG).show()
            }

            val height = binding.clientHeight.text.toString()
            when {
                height.isNotEmpty() -> i("Height: $height")
                else -> Snackbar.make(it, "Please enter a height", Snackbar.LENGTH_LONG).show()
            }

            val weight = binding.clientWeight.text.toString()
            when {
                weight.isNotEmpty() -> i("Weight: $weight")
                else -> Snackbar.make(it, "Please enter a weight", Snackbar.LENGTH_LONG).show()
            }

            val phoneNumber = binding.clientPhoneNumber.text.toString()
            when {
                isValidNumber(phoneNumber) -> i("Phone Number: $phoneNumber")
                else -> Snackbar.make(it, "Please enter a phone number", Snackbar.LENGTH_LONG).show()
            }

            val emailAddress = binding.clientEmailAddress.text.toString()
            when {
                isValidEmail(emailAddress) -> i("Email Address: $emailAddress")
                else -> Snackbar.make(it, "Please enter an email address", Snackbar.LENGTH_LONG).show()
            }

            client = ClientModel(
                firstName=firstName,
                lastName=lastName,
                height=height.toFloat(),
                weight=weight.toFloat(),
                phoneNumber=phoneNumber,
                emailAddress=emailAddress,
                dob = Date(),
                sex = Sex.Male,
                workouts = arrayListOf()
            )

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

