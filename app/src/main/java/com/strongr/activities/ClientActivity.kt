package com.strongr.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.ajalt.timberkt.Timber
import com.google.android.material.snackbar.Snackbar
import com.strongr.R
import com.strongr.databinding.ActivityClientBinding
import timber.log.Timber.i

class ClientActivity: AppCompatActivity() {

    // Used to bind this logic to the layout - activity_client.xml
    private lateinit var binding: ActivityClientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_client)

        Timber.plant(Timber.DebugTree())

        // Use the binding to add an on click listener to the add client button
        binding.addClient.setOnClickListener() {
            val clientName = binding.clientName.text.toString()
            if (clientName.isNotEmpty()) {
                i("Add Button Pressed: $clientName")
            }
            else {
                Snackbar
                    .make(it,"Please Enter a Name", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}