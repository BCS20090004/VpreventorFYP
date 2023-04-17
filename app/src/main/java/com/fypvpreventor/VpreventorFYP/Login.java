package com.fypvpreventor.VpreventorFYP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fypvpreventor.VpreventorFYP.MainActivity;
import com.fypvpreventor.VpreventorFYP.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener{
    private SharedPreferences sharedPreferences;

    private boolean logoutPressed = false;

    private TextView txtRegister,forgetPassword;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtRegister = (TextView) findViewById(R.id.txtRegister);
        txtRegister.setOnClickListener(this);

        signIn=(Button) findViewById(R.id.button);
        signIn.setOnClickListener(this);

        editTextEmail=(EditText) findViewById(R.id.InEmail);
        editTextPassword=(EditText) findViewById(R.id.InPassword);

        progressBar=(ProgressBar) findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        forgetPassword = (TextView)  findViewById(R.id.ForgetPassword);
        forgetPassword.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        // "MyPrefs" is the name of the shared preferences file

        // Check if user is already logged in
        if (sharedPreferences.contains("email") && sharedPreferences.contains("password")) {
            String email = sharedPreferences.getString("email", "");
            String password = sharedPreferences.getString("password", "");

            // Call login method with saved credentials
            userLogin(email, password);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.txtRegister:
                startActivity(new Intent(this,Register.class));
                break;

            case R.id.button:
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                userLogin(email, password);

                break;

            case R.id.ForgetPassword:
                startActivity(new Intent(this,ForgetPassword.class));
                break;
        }
    }

    private void userLogin(String email, String password) {
        // rest of the code remains the same
       // String email =editTextEmail.getText().toString().trim();
       // String password =editTextPassword.getText().toString().trim();

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

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()) {
                        // Save credentials to shared preferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.apply();

                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();// Prevent user from going back to login screen on back button press
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }else{
                    Toast.makeText(Login.this, "Failed to Login!Please Check your credentials", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        // Check if user is already logged in
        if (user != null && sharedPreferences.contains("email") && sharedPreferences.contains("password")) {
            String email = sharedPreferences.getString("email", "");
            String password = sharedPreferences.getString("password", "");

            // Call login method with saved credentials
            userLogin(email, password);
        } else {
            // If user is not logged in or has logged out, clear shared preferences
            if (logoutPressed) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                logoutPressed = false;
            }
        }
    }
}