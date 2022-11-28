package com.strongr.models.trainee

import android.os.Parcelable
import com.strongr.models.weight.WeightModel
import com.strongr.models.workout.WorkoutModel
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

// Trainees will only be created once. This will be from a users perspective so they will sign up and fill in arbitrary details about themselves.
// As they use the app they can then populate the app with workouts and track their body weight etc.
// Will maybe add calorie tracking integration from another app like MacroFactor / MyFitness Pal
@Parcelize
data class TraineeModel (
    // Auto-generated ID by firebase used to get documents from the db
    // Is assigned after signup or signin
    var id: String = "",
    var fullName: String = "",
    var dob: Date = Date(),
    var sex: String = "",
    var workoutsPerWeek: String = "",
    val activityLevel: String = "",
    var height: Float = 0.0f,
    var weight: ArrayList<WeightModel> = arrayListOf(),
    var emailAddress: String = "",
    var workouts: ArrayList<WorkoutModel> = arrayListOf()
): Parcelable


