package com.strongr.models.trainee

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.strongr.models.workout.WorkoutFireStore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.Date

class TraineeFireStore: TraineeStore {
    var currentTrainee = TraineeModel()
    private val db = Firebase.firestore
    private val collectionName = "trainees"
    val workouts = WorkoutFireStore()

    override suspend fun create(trainee: TraineeModel) {
        if (trainee.emailAddress.isNotEmpty() && trainee.id.isNotEmpty()) {
            db.collection(collectionName)
                .document(trainee.id)
                .set(trainee).await()
        }
    }

    override suspend fun update(trainee: TraineeModel) {

            val ref = db.collection(collectionName).document(trainee.id)
            ref.update(
                mapOf(
                    "fullName" to trainee.fullName,
                    "dob" to trainee.dob,
                    "sex" to trainee.sex,
                    "workoutsPerWeek" to trainee.workoutsPerWeek,
                    "activityLevel" to trainee.activityLevel,
                    "height" to trainee.height,
                )
            ).await()


    }

    override suspend fun delete(trainee: TraineeModel, user: FirebaseUser) {
        // Delete Firebase Document
        db.collection(collectionName).document(trainee.id).delete().await()

        // Delete Firebase Auth Profile
        user.delete().await()
    }

    override fun get(id: String): TraineeModel? = runBlocking {
        var trainee: TraineeModel? = null

        if (id.isNotEmpty()) {
            val snapshot = db.collection(collectionName)
                .document(id)
                .get()
                .await()

            trainee = snapshot.toObject<TraineeModel>()
        }

        return@runBlocking trainee
    }

    override fun fetchAuthProfile(): FirebaseUser? {
        return Firebase.auth.currentUser
    }

    private fun validUserDetails(trainee: TraineeModel): Boolean {
        return trainee.fullName.isNotEmpty()
                && trainee.dob.before(Date())
                && trainee.sex.isNotEmpty()
                && trainee.workoutsPerWeek != 0
                && trainee.activityLevel.isNotEmpty()
                && !trainee.height.equals(0.0f)
    }

}