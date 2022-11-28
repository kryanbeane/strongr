package com.strongr.models.trainee

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.strongr.controllers.FirebaseController
import kotlinx.coroutines.tasks.await

class TraineeFireStore: TraineeStore {
    private val db = Firebase.firestore

    override suspend fun create(trainee: TraineeModel): Boolean {
        var result = false

        if (trainee.emailAddress.isNotEmpty() && trainee.id.isNotEmpty()) {
            db.collection(FirebaseController.collectionName)
                .document(trainee.id)
                .set(trainee)
                .addOnSuccessListener { result = true }.await()
        }
        return result
    }

    override suspend fun update(trainee: TraineeModel): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun delete(trainee: TraineeModel): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: String): TraineeModel? {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAuthProfile(): FirebaseUser? {
        TODO("Not yet implemented")
    }


}