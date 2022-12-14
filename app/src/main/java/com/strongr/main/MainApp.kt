package com.strongr.main

import android.app.Application
import android.util.Log
import com.strongr.models.exercise.ExerciseFireStore
import com.strongr.models.trainee.TraineeFireStore
import com.strongr.models.trainee.TraineeModel
import com.strongr.models.workout.WorkoutFireStore

class MainApp: Application() {
    private val tag = "MAIN_APP"
    lateinit var traineeFS: TraineeFireStore
    lateinit var workoutFS: WorkoutFireStore
    lateinit var exerciseFS: ExerciseFireStore

    override fun onCreate() {
        super.onCreate()
        traineeFS = TraineeFireStore()
        workoutFS = WorkoutFireStore()
        exerciseFS = ExerciseFireStore()
    }
}