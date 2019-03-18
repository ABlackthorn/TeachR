package com.teachr.teachr.offer;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.teachr.teachr.models.Entry;
import com.teachr.teachr.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateOfferActivity extends Activity implements View.OnClickListener {

    CalendarView simpleCalendarView;
    Entry entry;

   public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.nextButton5:

                long selectedDate = simpleCalendarView.getDate(); // get selected date in milliseconds
                if(selectedDate < 0 ){
                    Toast.makeText(this, "La date est obligatoire", Toast.LENGTH_LONG).show();

                } else {
                    // Creating date format
                    DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm");

                    // Creating date from milliseconds
                    // using Date() constructor
                    Date result = new Date(selectedDate);
                    String result2 = simple.format(result);

                    // Formatting Date according to the
                    // given format
                    Log.d("Adam?", result2);
                    intent = new Intent(this, LastStepOfferActivity.class);
                    entry.setDate(result2);
                    intent.putExtra("entry", entry);
                    intent.putExtra("longDate", selectedDate);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_offer);

        Button button = findViewById(R.id.nextButton5);
        button.setOnClickListener(this);

        Intent myIntent = getIntent();
        entry = myIntent.getParcelableExtra("entry");

        simpleCalendarView = findViewById(R.id.calendar);

    }

}
