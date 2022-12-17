package com.strongr.activities.workouts.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.anurag.multiselectionspinner.MultiSelectionSpinnerDialog
import com.anurag.multiselectionspinner.MultiSpinner
import com.strongr.R
import com.strongr.models.workout.WorkoutModel

class EditWorkoutFragment: Fragment(), MultiSelectionSpinnerDialog.OnMultiSpinnerSelectionListener {

    private lateinit var workoutName: EditText
    private lateinit var muscleGroups: MultiSpinner
    private lateinit var workout: WorkoutModel
    private val muscleGroupList: MutableList<String> = mutableListOf (
        " Chest", " Upper Back", " Lats", " Rear Delts", " Side Delts",
        " Front Delts", " Triceps", " Biceps", " Forearms", " Abs",
        " Quads", " Hamstrings", " Calves", " Glutes"
    )

    companion object {
        fun newInstance(workout: WorkoutModel): EditWorkoutFragment {
            val fragment = EditWorkoutFragment()
            fragment.workout = workout
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_workout, container, false)

        workoutName = view.findViewById(R.id.workoutName)
        muscleGroups = view.findViewById(R.id.multiSpinner)
        val currentWorkout = workout

        muscleGroups.setAdapterWithOutImage(activity, muscleGroupList, this)
        muscleGroups.initMultiSpinner(activity, muscleGroups)

        workoutName.setText(currentWorkout.name)
        muscleGroups.text = currentWorkout.targetMuscleGroups.joinToString(", ")
        return view
    }

    override fun OnMultiSpinnerItemSelected(chosenItems: MutableList<String>?) {
        workout.targetMuscleGroups = chosenItems as ArrayList<String>
    }

}
