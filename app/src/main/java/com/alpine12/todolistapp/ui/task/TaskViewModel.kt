package com.alpine12.todolistapp.ui.task

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.alpine12.todolistapp.model.TaskDao
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

class TaskViewModel @Inject constructor (private val taskDao : TaskDao) : ViewModel() {



}