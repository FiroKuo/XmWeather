package com.gsx.xmweather.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @author Administrator
 * @version $Rev
 * @time 2017/1/27 13:45
 * @des ${TODO}
 * @updateAuthor $Author
 * @updateDate $Date
 * @updateDes ${TODO}
 */

public class ListDataIO {
    public static void saveList(Context context, String data) {

        BufferedWriter bw = null;
        try {
            FileOutputStream fs = context.openFileOutput("cities", Context.MODE_APPEND);
            bw = new BufferedWriter(new OutputStreamWriter(fs));
            bw.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw!=null){
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static String readData(Context context){

        BufferedReader br=null;
        try {

            StringBuilder sb=new StringBuilder();
            String line="";
            FileInputStream fs=context.openFileInput("cities");
            br=new BufferedReader(new InputStreamReader(fs));
            while ((line=br.readLine())!=null){
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (br!=null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
