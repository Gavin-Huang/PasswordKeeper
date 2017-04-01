package com.example.huanghailiang.passwordkeeper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by huanghailiang on 2016/8/12.
 */
public class SerialBitmap implements Serializable {


    private static final long serialVersionUID = 520891646;
    // TODO: Finish this constructor
    SerialBitmap(Bitmap map ) {
        // Take your existing call to BitmapFactory and put it here
        PSetMyBitMap( map);
    }
    SerialBitmap()
    {

    }
    private String title;
    private int sourceWidth ;
    private int sourceHeight ;


    private Bitmap currentImage;
    public Bitmap PGetMyBitmap() {
        return currentImage;
    }
    public  void PSetMyBitMap(Bitmap fsd)
    {
        currentImage=fsd;
        if(fsd!=null)
        {
            sourceWidth  =fsd.getWidth();
            sourceHeight  = fsd.getHeight();
        }
    }
    protected class BitmapDataObject implements Serializable {
        private static final long serialVersionUID = 111696345129311948L;
        public byte[] imageByteArray;
    }

    /** Included for serialization - write this layer to the output stream. */
    private void writeObject(ObjectOutputStream out) throws IOException{

        out.writeObject(title);
        out.writeInt(sourceWidth);
        out.writeInt(sourceHeight);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(currentImage!=null) {
            currentImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }
        BitmapDataObject bitmapDataObject = new BitmapDataObject();
        bitmapDataObject.imageByteArray = stream.toByteArray();

        out.writeObject(bitmapDataObject);

        //   out.defaultWriteObject();
    }

    /** Included for serialization - read this object from the supplied input stream. */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        //    in.defaultReadObject();
        title = (String)in.readObject();
        sourceWidth= in.readInt();
        sourceHeight= in.readInt();

        BitmapDataObject bitmapDataObject = (BitmapDataObject)in.readObject();
        currentImage = BitmapFactory.decodeByteArray(bitmapDataObject.imageByteArray, 0, bitmapDataObject.imageByteArray.length);

    }
}
