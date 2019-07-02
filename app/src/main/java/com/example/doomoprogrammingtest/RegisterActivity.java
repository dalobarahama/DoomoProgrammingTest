package com.example.doomoprogrammingtest;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "register";
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextName;
    private TextView textViewDateOfBirth;
    private Button buttonDatePicker, buttonRegister;
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword, textInputLayoutConfirmPassword,
            textInputLayoutName, textInputLayoutDateOfBirth;

    private NoteViewModel noteViewModel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Registering...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        textInputLayoutEmail = findViewById(R.id.textInputLayout_email);
        textInputLayoutPassword = findViewById(R.id.textInputLayout_password);
        textInputLayoutConfirmPassword = findViewById(R.id.textInputLayout_confirm_password);
        textInputLayoutName = findViewById(R.id.textInputLayout_name);
        textInputLayoutDateOfBirth = findViewById(R.id.textInputLayout_date);

        editTextEmail = findViewById(R.id.edit_text_register_email);
        editTextPassword = findViewById(R.id.edit_text_register_password);
        editTextName = findViewById(R.id.edit_text_register_name);
        textViewDateOfBirth = findViewById(R.id.text_view_register_date_of_birth);
        buttonDatePicker = findViewById(R.id.button_register_date_picker);
        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dataPicker = new DatePickerFragment();
                dataPicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        buttonRegister = findViewById(R.id.button_save_register_);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDataString = DateFormat.getDateInstance().format(calendar.getTime());

        textViewDateOfBirth.setText(currentDataString);
    }

    public void save() {
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
        final String name = editTextName.getText().toString();
        final String dateOfBirth = textViewDateOfBirth.getText().toString();

        if (!validateEmail() | !validatePassword() | !validateName() | !validateDateOfBirth()) {
            return;
        }

        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: saving data");
                Note note = new Note(email, name, password, dateOfBirth);
                noteViewModel.insert(note);
                progressDialog.dismiss();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        }, 1000);

    }

    private boolean isEmpty() {
        if (TextUtils.isEmpty(editTextEmail.getText().toString()) ||
                TextUtils.isEmpty((editTextPassword.getText().toString())) ||
                TextUtils.isEmpty(editTextName.getText().toString()) ||
                TextUtils.isEmpty(textViewDateOfBirth.getText().toString())) {

            return true;
        } else {
            return false;
        }
    }

    public boolean validateEmail() {
        String emailInput = textInputLayoutEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputLayoutEmail.setError("Field can't be empty");
            return false;
        } else {
            textInputLayoutEmail.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        String passwordInput = textInputLayoutPassword.getEditText().getText().toString().trim();
        String confirmPasswordInput = textInputLayoutConfirmPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputLayoutPassword.setError("Field can't be empty");
            return false;
        } else if (passwordInput.length() < 8) {
            textInputLayoutPassword.setError("Password must have 8 character or more");
            return false;
        } else if (!passwordInput.equals(confirmPasswordInput)) {
            textInputLayoutConfirmPassword.setError("Password is not the same");
            return false;
        } else {
            textInputLayoutPassword.setError(null);
            return true;
        }
    }

    public boolean validateName() {
        String nameInput = textInputLayoutName.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            textInputLayoutName.setError("Field can't be empty");
            return false;
        } else {
            textInputLayoutName.setError(null);
            return true;
        }
    }

    public boolean validateDateOfBirth() {

        if (TextUtils.isEmpty(textViewDateOfBirth.getText().toString())) {
            textInputLayoutDateOfBirth.setError("Field can't be empty");
            return false;
        } else {
            textInputLayoutDateOfBirth.setError(null);
            return true;
        }
    }

}
