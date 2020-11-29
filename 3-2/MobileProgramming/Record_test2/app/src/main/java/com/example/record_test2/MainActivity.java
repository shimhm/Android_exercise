package com.example.record_test2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    myDBHelper myHelper;
    SQLiteDatabase sqlDB;

    Button btn;
    TextView tvDate;

    Chronometer chrono;

    TextView tv;

    Button btnSelect;
    Button btnInit;

    TextView tvStart, tvEnd;

    BarChart chart;
   // BarDataSet barDataSet = new BarDataSet(null, null);
    //ArrayList<IBarDataSet> dataSets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myHelper = new myDBHelper(this);

        btn = (Button) findViewById(R.id.btn);
        tvDate = (TextView) findViewById(R.id.date);
        chrono = (Chronometer) findViewById(R.id.chronometer);

        tv = (TextView) findViewById(R.id.tv);

        btnSelect = (Button) findViewById(R.id.btnSelect);
        btnInit = (Button) findViewById(R.id.btnInit);
        tvStart = (TextView) findViewById(R.id.tvStart);
        tvEnd = (TextView) findViewById(R.id.tvEnd);

        chrono.stop();
        chrono.setBase(SystemClock.elapsedRealtime());

        chart = (BarChart) findViewById(R.id.barchart);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn.getText().toString().equals("수면시작")) {
                    btn.setText("수면종료");

                    //chronometer.start()
                    chrono.start();

                    //start_date 저장
                    String start = nowTime();

                    //
                    tvDate.setText(start);
                } else {
                    btn.setText("수면시작");

                    //chronometer.stop()
                    chrono.stop();

                    //chronometer 기록값 저장

                    //chronometer.reset()
                    chrono.setBase(SystemClock.elapsedRealtime());

                    //end_date 저장
                    String end = nowTime();

                    //String start, end 각각 startDate, endDate 저장
                    String start = tvDate.getText().toString();
                    tvDate.setText(end);
//                    tvDate.setText(end);
                    //tv.setText(s1+"\r"+e1);
                    //tv.setText("수면시작"+start+"수면종료"+end);


                    //{start_date, end_date} sleepDB에 저장
                    myHelper.insertData(start, end);
                    Toast.makeText(getApplicationContext(), "입력됨", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //database 조회 버튼
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("select * from userSleep;", null);

                String strStart = "startDate" + "\r\n" + "___________" + "\r\n";
                String strEnd = "endDate" + "\r\n" + "___________" + "\r\n";

                final ArrayList NoOfEmp = new ArrayList();
                int i=0;
                while (cursor.moveToNext()) {
                    String start = cursor.getString(0);
                    String end = cursor.getString(1);

                    strStart += start + "\r\n";
                    strEnd += end + "\r\n";

                    long diff = diffTime(start, end);
                    NoOfEmp.add(new BarEntry(diff, i));
                    i++;
                }

                drawChart(NoOfEmp);

                tvStart.setText(strStart);
                tvEnd.setText(strEnd);

                cursor.close();
                sqlDB.close();
            }
        });

        //database 초기화 버튼
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                myHelper.onUpgrade(sqlDB, 1, 2);
                sqlDB.close();
            }
        });
    }

    //------------------------------------------------------------------------------------//
    //현재 시간 구하기
    public String nowTime(){
        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(date);

        return formatDate;
    }

    //시간차이 구하기
    public long diffTime(String start, String end){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date sDate = sdf.parse(start);
            Date eDate = sdf.parse(end);

            long diff = eDate.getTime() - sDate.getTime();
            long timediff = diff / 1000;
            return timediff;
        } catch (Exception e){
            return 0;
        }
    }

    //
    public void drawChart(ArrayList NoOfEmp){
        //bar chart//
//        final ArrayList NoOfEmp = new ArrayList();
//
//        NoOfEmp.add(new BarEntry(945f, 0));
//        NoOfEmp.add(new BarEntry(1040f, 1));
//        NoOfEmp.add(new BarEntry(1133f, 2));
//        NoOfEmp.add(new BarEntry(1240f, 3));
//        NoOfEmp.add(new BarEntry(1369f, 4));
//        NoOfEmp.add(new BarEntry(1487f, 5));
//        NoOfEmp.add(new BarEntry(1501f, 6));
//        NoOfEmp.add(new BarEntry(1645f, 7));
//        NoOfEmp.add(new BarEntry(1578f, 8));
//        NoOfEmp.add(new BarEntry(1695f, 9));


        ArrayList day = new ArrayList();

        for (int i = 1; i <= 10; i++) {
            day.add("i");
        }

        BarDataSet bardataset = new BarDataSet(NoOfEmp, "time");
        chart.animateY(5000);
        BarData data = new BarData(day, bardataset);      // MPAndroidChart v3.X 오류 발생
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data);
    }

//    private ArrayList<Entry> getDataValues() {
//        ArrayList<Entry> dataVals = new ArrayList<>();
//        String[] columns = {"start", "end"};
//        Cursor cursor = sqlDB.query("userSleep", null, null, null, null, null, null);
//
//        for(int i=0;i<cursor.getCount();i++){
//            cursor.moveToNext();
//            //dataVals.add(new Entry(cursor.getString(0), cursor.getString(1)));
//        }
//        return dataVals;
//    }
}
