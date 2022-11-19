package com.strongr.models

import java.util.*
import kotlin.collections.ArrayList

data class WorkoutModel(
    var _id: UUID = UUID.randomUUID(),
    var name: String,
    var targetMuscleGroups: ArrayList<String>,
    var exercises: ArrayList<ExerciseModel>
)
