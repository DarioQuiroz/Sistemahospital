package com.example.lexth.sistemahospital;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lexth.sistemahospital.Patient.Confirmed_Appointment;
import com.example.lexth.sistemahospital.Patient.New_Appointment;
import com.example.lexth.sistemahospital.Patient.Wait_Appointment;

import java.util.ArrayList;

public class Personal_Info extends AppCompatActivity {

    String username,password,user_type;
    DatabaseHelper db;
    TextView name,age,sex,dob,bgroup,utype,city,pincode,mobno,uname,pword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);
        db = new DatabaseHelper(this);

        Bundle bb = getIntent().getExtras();
        username = bb.getString("username");
        password = bb.getString("password");
        user_type = bb.getString("password");

        name = (TextView) findViewById(R.id.name);
        age = (TextView) findViewById(R.id.age);
        sex = (TextView) findViewById(R.id.sex);
        dob = (TextView) findViewById(R.id.dob);
        bgroup = (TextView) findViewById(R.id.bgroup);
        utype = (TextView) findViewById(R.id.utype);
        city = (TextView) findViewById(R.id.city);
        pincode = (TextView) findViewById(R.id.pincode);
        mobno = (TextView) findViewById(R.id.tv_mno);
        uname = (TextView) findViewById(R.id.username);
        pword = (TextView) findViewById(R.id.password);

        Cursor y = db.checkduplicates_in_user_credentials(username, password, getResources().getString(R.string.user_credentials));

        if (y.moveToFirst()) {
            String name1 = y.getString(1);
            String name2 = y.getString(2);

            name.setText(name1+" "+name2);
            age.setText(y.getString(3));
            sex.setText(y.getString(6));
            dob.setText(y.getString(5));
            bgroup.setText(y.getString(4));
            utype.setText(y.getString(7));
            city.setText(y.getString(8));
            pincode.setText(y.getString(9));
            mobno.setText(y.getString(10));
            uname.setText(y.getString(12));
            pword.setText(y.getString(11));
        }
    }

    public void onClick(View view){

        Intent i;
        Bundle b = new Bundle();
        b.putString("username",username);
        b.putString("password",password);
        b.putString("user_type",user_type);

        i = new Intent(Personal_Info.this, Update.class);
        i.putExtras(b);
        startActivity(i);
        finish();
    }

    public static class View_Report extends AppCompatActivity {

        ListView lv_report;
        String username, password, user_type;
        DatabaseHelper dbh = new DatabaseHelper(this);
        ArrayList<String> p_problem = new ArrayList<>();
        ArrayList<String> p_report = new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.view_report);

            Bundle bb = getIntent().getExtras();
            username = bb.getString("username");
            password = bb.getString("password");
            user_type = bb.getString("user_type");

            lv_report = (ListView) findViewById(R.id.lv_reports);
            Cursor y = dbh.checkduplicates_in_user_credentials(username, password, "patient_identify");

            if (y.moveToFirst()) {
                while (true) {
                    if (y.getString(4).equals("F")) {
                        p_problem.add(y.getString(5));
                        p_report.add(y.getString(7));
                    }

                    if (y.isLast())
                        break;
                    y.moveToNext();
                }

                ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, p_problem);
                lv_report.setAdapter(adapter);
            } else {
                Message.message(View_Report.this, "No tiene reportes hoy");
                finish();
            }

            lv_report.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent i;
                    Bundle b = new Bundle();
                    b.putString("report", p_report.get(position));

                    i = new Intent(View_Report.this, Feedback.Final_View_Report.class);
                    i.putExtras(b);
                    startActivity(i);
                }
            });
        }
    }
}
