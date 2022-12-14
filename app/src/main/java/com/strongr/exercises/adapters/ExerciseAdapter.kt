package com.strongr.exercises.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strongr.activities.workouts.Workout2Activity
import com.strongr.activities.workouts.adapters.WorkoutAdapter
import com.strongr.databinding.CardExerciseBinding
import com.strongr.models.exercise.ExerciseModel
import com.strongr.models.workout.WorkoutModel

class ExerciseAdapter(
    private var exercises: MutableMap<String, ExerciseModel>,
    private val listener: ExerciseListener
):
    RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardExerciseBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = exercises.entries.elementAt(holder.adapterPosition).value
        holder.bind(exercise, listener)
    }

    override fun getItemCount(): Int = exercises.size

    class ViewHolder(private val binding: CardExerciseBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(exercise: ExerciseModel, listener: ExerciseListener) {
            binding.exerciseName.text = exercise.exerciseDetails.name
            binding.root.setOnClickListener { listener.onExerciseClick(exercise) }

        }
    }
}

interface ExerciseListener {
    fun onExerciseClick(exercise: ExerciseModel)
}