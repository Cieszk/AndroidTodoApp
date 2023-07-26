package pl.cieszk.dsw.androidtodoapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pl.cieszk.dsw.androidtodoapp.database.dao.TaskDao
import pl.cieszk.dsw.androidtodoapp.database.model.Task

open class TasksViewModel(private val taskDao: TaskDao) : ViewModel() {
    open val tasksFlow: Flow<List<Task>> = taskDao.getAllTasks()

    fun addTask(task: Task) {
        viewModelScope.launch {
            taskDao.insertTasks(task)
        }
    }

    fun updateTask(updatedTask: Task) {
        viewModelScope.launch {
            taskDao.updateTask(updatedTask)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
        }
    }
}