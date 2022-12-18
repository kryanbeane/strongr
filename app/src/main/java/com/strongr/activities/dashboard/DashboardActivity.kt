package com.strongr.activities.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.fitness.data.DataSet
import com.strongr.R
import com.strongr.activities.login.LoginActivity
import com.strongr.activities.workouts.WorkoutListActivity
import com.strongr.databinding.ActivityDashboardBinding
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.tasks.Tasks
import kotlin.math.roundToInt

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myworkouts.setOnClickListener {
            startActivity(Intent(this, WorkoutListActivity::class.java))
        }

        binding.signOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }


        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(application!!.getString(R.string.default_web_client_id))
            .requestProfile()
            .build()


        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
            .build()

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                this,
                0x1001,
                account,
                fitnessOptions)
        }
        val gsa = GoogleSignIn.getAccountForExtension(this, fitnessOptions)
        val history = Fitness.getHistoryClient(this, gsa)

        Tasks.whenAllSuccess<DataSet>(
            history.readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA),
            history.readDailyTotal(DataType.TYPE_CALORIES_EXPENDED))
            .addOnSuccessListener { dataSets ->
                binding.stepsText.text = getSteps(dataSets[0])
                binding.energyText.text = getEnergy(dataSets[1])
            }
    }


    private fun getSteps(dataSet: DataSet): String {
        return if (dataSet.isEmpty)
            "0"
        else
            String.format("%,d", dataSet.dataPoints[0].getValue(Field.FIELD_STEPS).asInt())
    }

    private fun getEnergy(dataSet: DataSet): String {
        return if (dataSet.isEmpty)
            "0"
        else
            String.format("%,d",
                dataSet.dataPoints[0].getValue(Field.FIELD_CALORIES).asFloat().roundToInt()
            )
    }
}

