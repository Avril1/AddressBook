package com.example.addressbook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConvertDataToJson {

    Context context;

    public ConvertDataToJson(Context context){
        this.context = context;
    }

    public void JsonData() {
        MySQLiteHelper helper = new MySQLiteHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor = database.query("information", null, null, null,
                null, null, null);
        cursor.moveToFirst();
        int i = 0;
        String[] id = new String[cursor.getCount()];
        String[] name = new String[cursor.getCount()];
        String[] address = new String[cursor.getCount()];
        String[] phone = new String[cursor.getCount()];
        String[] landline = new String[cursor.getCount()];
        String[] email = new String[cursor.getCount()];
        while (cursor.moveToNext()) {
            id[i] = cursor.getString(0);
            name[i] = cursor.getString(1);
            address[i] = cursor.getString(2);
            phone[i] = cursor.getString(3);
            landline[i] = cursor.getString(4);
            email[i] = cursor.getString(5);
            i++;
        }
        database.close();
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            jsonObject.put("id", name);
            jsonObject.put("name", name);
            jsonObject.put("address", address);
            jsonObject.put("phone", phone);
            jsonObject.put("lanline", landline);
            jsonObject.put("email", email);
            array.put(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileService fileService = new FileService();
        fileService.saveContentToSdcard("information.txt", String.valueOf(object));
    }
}
