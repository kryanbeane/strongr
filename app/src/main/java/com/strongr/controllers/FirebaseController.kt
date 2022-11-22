package com.strongr.controllers

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.strongr.models.TraineeModel



class FirebaseController {
    private val db = Firebase.firestore
    private var TAG = "FirebaseController"

    @SuppressLint("LogNotTimber")
    fun addTrainee(emailAddress: String) {
        // Create a new user with a first and last name
        val trainee = TraineeModel(emailAddress = emailAddress)

        // Add a new document with a generated ID
        db.collection("trainees")
            .add(trainee)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener {e ->
                Log.d(TAG, "Error adding document", e)
            }
    }

}