package com.example.ncrbcommoner;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class Commoner_Registration_Page extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUsername, editTextPhoneNo, editTextEmailId ,editTextPassword;
    private Button registerUser;
    private TextView textviewAhv;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commoner_registration_page);

        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button)findViewById(R.id.btn_commoner_Register);
        registerUser.setOnClickListener(this);

        editTextUsername = (EditText) findViewById(R.id.comm_uname);
        editTextPhoneNo  = (EditText) findViewById(R.id.edittxt_comm_phone);
        editTextEmailId  = (EditText) findViewById(R.id.edittxt_comm_email);
        editTextPassword = (EditText) findViewById(R.id.edittxt_comm_pass);

        textviewAhv = (TextView) findViewById(R.id.already_have_ac);
        textviewAhv.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.already_have_ac:
                startActivity(new Intent(this,Commoner_Login_Page.class));
                break;
            case R.id.btn_commoner_Register:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String username = editTextUsername.getText().toString().trim();
        String phone  = editTextPhoneNo.getText().toString().trim();
        String email  = editTextEmailId.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (username.isEmpty()) {
            editTextUsername.setError("Username is required!");
            editTextUsername.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            editTextPhoneNo.setError("Phone number is required!");
            editTextPhoneNo.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextEmailId.setError("Email Id is required!");
            editTextEmailId.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmailId.setError("Please provide valid Email");
            editTextEmailId.findFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Min password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(username,phone,email,password);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(Commoner_Registration_Page.this,"User has been registered successfully !",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(Commoner_Registration_Page.this, Nav_mainActivity.class));
                                        progressBar.setVisibility(View.GONE);
                                    }else {
                                        Toast.makeText(Commoner_Registration_Page.this,"Failed to register! Try again !",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(Commoner_Registration_Page.this,"Failed to register! Try again!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}