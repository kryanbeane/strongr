package com.strongr.models.workout

import android.os.Parcelable
import com.strongr.models.exercise.ExerciseModel
import kotlinx.parcelize.Parcelize
import kotlin.collections.ArrayList

@Parcelize
data class WorkoutModel(
    var name: String = "",
    var targetMuscleGroups: ArrayList<String> = arrayListOf(),
    var exercises: ArrayList<ExerciseModel> = arrayListOf()
): Parcelable
