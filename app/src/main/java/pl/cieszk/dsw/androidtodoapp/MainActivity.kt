package pl.cieszk.dsw.androidtodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import pl.cieszk.dsw.androidtodoapp.database.TaskDatabase
import pl.cieszk.dsw.androidtodoapp.ui.home.TasksScreen
import pl.cieszk.dsw.androidtodoapp.ui.home.TasksViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java, "task_database"
        ).build()

        val taskDao = db.taskDao()

        val tasksViewModel = TasksViewModel(taskDao)

        setContent {
            TasksScreen(tasksViewModel)
        }
    }
}
