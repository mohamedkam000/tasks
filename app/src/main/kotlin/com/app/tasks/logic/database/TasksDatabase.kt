package com.app.tasks.logic.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.tasks.constants.Constants

@Database(entities = [TasksEntity::class], version = 4)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun toDoDao(): TasksDao

    companion object {
        @Volatile
        private var INSTANCE: TasksDatabase? = null
        fun getDatabase(context: Context): TasksDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TasksDatabase::class.java,
                    Constants.DB_NAME
                )
                    .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
                    .fallbackToDestructiveMigration(false)
                    .build()

                INSTANCE = instance
                return instance
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL()
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL()
                db.execSQL()
                db.execSQL()
                db.execSQL()
            }
        }
    }
}