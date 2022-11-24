package com.strongr.models

import java.util.*
import kotlin.collections.ArrayList

data class WorkoutModel(
    var name: String,
    var targetMuscleGroups: ArrayList<String> = arrayListOf(),
    var exercises: ArrayList<ExerciseModel> = arrayListOf()
)
