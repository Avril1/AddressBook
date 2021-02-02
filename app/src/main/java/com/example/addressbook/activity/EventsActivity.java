package com.example.addressbook.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.addressbook.activity.AddEventActivity;
import com.example.addressbook.activity.UpdateEventActivity;
import com.example.addressbook.model.EventModel;
import com.example.addressbook.proxy.EventsProxy;
import com.example.addressbook.R;
import com.example.addressbook.adapter.EventsArrayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class EventsActivity extends AppCompatActivity implements EventsProxy.Listener {

    public static final String EXTRA_CALENDAR_ID = "calendar_id";

    private EventsProxy mEventsQueryHandler;

    private ArrayList<EventModel> mEvents;
    private EventsArrayAdapter mEventsAdapter;
    private ListView mEventsListView;

    private FloatingActionButton mCreateEventButton;

    private int mCalendarId;

    private String title;
    private String description;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String year_end;
    private String month_end;
    private String day_end;
    private String hour_end;
    private String minute_end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        this.mCalendarId = this.getIntent().getExtras().getInt(EXTRA_CALENDAR_ID, -1);

        this.mCreateEventButton = (FloatingActionButton) this.findViewById(R.id.button_add_event);
        this.mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventsActivity.this, AddEventActivity.class);
                startActivityForResult(intent,1);
            }
        });

        this.mEvents = new ArrayList<EventModel>();
        this.mEventsAdapter = new EventsArrayAdapter(this, R.layout.list_item_event, this.mEvents);
        this.mEventsListView = (ListView) this.findViewById(R.id.list_events);
        this.mEventsListView.setAdapter(this.mEventsAdapter);
        this.mEventsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(EventsActivity.this)
                        .setMessage("Are you sure you want to delete it?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                delete(position);
                                onEventDeleted();
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
                return true;
            }
        });
        this.mEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                SimpleDateFormat formatter = new SimpleDateFormat("y", Locale.getDefault());
                 year = formatter.format(mEvents.get(position).getStartDate().getTime());
                 year_end = formatter.format(mEvents.get(position).getEndDate().getTime());
                SimpleDateFormat formatter2 = new SimpleDateFormat("M", Locale.getDefault());
                month = formatter2.format(mEvents.get(position).getStartDate().getTime());
                month_end = formatter2.format(mEvents.get(position).getEndDate().getTime());
                SimpleDateFormat formatter3 = new SimpleDateFormat("d", Locale.getDefault());
                day = formatter3.format(mEvents.get(position).getStartDate().getTime());
                day_end = formatter3.format(mEvents.get(position).getEndDate().getTime());
                SimpleDateFormat formatter4 = new SimpleDateFormat("H", Locale.getDefault());
                hour = formatter4.format(mEvents.get(position).getStartDate().getTime());
                hour_end = formatter4.format(mEvents.get(position).getEndDate().getTime());
                SimpleDateFormat formatter5 = new SimpleDateFormat("m", Locale.getDefault());
                minute = formatter5.format(mEvents.get(position).getStartDate().getTime());
                minute_end = formatter5.format(mEvents.get(position).getEndDate().getTime());

                Intent intent = new Intent(EventsActivity.this, UpdateEventActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("title",mEvents.get(position).getTitle());
                intent.putExtra("description",mEvents.get(position).getDescription());
                intent.putExtra("year",year);
                intent.putExtra("month",month);
                intent.putExtra("day",day);
                intent.putExtra("hour",hour);
                intent.putExtra("minute",minute);
                intent.putExtra("year_end",year_end);
                intent.putExtra("month_end",month_end);
                intent.putExtra("day_end",day_end);
                intent.putExtra("hour_end",hour_end);
                intent.putExtra("minute_end",minute_end);
                startActivityForResult(intent,2);

            }
        });

        this.mEventsQueryHandler = new EventsProxy(this.getContentResolver());
        this.mEventsQueryHandler.registerListener(this);
        queryEvents();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == 1){
                title = data.getStringExtra("title");
                description = data.getStringExtra("description");
                year = data.getStringExtra("year");
                month = data.getStringExtra("month");
                day = data.getStringExtra("day");
                hour = data.getStringExtra("hour");
                minute = data.getStringExtra("minute");
                year_end = data.getStringExtra("year_end");
                month_end = data.getStringExtra("month_end");
                day_end = data.getStringExtra("day_end");
                hour_end = data.getStringExtra("hour_end");
                minute_end = data.getStringExtra("minute_end");
                add(title,description,year,month,day,hour,minute,year_end,month_end,day_end,hour_end,minute_end);
                onEventCreated();
            }
        }
        if(requestCode == 2){
            if(resultCode == 2){
                int position = data.getIntExtra("position",0);
                title = data.getStringExtra("title");
                description = data.getStringExtra("description");
                year = data.getStringExtra("year");
                month = data.getStringExtra("month");
                day = data.getStringExtra("day");
                hour = data.getStringExtra("hour");
                minute = data.getStringExtra("minute");
                year_end = data.getStringExtra("year_end");
                month_end = data.getStringExtra("month_end");
                day_end = data.getStringExtra("day_end");
                hour_end = data.getStringExtra("hour_end");
                minute_end = data.getStringExtra("minute_end");
                update(position,title,description,year,month,day,hour,minute,year_end,month_end,day_end,hour_end,minute_end);
                onEventUpdated();
            }
        }
    }



    // region Public methods

    public void queryEvents() {
        Log.e("CALENDARS_QUERY", "starting the query");

        this.mEventsQueryHandler.getAll(
                EventModel.create(),
                CalendarContract.Events.CALENDAR_ID + " = ?",
                new String[]{String.valueOf(this.mCalendarId)});
    }

    // endregion

    // region EventsProxy.Listener implementation

    @Override
    public void onEventsRetrieved(List<EventModel> events) {
        this.mEvents.clear();
        this.mEvents.addAll(events);
        this.mEventsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEventCreated() {
        this.queryEvents();
    }

    @Override
    public void onEventDeleted() {
        this.queryEvents();
    }

    @Override
    public void onEventUpdated() {
        this.queryEvents();
    }

    // endregion

    public void delete(int position){
        ContentResolver cr = getContentResolver();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, mEvents.get(position).getId());
        int rows = cr.delete(deleteUri, null, null);
    }

    public void add(String title,String description,String year, String month, String day,String hour,String minute,
                    String year_end, String month_end, String day_end,String hour_end,String minute_end){
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day),
                Integer.parseInt(hour),Integer.parseInt(minute));
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(Integer.parseInt(year_end), Integer.parseInt(month_end) - 1, Integer.parseInt(day_end),
                Integer.parseInt(hour_end),Integer.parseInt(minute_end));
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, mCalendarId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE,"GMT");
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
    }

    public void update(int position,String title,String description,String year, String month, String day,String hour,String minute,
                       String year_end, String month_end, String day_end,String hour_end,String minute_end){
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        Uri updateUri = null;

        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day),
                Integer.parseInt(hour),Integer.parseInt(minute));
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(Integer.parseInt(year_end), Integer.parseInt(month_end) - 1, Integer.parseInt(day_end),
                Integer.parseInt(hour_end),Integer.parseInt(minute_end));
        endMillis = endTime.getTimeInMillis();

        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, mCalendarId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE,"GMT");

        updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI,mEvents.get(position).getId());
        int rows = cr.update(updateUri, values, null, null);
    }

}