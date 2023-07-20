package com.example.tangeryme

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

class AccountManager {

    //objects allow for behaviour similar to static methods
    companion object {

        private var auth: FirebaseAuth = Firebase.auth
        private var functions: FirebaseFunctions = Firebase.functions

        private lateinit var userData: FirebaseUser

        //check if the user is currently logged in
        //returns TRUE if the user IS LOGGED IN
        fun checkLoginStatus(): Boolean {
            val currentUser = auth.currentUser
            return currentUser != null
        }

        //get the current user object
        fun getUserData(): FirebaseUser {
            return userData
        }

        //sign up a new user with email/password
        fun signUpNewUser(username: String, email: String, password: String): Task<HttpsCallableResult> {
            return functions
                    //check the validity of the username
                .getHttpsCallable("checkValidUsername")
                .call(hashMapOf("username" to username)).addOnCompleteListener { validityCheck ->
                    if(validityCheck.isSuccessful){

                        val result = validityCheck.result?.data as Map<*,*> //the map represents the JSON object that will be returned
                        if(result["code"] == 0){    //code 0 means success

                            //if the validity check is successful, create a new auth account
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { signUp ->
                                    if(signUp.isSuccessful){

                                        if(checkLoginStatus() == true){

                                            userData = auth.currentUser!!   //set the user data upon completion

                                            functions.getHttpsCallable("registerUser").call(hashMapOf(
                                                "username" to username,
                                                "email" to email
                                            )).addOnCompleteListener { registration ->
                                                if(registration.isSuccessful){
                                                    Log.d(TAG, "signupWithEmail:Success")
                                                }
                                            }
                                        }
                                    }
                                }
                        }
                    }
                }
        }
    }

}