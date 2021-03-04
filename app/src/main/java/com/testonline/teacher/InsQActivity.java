package com.testonline.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.testonline.MainActivity;
import com.testonline.R;
import com.testonline.student.StudentActivity;
import com.testonline.tools.Question;
import com.testonline.tools.Urlpath;

import net.sf.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class InsQActivity extends AppCompatActivity {

    private Button add;
    private EditText add_question,add_A,add_B,add_C,add_D,add_answer;
    private InsQActivity.MyHandler myhandler = new InsQActivity.MyHandler(this);
    public static final String TAG="InsQActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_q);

        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(InsQActivity.TAG,"insQActivity");
                add_question = findViewById(R.id.add_question);
                add_A = findViewById(R.id.add_A);
                add_B = findViewById(R.id.add_B);
                add_C = findViewById(R.id.add_C);
                add_D = findViewById(R.id.add_D);
                add_answer =findViewById(R.id.add_answer);
                final Question question=new Question();
                question.setqDescription(add_question.getText().toString());
                question.setChoiceA(add_A.getText().toString());
                question.setChoiceB(add_B.getText().toString());
                question.setChoiceC(add_C.getText().toString());
                question.setChoiceD(add_D.getText().toString());
                question.setqAnswer(add_answer.getText().toString());
                Log.d(InsQActivity.TAG,question.toString());
                new Thread(){
                    @Override
                    public void run() {
                        String str = send(question,"addQ");
                        Log.d(InsQActivity.TAG,str);
                        Bundle bundle = new Bundle();
                        bundle.putString("str",str);
                        Message msg = new Message();
                        msg.setData(bundle);
                        myhandler.sendMessage(msg);
                    }
                }.start();
            }
        });
    }
    //弱引用，防止内存泄露
    private static class MyHandler extends Handler {
        private final WeakReference<InsQActivity> mActivity;
        public MyHandler(InsQActivity activity) {
            mActivity = new WeakReference<InsQActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg);
            if (mActivity.get() == null) {
                return;            }
            mActivity.get().upUIThread(msg);
        }
    }
    private void upUIThread(Message msg){
        Bundle bundle = new Bundle();
        bundle = msg.getData();
        String str = bundle.getString("str");
        Intent intent=new Intent(InsQActivity.this, TeacherActivity.class);
        Toast.makeText(InsQActivity.this, str, Toast.LENGTH_LONG).show();
        startActivity(intent);
    }
    public String send(Question que,String urlpath){
        String urlPath= Urlpath.urlPath+urlpath;
        Log.d(TAG,"启动线程");
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
            Log.d(TAG,"发送的数据："+que.toString());
            //我们请求的数据
            String data = "qDescription="+ URLEncoder.encode(que.getqDescription(),"UTF-8")+
                    "&qAnswer="+URLEncoder.encode(que.getqAnswer(),"UTF-8")+
                    "&choiceA="+URLEncoder.encode(que.getChoiceA(),"UTF-8")+
                    "&choiceB="+URLEncoder.encode(que.getChoiceB(),"UTF-8")+
                    "&choiceC="+URLEncoder.encode(que.getChoiceC(),"UTF-8")+
                    "&choiceD="+URLEncoder.encode(que.getChoiceD(),"UTF-8");
            //獲取輸出流
            Log.d(TAG,"编码后："+data);
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
                Log.d(TAG,"解码前:"+msg);
                msg= URLDecoder.decode(msg,"utf-8");
                Log.d(TAG,"解码后:"+msg);
                return msg;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(MainActivity.TAG,"exit");
        return msg;
    }
}

