package com.teachr.teachr.offer;

import android.app.Activity;
import android.content.Intent;
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

import java.util.Iterator;

public class LastStepOfferActivity extends Activity implements View.OnClickListener {

    private DatabaseReference  _db;
    private Entry entry;
    private Subject subject;
    // Get a reference to your user
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refSubject;
    DatabaseReference refUser;
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

        refSubject = database.getReference().child("subject").child(entry.getSubject());
        refSubject.addValueEventListener(new ValueEventListener() {
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

        refUser = database.getReference().child("users").child(entry.getUser());
        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> entries = dataSnapshot.getChildren().iterator();
                //Check if current database contains any collection
                //check if the collection has any task or not
                while (entries.hasNext()) {
                    //get current task
                    DataSnapshot currentItem = entries.next();
                    if(currentItem.getKey().equals("type")){
                        entry.setType((Long) currentItem.getValue());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }

        });

        TextView text = findViewById(R.id.matiere1);

        text = findViewById(R.id.duration1);
        text.setText(String.format("%d", entry.getDuration()));

        text = findViewById(R.id.address1);
        text.setText(entry.getAddress());

        text = findViewById(R.id.price1);
        text.setText(String.format("%d", entry.getPrice()));

        CalendarView simpleCalendarView = findViewById(R.id.calendar);
        simpleCalendarView.setDate(date);
        simpleCalendarView.setClickable(false);

    }

}
