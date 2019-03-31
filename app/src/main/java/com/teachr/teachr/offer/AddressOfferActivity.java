package com.teachr.teachr.offer;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import com.teachr.teachr.models.Entry;
import com.teachr.teachr.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class AddressOfferActivity extends FragmentActivity implements View.OnClickListener {

    private Entry entry;
    Geocoder geocoder = new Geocoder(this, Locale.getDefault());

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

                List<Address> addresses = null;

                try {
                    addresses = geocoder.getFromLocation(
                            entry.getLatitude(),
                            entry.getLongitude(),
                            // In this sample, get just a single address.
                            1);
                    entry.setAddress(addresses.get(0).getAddressLine(0));
                    Log.d("adop", "address : " + addresses.get(0));
                } catch (IOException ioException) {
                    // Catch network or other I/O problems.
                    Log.e("error", "problème de connexion", ioException);
                } catch (IllegalArgumentException illegalArgumentException) {
                    // Catch invalid latitude or longitude values.
                    Log.e("error", "mauvaise localisation" + ". " +
                            "Latitude = " + entry.getLatitude() +
                            ", Longitude = " +
                            entry.getLongitude(), illegalArgumentException);
                }

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
