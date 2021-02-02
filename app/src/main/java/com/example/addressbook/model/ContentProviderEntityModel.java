package com.example.addressbook.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public abstract class ContentProviderEntityModel {
    protected int mId;

    // region Construction

    protected ContentProviderEntityModel() {
        super();
        this.mId = -1;
    }

    // endregion

    // region Public methods

    public int getId() {
        return this.mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public abstract Uri getEntityUri();

    public abstract String[] getProjection();

    public abstract void fillFromCursor(Cursor cursor);

    public abstract ContentValues fillContentValues();

    // endregion
}
