package com.strongr.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ExerciseModel(
    var name: String = "",
    var instructions: String = "",
    var sets: Int = 0,
    var reps: Int = 0,
    var rir: Float = 0f
): Parcelable
