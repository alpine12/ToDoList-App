package com.alpine12.todolistapp.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alpine12.todolistapp.model.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    val task = taskDao.getTask().asLiveData()

}