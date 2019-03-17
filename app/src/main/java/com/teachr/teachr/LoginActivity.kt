package com.teachr.teachr

import android.app.Activity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import android.app.ActivityOptions
import android.content.Intent
import android.util.Log
import android.widget.*


class LoginActivity : Activity() {

    var backpressCounter : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        //getSupportActionBar()?.hide(); // hide the title bar
        //actionBar.hide()
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_login)
        val mAuth:FirebaseAuth = FirebaseAuth.getInstance()

        val identifierEditText = findViewById<EditText>(R.id.matiereEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener{
            if(!identifierEditText.text.toString().equals("") && !passwordEditText.text.toString().equals("")) {
                mAuth.signInWithEmailAndPassword(identifierEditText.text.toString(), passwordEditText.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = mAuth.getCurrentUser()
                                Toast.makeText(this@LoginActivity, R.string.authentication_success,
                                        Toast.LENGTH_SHORT).show()
                                var intent: Intent = Intent(this, EntryListActivity::class.java)
                                startActivity(intent)
                            } else {
                                Log.d("MYERROR", task.exception?.localizedMessage.toString())
                                Toast.makeText(this@LoginActivity, R.string.authentication_failure,
                                        Toast.LENGTH_SHORT).show()
                            }

                            // ...
                        }
            } else {
                Toast.makeText(this@LoginActivity, R.string.enter_details_error, Toast.LENGTH_SHORT).show()
            }
        }

        val signupButton = findViewById<Button>(R.id.signupButton);
        signupButton.setOnClickListener{
            var intent : Intent = Intent(this, SignupActivity::class.java);
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        findViewById<TextView>(R.id.forgottenPasswordTextView).setOnClickListener{
            var fpDialog: ForgottenPasswordDialog = ForgottenPasswordDialog(this)
            fpDialog.show()
        }

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        backpressCounter = 0
    }

    override fun onBackPressed() {
        if (backpressCounter == 0){
            Toast.makeText(this@LoginActivity, R.string.backpress_to_leave_message, Toast.LENGTH_SHORT).show()
        backpressCounter++
        } else {
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(homeIntent)
        }
    }

}
