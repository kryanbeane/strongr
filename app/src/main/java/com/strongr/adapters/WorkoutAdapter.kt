package com.strongr.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strongr.databinding.CardWorkoutBinding
import com.strongr.models.workout.WorkoutModel


class WorkoutAdapter constructor(private var workouts: List<WorkoutModel>) :
    RecyclerView.Adapter<WorkoutAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardWorkoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val workout = workouts[holder.adapterPosition]
        holder.bind(workout)
    }

    override fun getItemCount(): Int = workouts.size

    class MainHolder(private val binding: CardWorkoutBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: WorkoutModel) {
            binding.workoutName.text = workout.name
        }
    }
}