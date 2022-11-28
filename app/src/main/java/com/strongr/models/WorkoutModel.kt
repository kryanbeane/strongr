package com.strongr.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class WorkoutModel(
    var name: String = "",
    var targetMuscleGroups: ArrayList<String> = arrayListOf(),
    var exercises: ArrayList<ExerciseModel> = arrayListOf()
): Parcelable
