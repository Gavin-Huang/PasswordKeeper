package com.example.huanghailiang.passwordkeeper;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ListActivity;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements android.view.View.OnClickListener{

    Button btnAdd;
    TextView student_Id;

   ListView listView=null;
    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.btnAdd)) {

            Intent intent = new Intent(this, EditNotes.class);
            intent.putExtra("student_Id", 0);
            startActivityForResult(intent,1);
        } else {
            ShowList();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String result=data.getStringExtra("result");
                Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        ShowList();
    }//onActivityResult
private  void ShowList()
{

    ArrayList<HashMap<String, String>> NoteList =  MyNotesControl.GetNoteList();
    if(NoteList.size()!=0) {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                student_Id = (TextView) view.findViewById(R.id.myNote_Id);
                String NoteID = student_Id.getText().toString();
                Intent objIndent = new Intent(getApplicationContext(), EditNotes.class);
                objIndent.putExtra("student_Id", Integer.parseInt(NoteID));
                startActivityForResult(objIndent,1);
            }
        });
        ListAdapter adapter = new SimpleAdapter( MainActivity.this,NoteList, R.layout.view_note_entry, new String[] { "id","title"}, new int[] {R.id.myNote_Id, R.id.myNote_title});
        listView.setAdapter(adapter);

    }else {
        Toast.makeText(this, "No Note!", Toast.LENGTH_SHORT).show();
    }
}
/* private  ListView getListView()
 {
     return  (ListView) findViewById(R.id.list);
 }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        MyNotesControl.Inialize(this);
        listView=(ListView) findViewById(R.id.Listmain);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        ShowList();
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
