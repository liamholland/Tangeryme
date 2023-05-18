package com.example.tangeryme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    var newPromptAvailable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(newPromptAvailable){
            setContentView(R.layout.poem_submission)
        }
        else{
            setContentView(R.layout.previous_poems)
        }
    }
}