package com.munny.lockscreensample

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.munny.lockscreensample.lock.LockManager

class LockActivity : AppCompatActivity() {
    private val lockManager = LockManager.getInstance()
    private val passwordList = ArrayList<String>()
    private val passwordViewList by lazy {
        arrayOf<View>(
            findViewById(R.id.first_password),
            findViewById(R.id.second_password),
            findViewById(R.id.third_password),
            findViewById(R.id.fourth_password)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)
    }

    override fun onBackPressed() {
        removePassword()
    }

    fun onKeyPadClicked(view: View) {
        when (view) {
            is TextView -> {
                addPassword(view.text.toString())
            }
            is ImageView -> {
                removePassword()
            }
            else -> {
                LockManager.getInstance().removePasswordAdmin()
            }
        }
    }

    private fun addPassword(num: String) {
        passwordList.add(num)
        notifyChangePasswordState()

        if (passwordList.size == 4) {
            val password = passwordList.joinToString(separator = "")
            if (lockManager.requestVerify(password)) {
                finish()
            } else {
                passwordList.clear()
                notifyChangePasswordState()
            }
        }
    }

    private fun removePassword() {
        if (passwordList.isEmpty()) {
            return
        }

        val removePosition = passwordList.size - 1
        passwordList.removeAt(removePosition)
        notifyChangePasswordState()
    }

    private fun notifyChangePasswordState() {
        passwordViewList.forEachIndexed { index, view ->
            val password = passwordList.getOrNull(index)

            if (password.isNullOrEmpty()) {
                val color = resources.getColor(R.color.white, null)
                view.setBackgroundColor(color)
            } else {
                val circleDrawable = resources.getDrawable(R.drawable.black_circle, null)
                view.background = circleDrawable
            }
        }
    }
}