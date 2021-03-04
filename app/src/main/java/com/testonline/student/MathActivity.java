package com.testonline.student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.testonline.MainActivity;
import com.testonline.R;
import com.testonline.tools.MyListView;
import com.testonline.tools.QlistAdapter;
import com.testonline.tools.Question;
import com.testonline.tools.Urlpath;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.testonline.tools.Urlpath.id;

public class MathActivity extends AppCompatActivity {

    private static final String TAG = "MathActivity";
    List<Question> qlist=new ArrayList<Question>();
    private MyListView mMyListView;
    private Map<Integer,String> map;
    private QlistAdapter qlistAdapter;
    private Button btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);
        String str=getIntent().getStringExtra("str");
        Log.d(MathActivity.TAG,"获取Stu的str:"+str);
        initQuestion(str); // 初始化数据
        QlistAdapter adapter = new QlistAdapter(MathActivity.this, R.layout.qlist_item, qlist);//实例化FruitAdapter
        mMyListView = findViewById(R.id.mathqlist);//绑定Listview
        mMyListView.setAdapter(adapter);//设置Adapter
        map = qlistAdapter.map;
        btn_submit=findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MathActivity.TAG,"map:"+map.toString());
                new Thread(){
                    @Override
                    public void run() {
                        int qid=0;
                        Looper.prepare();
                        String json = JSON.toJSONString(map);//map转String
                        JSONObject jsonObject = JSON.parseObject(json);//String转json
                        Log.d(MathActivity.TAG,"json:"+jsonObject.toString());
                        String str=stumath(jsonObject);
                        Log.d(MathActivity.TAG,"str:"+str);//把他显示处理diolg,确定就退出
                        AlertDialog.Builder builder=new AlertDialog.Builder(MathActivity.this);
                        builder.setTitle("答题结果");
                        builder.setMessage(str);
                        builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(MathActivity.this, StudentActivity.class);
                                startActivity(intent);
                            }
                        }).show();
                        Looper.loop();
                    }
                }.start();
            }
        });
    }
    private void initQuestion(String str) {
        try {
            JSONArray jsonArray=new JSONArray(str);
            Log.d(MathActivity.TAG,"andoridarray:"+jsonArray.toString());
            for(int i=0;i<jsonArray.length();i++){
                Question question=new Question();
                org.json.JSONObject jsonObject=jsonArray.getJSONObject(i);
                Log.d(MathActivity.TAG,"andoridobj:"+jsonObject.toString());
                question.setqDescription(jsonObject.getString("qDescription"));
                question.setqAnswer(jsonObject.getString("qAnswer"));
                question.setChoiceA("A:"+jsonObject.getString("choiceA"));
                question.setChoiceB("B:"+jsonObject.getString("choiceB"));
                question.setChoiceC("C:"+jsonObject.getString("choiceC"));
                question.setChoiceD("D:"+jsonObject.getString("choiceD"));
                int qid=Integer.parseInt(jsonObject.getString("qid"));
                question.setQid(qid);
                Log.d(MathActivity.TAG,"andoridQ:"+question.toString());
                qlist.add(question);
            }
            Log.d(MathActivity.TAG,"andoridList:"+qlist.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String stumath(JSONObject jsonObject){
        String urlPath= Urlpath.urlPath+"StuMath";
        Log.d(MathActivity.TAG,"启动答题线程");
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
            String data = "stumath="+ URLEncoder.encode(jsonObject.toString(),"UTF-8")+
                    "&id="+URLEncoder.encode(id,"UTF-8");
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