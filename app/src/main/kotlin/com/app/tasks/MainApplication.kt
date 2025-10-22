package com.app.tasks

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.app.tasks.logic.database.TasksDatabase
import com.app.tasks.ui.pages.crash.CrashHandler

class Tasks : Application() {
    private val database by lazy { TasksDatabase.getDatabase(this) }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var db: TasksDatabase
    }

    override fun onCreate() {
        super.onCreate()

        db = database
        context = applicationContext

        val crashHandler = CrashHandler(applicationContext)
        Thread.setDefaultUncaughtExceptionHandler(crashHandler)
    }
}