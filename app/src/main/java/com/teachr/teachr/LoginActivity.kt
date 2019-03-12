package com.teachr.teachr

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.app.ActivityOptions
import android.content.Intent
import android.util.Log


class LoginActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        //getSupportActionBar()?.hide(); // hide the title bar
        //actionBar.hide()
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_login)
        val mAuth:FirebaseAuth = FirebaseAuth.getInstance()

        val identifierEditText = findViewById<EditText>(R.id.identifierEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener{
            if(!identifierEditText.text.toString().equals("") && !passwordEditText.text.toString().equals("")) {
                mAuth.signInWithEmailAndPassword(identifierEditText.text.toString(), passwordEditText.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.i("LOGIN", "signInWithEmail:success")
                                val user = mAuth.getCurrentUser()
                                Toast.makeText(this@LoginActivity, "Successful Authentication",
                                        Toast.LENGTH_SHORT).show()
                                var intent: Intent = Intent(this, ListActivity::class.java)
                                startActivity(intent)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("LOGIN", "signInWithEmail:failure", task.exception)
                                Toast.makeText(this@LoginActivity, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show()
                                //updateUI(null)
                            }

                            // ...
                        }
            } else {
                Toast.makeText(this@LoginActivity, R.string.enter_details_error, Toast.LENGTH_SHORT).show()
            }
        }

        val signupButton = findViewById<Button>(R.id.signupButton);
        signupButton.setOnClickListener{
            var intent : Intent = Intent(this, ListActivity::class.java);
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

    }

    override fun onStart() {
        super.onStart()
    }

}
