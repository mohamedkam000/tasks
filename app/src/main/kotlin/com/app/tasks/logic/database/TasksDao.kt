package com.app.tasks.logic.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.tasks.constants.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(toDo: TasksEntity)

    @Query("SELECT * FROM ${Constants.DB_TABLE_NAME}")
    fun getAll(): Flow<List<TasksEntity>>

    @Update
    suspend fun update(toDo: TasksEntity)

    @Delete
    suspend fun delete(toDo: TasksEntity)

    @Query("DELETE FROM ${Constants.DB_TABLE_NAME} WHERE id in (:toDoIds)")
    suspend fun deleteFromIds(toDoIds: Set<Int>)
}