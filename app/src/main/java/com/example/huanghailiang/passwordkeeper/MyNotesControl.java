package com.example.huanghailiang.passwordkeeper;

import android.content.Context;

import java.net.IDN;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by huanghailiang on 2016/8/20.
 * control the Mynote and file
 */
public  class   MyNotesControl {
    private static  MyNoteRepo repo =null;
    private  static boolean IsInialized=false;
    private  static Context tempcontext=null;
    public  static  void Inialize(Context context) {
        if (!IsInialized) {
            repo = new MyNoteRepo(context);
            tempcontext=context;
        }
    }
    public static ArrayList<HashMap<String, String>> GetNoteList()
    {
        return  repo.getNotesList();
    }
    public  static  MyNote getNotesById(int Id)
    {
        return  repo.getNotesById(Id);
    }
    public static  void update(MyNote myNote,byte[] Content)
    {   if(myNote.content==null||myNote.content.equals(""))
    {
        SessionIdentifierGenerator eh =new SessionIdentifierGenerator();
        myNote.content=eh.nextSessionId();
    }
        ContentDataControl.WriteNoteContentByte(myNote.content,Content, tempcontext);
         repo.update(myNote);
    }
    public static boolean delete(int Note_ID)
    {   MyNote tre=repo.getNotesById(Note_ID);
        if(tre!=null)
        ContentDataControl.DeleteContentFile(tre.content,tempcontext);
       return  repo.delete(Note_ID);
    }
    public  static int insert(MyNote Note,byte[] Content) {
        SessionIdentifierGenerator eh =new SessionIdentifierGenerator();
        Note.content=eh.nextSessionId();
        ContentDataControl.WriteNoteContentByte(Note.content,Content, tempcontext);
       return repo.insert(Note);
    }
     public static  byte[] GetNoteContentBytes(String Content) {
         return ContentDataControl.ReadNoteContentByte(Content, tempcontext);
     }


}

