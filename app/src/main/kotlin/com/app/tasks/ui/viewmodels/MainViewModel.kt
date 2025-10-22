package com.app.tasks.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.tasks.*
import com.app.tasks.constants.Constants
import com.app.tasks.logic.Repository
import com.app.tasks.logic.database.TasksEntity
import com.app.tasks.logic.datastore.DataStoreManager
import com.app.tasks.logic.model.SortingMethod
import com.app.tasks.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel : ViewModel() {
    private val toDos: Flow<List<TasksEntity>> = Repository.getAllTodos()
    val sortedTodos: Flow<List<TasksEntity>> =
        DataStoreManager.sortingMethodFlow.flatMapLatest { sortingMethod ->
            toDos.map { list ->
                when (SortingMethod.fromId(sortingMethod)) {
                    SortingMethod.Sequential -> list.sortedBy { it.id }
                    SortingMethod.Category -> list.sortedBy { it.category }
                    SortingMethod.Priority -> list.sortedByDescending { it.priority }
                    SortingMethod.Completion -> list.sortedBy { it.isCompleted }
                    SortingMethod.AlphabeticalAscending -> list.sortedBy { it.content }
                    SortingMethod.AlphabeticalDescending -> list.sortedByDescending { it.content }
                }
            }
        }

    val showConfetti = mutableStateOf(false)
    var selectedEditTodo by mutableStateOf<TasksEntity?>(null)
        private set

    private val _selectedTodoIds = MutableStateFlow(listOf<Int>())
    val selectedTodoIds = _selectedTodoIds.asStateFlow()

    fun addTodo(toDo: TasksEntity) {
        viewModelScope.launch {
            Repository.insertTodo(toDo)
        }
    }

    fun updateTodo(toDo: TasksEntity) {
        viewModelScope.launch {
            Repository.updateTodo(toDo)
        }
    }

    fun deleteTodo(toDo: TasksEntity) {
        viewModelScope.launch {
            Repository.deleteTodo(toDo)
        }
    }

    fun setEditTodoItem(toDo: TodoEntity?) {
        selectedEditTodo = toDo
    }

    fun toggleTodoSelection(toDo: TodoEntity) {
        _selectedTodoIds.update { idList ->
            if (idList.contains(toDo.id)) {
                idList - toDo.id
            } else {
                idList + toDo.id
            }
        }
    }

    fun selectAllTodos() {
        viewModelScope.launch {
            toDos.firstOrNull()?.let { todos ->
                val allIds = todos.map { it.id }
                _selectedTodoIds.value = allIds
            }
        }
    }

    fun clearAllTodoSelection() {
        _selectedTodoIds.update { emptyList() }
    }

    fun deleteSelectedTodo() {
        viewModelScope.launch {
            Repository.deleteTodoFromIds(selectedTodoIds.value)
            clearAllTodoSelection()
        }
    }

    fun playConfetti() {
        showConfetti.value = true
    }

    fun backupAppData(uri: Uri, context: Context, onResult: (completed: Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = runCatching {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    ZipOutputStream(BufferedOutputStream(outputStream)).use { zipOutStream ->
                        getBackupFiles(context).forEach { file ->
                            FileUtils.addFileToZip(file, file.name, zipOutStream)
                        }
                    }
                }
            }.isSuccess
            withContext(Dispatchers.Main) { onResult(result) }
        }
    }

    fun restoreAppData(uri: Uri, context: Context, onResult: (completed: Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = runCatching {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    ZipInputStream(BufferedInputStream(inputStream)).use { zipInputStream ->
                        extractZipEntries(zipInputStream, context)
                    }
                }
            }.isSuccess
            withContext(Dispatchers.Main) { onResult(result) }
        }
    }

    private fun getBackupFiles(context: Context): List<File> {
        val dbPath = Tasks.db.openHelper.writableDatabase.path
        val prefPath = "${context.filesDir}/datastore"
        return listOf(
            context.getDatabasePath(Constants.DB_NAME),
            File("$dbPath-wal"),
            File("$dbPath-shm"),
            File("$prefPath/${Constants.SP_NAME}.preferences_pb")
        ).filter { it.exists() }
    }

    private fun extractZipEntries(zipInputStream: ZipInputStream, context: Context) {
        val dbPath = context.getDatabasePath(Constants.DB_NAME).parent
        val prefPath = "${context.filesDir}/datastore/"
        generateSequence { zipInputStream.nextEntry }.forEach { zipEntry ->
            val outputFile = File(
                if (zipEntry.name.endsWith(".preferences_pb")) prefPath else dbPath,
                zipEntry.name
            )
            if (zipEntry.isDirectory) {
                outputFile.mkdirs()
            } else {
                outputFile.parentFile?.mkdirs()
                FileOutputStream(outputFile).use { zipInputStream.copyTo(it) }
            }
            zipInputStream.closeEntry()
        }
    }
}