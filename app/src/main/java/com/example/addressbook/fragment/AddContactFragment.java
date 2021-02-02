package com.example.addressbook.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import androidx.fragment.app.Fragment;

import com.example.addressbook.MySQLiteHelper;
import com.example.addressbook.R;
import com.example.addressbook.activity.MainActivity;

public class AddContactFragment extends Fragment {

    private EditText name_et;
    private EditText address_et;
    private EditText phone_et;
    private EditText landline_et;
    private EditText email_et;
    private Button cancel;
    private Button save;
    boolean dualPane;

    MySQLiteHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View detailsFrame = getActivity().findViewById(R.id.fragment2);
        dualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        View addContactView = inflater.inflate(R.layout.add_contact_fragment,container,false);

        helper = new MySQLiteHelper(getContext());

        name_et = (EditText)addContactView.findViewById(R.id.name);
        address_et = (EditText)addContactView.findViewById(R.id.address);
        phone_et = (EditText)addContactView.findViewById(R.id.phone);
        landline_et = (EditText)addContactView.findViewById(R.id.landline);
        email_et = (EditText)addContactView.findViewById(R.id.email);
        cancel = (Button)addContactView.findViewById(R.id.cancel);
        save = (Button)addContactView.findViewById(R.id.modify);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dualPane){

                }else {
                    getActivity().finish();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                String address;
                String phone;
                String landline;
                String email;

                name = name_et.getText().toString();
                address = address_et.getText().toString();
                phone = phone_et.getText().toString();
                landline = landline_et.getText().toString();
                email = email_et.getText().toString();
                insert(name,address,phone,landline,email);

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        return addContactView;
    }

    public void insert(String name, String address, String phone, String landline, String email){

        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("address",address);
        values.put("phone",phone);
        values.put("landline",landline);
        values.put("email",email);
        database.insert("information",null,values);
        database.close();
    }
}
