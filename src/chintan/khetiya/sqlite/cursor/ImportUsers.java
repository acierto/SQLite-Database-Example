package chintan.khetiya.sqlite.cursor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ImportUsers extends Activity {

    private DatabaseHandler dbHandler = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // TODO: change to read from the URL
        dbHandler.addContact(new Contact("ValidName", "1 231-123-12", "asdf@dd.dd"));
        viewAll();
    }

    private void viewAll() {
        Intent viewUser = new Intent(ImportUsers.this, MainScreen.class);
        viewUser.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(viewUser);
    }


}
