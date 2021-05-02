package com.alpine12.todolistapp.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alpine12.todolistapp.model.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    @ExperimentalCoroutinesApi
    private val taskFlow = searchQuery.flatMapLatest {
        taskDao.getTask(it)
    }
    @ExperimentalCoroutinesApi
    val task = taskFlow.asLiveData()

}