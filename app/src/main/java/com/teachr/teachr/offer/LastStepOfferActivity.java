package com.teachr.teachr.offer;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teachr.teachr.models.Entry;
import com.teachr.teachr.EntryListActivity;
import com.teachr.teachr.R;
import com.teachr.teachr.Utils;
import com.teachr.teachr.models.Subject;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class LastStepOfferActivity extends Activity implements View.OnClickListener {

    private DatabaseReference  _db;
    private Entry entry;
    private Subject subject;
    // Get a reference to your user
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;
    // Attach a listener to read the data at your profile reference

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.nextButton5:
                Log.d("stringos", entry.toString());
                addEntry();
                intent = new Intent(this, EntryListActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void addEntry() {
        //Get the object id for the new task from the Firebase Database

        DatabaseReference newEntry = _db.push();
        entry.setId(newEntry.getKey());

        //Set the values for new task in the firebase using the footer form
        newEntry.setValue(entry);
        Toast.makeText(this, "Task added to the list successfully" + entry.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_step_offer);
        _db = FirebaseDatabase.getInstance().getReference().child(Utils.getFirebaseEntry());
        Button button = findViewById(R.id.nextButton5);
        button.setOnClickListener(this);
        Intent myIntent = getIntent();
        entry = myIntent.getParcelableExtra("entry");
        long date = myIntent.getLongExtra("longDate", 0);

        ref = database.getReference().child("subject").child(entry.getSubject());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> entries = dataSnapshot.getChildren().iterator();
                //Check if current database contains any collection
                Log.d("Zebi", "Before");

                //check if the collection has any task or not
                while (entries.hasNext()) {
                    //get current task
                    DataSnapshot currentItem = entries.next();
                    if(currentItem.getKey().equals("name")){
                        subject = new Subject(currentItem.getKey(), (String) currentItem.getValue());
                        TextView text = findViewById(R.id.matiere1);
                        text.setText(subject.getName());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }

        });

        Geocoder geocoder = new Geocoder(this);

        List<Address> addresses = null;
        String errorMessage = "";
        try {
            addresses = geocoder.getFromLocation(
                    entry.getLatitude(),
                    entry.getLongitude(),
                    // In this sample, get just a single address.
                    1);
            Log.d("Adam", addresses.get(0).getAddressLine(0));
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            Log.e("nop", "service_not_available", ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            Log.e("nop", "invalid_lat_long_used" + ". " +
                    "Latitude = " + entry.getLatitude() +
                    ", Longitude = " +
                    entry.getLongitude(), illegalArgumentException);
        }

        TextView text = findViewById(R.id.matiere1);

        text = findViewById(R.id.duration1);
        text.setText(String.format("%d", entry.getDuration()));

        text = findViewById(R.id.address1);
        text.setText(addresses.get(0).getAddressLine(0));

        text = findViewById(R.id.price1);
        text.setText(String.format("%d", entry.getPrice()));

        CalendarView simpleCalendarView = findViewById(R.id.calendar);
        simpleCalendarView.setDate(date);
        simpleCalendarView.setClickable(false);

    }

}
