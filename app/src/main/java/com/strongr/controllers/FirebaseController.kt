package com.strongr.controllers

import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.strongr.models.TraineeModel
import com.strongr.models.WorkoutModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.w3c.dom.Document
import java.lang.Exception

class FirebaseController {
    private val db = Firebase.firestore

    companion object {
        private var tag = "FIREBASE_CONTROLLER"
        private var collectionName = "trainees"
    }

    fun getTraineeByEmail(email: String): TraineeModel? {
        var traineeModel: TraineeModel? = null

        db.collection(collectionName)
            .whereEqualTo("emailAddress", email)
            .get()
            .addOnSuccessListener {
                traineeModel = it.documents[0].toObject<TraineeModel>()
            }
            .addOnFailureListener {
                Log.d(tag, "error getting documents: ", it)
            }
        return traineeModel
    }

    fun createTraineeRevised(trainee: TraineeModel): Exception? {
        var exception: Exception? = null
        if (trainee.emailAddress.isNotEmpty() && trainee.id.isNotEmpty()) {
//            db.collection(collectionName).add(trainee)
//                .addOnSuccessListener { Log.d(tag, "DocumentSnapshot successfully written!") }
//                .addOnFailureListener { exception = it }

            db.collection(collectionName)
                .document(trainee.id)
                .set(trainee)
                .addOnSuccessListener { Log.d(tag, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { exception = it }
        }
        return exception
    }

    fun createWorkoutRevised(name: String, trainee: TraineeModel): TraineeModel? {
        var updatedTrainee: TraineeModel? = trainee
        val workout = WorkoutModel(name)

        db.collection(collectionName)
            .document(trainee.id)
            .update("workouts", FieldValue.arrayUnion(workout))
            .addOnSuccessListener { updatedTrainee!!.workouts.add(workout) }
            .addOnFailureListener { updatedTrainee = null }

        return updatedTrainee
    }

    fun addWorkout(workoutName: String, trainee: TraineeModel) {
        val workout = WorkoutModel(name = workoutName)

        db.collection(collectionName).document(FirebaseAuth.getInstance().currentUser!!.uid)
            .update("workouts", FieldValue.arrayUnion(workout))
            .addOnSuccessListener {
                trainee.workouts.add(workout)
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