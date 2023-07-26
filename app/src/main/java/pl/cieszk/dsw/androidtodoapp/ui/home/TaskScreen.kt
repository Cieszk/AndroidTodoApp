package pl.cieszk.dsw.androidtodoapp.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pl.cieszk.dsw.androidtodoapp.database.model.Task
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TasksScreen(
    viewModel: TasksViewModel,
    navigateToTaskEntry: (Int) -> Unit
) {
    val tasks by viewModel.tasksFlow.collectAsState(emptyList())
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            NewTaskForm { title, description ->
                viewModel.addTask(
                    Task(
                        0,
                        title,
                        description,
                        false,
                        Date()
                    )
                )
                coroutineScope.launch { sheetState.hide() }
            }
        }
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch { sheetState.show() }
                    },
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    content = {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add Task"
                        )
                    }
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            content = {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(tasks) { task ->
                        TaskItem(task, { updatedTask ->
                            viewModel.updateTask(updatedTask)
                        }, navigateToTaskEntry)
                    }
                }
            }
        )
    }
}


@Composable
fun NewTaskForm(onCreateTask: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Task title") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Task description") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { onCreateTask(title, description) }) {
            Text("Create Task")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(task: Task, onTaskStatusChange: (Task) -> Unit, navigateToTaskEntry: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        onClick = { navigateToTaskEntry(task.id) }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(Modifier.weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.titleLarge)
            }
            Switch(
                checked = task.done,
                onCheckedChange = { isChecked ->
                    val updatedTask = task.copy(done = isChecked)
                    onTaskStatusChange(updatedTask)
                }
            )
        }
    }
}

//class MockTaskDao : TaskDao {
//    private val tasks = listOf(
//        Task(1, "Task 1", "This is task 1", false, Date()),
//        Task(2, "Task 2", "This is task 2", true, Date())
//    )
//
//    override fun getAllTasks(): Flow<List<Task>> = flowOf(tasks)
//
//    override fun getTask(id: Int): Flow<Task> = flowOf(tasks.first { it.id == id })
//
//    // For insert, update and delete methods we don't do anything as they're not used in preview
//    override suspend fun insertTasks(tasks: Task) {}
//
//    override suspend fun updateTask(task: Task) {}
//
//    override suspend fun deleteTask(task: Task) {}
//}
//
//class MockTasksViewModel :
//    TasksViewModel(MockTaskDao()) { // assuming your ViewModel can take a nullable Dao
//    // Replace with your own mock data
//    override val tasksFlow = flowOf(
//        listOf(
//            Task(1, "Task 1", "This is task 1", false, Date()),
//            Task(2, "Task 2", "This is task 2", true, Date())
//        )
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun TasksScreenPreview() {
//    val mockViewModel = MockTasksViewModel()
//    TasksScreen(mockViewModel) {
//        // no action for preview
//    }
//}




