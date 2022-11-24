package com.example.ejemplosqlite;


import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MainActivity extends AppCompatActivity {

    Button btnFetch,btnInsert;
    ListView txtData;
    EditText dataToInsert;

    private DBHandler dbHandler;

    private ArrayList<String> paisesNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtData = (ListView) findViewById(R.id.txtDataSQLite);
        btnFetch = (Button) findViewById(R.id.btnFetchSQLite);
        btnInsert = (Button) findViewById(R.id.btnInsertSQLite);
        dataToInsert = (EditText) findViewById(R.id.et_dataSQLite);

        dbHandler = new DBHandler(MainActivity.this);

        btnFetch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<CountryModal> countryNamesPre = dbHandler.readCourses();
                ArrayList<String> paisesNames = new ArrayList<String>();
                for(int i = 0; i < countryNamesPre.size();i++){
                    paisesNames.add(countryNamesPre.get(i).getCountryName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, paisesNames);
                txtData.setAdapter(adapter);

            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String data = dataToInsert.getText().toString();

                    if(!data.equals("")){
                        dbHandler.addNewCourse(data);
                        Toast.makeText(MainActivity.this, "Dato Insertado", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                catch (Exception e){

                }
            }
        });
    }


    class Country {

        public String nombre;

        public Country() {
            // Default constructor required for calls to DataSnapshot.getValue(Proveedor.class)
        }

        public Country(String nombre) {
            this.nombre = nombre;
        }
    }



    public class DBHandler extends SQLiteOpenHelper {

        private static final String DB_NAME = "myDB";

        private static final int DB_VERSION = 1;

        private static final String TABLE_NAME = "countries";

        private static final String ID_COL = "id";

        private static final String NAME_COL = "name";

        public DBHandler(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " ("
                    + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + NAME_COL + " TEXT)";
            db.execSQL(query);
        }

        public void addNewCourse(String countryName) {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(NAME_COL, countryName);

            db.insert(TABLE_NAME, null, values);

            db.close();
        }

        public ArrayList<CountryModal> readCourses() {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            ArrayList<CountryModal> courseModalArrayList = new ArrayList<>();

            if (cursorCourses.moveToFirst()) {
                do {
                    courseModalArrayList.add(new CountryModal(cursorCourses.getString(1)));
                } while (cursorCourses.moveToNext());
            }
            cursorCourses.close();
            return courseModalArrayList;
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


    public class CountryModal {

        private String countryName;
        private int id;

        public String getCountryName() {
            return countryName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public CountryModal(String countryName) {
            this.countryName = countryName;
        }
    }
}