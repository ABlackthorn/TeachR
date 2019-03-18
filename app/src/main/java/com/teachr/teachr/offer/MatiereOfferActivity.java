package com.teachr.teachr.offer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teachr.teachr.models.Entry;
import com.teachr.teachr.R;
import com.teachr.teachr.Utils;
import com.teachr.teachr.models.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MatiereOfferActivity extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private DatabaseReference _db;
    private ArrayList<Subject> list = new ArrayList<>();
    private ArrayAdapter<Subject> dataAdapter;
    private Entry entry;

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

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.nextButton:
                intent = new Intent(this, AddressOfferActivity.class);
                intent.putExtra("entry", entry);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matiere_offer);

        Spinner spinner = findViewById(R.id.spinner);
        Button button = findViewById(R.id.nextButton);
        button.setOnClickListener(this);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        Intent myIntent = getIntent();
        entry = myIntent.getParcelableExtra("entry");
        _db = FirebaseDatabase.getInstance().getReference().child(Utils.getFirebaseSubject());

        _db.orderByKey().addValueEventListener(_entryListener);
/*
        List<String> categories = new ArrayList<String>();
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");
*/
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    private void loadEntryList(DataSnapshot dataSnapshot) {
        Log.d("MainActivity", "loadEntryList");

        Iterator<DataSnapshot> entries = dataSnapshot.getChildren().iterator();
        //Check if current database contains any collection

        list.clear();

        //check if the collection has any task or not
        while (entries.hasNext()) {

            //get current task
            DataSnapshot currentItem = entries.next();

            //get current data in a map
            HashMap<String, Object> map = (HashMap<String, Object>) currentItem.getValue();
            //key will return the Firebase ID
            Subject subject = new Subject(currentItem.getKey(), (String) map.get("name"));
            list.add(subject);
        }

        //alert adapter that has changed
        this.dataAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        entry.setSubject(list.get(position).getId());

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + entry.toString(), Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


}
