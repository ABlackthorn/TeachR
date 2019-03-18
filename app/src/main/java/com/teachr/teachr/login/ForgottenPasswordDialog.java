package com.teachr.teachr.login;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.teachr.teachr.R;

public class ForgottenPasswordDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    public ForgottenPasswordDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_forgotten_password);
        yes = (Button) findViewById(R.id.confirmButton);
        no = (Button) findViewById(R.id.cancelButton);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmButton:
                //c.finish();
                String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
                if(!"".equals(email)){
                    setContentView(R.layout.wait_layout);
                    //send email
                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), R.string.forgotten_password_message_sent, Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    } else {
                                        Toast.makeText(getContext(), R.string.forgotten_password_dialog_error_message, Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(this.getContext(), R.string.enter_details_error, Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.cancelButton:
                dismiss();
                break;
            default:
                break;
        }
        //dismiss();
    }
}
