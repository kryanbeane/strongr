package com.strongr.models.trainee

import com.google.firebase.auth.FirebaseUser

interface TraineeStore {
    suspend fun create(trainee: TraineeModel): Boolean
    suspend fun update(trainee: TraineeModel): Boolean
    suspend fun delete(trainee: TraineeModel): Boolean
    suspend fun get(id: String): TraineeModel?
    suspend fun fetchAuthProfile(): FirebaseUser?
}