package com.aditya.dailynotes.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.aditya.dailynotes.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        this.supportActionBar?.hide()
        this.window.statusBarColor = this.resources.getColor(R.color.white)
//        this.window.navigationBarColor = this.resources.getColor(R.color.app_color)

        Handler().postDelayed({
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        },1200)


    }
}