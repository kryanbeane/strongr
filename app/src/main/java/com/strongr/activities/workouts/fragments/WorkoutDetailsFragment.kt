package com.strongr.activities.workouts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.strongr.R
import com.strongr.activities.workouts.ViewWorkoutActivity

class WorkoutDetailsFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout, container, false)
        val addExerciseButton = view.findViewById<Button>(R.id.addExercise)

        val workout = (activity as ViewWorkoutActivity).app.workoutFS.currentWorkout
        val name = view.findViewById<TextView>(R.id.workout_name)
        val muscleGroups = view.findViewById<TextView>(R.id.muscleGroups)

        name.text = workout.name
        muscleGroups.text = workout.targetMuscleGroups.joinToString(", ")

        addExerciseButton.setOnClickListener {
            val activity = activity as ViewWorkoutActivity
            activity.launchExerciseActivity()
        }

        val editWorkoutButton = view.findViewById<Button>(R.id.editWorkout)

        editWorkoutButton.setOnClickListener {
            val activity = activity as ViewWorkoutActivity
            activity.launchEditWorkoutFragment()
        }
        return view
    }

}