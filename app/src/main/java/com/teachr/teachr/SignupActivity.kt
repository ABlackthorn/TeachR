package com.teachr.teachr

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.content.Intent
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth


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
            val email = findViewById<EditText>(R.id.identifierEditText)
            val password = findViewById<EditText>(R.id.passwordEditText)
            createAccount(email.text.toString(), password.text.toString())
        }

        findViewById<Button>(R.id.cancelButton).setOnClickListener {
            finish()
        }

    }

    fun createAccount (email: String, password: String) {
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
                            var intent: Intent = Intent(this, ListActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@SignupActivity, getString(R.string.signup_failed), Toast.LENGTH_LONG).show()
                        }

                        // ...
                    }
        }
    }
}
