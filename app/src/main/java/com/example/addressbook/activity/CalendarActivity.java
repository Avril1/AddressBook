package com.example.addressbook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.addressbook.model.CalendarModel;
import com.example.addressbook.proxy.CalendarsProxy;
import com.example.addressbook.R;
import com.example.addressbook.adapter.CalendarsArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends AppCompatActivity implements CalendarsProxy.Listener {

    private CalendarsProxy mCalendarsQueryHandler;

    private ArrayList<CalendarModel> mCalendars;
    private CalendarsArrayAdapter mCalendarsAdapter;
    private ListView mCalendarsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        this.mCalendars = new ArrayList<CalendarModel>();
        this.mCalendarsAdapter = new CalendarsArrayAdapter(this, R.layout.list_item_calendar, this.mCalendars);
        this.mCalendarsListView = (ListView) this.findViewById(R.id.list_calendars);
        this.mCalendarsListView.setAdapter(this.mCalendarsAdapter);
        this.mCalendarsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(CalendarActivity.this)
                        .setMessage("What do you want to do?")
                        .setPositiveButton("open", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(CalendarActivity.this,EventsActivity.class)
                                        .putExtra(EventsActivity.EXTRA_CALENDAR_ID, mCalendars.get(position).getId()));
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });

        this.mCalendarsQueryHandler = new CalendarsProxy(this.getContentResolver());
        this.mCalendarsQueryHandler.registerListener(this);

        queryCalendars();
    }

// endregion

    // region Public methods

    public void queryCalendars() {
        this.mCalendarsQueryHandler.getAll(CalendarModel.create(), null, null);
    }

    // endregion

    // region CalendarsProxy.Listener implementation

    @Override
    public void onCalendarCreated() {
        this.queryCalendars();
    }

    @Override
    public void onCalendarDeleted() {
        this.queryCalendars();
    }

    @Override
    public void onCalendarsRetrieved(List<CalendarModel> calendars) {
        this.mCalendars.clear();
        this.mCalendars.addAll(calendars);
        this.mCalendarsAdapter.notifyDataSetChanged();
    }

    // endregion
}