package com.teachr.teachr;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;


public class AddressOfferActivity extends FragmentActivity implements View.OnClickListener {

    PlaceSelectionListener _placeListener = new PlaceSelectionListener() {
        public void onPlaceSelected (Place place){
            // TODO: Get info about the selected place.
            Log.d("Ok place", "Place: " + place.getName());//get place details here
        }

        public void onError (Status status){
            // TODO: Handle the error.
            Log.i("Error place", "An error occurred: $status");
        }
    };

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.nextButton2:
                intent = new Intent(this, PriceOfferActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adress_offer);

        Button button = findViewById(R.id.nextButton2);
        button.setOnClickListener(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager()
                                                            .findFragmentById(R.id.place_autocomplete_fragment);

        /*
         * The following code example shows setting an AutocompleteFilter on a PlaceAutocompleteFragment to
         * set a filter returning only results with a precise address.
         */
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();

        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(_placeListener);
    }

}
