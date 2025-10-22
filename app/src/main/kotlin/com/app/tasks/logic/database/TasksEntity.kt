package com.app.tasks.logic.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.tasks.constants.Constants

@Entity(tableName = Constants.DB_TABLE_NAME)
data class TasksEntity(
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "category") val category: String = "",
    @ColumnInfo(name = "completed") val isCompleted: Boolean = false,
    @ColumnInfo(name = "priority") val priority: Float,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
)