package com.teachr.teachr

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class ListActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        //getSupportActionBar()?.hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_list)
        val mAuth = FirebaseAuth.getInstance()
        findViewById<Button>(R.id.button).setOnClickListener{
            mAuth.signOut()
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
