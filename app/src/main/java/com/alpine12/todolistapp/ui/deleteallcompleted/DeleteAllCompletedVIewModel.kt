package com.alpine12.todolistapp.ui.deleteallcompleted

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.alpine12.todolistapp.data.TaskDao
import com.alpine12.todolistapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteAllCompletedVIewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

        fun onConfirmClick() = applicationScope.launch {
            taskDao.deleteCompletedTask()
        }
}