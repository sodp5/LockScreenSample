package com.munny.lockscreensample.lock

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log

class LockManagerImpl(application: Application) : LockManager {
    private val preference by lazy {
        application.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }
    private var verify = preference.getString(PASSWORD_KEY, "").isNullOrEmpty()
    private var lockActivity: Class<out Activity>? = null

    init {
        val callbacks = object : Application.ActivityLifecycleCallbacks {
            private var activityCount = 0

            override fun onActivityCreated(p0: Activity, p1: Bundle?) = Unit
            override fun onActivityStarted(p0: Activity) {
                activityCount++
                if (p0.javaClass == lockActivity) {
                    return
                }

                if (!LockManager.getInstance().isVerify()) {
                    lockActivity?.let { targetClass ->
                        application.startActivity(Intent(application, targetClass).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        })
                    }
                }
            }

            override fun onActivityResumed(p0: Activity) = Unit
            override fun onActivityPaused(p0: Activity) = Unit
            override fun onActivityStopped(p0: Activity) {
                if (--activityCount < 0 && !p0.isChangingConfigurations) {
                    LockManager.getInstance().lock()
                }
            }

            override fun onActivityDestroyed(p0: Activity) {
                if (activityCount < 0 && !p0.isChangingConfigurations) {
                    application.unregisterActivityLifecycleCallbacks(this)
                }
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) = Unit
        }

        application.registerActivityLifecycleCallbacks(callbacks)
    }

    override fun setPassword(password: String): Boolean {
        if (!verify) {
            return false
        }

        preference.edit()
            .putString(PASSWORD_KEY, password)
            .apply()
        verify = false

        return true
    }

    override fun requestVerify(password: String): Boolean {
        return (checkPassword(password)).also {
            verify = it
        }
    }

    override fun isVerify(): Boolean {
        return verify
    }

    override fun lock() {
        if (!preference.contains(PASSWORD_KEY)) {
            verify = true
            return
        }

        verify = false
    }

    override fun removePassword(password: String): Boolean {
        val success = checkPassword(password)

        if (success) {
            preference.edit()
                .remove(PASSWORD_KEY)
                .apply()
            verify = true
        }

        return success
    }

    override fun removePasswordAdmin() {
        preference.edit()
            .remove(PASSWORD_KEY)
            .apply()
        verify = true
    }

    override fun setLockActivity(lockActivity: Class<out Activity>) {
        this.lockActivity = lockActivity
    }

    private fun checkPassword(password: String): Boolean {
        return preference.getString(PASSWORD_KEY, null) == password
    }

    companion object {
        private const val PREFERENCE_NAME = "lock_pref"
        private const val PASSWORD_KEY = "lock_password"
    }
}