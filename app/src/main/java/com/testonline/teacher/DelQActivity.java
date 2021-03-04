package com.testonline.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.testonline.MainActivity;
import com.testonline.R;
import com.testonline.tools.Urlpath;
import com.testonline.tools.dellistAdapter;
import com.testonline.tools.MyListView;
import com.testonline.tools.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DelQActivity extends AppCompatActivity {

    private static final String TAG = "DelQActivity";
    List<Question> dellist=new ArrayList<Question>();
    private MyListView mMyListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_q);

        String str=getIntent().getStringExtra("str");
        String del=getIntent().getStringExtra("del");
        Log.d(DelQActivity.TAG,"获取del:"+del);
        Log.d(DelQActivity.TAG,"获取Stu的str:"+str);
        Toast.makeText(DelQActivity.this, del, Toast.LENGTH_LONG).show();
        initDel(str); // 初始化数据
        dellistAdapter adapter = new dellistAdapter(DelQActivity.this, R.layout.dellist_item, dellist);//实例化FruitAdapter
        mMyListView = findViewById(R.id.delqlist);//绑定Listview
        mMyListView.setAdapter(adapter);//设置Adapter
        //返回操作的id

    }
    private void initDel(String str) {
        try {
            JSONArray jsonArray=new JSONArray(str);
            Log.d(DelQActivity.TAG,"andoridarray:"+jsonArray.toString());
            for(int i=0;i<jsonArray.length();i++){
                Question question=new Question();
                org.json.JSONObject jsonObject=jsonArray.getJSONObject(i);
                Log.d(DelQActivity.TAG,"andoridobj:"+jsonObject.toString());
                question.setqDescription(jsonObject.getString("qDescription"));
                question.setqAnswer(jsonObject.getString("qAnswer"));
                question.setChoiceA(jsonObject.getString("choiceA"));
                question.setChoiceB(jsonObject.getString("choiceB"));
                question.setChoiceC(jsonObject.getString("choiceC"));
                question.setChoiceD(jsonObject.getString("choiceD"));
                int qid=Integer.parseInt(jsonObject.getString("qid"));
                question.setQid(qid);
                Log.d(DelQActivity.TAG,"andoridQ:"+question.toString());
                dellist.add(question);
            }
            Log.d(DelQActivity.TAG,"andoridList:"+dellist.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
