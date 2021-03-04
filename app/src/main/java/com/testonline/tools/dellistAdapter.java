package com.testonline.tools;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.testonline.MainActivity;
import com.testonline.R;
import com.testonline.teacher.DelQActivity;
import com.testonline.teacher.TeacherActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dellistAdapter extends ArrayAdapter {
        private final int resourceId;
        public static final Map<Integer, String> map = new HashMap<Integer, String>();
        public dellistAdapter(Context context, int textViewResourceId, List<Question> objects) {
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Question question= (Question) getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
            final int delqid =question.getQid();
            final TextView que=view.findViewById(R.id.del_question);
            final EditText A=view.findViewById(R.id.del_choiceA);
            final EditText B=view.findViewById(R.id.del_choiceB);
            final EditText C=view.findViewById(R.id.del_choiceC);
            final EditText D=view.findViewById(R.id.del_choiceD);
            final EditText answer=view.findViewById(R.id.del_answer);
            que.setText(question.getqDescription());
            A.setText(question.getChoiceA());
            B.setText(question.getChoiceB());
            C.setText(question.getChoiceC());
            D.setText(question.getChoiceD());
            answer.setText(question.getqAnswer());
            final Button del =view.findViewById(R.id.del);
            Button modify =view.findViewById(R.id.modify);
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("删除题目：",question.getqDescription());
                    Log.d("删除题目：", String.valueOf(delqid));
                    //String del = new Urlpath().connect(question.getqDescription(),"DelQuestion");
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            String del = new Urlpath().connect(question.getqDescription(),"DelQuestion");
                        }
                    }).start();
                }
            });
            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("修改题目：", String.valueOf(delqid));
                    final Question modifyQ =new Question();
                    modifyQ.setqDescription(String.valueOf(que.getText()));
                    modifyQ.setChoiceA(String.valueOf(A.getText()));
                    modifyQ.setChoiceB(String.valueOf(B.getText()));
                    modifyQ.setChoiceC(String.valueOf(C.getText()));
                    modifyQ.setChoiceD(String.valueOf(D.getText()));
                    modifyQ.setqAnswer(String.valueOf(answer.getText()));
                    modifyQ.setQid(delqid);
                    Log.d("修改2", modifyQ.toString());
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            String del = send(modifyQ,"ModifyQuestion");
                        }
                    }).start();
                }
            });
            return view;
        }
    public String send(Question que,String urlpath){
        String urlPath= Urlpath.urlPath+urlpath;
        Log.d("修改","启动线程");
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
            Log.d("修改","发送的数据："+que.toString());
            //我们请求的数据
            String data = "qDescription="+ URLEncoder.encode(que.getqDescription(),"UTF-8")+
                    "&qAnswer="+URLEncoder.encode(que.getqAnswer(),"UTF-8")+
                    "&choiceA="+URLEncoder.encode(que.getChoiceA(),"UTF-8")+
                    "&choiceB="+URLEncoder.encode(que.getChoiceB(),"UTF-8")+
                    "&choiceC="+URLEncoder.encode(que.getChoiceC(),"UTF-8")+
                    "&choiceD="+URLEncoder.encode(que.getChoiceD(),"UTF-8")+
                    "&qid="+URLEncoder.encode(String.valueOf(que.getQid()),"UTF-8")+
                    "&answer="+URLEncoder.encode(que.getqAnswer(),"UTF-8");
            //獲取輸出流
            Log.d("修改","编码后："+data);
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
                Log.d("修改","解码前:"+msg);
                msg= URLDecoder.decode(msg,"utf-8");
                Log.d("修改","解码后:"+msg);
                return msg;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(MainActivity.TAG,"exit");
        return msg;
    }
}
