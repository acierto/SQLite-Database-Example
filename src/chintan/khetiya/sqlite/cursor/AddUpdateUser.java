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
    EditText addName, addMobile, addEmail;
    Button add_save_btn, add_view_all, update_btn, update_view_all;
    LinearLayout addView, updateView;
    String valid_mob_number = null, valid_email = null, valid_name = null, Toast_msg = null;
    int USER_ID;
    DatabaseHandler dbHandler = new DatabaseHandler(this);

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

        add_save_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (valid_name != null && valid_mob_number != null
                        && valid_email != null && valid_name.length() != 0
                        && valid_mob_number.length() != 0
                        && valid_email.length() != 0) {

                    dbHandler.Add_Contact(new Contact(valid_name,
                            valid_mob_number, valid_email));
                    Toast_msg = "Data inserted successfully";
                    showToast(Toast_msg);
                    resetText();

                }

            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                valid_name = addName.getText().toString();
                valid_mob_number = addMobile.getText().toString();
                valid_email = addEmail.getText().toString();

                // check the value state is null or not
                if (valid_name != null && valid_mob_number != null
                        && valid_email != null && valid_name.length() != 0
                        && valid_mob_number.length() != 0
                        && valid_email.length() != 0) {

                    dbHandler.UpdateContact(new Contact(USER_ID, valid_name,
                            valid_mob_number, valid_email));
                    dbHandler.close();
                    Toast_msg = "Data Update successfully";
                    showToast(Toast_msg);
                    resetText();
                } else {
                    Toast_msg = "Sorry Some Fields are missing.\nPlease Fill up all.";
                    showToast(Toast_msg);
                }

            }
        });
        update_view_all.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent viewUser = new Intent(AddUpdateUser.this, MainScreen.class);
                viewUser.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(viewUser);
                finish();
            }
        });

        add_view_all.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent view_user = new Intent(AddUpdateUser.this, MainScreen.class);
                view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(view_user);
                finish();
            }
        });

    }

    public void addUpdateScreen() {

        addName = (EditText) findViewById(R.id.add_name);
        addMobile = (EditText) findViewById(R.id.add_mobile);
        addEmail = (EditText) findViewById(R.id.add_email);

        add_save_btn = (Button) findViewById(R.id.add_save_btn);
        update_btn = (Button) findViewById(R.id.update_btn);
        add_view_all = (Button) findViewById(R.id.add_view_all);
        update_view_all = (Button) findViewById(R.id.update_view_all);

        addView = (LinearLayout) findViewById(R.id.add_view);
        updateView = (LinearLayout) findViewById(R.id.update_view);

        addView.setVisibility(View.GONE);
        updateView.setVisibility(View.GONE);

    }

    public void isValidSignNumberValidation(int MinLen, int MaxLen,
                                            EditText edt) throws NumberFormatException {
        if (edt.getText().toString().length() <= 0) {
            edt.setError("Number Only");
            valid_mob_number = null;
        } else if (edt.getText().toString().length() < MinLen) {
            edt.setError("Minimum length " + MinLen);
            valid_mob_number = null;

        } else if (edt.getText().toString().length() > MaxLen) {
            edt.setError("Maximum length " + MaxLen);
            valid_mob_number = null;

        } else {
            valid_mob_number = edt.getText().toString();

        }

    } // END OF Edittext validation

    public void isValidEmail(EditText edt) {
        if (!isEmailValid(edt.getText().toString())) {
            edt.setError("Invalid Email Address");
            valid_email = null;
        } else {
            valid_email = edt.getText().toString();
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    } // end of email matcher

    public void isValidPersonName(EditText edt) throws NumberFormatException {
        if (edt.getText().toString().length() <= 0) {
            edt.setError("Accept Alphabets Only.");
            valid_name = null;
        } else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
            edt.setError("Accept Alphabets Only.");
            valid_name = null;
        } else {
            valid_name = edt.getText().toString();
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
