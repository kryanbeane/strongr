package com.strongr.controllers

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.strongr.models.trainee.TraineeModel
import com.strongr.models.workout.WorkoutModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class FirebaseController {
    private val db = Firebase.firestore

    companion object {
        private var tag = "FIREBASE_CONTROLLER"
        var collectionName = "trainees"
    }

    fun addWorkout(workoutName: String) {
        val workout = WorkoutModel(name = workoutName)

        db.collection(collectionName).document(FirebaseAuth.getInstance().currentUser!!.uid)
            .update("workouts", FieldValue.arrayUnion(workout))
            .addOnSuccessListener {
            }
            .addOnFailureListener { e -> Log.w(tag, "Error updating document", e) }
    }

    fun getTraineeDBReference(email: String?, id: String?): DocumentReference? {
        var docRef: DocumentReference? = null

        if(email != null) {
            db.collection(collectionName)
                .whereEqualTo("emailAddress", email).get()
                .addOnSuccessListener { docRef =it.documents[0].reference }
                .addOnFailureListener { docRef = null }
        } else if (id != null) {
            db.collection(collectionName).document(id).get()
                .addOnSuccessListener { docRef = it.reference }
                .addOnFailureListener { docRef = null }
        }

        return docRef
    }

    fun getTrainee(id: String): TraineeModel? = runBlocking {
        var trainee: TraineeModel? = null

        if (id.isNotEmpty()) {
            val task = db.collection(collectionName).document(id).get().await()
            trainee = task.toObject<TraineeModel>()
        }

        return@runBlocking trainee
    }


}