package com.teachr.teachr.login

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.content.Intent
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.Switch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.teachr.teachr.EntryListActivity
import com.teachr.teachr.HomeActivity
import com.teachr.teachr.R
import com.teachr.teachr.models.User


class SignupActivity : Activity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        mAuth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_signup)

        val signupButton = findViewById<Button>(R.id.signupButton)
        signupButton.setOnClickListener{

            //TODO: Implement form logic

            val email = findViewById<EditText>(R.id.identifierEditText).text.toString()
            val password = findViewById<EditText>(R.id.passwordEditText).text.toString()
            val passwordRepeat = findViewById<EditText>(R.id.passwordRepeatEditText).text.toString()
            val firstname = findViewById<EditText>(R.id.firstnameEditText).text.toString()
            val lastname = findViewById<EditText>(R.id.lastnameEditText).text.toString()
            val address = findViewById<EditText>(R.id.addressEditText).text.toString()
            val type = findViewById<Switch>(R.id.typeSwitch).isActivated

            if(password.length < 6) {
                Toast.makeText(this@SignupActivity, getString(R.string.password_too_short), Toast.LENGTH_LONG).show()
            } else if(password != passwordRepeat){
                Toast.makeText(this@SignupActivity, getString(R.string.passwords_mismatch), Toast.LENGTH_LONG).show()
            } else if(email.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || address.isEmpty()){
                Toast.makeText(this@SignupActivity, getString(R.string.enter_details_error), Toast.LENGTH_LONG).show()
            } else {
                createAccount(email, password, firstname, lastname, address, type)
            }
        }

        findViewById<Button>(R.id.cancelButton).setOnClickListener {
            finish()
        }

    }

    fun createAccount (email: String, password: String, firstname: String, lastname: String, address: String, type: Boolean) {
        if( !email.equals("") && !password.equals("")){
            mAuth?.createUserWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            mAuth?.currentUser?.sendEmailVerification()
                                    ?.addOnCompleteListener{ task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this@SignupActivity, getString(R.string.verification_message_sent), Toast.LENGTH_LONG).show()
                                        }
                                    }

                            val Fuser = FirebaseAuth.getInstance().currentUser
                            var user: User = User()
                            user.id = Fuser?.uid
                            user.email = email
                            user.password = password
                            user.firstname = firstname
                            user.lastname = lastname
                            user.address = address
                            if(type) user.type = 0
                            else user.type = 1
                            val database = FirebaseDatabase.getInstance().reference
                            database.child("users").child(user.id).setValue(user)

//                            database.runTransaction(object : Transaction.Handler {
//                                override fun doTransaction(mutableData: MutableData): Transaction.Result {
//                                    val u = mutableData.getValue(User::class.java)
//                                            ?: return Transaction.success(mutableData)
//
//                                    // Set value and report transaction success
//                                    mutableData.value = u
//                                    return Transaction.success(mutableData)
//                                }
//
//                                override fun onComplete(
//                                        databaseError: DatabaseError?,
//                                        b: Boolean,
//                                        dataSnapshot: DataSnapshot?
//                                ) {
//                                    // Transaction completed
//                                    //Log.d(TAG, "postTransaction:onComplete:" + databaseError!!)
//                                }
//                            })


                            var intent: Intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@SignupActivity, getString(R.string.signup_failed), Toast.LENGTH_LONG).show()
                        }

                        // ...
                    }
        }
    }
}
