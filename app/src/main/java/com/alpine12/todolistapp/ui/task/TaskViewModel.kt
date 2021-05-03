package com.alpine12.todolistapp.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.alpine12.todolistapp.data.PreferenceManager
import com.alpine12.todolistapp.data.SortOrder
import com.alpine12.todolistapp.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    val preferencesFlow = preferenceManager.preferencesFlow

    @ExperimentalCoroutinesApi
    private val taskFlow =
        combine(searchQuery, preferencesFlow)
        { query, filterPreferences ->
            Pair(query, filterPreferences)
        }.flatMapLatest { (query, filterPreferences) ->
            taskDao.getTask(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
        }

    fun onSortOrderSelected(sortOrder: SortOrder) {
        viewModelScope.launch {
            preferenceManager.updateSortOrder(sortOrder)
        }
    }

    fun onHideCompletedClick(hideCompleted : Boolean)= viewModelScope.launch {
        preferenceManager.updateHideCompleted(hideCompleted)
    }

    @ExperimentalCoroutinesApi
    val task = taskFlow.asLiveData()
}
