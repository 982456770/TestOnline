package com.testonline.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.testonline.R;
import com.testonline.tools.MyListView;
import com.testonline.tools.QlistAdapter;
import com.testonline.tools.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ShowQActivity extends AppCompatActivity {

    private static final String TAG = "ShowQActivity";
    List<Question> qlist=new ArrayList<Question>();
    private MyListView mMyListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_q);
        String str=getIntent().getStringExtra("str");
        Log.d(ShowQActivity.TAG,"获取Stu的str:"+str);
        initQuestion(str); // 初始化数据
        QlistAdapter adapter = new QlistAdapter(ShowQActivity.this, R.layout.qlist_item, qlist);//实例化FruitAdapter
        mMyListView = findViewById(R.id.mathqlist);//绑定Listview
        mMyListView.setAdapter(adapter);//设置Adapter
    }
    private void initQuestion(String str) {
        try {
            JSONArray jsonArray=new JSONArray(str);
            Log.d(ShowQActivity.TAG,"andoridarray:"+jsonArray.toString());
            for(int i=0;i<jsonArray.length();i++){
                Question question=new Question();
                org.json.JSONObject jsonObject=jsonArray.getJSONObject(i);
                Log.d(ShowQActivity.TAG,"andoridobj:"+jsonObject.toString());
                question.setqDescription(jsonObject.getString("qDescription"));
                question.setqAnswer(jsonObject.getString("qAnswer"));
                question.setChoiceA("A:"+jsonObject.getString("choiceA"));
                question.setChoiceB("B:"+jsonObject.getString("choiceB"));
                question.setChoiceC("C:"+jsonObject.getString("choiceC"));
                question.setChoiceD("D:"+jsonObject.getString("choiceD"));
                int qid=Integer.parseInt(jsonObject.getString("qid"));
                question.setQid(qid);
                Log.d(ShowQActivity.TAG,"andoridQ:"+question.toString());
                qlist.add(question);
            }
            Log.d(ShowQActivity.TAG,"andoridList:"+qlist.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}