package chintan.khetiya.sqlite.cursor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddUpdateUser extends Activity {

    private EditText addName, addMobile, addEmail;
    private Button addSaveBtn, addViewAll, updateBtn, updateViewAll;
    private LinearLayout addView, updateView;
    private String validMobileNumber = null, validEmail = null, validName = null, toastMessage = null;
    private int USER_ID;
    private DatabaseHandler dbHandler = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_update_screen);

        addUpdateScreen();

        String calledFrom = getIntent().getStringExtra("called");

        if (calledFrom.equalsIgnoreCase("add")) {
            addView.setVisibility(View.VISIBLE);
            updateView.setVisibility(View.GONE);
        } else {

            updateView.setVisibility(View.VISIBLE);
            addView.setVisibility(View.GONE);
            USER_ID = Integer.parseInt(getIntent().getStringExtra("USER_ID"));

            Contact c = dbHandler.getContact(USER_ID);

            addName.setText(c.getName());
            addMobile.setText(c.getPhoneNumber());
            addEmail.setText(c.getEmail());
        }
        addMobile.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                isValidSignNumberValidation(12, 12, addMobile);
            }
        });
        addMobile
                .addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        addEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                isValidEmail(addEmail);
            }
        });

        addName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                isValidPersonName(addName);
            }
        });

        addSaveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validName != null && validMobileNumber != null
                        && validEmail != null && validName.length() != 0
                        && validMobileNumber.length() != 0
                        && validEmail.length() != 0) {

                    dbHandler.Add_Contact(new Contact(validName,
                            validMobileNumber, validEmail));
                    toastMessage = "Data inserted successfully";
                    showToast(toastMessage);
                    resetText();
                    finish();
                }

            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                validName = addName.getText().toString();
                validMobileNumber = addMobile.getText().toString();
                validEmail = addEmail.getText().toString();

                if (isValid(validName) && isValid(validMobileNumber) && isValid(validEmail)) {
                    dbHandler.updateContact(new Contact(USER_ID, validName, validMobileNumber, validEmail));
                    dbHandler.close();
                    toastMessage = "Data Update successfully";
                    showToast(toastMessage);
                    viewAll();
                } else {
                    toastMessage = "Sorry Some Fields are missing.\nPlease Fill up all.";
                    showToast(toastMessage);
                }

            }
        });
        updateViewAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewAll();
            }
        });

        addViewAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewAll();
            }
        });
    }

    private boolean isValid(String field) {
        return field != null && field.length() > 0;
    }

    private void viewAll() {
        Intent viewUser = new Intent(AddUpdateUser.this, MainScreen.class);
        viewUser.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(viewUser);
        finish();
    }


    public void addUpdateScreen() {

        addName = (EditText) findViewById(R.id.add_name);
        addMobile = (EditText) findViewById(R.id.add_mobile);
        addEmail = (EditText) findViewById(R.id.add_email);

        addSaveBtn = (Button) findViewById(R.id.add_save_btn);
        updateBtn = (Button) findViewById(R.id.update_btn);
        addViewAll = (Button) findViewById(R.id.add_view_all);
        updateViewAll = (Button) findViewById(R.id.update_view_all);

        addView = (LinearLayout) findViewById(R.id.add_view);
        updateView = (LinearLayout) findViewById(R.id.update_view);

        addView.setVisibility(View.GONE);
        updateView.setVisibility(View.GONE);

    }

    public void isValidSignNumberValidation(int MinLen, int MaxLen,
                                            EditText edt) throws NumberFormatException {
        if (edt.getText().toString().length() <= 0) {
            edt.setError("Number Only");
            validMobileNumber = null;
        } else if (edt.getText().toString().length() < MinLen) {
            edt.setError("Minimum length " + MinLen);
            validMobileNumber = null;

        } else if (edt.getText().toString().length() > MaxLen) {
            edt.setError("Maximum length " + MaxLen);
            validMobileNumber = null;

        } else {
            validMobileNumber = edt.getText().toString();

        }

    } // END OF Edittext validation

    public void isValidEmail(EditText edt) {
        if (!isEmailValid(edt.getText().toString())) {
            edt.setError("Invalid Email Address");
            validEmail = null;
        } else {
            validEmail = edt.getText().toString();
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    } // end of email matcher

    public void isValidPersonName(EditText edt) throws NumberFormatException {
        if (edt.getText().toString().length() <= 0) {
            edt.setError("Accept Alphabets Only.");
            validName = null;
        } else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
            edt.setError("Accept Alphabets Only.");
            validName = null;
        } else {
            validName = edt.getText().toString();
        }

    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void resetText() {
        addName.getText().clear();
        addMobile.getText().clear();
        addEmail.getText().clear();
    }

}
