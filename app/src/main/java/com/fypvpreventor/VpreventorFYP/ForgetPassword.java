package com.fypvpreventor.VpreventorFYP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fypvpreventor.VpreventorFYP.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private EditText emailEditText;
    private Button fogetpasswordbutton;
    private ProgressBar progressBar;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailEditText = (EditText) findViewById(R.id.forgetpassemail);
        fogetpasswordbutton = (Button) findViewById(R.id.ForgetPasswordbtn);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        auth = FirebaseAuth.getInstance();

        fogetpasswordbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPasswordbtn();
            }
        });
    }

        private void ForgetPasswordbtn() {
            String email = emailEditText.getText().toString().trim();

            if (email.isEmpty()) {
                emailEditText.setError("Email is required!");
                emailEditText.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.setError("Please provide valid email!");
                emailEditText.requestFocus();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(ForgetPassword.this, "Check your email to reset your password!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ForgetPassword.this, "Try again! Something wrong happend!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }