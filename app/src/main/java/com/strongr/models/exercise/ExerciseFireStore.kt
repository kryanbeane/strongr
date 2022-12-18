package com.strongr.models.exercise

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.strongr.models.trainee.TraineeModel
import com.strongr.models.workout.WorkoutModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class ExerciseFireStore: ExerciseStore {
    var currentExercise = ExerciseModel()
    private val db = Firebase.firestore
    private val collectionName = "trainees"

    override suspend fun create(exercise: ExerciseModel, workout: WorkoutModel, trainee: TraineeModel): Boolean {
        if (exercise.exerciseDetails.name.isNotEmpty() && exercise.exerciseDetails.muscleGroups.isNotEmpty()) {
            val ref = db.collection(collectionName).document(trainee.id)
            ref.update("workouts.${workout.id}.exercises.${exercise.id}", exercise).await()
            return true
        }
        return false
    }

    override suspend fun update(exercise: ExerciseModel, workout: WorkoutModel, trainee: TraineeModel) {
        if (exercise.exerciseDetails.name.isNotEmpty()) {
            val ref = db.collection(collectionName)
                .document(trainee.id)
            ref.update("workouts.${workout.id}.exercises.${exercise.id}", exercise).await()
        }
    }


    override suspend fun delete(exercise: ExerciseModel, workout: WorkoutModel, trainee: TraineeModel) {
        val ref = db.collection(collectionName).document(trainee.id)
        val updates = mapOf("workouts.${workout.id}.exercises.${exercise.id}" to FieldValue.delete())
        ref.update(updates).await()
    }

    override fun get(id: String, workout: WorkoutModel, trainee: TraineeModel): ExerciseModel? = runBlocking {
        var exercise: ExerciseModel? = null

        if (id.isNotEmpty()) {
            exercise = trainee.workouts[workout.id]?.exercises?.get(id)
        }

        return@runBlocking exercise
    }
}