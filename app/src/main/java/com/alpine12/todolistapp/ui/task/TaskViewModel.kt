package com.alpine12.todolistapp.ui.task

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.alpine12.todolistapp.data.PreferenceManager
import com.alpine12.todolistapp.data.SortOrder
import com.alpine12.todolistapp.data.Task
import com.alpine12.todolistapp.data.TaskDao
import com.alpine12.todolistapp.ui.ADD_TASK_RESULT_OK
import com.alpine12.todolistapp.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class TaskViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    private val preferenceManager: PreferenceManager,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    //    val searchQuery = MutableStateFlow("")
    val preferencesFlow = preferenceManager.preferencesFlow

    private val taskEventChannel = Channel<TaskEvent>()
    val taskEvent = taskEventChannel.receiveAsFlow()

    @ExperimentalCoroutinesApi
    private val taskFlow =
        combine(searchQuery.asFlow(), preferencesFlow)
        { query, filterPreferences ->
            Pair(query, filterPreferences)
        }.flatMapLatest { (query, filterPreferences) ->
            taskDao.getTask(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
        }

    @ExperimentalCoroutinesApi
    val task = taskFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) {
        viewModelScope.launch {
            preferenceManager.updateSortOrder(sortOrder)
        }
    }

    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferenceManager.updateHideCompleted(hideCompleted)
    }

    fun onTaskSelected(task: Task) = viewModelScope.launch {
        taskEventChannel.send(TaskEvent.NavigateToEditTaskScreen(task))
    }

    fun onTaskCheckedChanged(task: Task, isChecked: Boolean) = viewModelScope.launch {
        taskDao.update(task.copy(completed = isChecked))
    }

    fun onTaskSwiped(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
        taskEventChannel.send(TaskEvent.ShowUndoDeleteTaskMessage(task))
    }

    fun onUndoDeleteClick(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }

    fun onAddNewTaskClick() = viewModelScope.launch {
        taskEventChannel.send(TaskEvent.NavigateToAddTaskScreen)
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task added")
            EDIT_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task updated")
        }
    }

    private fun showTaskSavedConfirmationMessage(msg: String) = viewModelScope.launch {
        taskEventChannel.send(TaskEvent.ShowTaskSavedConfirmationMessage(msg))
    }

    sealed class TaskEvent {
        object NavigateToAddTaskScreen : TaskEvent()
        data class NavigateToEditTaskScreen(val task: Task) : TaskEvent()
        data class ShowUndoDeleteTaskMessage(val task: Task) : TaskEvent()
        data class ShowTaskSavedConfirmationMessage(val msg : String) : TaskEvent()
    }

}
