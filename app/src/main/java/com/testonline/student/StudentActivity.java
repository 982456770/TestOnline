package com.testonline.student;

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

public class StudentActivity extends AppCompatActivity {

    private static final String TAG = "StudentActivity";
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        btn_1=findViewById(R.id.btn_1);
        btn_2=findViewById(R.id.btn_2);
        btn_3=findViewById(R.id.btn_3);
        setListeners();
    }
    private void setListeners(){
        OnClick onClick=new OnClick();
        btn_1.setOnClickListener(onClick);
        btn_2.setOnClickListener(onClick);
        btn_3.setOnClickListener(onClick);
    }
    private class OnClick implements View.OnClickListener{
        public void onClick(View v){
            switch (v.getId()){
                case R.id.btn_1:
                    new Thread(){
                        @Override
                        public void run() {
                            String str = new Urlpath().connect("","SendQuestion");
                            Log.d(StudentActivity.TAG,"进入math:"+str);
                            Intent intent=new Intent(StudentActivity.this, MathActivity.class);
                            intent.putExtra("str",str);
                            startActivity(intent);
                        }
                    }.start();
                    break;
                case R.id.btn_2:
                    Intent intent2=new Intent(StudentActivity.this,MainActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.btn_3:
                    Intent intent3=new Intent(StudentActivity.this,MainActivity.class);
                    startActivity(intent3);
                    break;
            }
        }
    }
}

