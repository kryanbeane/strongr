package com.strongr.models.exercise

import com.strongr.models.trainee.TraineeModel
import com.strongr.models.workout.WorkoutModel

class ExerciseFireStore: ExerciseStore {
    override fun create(exercise: ExerciseModel, workout: WorkoutModel, trainee: TraineeModel): Boolean {
        TODO("Not yet implemented")
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