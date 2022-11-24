package com.strongr.controllers

import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.strongr.models.TraineeModel
import com.strongr.models.WorkoutModel

class FirebaseController {
    private val db = Firebase.firestore
    private var tag = "FIREBASE_CONTROLLER"
    private lateinit var currentTrainee: TraineeModel
    private lateinit var traineeDocRef: DocumentReference
    fun getTraineeByEmail(email: String) {
        db.collection("trainees")
            .whereEqualTo("emailAddress", email)
            .get()
            .addOnSuccessListener {
                currentTrainee = it.documents[0].toObject<TraineeModel>()!!
                traineeDocRef = it.documents[0].reference
            }
            .addOnFailureListener {
                Log.d(tag, "Error getting documents: ", it)
            }
    }

    fun createTrainee(emailAddress: String, userUid: String) {
        // Init new trainee obj
        val trainee = TraineeModel(emailAddress = emailAddress, id = userUid)
        // Set the doc ref to a document with the new id
        traineeDocRef = db.collection("trainees").document(userUid)

        // Set the document in db to that document and populate with the trainee obj
        traineeDocRef.set(trainee)
            .addOnSuccessListener { _ ->
                Log.d(tag, "DocumentSnapshot added with ID: $userUid")
                // Update the current trainee ref & id
                currentTrainee = trainee
                currentTrainee.id = userUid
            }
            .addOnFailureListener {e ->
                Log.d(tag, "Error adding document", e)
            }

    }

    fun addWorkout(workoutName: String) {
        val workout = WorkoutModel(name = workoutName)

        db.collection("trainees").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .update("workouts", FieldValue.arrayUnion(workout))
            .addOnSuccessListener { Log.d(tag, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(tag, "Error updating document", e) }
    }

}