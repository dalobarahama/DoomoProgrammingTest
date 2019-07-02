package com.example.doomoprogrammingtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    public static final int REGISTER_NOTE_REQUEST = 2;

    TextInputEditText editTextEmail, editTextPassword;
    TextInputLayout textInputLayoutEmail, textInputLayoutPassword;
    Button registerButton, loginButton;

    ProgressDialog progressDialog;

    private NoteDao noteDao;
    private NoteDatabase noteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.email_login_edit_text);
        editTextPassword = findViewById(R.id.password_login_edit_text);

        textInputLayoutEmail = findViewById(R.id.textInputLayout_login_email);
        textInputLayoutPassword = findViewById(R.id.textInputLayout_login_password);

        noteDatabase = NoteDatabase.getInstance(this);
        noteDao = noteDatabase.noteDao();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Check User...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateEmail() | !validatePassword()) {
                    return;
                } else {
                    progressDialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Note note = noteDao.getUser(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                            if (note != null) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("User", note);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Unregistered user or incorrect input", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    }, 1000);
                }

            }
        });

        registerButton = findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REGISTER_NOTE_REQUEST);
            }
        });
    }

    private boolean emptyValidation() {
        if (TextUtils.isEmpty(editTextEmail.getText().toString()) ||
                TextUtils.isEmpty(editTextPassword.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validateEmail() {
        String emailInput = textInputLayoutEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputLayoutEmail.setError("Field can't be empty");
            return false;
        } else {
            textInputLayoutEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = textInputLayoutPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputLayoutPassword.setError("Field can't be empty");
            return false;
        } else if (passwordInput.length() < 8) {
            textInputLayoutPassword.setError("Password must have 8 character or more");
            return false;
        } else {
            textInputLayoutPassword.setError(null);
            return true;
        }
    }
}
