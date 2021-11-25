package com.y2021s2.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    String name, ageSt, dept;
    int age;

    int onClickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editTextName = findViewById(R.id.editText_name);
        EditText editTextAge = findViewById(R.id.editText_age);
        EditText editTextDept = findViewById(R.id.editText_dept);

        Button buttonParse = findViewById(R.id.button_parse);
        Button buttonAdd = findViewById(R.id.button_add);
        Button buttonDel = findViewById(R.id.button_del);
        Button buttonSearch = findViewById(R.id.button_search);

        ClassDbHelper classDbHelper = new ClassDbHelper(getApplicationContext());

        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               parseEditText(editTextName, editTextAge, editTextDept);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = parseEditText(editTextName, editTextAge, editTextDept);

                if (result != -1) {
                    ContentValues values = new ContentValues();

                    values.put(DB.ClassDB.COLUMN_TITLE_NAME, name);
                    values.put(DB.ClassDB.COLUMN_TITLE_AGE, age);
                    values.put(DB.ClassDB.COLUMN_TITLE_DEPT, dept);

                    SQLiteDatabase db = classDbHelper.getWritableDatabase();
                    long newRowId = db.insert(DB.ClassDB.TABLE_NAME, null, values);

                    Toast.makeText(getApplicationContext(), "추가되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonDel.setOnClickListener(new View.OnClickListener() {

            /*
             * Remove data that matches the input value when clicking the button.
             * If there is an input value, replace the wildcard character '%'
             */
            @Override
            public void onClick(View v) {
                try {
                    name = editTextName.getText().toString();
                    ageSt = editTextAge.getText().toString();
                    dept = editTextDept.getText().toString();
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "입력 값을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

                SQLiteDatabase db = classDbHelper.getWritableDatabase();

                String[] args = new String[] {"%", "%", "%"};

                if (!name.equals("")) {
                    args[0] = name;
                }
                if (!ageSt.equals("")) {
                    args[1] = ageSt;
                }
                if (!dept.equals("")) {
                    args[2] = dept;
                }

                int result = db.delete(DB.ClassDB.TABLE_NAME, "Name LIKE ? and Age LIKE ? and Dept LIKE ?", args);
                Toast.makeText(getApplicationContext(),result + "개의 데이터가 제거되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  Save the value "onClick" through SharedPreferences.
                  When returning to the app, the previously saved value is retrieved.
                 */
                SharedPreferences sharedPref = getSharedPreferences("SearchOnClickCount", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                onClickCount = sharedPref.getInt("onClickCount", 0);
                onClickCount++;

                editor.putInt("onClickCount", onClickCount);
                editor.commit();

                Log.i(TAG,"onClickCount: " + onClickCount);


                /*
                  This is a phrase for SQL Select Query.
                  If there is an input value, replace the wildcard character '%'
                 */
                try {
                    name = editTextName.getText().toString();
                    ageSt = editTextAge.getText().toString();
                    dept = editTextDept.getText().toString();
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "입력 값을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

                SQLiteDatabase db = classDbHelper.getReadableDatabase();

                String SQL_SELECT_QUERY = "SELECT * FROM " + DB.ClassDB.TABLE_NAME + " WHERE Name LIKE ? and Age LIKE ? and Dept LIKE ? ";

                String[] args = {"%","%","%"};

                if (!name.equals("")) {
                    args[0] = name;
                }
                if (!ageSt.equals("")) {
                    args[1] = ageSt;
                }
                if (!dept.equals("")) {
                    args[2] = dept;
                }

                Cursor cursor = db.rawQuery(SQL_SELECT_QUERY, args);

                int queryCount = 0;
                while (cursor.moveToNext()) {
                    String col1 = cursor.getString(0);
                    String col2 = cursor.getString(1);
                    int col3 = cursor.getInt(2);
                    String col4 = cursor.getString(3);

                    Log.i(TAG, "col1: " + col1 + ", col2: " + col2 + ", col3: " + col3 + ", col4: " + col4);
                    queryCount++;
                }
                Toast.makeText(getApplicationContext(),queryCount + "개의 값이 조회되었습니다",Toast.LENGTH_SHORT).show();

                cursor.close();
                db.close();
            }
        });
    }

    /**
     * If parsing is successful, return 0 as the return value.
     * Return -1 if parsing fails or there is no input value.
     */
    private int parseEditText(EditText editTextName, EditText editTextAge, EditText editTextDept)  {

        try {
            name = editTextName.getText().toString();

            if(name.equals("")) {
                Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return -1;
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "이름을 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

        try {
            age = Integer.parseInt(editTextAge.getText().toString());

            if (age < 1) {
                Toast.makeText(getApplicationContext(), "나이를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                return -1;
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "나이를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

        try {
            dept = editTextDept.getText().toString();

            if(dept.equals("")) {
                Toast.makeText(getApplicationContext(), "학과를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return -1;
            }
            return 0;
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "학과를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

        return -1;
    }
}