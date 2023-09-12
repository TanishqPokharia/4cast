package com.tanishq.a4cast

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val splashImg:ImageView = findViewById(R.id.imageView)
        val fadeAnimation = AnimationUtils.loadAnimation(this,R.anim.fade)
        splashImg.startAnimation(fadeAnimation)
        val textView:TextView = findViewById(R.id.textView)
        textView.startAnimation(fadeAnimation)
        Handler().postDelayed({
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        },2000)
    }
}