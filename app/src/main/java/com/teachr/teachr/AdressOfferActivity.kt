package com.teachr.teachr

import android.os.Bundle
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.widget.Button

import kotlinx.android.synthetic.main.activity_adress_offer.*
import android.widget.Toast
import com.google.android.libraries.places.api.Places;
import com.google.android.gms.location.places.Place; // "Place" is not resolved
import com.google.android.gms.location.places.ui.PlacePicker; // "ui" is not resolved





class AdressOfferActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adress_offer)

        // Initialize Places.
        val apiKey = "AIzaSyAvHwMXGx06mGfWTn5hWDyNLVFlX-rZtrg"
        Places.initialize(applicationContext, apiKey)

        // Create a new Places client instance.
        val placesClient = Places.createClient(this)

        val PLACE_PICKER_REQUEST = 0
        val builder = PlacePicker.IntentBuilder()

        val adressButton = findViewById<Button>(R.id.adressButton);
        adressButton.setOnClickListener{
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        }
    }

}
