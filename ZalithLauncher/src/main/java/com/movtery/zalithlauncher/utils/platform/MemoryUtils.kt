package com.movtery.zalithlauncher.utils.platform

import android.app.ActivityManager
import android.content.Context

class MemoryUtils {
    companion object {
        private var activityManager: ActivityManager? = null

        private fun init(context: Context) {
            activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        }

        @JvmStatic
        fun getTotalDeviceMemory(context: Context): Long {
            activityManager ?: run { init(context) }

            val memInfo = ActivityManager.MemoryInfo()
            activityManager?.getMemoryInfo(memInfo)
            return memInfo.totalMem
        }

        @JvmStatic
        fun getUsedDeviceMemory(context: Context): Long {
            activityManager ?: run { init(context) }

            val memInfo = ActivityManager.MemoryInfo()
            activityManager?.getMemoryInfo(memInfo)
            return memInfo.totalMem - memInfo.availMem
        }

        @JvmStatic
        fun getFreeDeviceMemory(context: Context): Long {
            activityManager ?: run { init(context) }

            val memInfo = ActivityManager.MemoryInfo()
            activityManager?.getMemoryInfo(memInfo)
            return memInfo.availMem
        }
    }
}