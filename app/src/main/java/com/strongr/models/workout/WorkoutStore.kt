package com.strongr.models.workout

import com.google.firebase.auth.FirebaseUser
import com.strongr.models.trainee.TraineeModel

interface WorkoutStore {
    suspend fun create(workout: WorkoutModel, trainee: TraineeModel)
    suspend fun update(workout: WorkoutModel, trainee: TraineeModel)
    suspend fun delete(workout: WorkoutModel, trainee: TraineeModel)
    fun get(name: String, trainee: TraineeModel): WorkoutModel?
}