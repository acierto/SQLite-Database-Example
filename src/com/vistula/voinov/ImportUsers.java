package com.vistula.voinov;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import chintan.khetiya.sqlite.cursor.R;

import java.net.URL;

public class ImportUsers extends Activity {

    private static final String USER_CONTACTS_URL = "http://10.0.2.2:4444/userContacts.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
            new FetchUsersTask(this).execute(new URL(USER_CONTACTS_URL));
        } catch (Exception e) {
            Log.e(e.getMessage(), "" + e);
        }

        viewAll();
    }

    private void viewAll() {
        Intent viewUser = new Intent(ImportUsers.this, MainScreen.class);
        viewUser.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(viewUser);
    }

}
