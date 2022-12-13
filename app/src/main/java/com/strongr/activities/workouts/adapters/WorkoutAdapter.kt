package com.strongr.activities.workouts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strongr.databinding.CardWorkoutBinding
import com.strongr.models.workout.WorkoutModel


class WorkoutAdapter(
    private var workouts: MutableMap<String, WorkoutModel>,
    private val listener: WorkoutListener
):
    RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardWorkoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var workout = workouts.entries.elementAt(holder.adapterPosition).value
        holder.bind(workout, listener)
    }

    override fun getItemCount(): Int = workouts.size

    class ViewHolder(private val binding: CardWorkoutBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: WorkoutModel, listener: WorkoutListener) {
            binding.workoutName.text = workout.name
            binding.muscleGroups.text = workout.targetMuscleGroups.joinToString(", ")
            binding.root.setOnClickListener { listener.onWorkoutClick(workout) }
        }
    }
}

interface WorkoutListener {
    fun onWorkoutClick(workout: WorkoutModel)
}