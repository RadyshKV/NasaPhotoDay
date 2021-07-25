package com.example.nasaphotoday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nasaphotoday.ui.main.picture.NasaPhotoDayFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, NasaPhotoDayFragment.newInstance())
                    .commitNow()
        }
    }
}