package com.example.lexth.sistemahospital.Doctor;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.lexth.sistemahospital.DatabaseHelper;
import com.example.lexth.sistemahospital.Message;
import com.example.lexth.sistemahospital.R;

import java.util.ArrayList;

public class    Specialization extends AppCompatActivity {

    ListView lvs;
    String s, username, password, user_type;
    EditText et;
    ArrayList<String> sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specialization);

        Bundle bb = getIntent().getExtras();
        username = bb.getString("username");
        password = bb.getString("password");
        user_type = bb.getString("user_type");

        lvs = (ListView) findViewById(R.id.lv_specialization);
        et = (EditText) findViewById(R.id.et_spl);

        sp = new ArrayList<>();

        DatabaseHelper dbh = new DatabaseHelper(this);
        Cursor y = dbh.checkduplicates_in_user_credentials(username, password, getResources().getString(R.string.doctor_slot));

        if (y.moveToFirst()) {
            String br = y.getString(2);

            for (int i = 0; i < br.length(); i++) {
                if (br.charAt(i) == '_') {
                    if (i + 1 < br.length()) {
                        String ad = "";
                        for (int j = i + 1; j < br.length(); j++) {

                            if (br.charAt(j) == '_') {
                                i = j - 1;
                                break;
                            }
                            ad += br.charAt(j);
                        }
                        if (ad.length() != 0)
                            sp.add(ad);
                    }
                }
            }
            if (sp.size() > 0) {
                ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sp);
                lvs.setAdapter(adapter);
            }else {
                Message.message(Specialization.this, "No tiene Especialidades");
            }
        } else {
            Message.message(Specialization.this, "No tiene ninguna Especialidad");
        }
    }

    public void onClick(View view) {
        s = et.getText().toString();

        if (s.length() > 0) {
            DatabaseHelper db = new DatabaseHelper(this);
            Cursor y = db.checkduplicates_in_user_credentials(username, password, getResources().getString(R.string.doctor_slot));

            if (y.moveToFirst()) {
                boolean b = db.update_slot(username, password, y.getString(2) + "_" + s, y.getString(3), y.getString(4), y.getString(5));
                if (b) {
                    Message.message(Specialization.this, "Su Especialidad ha sido Actualizada");
                } else {
                    Message.message(Specialization.this, "Ha ocurrido un error, Intentalo de Nuevo");
                }
            } else {
                boolean b = db.insert_slot(username, password, "_" + s, "-", "-", "Y");
                if (b) {
                    Message.message(Specialization.this, "Tu especialidad ha sido agregada");
                } else {
                    Message.message(Specialization.this, "Ha ocurrido un error, Intentalo de Nuevo");
                }
            }

            sp.add(s);
            ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sp);
            lvs.setAdapter(adapter);
            db.close();
        } else {
            Message.message(Specialization.this, "Por Favor Escriba su Especialidad");
        }
    }
}

