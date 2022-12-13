package com.strongr.utils

import android.app.Activity
import android.content.Intent
import com.strongr.models.exercise.ExerciseModel
import com.strongr.models.trainee.TraineeModel
import com.strongr.models.workout.WorkoutModel

fun parcelizeTraineeIntent(currentActivity: Activity, nextActivity: Activity, field: String, trainee: TraineeModel): Intent {
    val launcherIntent = Intent(currentActivity, nextActivity::class.java)
    launcherIntent.putExtra(field, trainee)
    return launcherIntent
}

fun parcelizeWorkoutIntent(currentActivity: Activity, nextActivity: Activity, field: String, workout: WorkoutModel): Intent {
    val launcherIntent = Intent(currentActivity, nextActivity::class.java)
    launcherIntent.putExtra(field, workout)
    return launcherIntent
}

fun parcelizeExerciseIntent(currentActivity: Activity, nextActivity: Activity, field: String, exercise: ExerciseModel): Intent {
    val launcherIntent = Intent(currentActivity, nextActivity::class.java)
    launcherIntent.putExtra(field, exercise)
    return launcherIntent
}