package com.strongr.activities.workouts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.strongr.R
import com.strongr.activities.workouts.Workout2Activity

class WorkoutFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout, container, false)
        val addExerciseButton = view.findViewById<ImageButton>(R.id.addExercise)

        val workout = (activity as Workout2Activity).app.workoutFS.currentWorkout
        val name = view.findViewById<TextView>(R.id.workout_name)
        val muscleGroups = view.findViewById<TextView>(R.id.muscleGroups)

        name.text = workout.name
        muscleGroups.text = workout.targetMuscleGroups.joinToString(", ")

        addExerciseButton.setOnClickListener {
            val activity = activity as Workout2Activity
            activity.launchExerciseActivity()
        }

        val editWorkoutButton = view.findViewById<ImageButton>(R.id.editWorkout)

        editWorkoutButton.setOnClickListener {

        }
        return view
    }

}