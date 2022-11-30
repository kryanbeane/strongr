package com.strongr.models.trainee

import com.google.firebase.auth.FirebaseUser

interface TraineeStore {
    suspend fun create(trainee: TraineeModel)
    suspend fun update(trainee: TraineeModel)
    suspend fun delete(trainee: TraineeModel, user: FirebaseUser)
    fun get(id: String): TraineeModel?
    fun fetchAuthProfile(): FirebaseUser?
}