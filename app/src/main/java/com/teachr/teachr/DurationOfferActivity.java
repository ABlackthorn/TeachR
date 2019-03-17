package com.teachr.teachr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DurationOfferActivity extends Activity implements View.OnClickListener {
    Entry entry;
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.nextButton4:
                EditText edit = findViewById(R.id.durationEditText);
                entry.setPrice( Long.parseLong(edit.getText().toString()));
                intent = new Intent(this, LastStepOfferActivity.class);
                intent.putExtra("entry", entry);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration_offer);


        Button button = findViewById(R.id.nextButton4);
        button.setOnClickListener(this);

        Intent myIntent = getIntent();
        entry = myIntent.getParcelableExtra("entry");
    }

}
