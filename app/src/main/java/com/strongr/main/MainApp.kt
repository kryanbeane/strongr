package com.strongr.main

import android.app.Application
import android.util.Log
import com.strongr.models.trainee.TraineeModel

class MainApp: Application() {
    private val tag = "MAIN_APP"
    lateinit var trainee: TraineeModel

    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "MainApp started")

        trainee = TraineeModel(emailAddress = "")
    }
}