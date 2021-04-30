package com.alpine12.todolistapp.ui.task

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alpine12.todolistapp.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_task) {


    private val viewModel : TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}