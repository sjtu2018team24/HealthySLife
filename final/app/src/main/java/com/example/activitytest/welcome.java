package com.example.activitytest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import com.example.activitytest.*;

public class welcome extends Activity {
    private sqler database;
    private SQLiteDatabase db;
    private DemoApplication p;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        * ****隐藏标题栏****
        * */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome);
        p = (DemoApplication) this.getApplication();
        db = this.openOrCreateDatabase("myMusic",MODE_PRIVATE,null);//判断数据库是否存在不存在就创建
        database = new sqler(this,"myMusic",null,1);
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
               next();
            }
        };
        //延时跳转
        Timer time=new Timer();
        time.schedule(t,1500);
    }//
    //查询数据库，不存在就创建
    private void next(){
        if(!database.tableIsExist("localMusic")){
            String sql="create table localMusic(musician,musicname,path)";
            db.execSQL(sql);
        }else{
            String sql="select * from localMusic";
            List<Map<String, Object>> ls = new ArrayList<>();
            Map<String,Object> m=new HashMap<>();
            Cursor cursor=db.rawQuery(sql,null);
            while (cursor.moveToNext()){
                m = new HashMap<>();
                m.put("musician",cursor.getString(cursor.getColumnIndex("musician")));
                m.put("musicname",cursor.getString(cursor.getColumnIndex("musicname")));
                m.put("path",cursor.getString(cursor.getColumnIndex("path")));
                ls.add(m);
            }
            if(ls.size()>0){
                p.setLocalMusic(ls);
            }
          }
        Intent in=new Intent(getApplicationContext(), musicActivity.class);
        startActivity(in);
        finish();
    }
}
