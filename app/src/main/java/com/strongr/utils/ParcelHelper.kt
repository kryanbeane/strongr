package com.strongr.utils

import android.app.Activity
import android.content.Intent
import com.strongr.models.trainee.TraineeModel

fun parcelizeIntent(currentActivity: Activity, nextActivity: Activity, field: String, trainee: TraineeModel): Intent {
    val launcherIntent = Intent(currentActivity, nextActivity::class.java)
    launcherIntent.putExtra(field, trainee)
    return launcherIntent
}