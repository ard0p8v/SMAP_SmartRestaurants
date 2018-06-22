package com.sample.smartrestaurants;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        txtEmail = findViewById(R.id.registration_email);
        txtPassword = findViewById(R.id.registration_password);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void actionRegistration(View view) {
        //final DelayedProgressDialog progressDialog = ProgressDialog.show(RegistrationActivity.this, )

        (firebaseAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtEmail.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "Registration was succesful.", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                else
                {
                    Log.e("Error", task.getException().toString());
                    Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
