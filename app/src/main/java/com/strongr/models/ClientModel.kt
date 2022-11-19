package com.strongr.models

import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Email
import android.telephony.PhoneNumberUtils
import java.util.*
import kotlin.collections.ArrayList

// https://stackoverflow.com/questions/6106859/how-to-format-a-phone-number-using-phonenumberutils
data class ClientModel(
    var _id: UUID = UUID.randomUUID(),
    var firstName: String,
    var lastName: String,
    var dob: Date,
    var sex: Enum<Sex>,
    var height: Float,
    var weight: Float,
    var phoneNumber: String,
    var emailAddress: String,
    var workouts: ArrayList<WorkoutModel>
)


enum class Sex {
    Male, Female
}
