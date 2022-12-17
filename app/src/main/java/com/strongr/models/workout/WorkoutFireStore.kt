package com.strongr.models.workout

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.strongr.models.trainee.TraineeModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class WorkoutFireStore: WorkoutStore {
    var currentWorkout = WorkoutModel()
    private val db = Firebase.firestore
    private val collectionName = "trainees"

    override suspend fun create(workout: WorkoutModel, trainee: TraineeModel) {
        if (workout.name.isNotEmpty()) {
            val ref = db.collection(collectionName)
                .document(trainee.id)
            ref.update("workouts.${workout.id}", workout).await()
        }
    }
    
    override suspend fun update(workout: WorkoutModel, trainee: TraineeModel) {
        if (workout.name.isNotEmpty()) {
            val ref = db.collection(collectionName)
                .document(trainee.id)
            ref.update("workouts.${workout.id}", workout).await()
        }
    }

    override suspend fun delete(workout: WorkoutModel, trainee: TraineeModel) {
        val ref = db.collection(collectionName).document(trainee.id)
        val updates = mapOf("workouts.${workout.id}" to FieldValue.delete())
        ref.update(updates).await()
    }

    override fun get(id: String, trainee: TraineeModel): WorkoutModel? = runBlocking {
        var workout: WorkoutModel? = null

        if (id.isNotEmpty()) {
            workout = trainee.workouts[id]
        }

        return@runBlocking workout
    }
}