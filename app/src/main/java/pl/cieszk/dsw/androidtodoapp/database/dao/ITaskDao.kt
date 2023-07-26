package pl.cieszk.dsw.androidtodoapp.database.dao

import kotlinx.coroutines.flow.Flow
import pl.cieszk.dsw.androidtodoapp.database.model.Task

interface ITaskDao {
    fun getAllTasks(): Flow<List<Task>>

    fun getTask(id: Int): Flow<Task>

    suspend fun insertTasks(tasks: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)
}