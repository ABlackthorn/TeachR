package com.teachr.teachr.offer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teachr.teachr.models.Entry;
import com.teachr.teachr.R;

public class PriceOfferActivity extends Activity implements View.OnClickListener {

    private Entry entry;
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.nextButton3:
                EditText edit = findViewById(R.id.priceEditText);
                String editString = edit.getText().toString();
                if(editString.isEmpty()){
                    Toast.makeText(this, "La durée est obligatoire", Toast.LENGTH_LONG).show();
                } else {
                    entry.setPrice( Long.parseLong(edit.getText().toString()));
                    intent = new Intent(this, DurationOfferActivity.class);
                    intent.putExtra("entry", entry);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_offer);


        Button button = findViewById(R.id.nextButton3);
        button.setOnClickListener(this);

        Intent myIntent = getIntent();
        entry = myIntent.getParcelableExtra("entry");
    }
}
