package com.alpine12.todolistapp.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alpine12.todolistapp.model.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted = MutableStateFlow(false)

//    @ExperimentalCoroutinesApi
//    private val taskFlow = searchQuery.flatMapLatest {
//        taskDao.getTask(it)
//    }

    @ExperimentalCoroutinesApi
    private val taskFlow =
        combine(searchQuery, sortOrder, hideCompleted)
        { query, sortOrder, hideCompleted ->
            Triple(query, sortOrder, hideCompleted)
        }.flatMapLatest { (query, sortOrder, hideCompleted) ->
            taskDao.getTask(query, sortOrder, hideCompleted)
        }

    @ExperimentalCoroutinesApi
    val task = taskFlow.asLiveData()

}

enum class SortOrder {
    BY_NAME, BY_DATE
}