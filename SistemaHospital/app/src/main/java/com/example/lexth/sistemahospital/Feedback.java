package com.example.lexth.sistemahospital;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lexth.sistemahospital.Doctor.D_Slot;
import com.example.lexth.sistemahospital.Doctor.Doctor_Patient.Report_Upload;
import com.example.lexth.sistemahospital.Doctor.Leaves.Leaves;
import com.example.lexth.sistemahospital.Doctor.Specialization;
import com.example.lexth.sistemahospital.Doctor.View_Assigned_Staff;

import java.util.ArrayList;

public class Feedback extends AppCompatActivity {

    ListView lv_feed;
    EditText et_feed;
    String username, password, user_type, tmp;
    ArrayList<String> feedback = new ArrayList<>();

    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        Bundle bb = getIntent().getExtras();
        username = bb.getString("username");
        password = bb.getString("password");
        user_type = bb.getString("user_type");

        lv_feed = (ListView) findViewById(R.id.lv_feedback);
        et_feed = (EditText) findViewById(R.id.et_feedback);


        Cursor y = db.checkduplicates_in_user_credentials(username, password, "FEEDBACK");

        if (y.moveToFirst()) {
            while (true) {
                feedback.add(y.getString(2));

                if (y.isLast())
                    break;
                y.moveToNext();
            }

            ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, feedback);
            lv_feed.setAdapter(adapter);
        }

    }

    public void onClick(View view) {
        tmp = et_feed.getText().toString();
        if (tmp.length() == 0) {
            Message.message(Feedback.this, "Por Favor Escriba su Comentario");
        } else {
            boolean b = db.insert_feedback(username, password, tmp);

            if (b) {
                Message.message(Feedback.this, "Comentario Enviado");
                finish();
            } else {
                Message.message(Feedback.this, "El Comentario no se pudo Agregar");
            }
        }
    }

    public static class Final_View_Report extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.final_view_report);

            Bundle bb = getIntent().getExtras();
            String report = bb.getString("report");
            TextView final_report = (TextView) findViewById(R.id.tv_report);
            final_report.setText(report);

        }

    }
}
