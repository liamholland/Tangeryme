package com.example.tangeryme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    var newPromptAvailable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initialise the firebase authentication connection
        AccountManager.initFirebaseAuth()
    }

    public override fun onStart() {
        super.onStart()

        //Set the UI based on whether the user is logged in
        if(AccountManager.checkLoginStatus()){
            //present poetry submission / view poetry page
        }
        else{
            //present login/signup page
        }
    }
}