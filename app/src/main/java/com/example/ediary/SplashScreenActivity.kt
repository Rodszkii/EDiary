package com.example.ediary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.lang.Exception
import kotlin.concurrent.thread

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        val background = object :Thread()
        {
            override fun run()
            {
             try {
                Thread.sleep(750)
                 val intent = Intent(baseContext,MainActivity::class.java)
                 startActivity(intent)
             }catch (e:Exception)
             {
                 e.printStackTrace()
             }
            }
        }
        background.start()
    }
}
