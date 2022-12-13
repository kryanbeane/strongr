package com.strongr.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.strongr.activities.workouts.adapters.WorkoutAdapter
import com.strongr.exercises.adapters.ExerciseAdapter


class RearrangeCardHelper(var adapter: WorkoutAdapter) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN
        or ItemTouchHelper.START or ItemTouchHelper.END,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val adapter = recyclerView.adapter as WorkoutAdapter
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        adapter.notifyItemMoved(from, to)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        var pos=viewHolder.adapterPosition
    }

}

class RearrangeCardHelperExercise(var adapter: ExerciseAdapter) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN
        or ItemTouchHelper.START or ItemTouchHelper.END,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val adapter = recyclerView.adapter as ExerciseAdapter
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        adapter.notifyItemMoved(from, to)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        var pos=viewHolder.adapterPosition
    }

}