package com.example.assignment3;

import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private LinearLayout register, information;
    private EditText name, mobile, pass, email, bio;
    private RadioGroup gender;
    private Spinner location;
    private Button submit, register_again;
    private TextView user_info;
    private CheckBox conditions;
    private RadioButton selectedButton;

    private String inputName, inputGender, inputLocation, inputMobile, inputEmail, inputPassword, inputBio;
    private boolean conditionChecked;

    //patterns
    private Pattern namePattern = Pattern.compile("[a-zA-Z\\\\s.-]+");
    private Pattern mobilePattern = Pattern.compile("01[3,7,8,9][0-9]{8}");
    private Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$");
    private Pattern passwordPattern = Pattern.compile(".{6,18}");
    private Pattern bioPattern = Pattern.compile("[a-zA-Z\\\\s.-]+");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        register = findViewById(R.id.register_layout);
        information = findViewById(R.id.information_layout);

        name = findViewById(R.id.et_name);
        mobile = findViewById(R.id.et_mobile);
        email = findViewById(R.id.et_email);
        bio = findViewById(R.id.et_bio);
        pass = findViewById(R.id.et_password);

        gender = findViewById(R.id.radio_gender);

        location = findViewById(R.id.location_spinner);

        submit = findViewById(R.id.btn_submit);
        register_again = findViewById(R.id.btn_register_again);

        user_info = findViewById(R.id.tv_information);

        conditions = findViewById(R.id.condition_check_box);

        //spinner
        String[] locations = {"-none-", "Sylhet", "Dhaka", "Moulvibazar",
                "Rajsahi", "Chittagong", "Barisal"};

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);

        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

        location.setAdapter(arrayAdapter);

        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inputLocation = locations[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //radio group
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    selectedButton = findViewById(checkedId);
                    inputGender = selectedButton.getText().toString();
                }
            }
        });

        //check box
        conditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(MainActivity.this, "condition checked", Toast.LENGTH_SHORT).show();

                    conditionChecked = isChecked;
                }
            }
        });

        // Inside the submit.setOnClickListener
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values
                inputName = name.getText().toString().trim();
                inputMobile = mobile.getText().toString().trim();
                inputEmail = email.getText().toString().trim();
                inputPassword = pass.getText().toString().trim();
                inputBio = bio.getText().toString().trim();

                // Check if the inputs match the required patterns
                boolean isNameValid = patternCheck(name, inputName, namePattern);
                boolean isMobileValid = patternCheck(mobile, inputMobile, mobilePattern);
                boolean isEmailValid = patternCheck(email, inputEmail, emailPattern);
                boolean isPasswordValid = patternCheck(pass, inputPassword, passwordPattern);
                boolean isBioValid = patternCheck(bio, inputBio, bioPattern);

                if (inputName.isEmpty() || inputMobile.isEmpty() || inputEmail.isEmpty() ||
                        inputPassword.isEmpty() || inputBio.isEmpty() || !conditions.isChecked()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields and accept conditions",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isNameValid) {
                    name.setError("Invalid name!\nNo space");
                    name.requestFocus();
                } else if (isMobileValid) {
                    mobile.setError("Invalid mobile number!\nShould be 017XXXXXXXX,018XXXXXXXX etc.");
                    mobile.requestFocus();
                } else if (isEmailValid) {
                    email.setError("Invalid email!\ntest123@example.com");
                    email.requestFocus();
                } else if (isPasswordValid) {
                    pass.setError("Invalid password!");
                    pass.requestFocus();
                } else if (isBioValid) {
                    bio.setError("Does not include special characters!");
                    bio.requestFocus();
                } else if (!conditionChecked) {
                    // If the checkbox is not checked, show a message
                    Toast.makeText(MainActivity.this, "Please accept the terms and conditions",
                            Toast.LENGTH_SHORT).show();
                } else if (inputGender.isEmpty()) {
                    // If no gender is selected, show a message
                    Toast.makeText(MainActivity.this, "Please select gender",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // If all fields are valid
                    String userInfo = "Name: " + inputName + "\n" +
                            "Gender: " + inputGender + "\n" +
                            "Location: " + inputLocation + "\n" +
                            "Mobile: " + inputMobile + "\n" +
                            "Email: " + inputEmail + "\n" +
                            "Bio: " + inputBio;

                    user_info.setText(userInfo);

                    register.setVisibility(View.GONE);
                    information.setVisibility(View.VISIBLE);
                }

                // Reset all input fields
                name.setText("");
                email.setText("");
                mobile.setText("");
                bio.setText("");
                pass.setText("");

                // Clear gender selection and conditions
                gender.clearCheck();
                conditions.setChecked(false);

                // Reset the location spinner to the default value
                location.setSelection(0);
            }
        });


        // Register Again Button logic
        register_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the information layout and show the registration layout
                information.setVisibility(View.GONE);
                register.setVisibility(View.VISIBLE);
            }
        });

    }

    boolean patternCheck(EditText e1, String values, Pattern pattern) {
        if(!pattern.matcher(values).matches()) {
            return true;
        }
        else {
            return false;
        }
    }
}