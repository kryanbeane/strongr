package com.strongr.models.exercise

import com.google.firebase.auth.FirebaseUser
import com.strongr.models.trainee.TraineeModel
import com.strongr.models.workout.WorkoutModel

interface ExerciseStore {
    suspend fun create(exercise: ExerciseModel, workout: WorkoutModel, trainee: TraineeModel): Boolean
    suspend fun update(exercise: ExerciseModel, workout: WorkoutModel, trainee: TraineeModel)
    suspend fun delete(exercise: ExerciseModel, workout: WorkoutModel, trainee: TraineeModel)
    fun get(id: String, workout: WorkoutModel, trainee: TraineeModel): ExerciseModel?
}