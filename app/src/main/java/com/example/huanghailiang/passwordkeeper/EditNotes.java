package com.example.huanghailiang.passwordkeeper;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EditNotes extends AppCompatActivity implements android.view.View.OnClickListener {


    Button btnSave , btnDelete;
    Button btnClose;
    EditText editNoteTitle;
    EditText NoteDate;

    private int _Note_Id =0;


    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1022;
    private RichTextEditor richTextEditor;
    private View btn1, btn2;
    private View.OnClickListener btnListener;
    RelativeLayout layout;
    private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    private File mCurrentPhotoFile;// 照相机拍照得到的图片

    MyNote myNotetemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* setTheme(R.style.ThemeLight);*/
        setContentView(R.layout.activity_edit_notes);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnClose = (Button) findViewById(R.id.btnClose);

        editNoteTitle = (EditText) findViewById(R.id.editNoteTitle);
        NoteDate = (EditText) findViewById(R.id.NoteDate);


        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnClose.setOnClickListener(this);


        _Note_Id =0;
        Intent intent = getIntent();
        _Note_Id =intent.getIntExtra("student_Id", 0);
        MyNoteRepo repo = new MyNoteRepo(this);
         myNotetemp = new MyNote();
        myNotetemp = repo.getNotesById(_Note_Id);
        if(myNotetemp==null)
            myNotetemp=new MyNote();

        editNoteTitle.setText(myNotetemp.title);
        NoteDate.setEnabled(false);
        if(myNotetemp.date==null)
        {
            NoteDate.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime()));
            btnDelete.setVisibility(View.GONE);
        }
        else {
            btnDelete.setVisibility(View.VISIBLE);
            NoteDate.setText(myNotetemp.date);
        }



        richTextEditor = (RichTextEditor) findViewById(R.id.richEditor);
         layout =(RelativeLayout)findViewById(R.id.outsideLayout);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Put your code here.
                if(_Note_Id!=0&&myNotetemp!=null&&myNotetemp.content!=null&&!myNotetemp.content.equals(""))//当前是浏览状态时
                {  byte[] fsd=MyNotesControl.GetNoteContentBytes(myNotetemp.content);
                    if(fsd!=null) {
                        richTextEditor.SetBytes(fsd);
                    }
                }
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        btnListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                richTextEditor.hideKeyBoard();
                if (v.getId() == btn1.getId()) {
                    // 打开系统相册
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");// 相片类型
                    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                } else if (v.getId() == btn2.getId()) {
                    // 打开相机
                    openCamera();
                }
            }
        };

        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);

        btn1.setOnClickListener(btnListener);
        btn2.setOnClickListener(btnListener);

    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    protected void dealEditData(List<EditData> editList) {
  /*    *//*  for (EditData itemData : editList) {
            if (itemData.inputStr != null) {
                Log.d("RichEditor", "commit inputStr=" + itemData.inputStr);
            } else if (itemData.imagePath != null) {
                Log.d("RichEditor", "commit imgePath=" + itemData.imagePath);
            }*//*

        }*/
    }
    private static Uri mImageCaptureUri;

    protected void openCamera() {
 /*       try {
*//*
            // Launch camera to take photo for selected contact
            PHOTO_DIR.mkdirs();// 创建照片的存储目录
            mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
*//*

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }*/
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

        try {
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode != RESULT_OK) {
                return;
            }

            if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                Uri uri = data.getData();

                insertBitmap(getRealFilePath(uri));
            } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
  /*              Bundle extras = data.getExtras();
                Bitmap photo = null;
                if (extras != null) {
                    photo = extras.getParcelable("data");
                }*/
                Bitmap photo = null;
                File f = new File(mImageCaptureUri.getPath());

                if (f.exists()) {
                    photo= BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageCaptureUri));
                    f.delete();

                }
                if (photo != null)
                {
                    richTextEditor.InsertImageDynamic(photo);
                }
            }
        }
        catch (Exception ef)
        {
            ef.printStackTrace();
        }
    }

    /**
     * 添加图片到富文本剪辑器
     *
     * @param imagePath
     */
    private void insertBitmap(String imagePath)
    {
        richTextEditor.insertImage(imagePath);
    }

    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public String getRealFilePath(final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public void onClick(View view) {
        try {
            if (view == findViewById(R.id.btnSave)) {
                MyNote myNote = new MyNote();
                myNote.content = "";
                myNote.date = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime());
                myNote.title = editNoteTitle.getText().toString();
                myNote.Note_ID = _Note_Id;
                String result = "";
                if (_Note_Id == 0) {
                    _Note_Id = MyNotesControl.insert(myNote, richTextEditor.GetBytes());

                    result = "New Note Insert";
                    //Toast.makeText(this,"New Note Insert",Toast.LENGTH_SHORT).show();

                } else {
                    myNote = MyNotesControl.getNotesById(_Note_Id);
                    myNote.title = editNoteTitle.getText().toString();
                    MyNotesControl.update(myNote, richTextEditor.GetBytes());
                    result = "Note Record updated";

                }
                Intent returnIntent0 = new Intent();
                returnIntent0.putExtra("result", result);
                setResult(RESULT_OK, returnIntent0);
                finish();
            } else if (view == findViewById(R.id.btnDelete)) {
                String result = "";
                if (MyNotesControl.delete(_Note_Id)) {
                    result = "Note Record Deleted";

                } else {
                    result = "Not Deleted";
                }
                Intent returnIntent1 = new Intent();
                returnIntent1.putExtra("result", result);
                setResult(RESULT_OK, returnIntent1);
                finish();
            } else if (view == findViewById(R.id.btnClose)) {
                String result = "close";
                Intent returnIntent3 = new Intent();
                returnIntent3.putExtra("result", result);
                setResult(RESULT_CANCELED, returnIntent3);
                finish();
            }
        }
        catch (Exception ef)
        {
            ef.printStackTrace();
        }
        finally {
            Intent returnIntent0 = new Intent();
            returnIntent0.putExtra("result", "test");
            setResult(RESULT_OK, returnIntent0);
            finish();
        }

    }

}
