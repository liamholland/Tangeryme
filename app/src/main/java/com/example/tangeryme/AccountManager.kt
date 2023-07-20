package com.example.tangeryme

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountManager {

    //objects allow for behaviour similar to static methods
    companion object {

        private lateinit var auth: FirebaseAuth

        //initialise Firebase authentication
        fun initFirebaseAuth() {
            auth = Firebase.auth
        }

        //check if the user is currently logged in
        //returns TRUE if the user IS LOGGED IN
        fun checkLoginStatus(): Boolean {
            val currentUser = auth.currentUser
            return currentUser != null
        }

        //sign up a new user with email/password
        fun signUpNewUser(username: String, email: String, Password: String) {

        }
    }

}