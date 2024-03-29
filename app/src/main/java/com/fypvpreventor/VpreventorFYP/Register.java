package com.fypvpreventor.VpreventorFYP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fypvpreventor.VpreventorFYP.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private TextView banner2, Registerbutton, txtRegister2;
    private EditText editTextFullname, editTextAge,editTextEmail,editTextPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        banner2=(TextView) findViewById(R.id.banner2);
        banner2.setOnClickListener(this);

        txtRegister2=(TextView) findViewById(R.id.txtRegister2);
        txtRegister2.setOnClickListener(this);

        Registerbutton = (Button) findViewById(R.id.Registerbutton);
        Registerbutton.setOnClickListener(this);

        editTextFullname = (EditText) findViewById(R.id.fullname);
        editTextAge=(EditText) findViewById(R.id.age);
        editTextEmail=(EditText) findViewById(R.id.email);
        editTextPassword=(EditText) findViewById(R.id.password);

        progressBar=(ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.txtRegister2:
                startActivity(new Intent(this,Login.class));
                break;

            case R.id.banner2:
                startActivity(new Intent(this,Login.class));
                break;

            case R.id.Registerbutton:
                Registerbutton();
                break;
        }
    }

    private void Registerbutton() {
        String email= editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullname= editTextFullname.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();

        if(fullname.isEmpty()){
            editTextFullname.setError("Name is required!");
            editTextFullname.requestFocus();
            return;
        }
        if(age.isEmpty()){
            editTextAge.setError("Age is required!");
            editTextAge.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            editTextPassword.setError("Min password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullname,age,email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(Register.this, "Verification email sent to " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                                                                } else {
                                                                    Toast.makeText(Register.this, "Failed to send verification email.", Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                                Toast.makeText(Register.this, "User has been registered succesfully!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                Intent intent = new Intent(Register.this, Login.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                String errorMessage = task.getException().getMessage();
                                                Toast.makeText(Register.this, "Failed to register: " + errorMessage, Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(Register.this, "Failed to register2: " + errorMessage, Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}