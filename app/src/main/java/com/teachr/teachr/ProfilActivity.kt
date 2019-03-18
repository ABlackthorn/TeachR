package com.teachr.teachr

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profil.*
import com.google.firebase.storage.StorageReference
import android.graphics.drawable.BitmapDrawable
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream

class ProfilActivity : Activity() {

    private var mAuth: FirebaseAuth? = null
    private var mStorageRef: StorageReference? = null
    val GALLERY_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        mStorageRef = FirebaseStorage.getInstance().getReference();

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

        val saveModification = findViewById<Button>(R.id.saveModificationButton);
        saveModification.setOnClickListener{

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

        val profilButton = findViewById<Button>(R.id.photoButton)
        profilButton.setOnClickListener {
            pickFromGallery()
        }

        val storageRef = mStorageRef
        var profilRef = storageRef?.child("profil.jpg")

        Glide.with(this).load(profilRef).into(profilImageView)

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    //data.getData returns the content URI for the selected Image
                    val selectedImage = data.data
                    profilImageView.setImageURI(selectedImage)
                }

            }
        val storageRef = mStorageRef

        val profilRef = storageRef?.child("profil.jpg")


        // Get the data from an ImageView as bytes
        profilImageView.isDrawingCacheEnabled = true
        profilImageView.buildDrawingCache()
        val bitmap = (profilImageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = profilRef?.putBytes(data)
        uploadTask?.addOnFailureListener {
            // Handle unsuccessful uploads
        }?.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
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

    private fun pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        val intent = Intent(Intent.ACTION_PICK)
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.type = "image/*"
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

}
