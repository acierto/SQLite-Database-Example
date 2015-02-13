package com.vistula.voinov;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import chintan.khetiya.sqlite.cursor.R;

import java.util.ArrayList;

public class MainScreen extends Activity {

    private Button addBtn, importBtn;
    private ListView contactListView;
    private ArrayList<Contact> contactData = new ArrayList<Contact>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try {
            contactListView = (ListView) findViewById(R.id.list);
            contactListView.setItemsCanFocus(false);
            addBtn = (Button) findViewById(R.id.add_btn);
            importBtn = (Button) findViewById(R.id.import_btn);

            refreshData();

        } catch (Exception e) {
            Log.e("some error", "" + e);
        }
        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent addUser = new Intent(MainScreen.this, AddUpdateUser.class);
                addUser.putExtra("called", "add");
                addUser.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(addUser);
                finish();
            }
        });

        importBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent importUsers = new Intent(MainScreen.this, ImportUsers.class);
                importUsers.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(importUsers);
                finish();
            }
        });

    }

    public void refreshData() {
        contactData.clear();
        DatabaseHandler db = new DatabaseHandler(this);
        ArrayList<Contact> contacts = db.getContacts();

        for (Contact contact : contacts) {

            int tidno = contact.getID();
            String name = contact.getName();
            String mobile = contact.getPhoneNumber();
            String email = contact.getEmail();
            Contact cnt = new Contact();
            cnt.setID(tidno);
            cnt.setName(name);
            cnt.setEmail(email);
            cnt.setPhoneNumber(mobile);

            contactData.add(cnt);
        }
        db.close();
        ContactAdapter cAdapter = new ContactAdapter(MainScreen.this, R.layout.listview_row, contactData);
        contactListView.setAdapter(cAdapter);
        cAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();

    }

    public class ContactAdapter extends ArrayAdapter<Contact> {
        Activity activity;
        int layoutResourceId;
        Contact user;
        ArrayList<Contact> data = new ArrayList<Contact>();

        public ContactAdapter(Activity act, int layoutResourceId, ArrayList<Contact> data) {
            super(act, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.activity = act;
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            UserHolder holder;

            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(activity);

                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new UserHolder();
                holder.name = (TextView) row.findViewById(R.id.user_name_txt);
                holder.email = (TextView) row.findViewById(R.id.user_email_txt);
                holder.number = (TextView) row.findViewById(R.id.user_mob_txt);
                holder.edit = (ImageButton) row.findViewById(R.id.btn_update);
                holder.delete = (ImageButton) row.findViewById(R.id.btn_delete);
                row.setTag(holder);
            } else {
                holder = (UserHolder) row.getTag();
            }
            user = data.get(position);
            holder.edit.setTag(user.getID());
            holder.delete.setTag(user.getID());
            holder.name.setText(user.getName());
            holder.email.setText(user.getEmail());
            holder.number.setText(user.getPhoneNumber());

            holder.edit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i("Edit Button Clicked", "**********");

                    Intent updateUser = new Intent(activity, AddUpdateUser.class);
                    updateUser.putExtra("called", "update");
                    updateUser.putExtra("USER_ID", v.getTag().toString());
                    activity.startActivity(updateUser);

                }
            });
            holder.delete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(final View v) {

                    AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                    adb.setTitle("Delete?");
                    adb.setMessage("Are you sure you want to delete ");
                    final int userId = Integer.parseInt(v.getTag().toString());
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseHandler dBHandler = new DatabaseHandler(activity.getApplicationContext());
                                    dBHandler.DeleteContact(userId);
                                    MainScreen.this.onResume();

                                }
                            });
                    adb.show();
                }

            });
            return row;

        }

        class UserHolder {
            private TextView name;
            private TextView email;
            private TextView number;
            private ImageButton edit;
            private ImageButton delete;
        }

    }

}
