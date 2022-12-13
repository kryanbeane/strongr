package com.strongr.models.workout

import android.os.Parcelable
import com.strongr.models.exercise.ExerciseModel
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class WorkoutModel(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var targetMuscleGroups: ArrayList<String> = arrayListOf(),
    var exercises: MutableMap<String, ExerciseModel> = mutableMapOf()
): Parcelable
