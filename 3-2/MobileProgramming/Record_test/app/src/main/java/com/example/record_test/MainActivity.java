package com.example.record_test;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    myDBHelper myHelper;
    SQLiteDatabase sqlDB;

    Button btn;

    View dialogView;
    EditText dlgId, dlgStart, dlgEnd, dlgTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btn);
        myHelper = new myDBHelper(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView = (View) View.inflate(MainActivity.this, R.layout.dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("사용자 정보 입력");
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dlgId = (EditText) dialogView.findViewById(R.id.edt_id);
                                dlgStart = (EditText) dialogView.findViewById(R.id.edt_start);
                                dlgEnd = (EditText) dialogView.findViewById(R.id.edt_end);
                                dlgTime = (EditText) dialogView.findViewById(R.id.edt_time);

                                //DB에 저장
                                sqlDB = myHelper.getWritableDatabase();
                                sqlDB.execSQL("insert into userSleep values ( '" + dlgId.getText().toString() + "', " + dlgStart.getText().toString() +
                                        "," + dlgEnd.getText().toString() + "," + dlgTime.getText().toString() +");");
                            }
                        });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });

    }

    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "userSLeep", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE userSleep (id char(20), start_date date, end_date date, sleep_time int);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS userSleep");
            onCreate(db);
        }
    }
}
