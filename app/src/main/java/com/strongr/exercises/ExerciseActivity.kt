package com.strongr.exercises

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.Toast
import com.anurag.multiselectionspinner.MultiSelectionSpinnerDialog
import com.strongr.R
import com.strongr.databinding.ActivityExerciseBinding
import com.strongr.main.MainApp
import com.strongr.models.exercise.ExerciseDetailsModel
import com.strongr.models.exercise.ExerciseModel
import com.strongr.models.workout.WorkoutModel
import kotlinx.coroutines.runBlocking

class ExerciseActivity: AppCompatActivity(), MultiSelectionSpinnerDialog.OnMultiSpinnerSelectionListener {
    private lateinit var binding: ActivityExerciseBinding
    private lateinit var app: MainApp
    private var workout = WorkoutModel()
    private var exercise = ExerciseModel()
    private var muscleGroups = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
        workout = app.workoutFS.currentWorkout
        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)
        configureSeekBar(binding)

        val muscleGroupList: MutableList<String> = mutableListOf(
            " Chest", " Upper Back", " Lats", " Rear Delts", " Side Delts",
            " Front Delts", " Triceps", " Biceps", " Forearms", " Abs",
            " Quads", " Hamstrings", " Calves", " Glutes"
        )

        binding.multiSpinner.setAdapterWithOutImage(this, muscleGroupList, this)
        binding.multiSpinner.initMultiSpinner(this, binding.multiSpinner)

        if (intent.hasExtra("exercise")) {
            exercise = intent.getParcelableExtra("exercise")!!

            binding.exerciseName.hint = exercise.exerciseDetails.name
            binding.instructions.hint = exercise.instructions
            binding.multiSpinner.text = exercise.exerciseDetails.muscleGroups.joinToString(", ")
            binding.setNumber.text = exercise.sets.toString()
            binding.setSlider.progress = exercise.sets
            binding.repNumber.text = exercise.sets.toString()
            binding.repSlider.progress = exercise.reps
            binding.rirNumber.text = exercise.sets.toString()
            binding.rirSlider.progress = exercise.rir.toInt()
            binding.createExercise.text = "Save Changes"
        }

        binding.createExercise.setOnClickListener {
            exercise = ExerciseModel(
                exerciseDetails = ExerciseDetailsModel(
                    binding.exerciseName.text.toString(),
                    muscleGroups,
                ),
                instructions = binding.instructions.text.toString(),
                sets = binding.setSlider.progress,
                reps = binding.repSlider.progress,
                rir = binding.rirSlider.progress.toFloat(),
            )
            if (!createExercise(exercise)) {
                Toast.makeText(this, "Failed to create new exercise, Try Again!", Toast.LENGTH_LONG).show()
            } else {
                app.workoutFS.currentWorkout.exercises += exercise.id to exercise
                setResult(RESULT_OK)
                finish()
            }
        }

    }

    private fun configureSeekBar(binding: ActivityExerciseBinding) {
        binding.setSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) { binding.setNumber.text = progress.toString() }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { binding.setNumber.text = seekBar!!.progress.toString() }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { binding.setNumber.text = seekBar!!.progress.toString() }
        })

        binding.repSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) { binding.repNumber.text = progress.toString() }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { binding.repNumber.text = seekBar!!.progress.toString() }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { binding.repNumber.text = seekBar!!.progress.toString() }
        })

        binding.rirSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) { binding.rirNumber.text = progress.toString() }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { binding.rirNumber.text = seekBar!!.progress.toString() }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { binding.rirNumber.text = seekBar!!.progress.toString() }
        })
    }


    private fun createExercise(exercise: ExerciseModel) = runBlocking {
        app.exerciseFS.create(exercise, workout, app.traineeFS.currentTrainee)
    }

    override fun OnMultiSpinnerItemSelected(chosenItems: MutableList<String>?) {
        if (intent.hasExtra("exercise")){
            exercise.exerciseDetails.muscleGroups = chosenItems as ArrayList<String>
        }
        muscleGroups = chosenItems as ArrayList<String>
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_exercise, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun deleteExercise() = runBlocking {
        app.exerciseFS.delete(app.exerciseFS.currentExercise, app.workoutFS.currentWorkout, app.traineeFS.currentTrainee)
        app.workoutFS.currentWorkout.exercises.remove(app.exerciseFS.currentExercise.id)
        workout.exercises.remove(app.exerciseFS.currentExercise.id)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.return_button -> {
                setResult(RESULT_OK)
                finish()
            }
            R.id.del_exercise -> {
                deleteExercise()
                setResult(RESULT_OK)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}