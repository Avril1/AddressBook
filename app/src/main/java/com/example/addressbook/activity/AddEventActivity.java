package com.example.addressbook.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.addressbook.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_title;
    private TextInputEditText et_description;
    private Button bt_start;
    private Button bt_start_time;
    private Button bt_end;
    private Button bt_end_time;
    private Button bt_add;
    private Button bt_cancel;
    private TextView tv_year;
    private TextView tv_month;
    private TextView tv_day;
    private TextView tv_hour;
    private TextView tv_minute;
    private TextView tv_year_end;
    private TextView tv_month_end;
    private TextView tv_day_end;
    private TextView tv_hour_end;
    private TextView tv_minute_end;


    Calendar calendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        et_title = (EditText)findViewById(R.id.et_title);
        et_description = (TextInputEditText)findViewById(R.id.et_description);
        bt_start = (Button)findViewById(R.id.bt_start);
        bt_start_time = (Button)findViewById(R.id.bt_start_time);
        bt_end = (Button)findViewById(R.id.bt_end);
        bt_end_time = (Button)findViewById(R.id.bt_end_time);
        bt_add = (Button)findViewById(R.id.bt_add);
        bt_cancel = (Button)findViewById(R.id.bt_cancel);
        tv_year = (TextView)findViewById(R.id.tv_year);
        tv_month = (TextView)findViewById(R.id.tv_month);
        tv_day = (TextView)findViewById(R.id.tv_day);
        tv_hour = (TextView)findViewById(R.id.tv_hour);
        tv_minute = (TextView)findViewById(R.id.tv_minute);
        tv_year_end = (TextView)findViewById(R.id.tv_year_end);
        tv_month_end = (TextView)findViewById(R.id.tv_month_end);
        tv_day_end = (TextView)findViewById(R.id.tv_day_end);
        tv_hour_end = (TextView)findViewById(R.id.tv_hour_end);
        tv_minute_end = (TextView)findViewById(R.id.tv_minute_end);
        
        bt_start.setOnClickListener(this);
        bt_start_time.setOnClickListener(this);
        bt_end.setOnClickListener(this);
        bt_end_time.setOnClickListener(this);


        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("title",et_title.getText().toString());
                intent.putExtra("description",et_description.getText().toString());
                intent.putExtra("year",tv_year.getText().toString());
                intent.putExtra("month",tv_month.getText().toString());
                intent.putExtra("day",tv_day.getText().toString());
                intent.putExtra("hour",tv_hour.getText().toString());
                intent.putExtra("minute",tv_minute.getText().toString());
                intent.putExtra("year_end",tv_year_end.getText().toString());
                intent.putExtra("month_end",tv_month_end.getText().toString());
                intent.putExtra("day_end",tv_day_end.getText().toString());
                intent.putExtra("hour_end",tv_hour_end.getText().toString());
                intent.putExtra("minute_end",tv_minute_end.getText().toString());
                setResult(1,intent);
                finish();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start:
                showDatePickerDialog(this,0,tv_year,tv_month,tv_day,calendar);
                ;
                break;
            case R.id.bt_start_time:
                showTimePickerDialog(this,0,tv_hour,tv_minute,calendar);
                break;
            case R.id.bt_end:
                showDatePickerDialog(this,0,tv_year_end,tv_month_end,tv_day_end,calendar);
                ;
                break;
            case R.id.bt_end_time:
                showTimePickerDialog(this,0,tv_hour_end,tv_minute_end,calendar);
                break;
            default:
                break;
        }
    }

    public static void showDatePickerDialog(Activity activity, int themeResId, final TextView tv_year,final TextView tv_month
            ,final TextView tv_day, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                tv_year.setText(""+year);
                tv_month.setText(""+(monthOfYear + 1));
                tv_day.setText(""+dayOfMonth);

            }
        }

                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public static void showTimePickerDialog(Activity activity,int themeResId, final TextView tv_hour,final TextView tv_minute, Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog(activity, themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv_hour.setText(""+hourOfDay);
                        tv_minute.setText(""+minute);
                    }
                }
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                , true).show();

    }
    }