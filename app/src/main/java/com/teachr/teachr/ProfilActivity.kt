package com.teachr.teachr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfilActivity : Activity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        val Fuser = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance().reference
        val userEventListener = object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                var user = p0.getValue(User::class.java)

                var email : String
                var firstname : String
                var lastname : String
                var address : String

                if (user != null){
                    email = user.email
                    firstname = user.firstname
                    lastname = user.lastname
                    address = user.address

                    setTextProfil(email,firstname,lastname,address)

                }
            }

        }
        database.child("users").child(""+Fuser?.uid).addValueEventListener(userEventListener)

        val signupButton = findViewById<Button>(R.id.signupButton);
        signupButton.setOnClickListener{

            val email = findViewById<EditText>(R.id.emailProfilEditText).text.toString()
            val firstname =findViewById<EditText>(R.id.firstnameProfilEditText).text.toString()
            val lastname = findViewById<EditText>(R.id.lastnameProfilEditText).text.toString()
            val address = findViewById<EditText>(R.id.addressProfilEditText).text.toString()

            if(email.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || address.isEmpty()){
                Toast.makeText(this@ProfilActivity, getString(R.string.enter_details_error), Toast.LENGTH_LONG).show()
            } else {
                ModifyAccount(email, firstname, lastname, address)
            }

        }
    }

    fun ModifyAccount (email: String, firstname: String, lastname: String, address: String) {

        val Fuser = FirebaseAuth.getInstance().currentUser
        var user: User = User()
        user.id = Fuser?.uid
        user.email = email
        user.firstname = firstname
        user.lastname = lastname
        user.address = address
        val database = FirebaseDatabase.getInstance().reference
        database.child("users").push().setValue(user)

        var intent: Intent = Intent(this, EntryListActivity::class.java)
        startActivity(intent)
    }

    fun setTextProfil (email: String, firstname: String, lastname: String, address: String) {

        findViewById<EditText>(R.id.emailProfilEditText).setText(email)
        findViewById<EditText>(R.id.firstnameProfilEditText).setText(firstname)
        findViewById<EditText>(R.id.lastnameProfilEditText).setText(lastname)
        findViewById<EditText>(R.id.addressProfilEditText).setText(address)

    }
}
