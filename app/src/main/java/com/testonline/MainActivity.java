package com.testonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.testonline.student.StudentActivity;
import com.testonline.teacher.InsQActivity;
import com.testonline.teacher.TeacherActivity;
import com.testonline.tools.Urlpath;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity{

    private Button btn_login;
    private EditText login_xuehao,login_password;
    private MyHandler myhandler = new MyHandler(this);
    public static final String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();

//        btn_login = findViewById(R.id.btn_login);
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this, InsQActivity.class);
//                startActivity(intent);
//            }
//        });

    }
    //弱引用，防止内存泄露
    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;
        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg);
            if (mActivity.get() == null) {
                return;            }
            mActivity.get().updateUIThread(msg);
        }
    }
    //配合子线程更新UI线程
    private void updateUIThread(Message msg){
        Bundle bundle = new Bundle();
        bundle = msg.getData();
        String str = bundle.getString("result");
        if(str.equals("老师")) {
            Intent intent=new Intent(MainActivity.this, TeacherActivity.class);
            startActivity(intent);
        }
        else if(str.equals("学生")) {
            Intent intent=new Intent(MainActivity.this, StudentActivity.class);
            startActivity(intent);
        }
        else if(str.equals("出错")) {
            Toast.makeText(MainActivity.this, "账号或密码错误", Toast.LENGTH_LONG).show();
        }
    }

    void InitView(){
        btn_login = findViewById(R.id.btn_login);
        login_xuehao = findViewById(R.id.login_xuehao);
        login_password = findViewById(R.id.login_password);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login_xuehao.getText().toString().isEmpty()||login_password.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this,"学号或密码不能为空",Toast.LENGTH_LONG).show();
                }
                else{
                    //开启访问数据库线程
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            String str = Login(login_xuehao.getText().toString(),login_password.getText().toString());
                            Bundle bundle = new Bundle();
                            JSONObject jsonObject = JSON.parseObject(str);
                            bundle.putString("result",jsonObject.getString("result"));
                            bundle.putString("id",jsonObject.getString("id"));
                            Urlpath.id=jsonObject.getString("id");
                            Log.d(MainActivity.TAG,"id:"+Urlpath.id);
                            Message msg = new Message();
                            msg.setData(bundle);
                            myhandler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
    }
    private String Login(String xuehao,String password){
        String urlPath= Urlpath.urlPath+"login";
        Log.d(MainActivity.TAG,"启动登录线程");
        String msg = "";
        try {
            //初始化URL
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式
            conn.setRequestMethod("POST");
            //设置超时信息
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            //设置允许输入
            conn.setDoInput(true);
            //设置允许输出
            conn.setDoOutput(true);
            //post方式不能设置缓存，需手动设置为false
            conn.setUseCaches(false);
            //我们请求的数据
            String data = "password="+ URLEncoder.encode(password,"UTF-8")+
                    "&id="+URLEncoder.encode(xuehao,"UTF-8");
            //獲取輸出流
            Log.d(MainActivity.TAG,data);
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();
            conn.connect();
            if (conn.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = conn.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    message.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                message.close();
                // 返回字符串
                msg = new String(message.toByteArray());
                Log.d(MainActivity.TAG,"解码前:"+msg);
                msg= URLDecoder.decode(msg,"utf-8");
                Log.d(MainActivity.TAG,"解码后:"+msg);
                return msg;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(MainActivity.TAG,"exit");
        return msg;
    }
}
