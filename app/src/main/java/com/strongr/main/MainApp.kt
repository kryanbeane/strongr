package com.strongr.main

import android.app.Application
import android.util.Log
import com.strongr.models.trainee.TraineeFireStore
import com.strongr.models.trainee.TraineeModel

class MainApp: Application() {
    private val tag = "MAIN_APP"
    lateinit var firestore: TraineeFireStore

    override fun onCreate() {
        super.onCreate()
        firestore = TraineeFireStore()
    }
}