package com.strongr.models.exercise

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class ExerciseModel(
    val id: String = UUID.randomUUID().toString(),
    var exerciseDetails: ExerciseDetailsModel = ExerciseDetailsModel(),
    var instructions: String = "",
    var sets: Int = 0,
    var reps: Int = 0,
    var rir: Float = 0f
): Parcelable

@Parcelize
data class ExerciseDetailsModel(
    var name: String = "",
    var muscleGroups: ArrayList<String> = arrayListOf(),
): Parcelable
