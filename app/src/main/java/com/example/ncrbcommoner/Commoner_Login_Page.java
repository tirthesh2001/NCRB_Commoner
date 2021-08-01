package com.example.ncrbcommoner;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class Commoner_Login_Page extends AppCompatActivity  implements View.OnClickListener{

    private TextView textViewDhv;
    private EditText editTextEmail,editTextPassword;
    private Button SignIn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commoner_login_page);

        textViewDhv = (TextView) findViewById(R.id.dont_have_ac);
        textViewDhv.setOnClickListener(this);

        SignIn = (Button) findViewById(R.id.btn_commoner_Login);
        SignIn.setOnClickListener(this);

        editTextEmail = (EditText)findViewById(R.id.comm_email_edittxt);
        editTextPassword = (EditText)findViewById(R.id.comm_password_edittxt);

        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dont_have_ac:
                startActivity(new Intent(this,Commoner_Registration_Page.class));
                break;
            case R.id.btn_commoner_Login:
                userLogin();
                break;
        }

    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;

        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Min password length is 6 characters!");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Commoner_Login_Page.this, Nav_mainActivity.class));
//                            progressBar.setVisibility(View.GONE);

                        }else {
                            Toast.makeText(Commoner_Login_Page.this,"Failed to Login! Please check your credentials", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}
