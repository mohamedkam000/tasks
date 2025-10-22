package com.app.tasks.ui.pages.crash

import android.content.Context
import android.content.Intent
import android.os.Process
import com.app.tasks.ui.activities.CrashActivity
import kotlin.system.exitProcess

class CrashHandler(private val context: Context) : Thread.UncaughtExceptionHandler {
    private val defaultUEH = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        val stackTrace = ex.stackTraceToString()

        val intent = Intent(context, CrashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("crash_logs", stackTrace)
        }
        context.startActivity(intent)
        defaultUEH?.uncaughtException(thread, ex)
        Process.killProcess(Process.myPid())
        exitProcess(10)
    }
}