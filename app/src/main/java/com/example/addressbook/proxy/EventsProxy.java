package com.example.addressbook.proxy;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import com.example.addressbook.model.EventModel;

import java.util.ArrayList;
import java.util.List;

public class EventsProxy extends CalendarContractProxy<EventModel> {


    private final ArrayList<Listener> mListeners;

    // region Construction

    public EventsProxy(ContentResolver cr) {
        super(cr);
        this.mListeners = new ArrayList<Listener>();
    }

    // endregion

    // region AsyncQueryHandler implementation

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        this.notifyCalendarCreated();
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        this.notifyCalendarDeleted();
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        this.notifyCalendarUpdated();
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        Log.e("CAL_QUERY_HANDLER", "query completed with " + cursor.getCount() + " records");

        EventModel model;
        ArrayList<EventModel> events = new ArrayList<EventModel>();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                model = EventModel.create();
                model.fillFromCursor(cursor);
                events.add(model);
            }
        }

        this.notifyCalendarsRetrieved(events);
    }

    // endregion

    // region Public methods

    public void deleteAllEventsForCalendar(EventModel model, int calendarId) {
        Log.e("DELETE_ALL_EVENTS", "" + calendarId);
        this.startDelete(1, null, model.getEntityUri(), CalendarContract.Events.CALENDAR_ID + " = ? ", new String[]{String.valueOf(calendarId)});
    }

    public void registerListener(Listener listener) {
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
    }

    public void unregisterListener(Listener listener) {
        if (this.mListeners.contains(listener)) {
            this.mListeners.remove(listener);
        }
    }

    // endregion

    // region Private methods

    private void notifyCalendarCreated() {
        for (Listener listener : this.mListeners) {
            listener.onEventCreated();
        }
    }

    private void notifyCalendarUpdated() {
        for (Listener listener : this.mListeners) {
            listener.onEventUpdated();
        }
    }

    private void notifyCalendarDeleted() {
        for (Listener listener : this.mListeners) {
            listener.onEventDeleted();
        }
    }

    private void notifyCalendarsRetrieved(ArrayList<EventModel> events) {
        for (Listener listener : this.mListeners) {
            listener.onEventsRetrieved(events);
        }
    }

    // endregion

    // region Inner interface

    public interface Listener {

        void onEventsRetrieved(List<EventModel> events);

        void onEventCreated();

        void onEventDeleted();

        void onEventUpdated();
    }

    // endregion
}
