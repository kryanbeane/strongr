package com.strongr.main

import android.app.Application
import android.util.Log
import com.strongr.models.TraineeModel
import com.strongr.models.WorkoutModel

class MainApp: Application() {
    private val tag = "MAIN_APP"
    lateinit var trainee: TraineeModel

    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "MainApp started")

        trainee = TraineeModel(emailAddress = "")
    }
}