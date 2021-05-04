package com.alpine12.todolistapp.ui.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alpine12.todolistapp.data.Task
import com.alpine12.todolistapp.databinding.ItemTaskBinding

class TaskAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

   inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        listener.onItemCLick(task)
                    }
                }

                checkboxComplete.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        listener.onCheckBoxClick(task, checkboxComplete.isChecked)
                    }
                }
            }
        }

        fun bind(task: Task) {
            binding.apply {
                checkboxComplete.isChecked = task.completed
                textviewName.text = task.name
                textviewName.paint.isStrikeThruText = task.completed
                labelPriority.isVisible = task.important
            }
        }
    }

    interface OnItemClickListener {
        fun onItemCLick(task: Task)
        fun onCheckBoxClick(task: Task, isChecked: Boolean)
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}