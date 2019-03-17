package com.teachr.teachr;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.api.Places;

import java.util.Arrays;


public class AddressOfferActivity extends FragmentActivity implements View.OnClickListener {

    private Entry entry;

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.nextButton2:
                if(entry.getLatitude() == 0) {
                    Toast.makeText(this, "Adresse obligatoire", Toast.LENGTH_LONG).show();
                } else {
                    intent = new Intent(this, PriceOfferActivity.class);
                    intent.putExtra("entry", entry);
                    startActivity(intent);
                }
                break;
                
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adress_offer);

        Button button = findViewById(R.id.nextButton2);
        button.setOnClickListener(this);

        // Initialize Places.
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBy5wzih3SDrAiC2D9SjbbGjrmmE0R32DY");
        }

// Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            private Place place;

            @Override
            public void onPlaceSelected(Place place) {
                this.place = place;
                // TODO: Get info about the selected place.
                Log.d("yes we got it", "Place: " + this.place + ", " + place.getId());
                entry.setLatitude(place.getLatLng().latitude);
                entry.setLongitude(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("nop", "An error occurred: " + status);
            }
        });

        Intent myIntent = getIntent();
        entry = myIntent.getParcelableExtra("entry");
    }

}
