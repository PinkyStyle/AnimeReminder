package com.example.animereminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    //Variables de elementos visuales
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mButtonLogin;
    private TextView cna;
    //Variables Firebase
    FirebaseAuth mAuth;

    //Variables extra
    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        this.cna = (TextView) findViewById(R.id.createnewaccount);
        this.mEditTextEmail = (EditText) findViewById(R.id.etemail);
        this.mEditTextPassword = (EditText) findViewById(R.id.mypass);
        this.mButtonLogin = (Button) findViewById(R.id.btnlogin);

        cna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        this.mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEditTextEmail.getText().toString();
                password = mEditTextPassword.getText().toString();
                String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(email);

                if (email.isEmpty()) {
                    mEditTextEmail.setError("Debe ingresar su correo electrónico");
                }
                else if (matcher.matches()==false) {
                    mEditTextEmail.setError("Debe ingresar un correo electrónico válido");
                }
                if (password.isEmpty()) {
                    mEditTextPassword.setError("Debe ingresar una contraseña");
                }
                if (!email.isEmpty() && !password.isEmpty() && matcher.matches()==true) {
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent i = new Intent(MainActivity.this, NavigationActivity.class);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(MainActivity.this,"Usuario o contraseña incorrectos. Inténtelo nuevamente",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}