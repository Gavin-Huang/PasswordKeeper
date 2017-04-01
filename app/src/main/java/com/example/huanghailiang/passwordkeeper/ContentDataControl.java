package com.example.huanghailiang.passwordkeeper;

import android.content.Context;

import org.cryptonode.jncryptor.AES256JNCryptor;
import org.cryptonode.jncryptor.CryptorException;
import org.cryptonode.jncryptor.JNCryptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by huanghailiang on 2016/8/20.
 */
public  class ContentDataControl {
    public static void  InializePassword(String password)
    {
      Password=password;
    }
   private  static byte[] Encryption(byte[] source,String Password)
     {
         JNCryptor cryptor = new AES256JNCryptor();
         byte[] ciphertext=null;
         try {
             ciphertext = cryptor.encryptData(source, Password.toCharArray());
             return  ciphertext;
         } catch (CryptorException e) {
             // Something went wrong
             e.printStackTrace();
         }
         return ciphertext;
     }
    private  static byte[] Decryption(byte[] source,String Password)
    {
        JNCryptor cryptor = new AES256JNCryptor();
        byte[] ciphertext = null;
        try {
            ciphertext = cryptor.decryptData(source, Password.toCharArray());
            return ciphertext;
        } catch (CryptorException e) {
            // Something went wrong
            e.printStackTrace();
        }
        return ciphertext;

    }
    private  static  String Password="";
     public static byte[] ReadNoteContentByte(String filename,Context tex) {
         byte[] result = null;
         byte[] temp=null;
         FileInputStream fis = null;
         try {
             fis = tex.openFileInput(filename);
             temp = new byte[fis.available()];
             fis.read(temp);
             //当输入输出都指定字符集编码的时候，就不会出现乱码的情况
             result=Decryption(temp,Password);//解密
             return result;
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (UnsupportedEncodingException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } finally {
             try {
                 if (fis != null) {
                     fis.close();
                 }
             } catch (IOException ex) {
                 ex.printStackTrace();
             }

         }
         return result;
     }
    public  static boolean DeleteContentFile(String FileName,Context context) {
        return context.deleteFile(FileName);
    }
    public  static boolean WriteNoteContentByte(String filename ,byte[] content,Context tex) {
        FileOutputStream fos = null;
        try {
            fos = tex.openFileOutput(filename, Context.MODE_PRIVATE);
            //FileOutputStream是字节流，如果是写文本的话，需要进一步把FileOutputStream包装 UTF-8是编码
            byte[] result =Encryption(content,Password);
            fos.write(result, 0, result.length);
            fos.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                return false;
            }
        }
        return true;
    }
    public static boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }



}
