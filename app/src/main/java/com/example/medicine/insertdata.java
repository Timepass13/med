package com.example.medicine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class insertdata extends AppCompatActivity {
    Button btninsert, btnhomepage;
    EditText medname, meddate, medtime;
    TextView displayMedicines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertdata);

        medname = findViewById(R.id.editText);
        meddate = findViewById(R.id.editText1);
        medtime = findViewById(R.id.editText2);
        btninsert = findViewById(R.id.button);
        btnhomepage = findViewById(R.id.buttonhome);
        displayMedicines = findViewById(R.id.displayMedicines);

        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String medicinename = medname.getText().toString();
                String medicinedate = meddate.getText().toString();
                String medicinetime = medtime.getText().toString();

                if (medicinename.isEmpty() || medicinedate.isEmpty() || medicinetime.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
                } else {
                    MedicineHelper helper = new MedicineHelper(getBaseContext(), MedicineHelper.DATABASE_NAME, null, 1);
                    SQLiteDatabase database = null;
                    try {
                        database = helper.getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put("name", medicinename);
                        cv.put("date", medicinedate);
                        cv.put("time", medicinetime);
                        database.insert("Medicine", null, cv);
                        Toast.makeText(getBaseContext(), "Record inserted successfully", Toast.LENGTH_LONG).show();
                        displayMedicines();
                    } catch (SQLException e) {
                        Toast.makeText(getBaseContext(), "Error inserting record: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnhomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(insertdata.this, MainActivity.class);
                startActivity(home);
            }
        });

        displayMedicines();
    }

    private void displayMedicines() {
        MedicineHelper helper = new MedicineHelper(getBaseContext(), MedicineHelper.DATABASE_NAME, null, 1);
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = helper.getReadableDatabase();
            cursor = database.query("Medicine", null, null, null, null, null, null);
            StringBuilder builder = new StringBuilder();
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                builder.append("Name: ").append(name).append("\n")
                        .append("Date: ").append(date).append("\n")
                        .append("Time: ").append(time).append("\n\n");
            }
            displayMedicines.setText(builder.toString());
        } catch (SQLException e) {
            Toast.makeText(getBaseContext(), "Error fetching records: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
