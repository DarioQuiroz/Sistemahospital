package com.example.lexth.sistemahospital.Desktop_Admin;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lexth.sistemahospital.DatabaseHelper;
import com.example.lexth.sistemahospital.Message;
import com.example.lexth.sistemahospital.R;

import java.util.ArrayList;

public class Delete_Users extends AppCompatActivity {

    ListView lv_all;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> uname = new ArrayList<>();
    ArrayList<String> pass = new ArrayList<>();
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_users);

        lv_all = (ListView) findViewById(R.id.lv_all_users);


        Cursor y = db.checkduplicates_in_user_credentials("", "", "get_all_doctors");

        if (!y.moveToFirst()) {
            Message.message(Delete_Users.this, "No tiene usuarios");
            finish();
        } else {
            while (true) {
                name.add(y.getString(1) + " " + y.getString(2) + " (" + y.getString(7) + ")");
                uname.add(y.getString(12));
                pass.add(y.getString(11));

                if (y.isLast())
                    break;
                y.moveToNext();
            }

            ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, name);
            lv_all.setAdapter(adapter);
        }


        lv_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                boolean by = db.delete_user_credentials(uname.get(position), pass.get(position));

                if (by) {
                    Message.message(Delete_Users.this, "Usuario Eliminado");
                    finish();
                } else {
                    Message.message(Delete_Users.this, "El Usuario no pudo eliminarse Intentalo de Nuevo");
                }
            }
        });
    }
}