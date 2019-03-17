package com.teachr.teachr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Iterator;

public class LastStepOfferActivity extends Activity implements View.OnClickListener {

    private Entry entry;
    private Subject subject;
    // Get a reference to your user
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;
    // Attach a listener to read the data at your profile reference

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.button:

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_step_offer);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(this);
        Intent myIntent = getIntent();
        entry = myIntent.getParcelableExtra("entry");
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
                    Log.d("Zebi", "Key: " + currentItem.getKey());
                    Log.d("Zebi", "Subject: " + entry.getSubject());
                    if(currentItem.getKey().equals("name")){
                        Log.d("Zebi", "Value : " + currentItem.getValue());
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

        TextView text = findViewById(R.id.matiere1);

        text = findViewById(R.id.duration1);
        text.setText(String.format("%d", entry.getDuration()));

        text = findViewById(R.id.address1);
        text.setText(String.format("%1$,.2f", entry.getLatitude()));

        text = findViewById(R.id.price1);
        text.setText(String.format("%d", entry.getPrice()));

    }

}
