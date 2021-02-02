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

public class ShowDetailsFragment extends Fragment {

    private EditText name_et;
    private EditText address_et;
    private EditText phone_et;
    private EditText landline_et;
    private EditText email_et;
    private Button cancel;
    private Button modify;
    RecyclerViewFragment recyclerViewFragment;

    String name;
    String address;
    String phone;
    String landline;
    String email;
    String id;

    boolean dualPane;

    MySQLiteHelper helper;

    //create a new instance of DetailsFragment ,initialized to show the text at 'index'
    public static ShowDetailsFragment newInstance(int index,String id,String name,String address,String phone,String landline,String email){
        ShowDetailsFragment f = new ShowDetailsFragment();

        Bundle args  = new Bundle();
        args.putInt("index",index);
        args.putString("id",id);
        args.putString("name",name);
        args.putString("address",address);
        args.putString("phone",phone);
        args.putString("landline",landline);
        args.putString("email",email);

        f.setArguments(args);
        return f;
    }

    public  String getShownId(){
        return getArguments().getString("id","");
    }
    public  String getShowName(){
        return getArguments().getString("name","");
    }
    public  String getShownAddress(){
        return getArguments().getString("address","");
    }
    public  String getShownPhone(){
        return getArguments().getString("phone","");
    }
    public  String getShownLandline(){
        return getArguments().getString("landline","");
    }
    public  String getShownEmail(){
        return getArguments().getString("email","");
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View detailsFrame = getActivity().findViewById(R.id.fragment2);
        dualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        View showDetailsView = inflater.inflate(R.layout.show_details_fragment,container,false);

        helper = new MySQLiteHelper(getContext());

        name_et = (EditText)showDetailsView.findViewById(R.id.name);
        address_et = (EditText)showDetailsView.findViewById(R.id.address);
        phone_et = (EditText)showDetailsView.findViewById(R.id.phone);
        landline_et = (EditText)showDetailsView.findViewById(R.id.landline);
        email_et = (EditText)showDetailsView.findViewById(R.id.email);
        cancel = (Button)showDetailsView.findViewById(R.id.cancel);
        modify = (Button)showDetailsView.findViewById(R.id.modify);

        if(dualPane){
            name = getShowName();
            address = getShownAddress();
            phone = getShownPhone();
            landline = getShownLandline();
            email = getShownEmail();
            id = getShownId();
        }
        else

    {
        Intent intent = getActivity().getIntent();

        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        phone = intent.getStringExtra("phone");
        landline = intent.getStringExtra("landline");
        email = intent.getStringExtra("email");
        id = intent.getStringExtra("id");
    }
            name_et.setText(name);
            address_et.setText(address);
            phone_et.setText(phone);
            landline_et.setText(landline);
            email_et.setText(email);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dualPane){

                }else {
                    getActivity().finish();
                }

            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update(id);

                Intent intent2 = new Intent(getContext(), MainActivity.class);
                startActivity(intent2);
            }
        });
        return showDetailsView;
    }

    public int update(String id){
        name = name_et.getText().toString();
        address = address_et.getText().toString();
        phone = phone_et.getText().toString();
        landline = landline_et.getText().toString();
        email = email_et.getText().toString();
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("address",address);
        values.put("phone",phone);
        values.put("landline",landline);
        values.put("email",email);
        int number = database.update("information",values, "_id=?", new String[]{id});
        database.close();
        return number;
    }
}
