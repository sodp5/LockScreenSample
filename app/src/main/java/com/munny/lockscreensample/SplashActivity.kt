package com.munny.lockscreensample

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.munny.lockscreensample.lock.LockManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 500)
    }

    override fun onStart() {
        super.onStart()

        LockManager.init(application)
        LockManager.getInstance().setLockActivity(LockActivity::class.java)
    }
}