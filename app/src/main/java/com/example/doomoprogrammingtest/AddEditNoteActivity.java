package com.example.doomoprogrammingtest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class AddEditNoteActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final String EXTRA_ID =
            "com.example.doomoprogrammingtest.EXTRA_ID";
    public static final String EXTRA_NAME =
            "com.example.doomoprogrammingtest.EXTRA_NAME";
    public static final String EXTRA_EMAIL =
            "com.example.doomoprogrammingtest.EXTRA_EMAIL";
    public static final String EXTRA_PASSWORD =
            "com.example.doomoprogrammingtest.EXTRA_PASSWORD";
    public static final String EXTRA_DATE_OF_BIRTH =
            "com.example.doomoprogrammingtest.EXTRA_DATE_OF_BIRTH";

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword, editTextConfirmPassword;
    private TextInputEditText editTextName;
    private TextView editTextDateOfBirth;
    private Button buttonDatePicker;
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword, textInputLayoutConfirmPassword, textInputLayoutName, textInputLayoutDateOfBirth;

    NoteViewModel noteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        textInputLayoutEmail = findViewById(R.id.textInputLayout_edit_email);
        textInputLayoutPassword = findViewById(R.id.textInputLayout_edit_password);
        textInputLayoutConfirmPassword = findViewById(R.id.textInputLayout_edit_confirm_password);
        textInputLayoutName = findViewById(R.id.textInputLayout_edit_name);
        textInputLayoutDateOfBirth = findViewById(R.id.textInputLayout_edit_date);

        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextConfirmPassword = findViewById(R.id.edit_text__confirm_password);
        editTextName = findViewById(R.id.edit_text_name);
        editTextDateOfBirth = findViewById(R.id.text_view_date_of_birth);
        buttonDatePicker = findViewById(R.id.button_date_picker);
        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dataPicker = new DatePickerFragment();
                dataPicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit User");
            editTextEmail.setText(intent.getStringExtra(EXTRA_EMAIL));
            editTextPassword.setText(intent.getStringExtra(EXTRA_PASSWORD));
            editTextConfirmPassword.setText(intent.getStringExtra(EXTRA_PASSWORD));
            editTextName.setText(intent.getStringExtra(EXTRA_NAME));
            editTextDateOfBirth.setText(intent.getStringExtra(EXTRA_DATE_OF_BIRTH));

        } else {
            setTitle("Register User");
        }


    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDataString = DateFormat.getDateInstance().format(calendar.getTime());

        editTextDateOfBirth.setText(currentDataString);

    }

    private void saveNote() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String name = editTextName.getText().toString();
        String dateOfBirth = editTextDateOfBirth.getText().toString();

        if (!validateEmail() | !validatePassword() | !validateName() | !validateDateOfBirth()) {
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_EMAIL, email);
        data.putExtra(EXTRA_PASSWORD, password);
        data.putExtra(EXTRA_DATE_OF_BIRTH, dateOfBirth);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public boolean validateEmail() {
        String inputEmail = textInputLayoutEmail.getEditText().getText().toString().trim();

        if (inputEmail.isEmpty()) {
            textInputLayoutEmail.setError("Field can't be empty");
            return false;
        } else {
            textInputLayoutEmail.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        String inputPassword = textInputLayoutPassword.getEditText().getText().toString().trim();
        String passwordInput = textInputLayoutConfirmPassword.getEditText().getText().toString().trim();

        if (inputPassword.isEmpty()) {
            textInputLayoutPassword.setError("Field can't be empty");
            return false;
        } else if (inputPassword.length() < 8) {
            textInputLayoutPassword.setError("Password must have 8 character or more");
            return false;
        } else if (!inputPassword.equals(passwordInput)) {
            textInputLayoutConfirmPassword.setError("Password is not the same");
            return false;
        } else {
            textInputLayoutPassword.setError(null);
            return true;
        }
    }

    public boolean validateName() {
        String inputName = textInputLayoutName.getEditText().getText().toString().trim();

        if (inputName.isEmpty()) {
            textInputLayoutName.setError("Field can't be empty");
            return false;
        } else {
            textInputLayoutName.setError(null);
            return true;
        }
    }

    public boolean validateDateOfBirth() {
        if (TextUtils.isEmpty(editTextDateOfBirth.getText().toString())) {
            textInputLayoutDateOfBirth.setError("Field can't be empty");
            return false;
        } else {
            textInputLayoutDateOfBirth.setError(null);
            return true;
        }
    }
}
