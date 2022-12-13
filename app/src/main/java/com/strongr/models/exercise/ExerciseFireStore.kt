package com.strongr.models.exercise

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.strongr.models.trainee.TraineeModel
import com.strongr.models.workout.WorkoutModel
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

    override fun update(exercise: ExerciseModel, workout: WorkoutModel, trainee: TraineeModel): Boolean {
        TODO("Not yet implemented")
    }

    override fun delete(exercise: ExerciseModel, workout: WorkoutModel, trainee: TraineeModel): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(name: String): ExerciseModel? {
        TODO("Not yet implemented")
    }
}