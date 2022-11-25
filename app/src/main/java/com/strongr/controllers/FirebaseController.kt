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
import org.w3c.dom.Document

class FirebaseController {
    private val db = Firebase.firestore
    private lateinit var currentTrainee: TraineeModel
    private lateinit var traineeDocRef: DocumentReference

    companion object {
        private var tag = "FIREBASE_CONTROLLER"
        private var collectionName = "trainees"
    }

    fun getTraineeByEmail(email: String) {
        db.collection(collectionName)
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
        traineeDocRef = db.collection(collectionName).document(userUid)

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

    fun createTraineeRevised(emailAddress: String, userUid: String): Pair<TraineeModel?, FirebaseException?> {
        lateinit var returnPair: Pair<TraineeModel?, FirebaseException?>

        if (emailAddress == "" || userUid == "") {
            returnPair = Pair(null, FirebaseException("trainee must have an email and id"))
        } else {
            val trainee = TraineeModel(emailAddress=emailAddress, id=userUid)
            returnPair = Pair(trainee, null)

            db.collection(collectionName)
                .document(userUid)
                .set(trainee)
                .addOnSuccessListener { returnPair = Pair(trainee, null) }
                .addOnFailureListener {e -> returnPair = Pair(null, FirebaseException("failed to create trainee", e)) }
        }

        return returnPair
    }

    fun addWorkout(workoutName: String, trainee: TraineeModel) {
        val workout = WorkoutModel(name = workoutName)

        db.collection(collectionName).document(FirebaseAuth.getInstance().currentUser!!.uid)
            .update("workouts", FieldValue.arrayUnion(workout))
            .addOnSuccessListener {
                trainee.workouts.add(workout)
                Log.d(tag, "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e -> Log.w(tag, "Error updating document", e) }
    }

    fun getWorkouts() {

    }

    fun getTraineeDBReference(email: String?, id: String?): Pair<DocumentReference?, FirebaseException?> {
        lateinit var returnPair: Pair<DocumentReference?, FirebaseException?>

        // Check one parameter is valid
        if (email == null && id == null) {
            returnPair = Pair(null, FirebaseException("email and id cannot both be null"))
        }
        // If not searching by id
        else if(email != null) {
            db.collection(collectionName)
                .whereEqualTo("emailAddress", email).get()
                .addOnSuccessListener { returnPair = Pair(it.documents[0].reference, null) }
                .addOnFailureListener { returnPair = Pair(null, FirebaseException("error getting document with email $email")) }
        // If not searching by email
        } else if (id != null) {
            db.collection(collectionName).document(id).get()
                .addOnSuccessListener { returnPair = Pair(it.reference, null) }
                .addOnFailureListener { returnPair = Pair(null, FirebaseException("error getting document with id $id")) }
        } else {
            returnPair = Pair(null, FirebaseException("unexpected error getting document, email $email, id $id"))
        }

        return returnPair
    }

}