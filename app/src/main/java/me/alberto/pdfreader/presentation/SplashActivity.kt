package me.alberto.pdfreader.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import me.alberto.pdfreader.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        makeFullScreen()

        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            finish()
        }, 2000)

    }

    private fun makeFullScreen() {
        //remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        //make full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //Hide toolbar
        supportActionBar?.hide()
    }
}