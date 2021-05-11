package com.example.animereminder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    //Variables de Elementos visuales
    private EditText mEditTextEmail;
    private EditText mEditTextUser;
    private EditText mEditTextPassword;

    private CheckBox mCheckBoxTC;
    private Button mButtonRegister;

    //Variables Firebase
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    //Variables "extra"
    private String user;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        this.mEditTextEmail = (EditText) findViewById(R.id.etemail);
        this.mEditTextUser = (EditText) findViewById(R.id.mypass2);
        this.mEditTextPassword = (EditText) findViewById(R.id.mypass);

        this.mCheckBoxTC = (CheckBox) findViewById(R.id.checkBox);
        this.mButtonRegister = (Button) findViewById(R.id.btnregister);

        this.mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = mEditTextUser.getText().toString();
                email = mEditTextEmail.getText().toString();
                password = mEditTextPassword.getText().toString();

                if( !user.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    Log.i("mensaje", "se crea usuario");
                    registerUser();
                }
                else {
                    //Error
                    Log.i("mensaje", "falta parametro");
                }
            }
        });

    }

    private void registerUser(){
        mAuth.createUserWithEmailAndPassword(this.email, this.password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //agregarUsuario
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(RegisterActivity.this,"F",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
        