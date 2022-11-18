package com.strongr.models

import android.telephony.PhoneNumberUtils
import java.util.*
import kotlin.collections.ArrayList

// https://stackoverflow.com/questions/6106859/how-to-format-a-phone-number-using-phonenumberutils
data class ClientModel(
    var _id: UUID = UUID.randomUUID(),
    var firstName: String,
    var lastName: String,
    var phoneNumber: PhoneNumberUtils,
    var emailAddress: String,
    var workoutPlan: ArrayList<WorkoutModel>
)
