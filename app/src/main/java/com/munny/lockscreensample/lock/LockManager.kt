package com.munny.lockscreensample.lock

import android.app.Activity
import android.app.Application

interface LockManager {
    fun setPassword(password: String): Boolean
    fun requestVerify(password: String): Boolean
    fun isVerify(): Boolean
    fun lock()
    fun removePassword(password: String): Boolean
    fun removePasswordAdmin()
    fun setLockActivity(lockActivity: Class<out Activity>)

    companion object {
        private var lockManager: LockManager? = null

        fun init(application: Application) {
            lockManager = LockManagerImpl(application)
        }

        fun getInstance(): LockManager {
            return lockManager ?: throw IllegalStateException("must be init")
        }
    }
}