package com.teachr.teachr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DurationOfferActivity extends Activity implements View.OnClickListener {

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.nextButton4:
                intent = new Intent(this, DurationOfferActivity.class);
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
    }

}
