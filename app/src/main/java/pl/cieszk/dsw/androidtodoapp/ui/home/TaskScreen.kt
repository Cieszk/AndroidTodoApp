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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.cieszk.dsw.androidtodoapp.database.model.Task
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TasksScreen(viewModel: TasksViewModel) {
    val tasks by viewModel.tasksFlow.collectAsState(emptyList())
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    val imePadding =
        with(LocalDensity.current) { LocalWindowInsets.current.ime.bottom.toDp() }

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
            floatingActionButtonPosition = FabPosition.End
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = imePadding) // Apply the padding to the bottom
            ) {
                items(tasks) { task ->
                    TaskItem(
                        task,
                        viewModel::updateTask,
                        viewModel::deleteTask
                    )
                }
            }
        }
    }
}


@Composable
fun NewTaskForm(onCreateTask: (String, String) -> Unit) {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Task title") },
            keyboardActions = KeyboardActions(
                onDone = { focusRequester2.requestFocus() }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Next),
            modifier = Modifier.focusRequester(focusRequester1)

        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Task description") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
            modifier = Modifier.focusRequester(focusRequester2)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { onCreateTask(title, description) }) {
            Text("Create Task")
        }
    }
}


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TaskItem(
        task: Task,
        onTaskStatusChange: (Task) -> Unit,
        onTaskDelete: (Task) -> Unit
    ) {
        var isExpanded by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            onClick = { isExpanded = !isExpanded }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row {
                    Column(Modifier.weight(1f)) {
                        Text(text = task.title, style = MaterialTheme.typography.titleLarge)
                    }
                    Switch(
                        checked = task.done,
                        onCheckedChange = { isChecked ->
                            val updatedTask = task.copy(done = isChecked)
                            onTaskStatusChange(updatedTask)
                            if (isChecked) {
                                coroutineScope.launch {
                                    delay(200)
                                    onTaskDelete(task)
                                }
                            }
                        }
                    )
                }

                // This part is only visible when the card is clicked
                if (isExpanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = task.description, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Created: ${task.creationDate}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }



