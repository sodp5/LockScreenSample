package com.munny.lockscreensample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.munny.lockscreensample.lock.LockManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.edit_password)

        findViewById<Button>(R.id.btn_set_password).setOnClickListener {
            val password = editText.text.toString()
            if (password.length != 4) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            LockManager.getInstance().setPassword(password)
            Toast.makeText(this, "비밀번호를 설정했습니다.", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btn_remove_password).setOnClickListener {
            val password = editText.text.toString()
            if (password.length != 4) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            LockManager.getInstance().removePassword(password)
            Toast.makeText(this, "비밀번호를 삭제했습니다.", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btn_request_verify).setOnClickListener {
            if (LockManager.getInstance().isVerify()) {
                Toast.makeText(this, "인증되어있거나 비밀번호 설정이 안되어있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            startActivity(Intent(this, LockActivity::class.java))
        }

        findViewById<Button>(R.id.btn_remove_password_admin).setOnClickListener {
            LockManager.getInstance().removePasswordAdmin()
            Toast.makeText(this, "비밀번호를 삭제했습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}