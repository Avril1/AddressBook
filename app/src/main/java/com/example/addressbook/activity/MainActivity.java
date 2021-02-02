package com.example.addressbook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.addressbook.ConvertDataToJson;
import com.example.addressbook.FileService;
import com.example.addressbook.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Request permission
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
        }
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        }
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actions,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item){

        switch (item.getItemId()){
            case R.id.export_menu:
                ConvertDataToJson convertDataToJson = new ConvertDataToJson(getApplicationContext());
                convertDataToJson.JsonData();
                return true;
            case R.id.import_menu:
                FileService fileService = new FileService();
                fileService.getFileFromSdcard("information.txt");
                return true;
            case R.id.schedule:
                //permission
                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_CALENDAR)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CALENDAR},1);
                }
                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_CALENDAR)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_CALENDAR},1);
                }
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}