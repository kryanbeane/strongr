package com.strongr.main

import android.app.Application
import android.util.Log
import com.strongr.models.WorkoutModel

class MainApp : Application() {

    var workouts = ArrayList<WorkoutModel>()
    private val tag = "MAIN APP"
    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "MainApp started")
    }
}