package com.example.huanghailiang.passwordkeeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.codec.digest.DigestUtils;

public class LoginActivity extends AppCompatActivity  implements android.view.View.OnClickListener{
    EditText password;
    EditText passwordConfirm;
    Button Confirm;
    TextView txtForgetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        password = (EditText) findViewById(R.id.passwordBtn);
        passwordConfirm = (EditText) findViewById(R.id.passwordBtnConfirm);
        Confirm = (Button) findViewById(R.id.LoginBtn);
        Confirm.setOnClickListener(this);

        txtForgetPassword=(TextView)findViewById(R.id.txtForgetPassword);
        txtForgetPassword.setOnClickListener(this);
        GoCheck();
    }
    int LoginState=0;
    static  int CreateAccount =1;
    static int LoginNormal=2;
    static  int LoginError=3;
    private  void GoCheck()
    {
        MyAccountRepo repo = new MyAccountRepo(this);

        ArrayList<HashMap<String, String>> NoteList =  repo.getMyAccountList();
        if(NoteList.size()!=0) {
            passwordConfirm.setVisibility(View.GONE);
            Confirm.setText("登录");
            LoginState=LoginNormal;
        }else {
            passwordConfirm.setVisibility(View.VISIBLE);
            Confirm.setText("设置密码");
            LoginState= CreateAccount;
            Toast.makeText(this, "No Note!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.LoginBtn)) {
            if (LoginState == CreateAccount) {
                String P1 = password.getText().toString();
                String p2 = passwordConfirm.getText().toString();
                if (P1.equals(p2) && passwordCheck(P1)) {
                    MyAccount myAccount = new MyAccount();
                    myAccount.password =hash(P1);
                     MyAccountRepo repo=new MyAccountRepo(this);
                    repo.insert(myAccount);
                    ContentDataControl.InializePassword(p2);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    if (!P1.equals(p2))
                    {
                        passwordConfirm.setError("密码不一致");
                    }
                }
            }
            else if(LoginState==LoginNormal)
            {
                MyAccountRepo repo = new MyAccountRepo(this);
                ArrayList<HashMap<String, String>> NoteList =  repo.getMyAccountList();
                boolean result=false;
                String id="Unkown";
                String Password="";
                for (int i =0; i< NoteList.size();i++)
                {
                   id =  NoteList.get(i).get("id");
                    Password=NoteList.get(i).get("password");
                    if(hash(password.getText().toString()).equals(Password))
                    {
                        result = true;
                        continue;
                    }
                }
                  if(result)
                  {
                      Intent intent = new Intent(this, MainActivity.class);
                      ContentDataControl.InializePassword(password.getText().toString());
                      startActivity(intent);
                  }
                else
                  {
                      Toast.makeText(this,"ID:"+ id+"登录失败,请重新登录", Toast.LENGTH_SHORT).show();
                  }
            }

        }
        else {
            if (view == findViewById(R.id.txtForgetPassword)) {
                new AlertDialog.Builder(this)
                        .setTitle("警告")
                        .setMessage("你真的要重置密码吗?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                MyAccountRepo repo =new MyAccountRepo(LoginActivity.this);
                                repo.ResetPassword();
                                Toast.makeText(LoginActivity.this, "重置成功", Toast.LENGTH_SHORT).show();
                                GoCheck();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        }

    }
    String hash(String orin) {
        return  MD5(orin);
    }
    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    private  boolean passwordCheck(String p1) {
        if (p1.length() < 8) {
            password.setError("密码长度太短");
            return false;
        }
        return true;
    }

}
