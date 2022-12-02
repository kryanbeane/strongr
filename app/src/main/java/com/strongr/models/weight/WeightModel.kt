package com.strongr.models.weight

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class WeightModel (
    var weight: Float,
    var date: Date
): Parcelable