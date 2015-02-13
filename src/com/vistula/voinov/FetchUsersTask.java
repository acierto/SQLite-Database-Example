package com.vistula.voinov;

import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;

public class FetchUsersTask extends AsyncTask<URL, String, String> {

    private DatabaseHandler dbHandler;

    public FetchUsersTask(Context context) {
        this.dbHandler = new DatabaseHandler(context);
    }

    @Override
    protected String doInBackground(URL... url) {
        try {
            return URLConnectionReader.readFromUrl(url[0]);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {

        for (String userContact: result.split("\n")) {
            String[] contact = userContact.split(",");
            dbHandler.addContact(new Contact(contact[0], contact[1], contact[2]));
        }
    }
}
