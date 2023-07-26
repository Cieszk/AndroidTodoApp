package pl.cieszk.dsw.androidtodoapp.database


import kotlinx.coroutines.flow.Flow
import pl.cieszk.dsw.androidtodoapp.database.model.Task

interface TaskRepository {

    fun getAllTasksStream(): Flow<List<Task>>

    fun getTaskStream(id: Int): Flow<Task?>

    suspend fun insertTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)
}