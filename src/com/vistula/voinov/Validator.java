package com.vistula.voinov;

public class Validator {

    public static boolean isValidPersonName(String value) {
        return value.length() > 0 && value.matches("[a-zA-Z ]+");
    }

    public static boolean isEmailValid(CharSequence email) {
        return email != null && email.length() > 0 && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String value) {
        return value != null && value.length() == 12;
    }
}
