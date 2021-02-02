package com.example.addressbook;

import android.content.Context;
import android.graphics.Paint;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileService {

    private Context context;

    public FileService(Context context){
        this.context = context;
    }

    public FileService(){ }

    public String getFileFromSdcard(String fileName){
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        File file = new File(Environment.getExternalStorageDirectory(),fileName);

        if(Environment.MEDIA_UNMOUNTED.equals(Environment.getExternalStorageState())){
            try{
                inputStream = new FileInputStream(file);
                int len = 0;
                byte[] data = new byte[1024];
                while((len = inputStream.read(data)) != -1){
                    outputStream.write(data,0,len);
                }
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                if(inputStream != null){
                    try{
                        inputStream.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return new String(outputStream.toByteArray());
    }

    public boolean saveContentToSdcard(String filename,String content){
        boolean flag = false;
        FileOutputStream outputStream = null;
        File file = new File(Environment.getExternalStorageDirectory(),filename);

        if(Environment.MEDIA_UNMOUNTED.equals(Environment.getExternalStorageState())){
            try{
                outputStream = new FileOutputStream(file);
                outputStream.write(content.getBytes());
                flag = true;
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                if(outputStream != null){
                    try {
                        outputStream.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return flag;
    }

}
