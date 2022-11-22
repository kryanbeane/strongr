package com.strongr.models

import java.util.*
import kotlin.collections.ArrayList

// Trainees will only be created once. This will be from a users perspective so they will sign up and fill in arbitrary details about themselves.
// As they use the app they can then populate the app with workouts and track their body weight etc.
// Will maybe add calorie tracking integration from another app like MacroFactor / MyFitness Pal
data class TraineeModel(
    var fullName: String = "",
    var dob: Date = Date(),
    var sex: Enum<Sex> = Sex.Male,
    var height: Float = 0.0f,
    var weight: ArrayList<WeightModel> = arrayListOf(),
    var emailAddress: String,
    var workouts: ArrayList<WorkoutModel> = arrayListOf()
)

data class WeightModel(
    var weight: Float,
    var date: Date
)

enum class Sex {
    Male, Female
}
