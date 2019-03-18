package com.teachr.teachr.offer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teachr.teachr.models.Entry;
import com.teachr.teachr.R;
import com.teachr.teachr.Utils;

import java.util.HashMap;
import java.util.Iterator;

public class DurationOfferActivity extends Activity implements View.OnClickListener {

    Entry entry;
    private DatabaseReference _db;

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.nextButton4:
                EditText edit = findViewById(R.id.durationEditText);
                String editString = edit.getText().toString();
                if(editString.isEmpty()){
                    Toast.makeText(this, "La durée est obligatoire", Toast.LENGTH_LONG).show();
                } else {
                    entry.setPrice( Long.parseLong(edit.getText().toString()));
                    intent = new Intent(this, DateOfferActivity.class);
                    intent.putExtra("entry", entry);
                    startActivity(intent);
                }
                break;
        }
    }

    ValueEventListener _entryListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            loadEntryList(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException());
        }
    };

    private void loadEntryList(DataSnapshot dataSnapshot) {
        Log.d("MainActivity", "loadEntryList");

        Iterator<DataSnapshot> entries = dataSnapshot.getChildren().iterator();
        //Check if current database contains any collection

        //check if the collection has any task or not
        while (entries.hasNext()) {

            //get current task
            DataSnapshot currentItem = entries.next();

            //get current data in a map
            HashMap<String, Object> map = (HashMap<String, Object>) currentItem.getValue();
            //key will return the Firebase ID

            if(map.get("id") == entry.getId()){
                entry.setType((int) map.get("type"));
                break;
            }
        }

        //alert adapter that has changed
        //this.dataAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration_offer);


        Button button = findViewById(R.id.nextButton4);
        button.setOnClickListener(this);

        Intent myIntent = getIntent();
        entry = myIntent.getParcelableExtra("entry");

        _db = FirebaseDatabase.getInstance().getReference().child(Utils.getFirebaseUser());

        _db.orderByKey().addValueEventListener(_entryListener);
    }

}
