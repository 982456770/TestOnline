package com.testonline.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.testonline.MainActivity;
import com.testonline.R;
import com.testonline.tools.Urlpath;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class TeacherActivity extends AppCompatActivity {

    private static final String TAG = "TeacherActivity";
    private Button btn_t1;
    private Button btn_t2;
    private Button btn_t3;
    private Button btn_t4;
    private Button btn_out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        btn_t1=findViewById(R.id.btn_t1);
        btn_t2=findViewById(R.id.btn_t2);
        btn_t3=findViewById(R.id.btn_t3);
        btn_t4=findViewById(R.id.btn_t4);
        btn_out=findViewById(R.id.btn_out);
        setListeners();
    }
    private void setListeners(){
        OnClick onClick=new OnClick();
        btn_t1.setOnClickListener(onClick);
        btn_t2.setOnClickListener(onClick);
        btn_t3.setOnClickListener(onClick);
        btn_t4.setOnClickListener(onClick);
        btn_out.setOnClickListener(onClick);
    }
    private class OnClick implements View.OnClickListener{
        public void onClick(View v){
            switch (v.getId()){
                case R.id.btn_t1:
                    Intent intent=new Intent(TeacherActivity.this, InsQActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_t2:
                    new Thread(){
                        @Override
                        public void run() {
                            String str = new Urlpath().connect("","SendQuestion");
                            Log.d(TeacherActivity.TAG,"进入math:"+str);
                            Intent intent=new Intent(TeacherActivity.this, ShowQActivity.class);
                            intent.putExtra("str",str);
                            startActivity(intent);
                        }
                    }.start();
                    break;
                case R.id.btn_t3:
                    new Thread(){
                        @Override
                        public void run() {
                            String str = new Urlpath().connect("","SendQuestion");
                            Log.d(TeacherActivity.TAG,"进入math:"+str);
                            Intent intent=new Intent(TeacherActivity.this, DelQActivity.class);
                            intent.putExtra("str",str);
                            startActivity(intent);
                        }
                    }.start();
                    break;
                case R.id.btn_t4:
                    new Thread(){
                        @Override
                        public void run() {
                            String str = new Urlpath().connect("123","SendMstu");
                            Log.d(TeacherActivity.TAG,"进入math:"+str);
                            Intent intent=new Intent(TeacherActivity.this,StuAnswerActivity.class);
                            intent.putExtra("str",str);
                            startActivity(intent);
                        }
                    }.start();
                    break;
                case R.id.btn_out:
                    intent=new Intent(TeacherActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
