package com.strongr.models

import java.util.*

data class ExerciseModel(
    var _id: UUID = UUID.randomUUID(),
    var name: String,
    var instructions: String,
    var sets: Int,
    var reps: Int,
    var rir: Float
)
