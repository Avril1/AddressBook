package com.example.addressbook.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addressbook.adapter.CustomAdapter;
import com.example.addressbook.adapter.CustomAdapterNew;
import com.example.addressbook.MySQLiteHelper;
import com.example.addressbook.R;
import com.example.addressbook.activity.AddContactActivity;
import com.example.addressbook.activity.MainActivity;
import com.example.addressbook.activity.ShowDetailsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RecyclerViewFragment extends Fragment {

    MySQLiteHelper helper;
    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset_name;
    protected String[] mDataset_phone;
    protected String[] information;
    private int scrollPosition = 0;
    boolean dualPane;
    int curCheckPositon = 0;
    View rootView;

    private FloatingActionButton floatingActionButton;
    private EditText editText;
    private TextView textView;
    private CustomAdapterNew customAdapterNew;
    private Cursor cursor;

    @Override
    public void onSaveInstanceState( Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice",curCheckPositon);
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper  = new MySQLiteHelper(getContext());

        information = new String[6];

        //Initialize dataset,this data would usually from a local content provider or remote server
        initDatasetName();
        initDatasetPhone();


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //check to see if we have a frame in  witch to embed the details
        //fragment directly in the containing UI
        View detailsFrame = getActivity().findViewById(R.id.fragment2);
        dualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if(savedInstanceState != null){
            //restore last state for checked position
            curCheckPositon = savedInstanceState.getInt("curChoice",0);
        }

        if(dualPane){
            addContact();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.recycler_view_fragment,container,false);

        //initialize RecyclerView
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.contact_recyclerView);

        //RecyclerView.LayoutManager defines how elements are laid out
        mLayoutManager = new LinearLayoutManager(getActivity());

        //if a layout manager has already been set,get current scroll position
        if(mRecyclerView.getLayoutManager() != null){
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);

        mAdapter = new CustomAdapter(mDataset_name,mDataset_phone);
        //set CustomAdapter as the Adapter for RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnViewItemClickListener(new CustomAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view,String name,int position) {

                if(dualPane){
                    showDetails(position,name);
                }else{
                    //otherwise we need to launch a new activity to display
                    //the dialog fragment with selected text
                    information = find(name);
                    Intent intent = new Intent(getActivity(), ShowDetailsActivity.class);
                    intent.putExtra("id", information[0]);
                    intent.putExtra("name", information[1]);
                    intent.putExtra("address", information[2]);
                    intent.putExtra("phone", information[3]);
                    intent.putExtra("lanline", information[4]);
                    intent.putExtra("email", information[5]);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view,String name,int position) {
                information = find(name);
                PopupMenu popupMenu = new PopupMenu(getContext(),view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete:
                                delete(name);
                                Intent intent1 = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent1);
                                return true;
                            case R.id.call:
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+information[3]));
                              // Toast.makeText(getContext(),"Phone is :"+information[3],Toast.LENGTH_LONG).show();
                                startActivity(intent);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.actions,popupMenu.getMenu());
                popupMenu.show();

            }
        });

        editText = (EditText)rootView.findViewById(R.id.search);
        textView = (TextView)rootView.findViewById(R.id.serch_tv);

        customAdapterNew = new CustomAdapterNew();

        floatingActionButton = (FloatingActionButton)rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dualPane){
                    addContact();
                }else {
                    Intent intent = new Intent(getContext(), AddContactActivity.class);
                    startActivity(intent);
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() != 0){
                    showRecyclerView();
                }else{
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()  == 0){
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editText.getText().toString().trim())){
                    Toast.makeText(getContext(),"Please enter what you want to search",Toast.LENGTH_SHORT).show();
                }else {
                    if(cursor != null){
                        int columnCount = cursor.getCount();
                        if(columnCount == 0){
                            Toast.makeText(getContext(),"Sorry, there is no content you want to search"
                                    ,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        return rootView;
    }


    //Generates Strings for RecyclerView's adapter .this data would usually come from a local content provider or remote server
    public String[] initDatasetName(){
        mDataset_name = findName();
        return mDataset_name;
    }

    public String[] initDatasetPhone(){
        mDataset_phone = findPhone();
        return mDataset_phone;
    }

    public String[] findName(){
        String[] name ;
        int i = 0;
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("information",null,null,null,
                null,null,null);
        name = new String[cursor.getCount()];
        while (cursor.moveToNext()){
            name[i] = cursor.getString(1);
            i++;
        }
        database.close();
        return name;
    }

    public String[] findPhone(){
        String[] phone;
        int i = 0;
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("information",null,null,null,
                null,null,null);
        phone = new String[cursor.getCount()];
        while(cursor.moveToNext()){
            phone[i] = cursor.getString(3);
            i++;
        }
        database.close();
        return phone;
    }

    public String[] find(String name){
        String[] information = new String[6];
        SQLiteDatabase database = helper.getReadableDatabase();

            Cursor cursor = database.query("information", null, "name=?", new String[]{String.valueOf(name)},
                    null, null, null);

        if(cursor.moveToFirst() ) {
            information[0] = cursor.getString(0);
            information[1] = cursor.getString(1);
            information[2] = cursor.getString(2);
            information[3] = cursor.getString(3);
            information[4] = cursor.getString(4);
            information[5] = cursor.getString(5);
        }
        database.close();
        return information;
    }
    public void delete(String name){
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete("information", "name=?", new String[]{name});
        database.close();
    }

       void showDetails(int index,String name){
            curCheckPositon = index;

           information = find(name);

           //make a new fragment to show this selection
           ShowDetailsFragment details = ShowDetailsFragment.newInstance(index,information[0],information[1],information[2],information[3],information[4],information[5]);

           //execute a transaction ,replacing any existing fragment
           //with this one inside the frame
           FragmentTransaction ft = getFragmentManager().beginTransaction();

           ft.replace(R.id.fragment2,details);
           ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
           ft.commit();

    }

    void addContact(){
        //check what fragment is currently shown,replace if needed
        AddContactFragment addContactFragment = new AddContactFragment();

        //execute a transaction ,replacing any existing fragment
        //with this one inside the frame
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment2,addContactFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }
    private void showRecyclerView(){

        String str = editText.getText().toString().trim();

        MySQLiteHelper helper = new MySQLiteHelper(getContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        cursor = database.rawQuery("select * from information where name like '%"+str+"%'",null);

        customAdapterNew = new CustomAdapterNew(cursor);
        mRecyclerView.setAdapter(customAdapterNew);

        customAdapterNew.setOnViewItemClickListener(new CustomAdapterNew.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String name,int position) {
                if(dualPane){
                    showDetails(position,name);
                }else {
                    information = find(name);
                    Intent intent = new Intent(getContext(), ShowDetailsActivity.class);
                    intent.putExtra("id", information[0]);
                    intent.putExtra("name", information[1]);
                    intent.putExtra("address", information[2]);
                    intent.putExtra("phone", information[3]);
                    intent.putExtra("lanline", information[4]);
                    intent.putExtra("email", information[5]);
                    startActivity(intent);
                }
            }
            @Override
            public void onItemLongClick(View view, String name,int position) {
            }
        });
    }

}
