package com.example.huanghailiang.passwordkeeper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by huanghailiang on 2016/8/11.
 */
public class EditData implements Serializable {


    public String inputStr;
    public SerialBitmap bitmap;

    public Bitmap  GetBitmap()
    {
        if(bitmap!=null)
            return  bitmap.PGetMyBitmap();
        else
        return null;
    }
    public void SetBitmap(Bitmap dfs)
    {
        bitmap=new SerialBitmap(dfs);
    }
    private static final long serialVersionUID = 543891649L;
}
