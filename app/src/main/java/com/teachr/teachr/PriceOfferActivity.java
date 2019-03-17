package com.teachr.teachr;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

public class PriceOfferActivity extends Activity implements View.OnClickListener {

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.nextButton3:
                intent = new Intent(this, DurationOfferActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_offer);


        Button button = findViewById(R.id.nextButton3);
        button.setOnClickListener(this);
    }
}
