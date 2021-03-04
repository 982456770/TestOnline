package com.testonline.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.testonline.R;
import com.testonline.tools.MyListView;
import com.testonline.tools.Question;
import com.testonline.tools.SelManswer;
import com.testonline.tools.StuAnswerAdapter;
import com.testonline.tools.dellistAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class StuAnswerActivity extends AppCompatActivity {

    private static final String TAG = "StuAnswerActivity";
    List<SelManswer> mstulist=new ArrayList<SelManswer>();
    private MyListView mMyListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_answer);

        String str=getIntent().getStringExtra("str");
        Log.d(StuAnswerActivity.TAG,"获取Stu的str:"+str);
        initDel(str); // 初始化数据
        StuAnswerAdapter adapter = new StuAnswerAdapter(StuAnswerActivity.this, R.layout.mstulist_item, mstulist);//实例化FruitAdapter
        mMyListView = findViewById(R.id.mstuqlist);//绑定Listview
        mMyListView.setAdapter(adapter);//设置Adapter
        //返回操作的id
    }
    private void initDel(String str) {
        try {
            JSONArray jsonArray=new JSONArray(str);
            Log.d(StuAnswerActivity.TAG,"andoridarray:"+jsonArray.toString());
            for(int i=0;i<jsonArray.length();i++){
                SelManswer selManswer=new SelManswer();
                org.json.JSONObject jsonObject=jsonArray.getJSONObject(i);
                Log.d(StuAnswerActivity.TAG,"andoridobj:"+jsonObject.toString());
                selManswer.setqDescription(jsonObject.getString("qDescription"));
                selManswer.setqAnswer(jsonObject.getString("qAnswer"));
                selManswer.setChoiceA(jsonObject.getString("choiceA"));
                selManswer.setChoiceB(jsonObject.getString("choiceB"));
                selManswer.setChoiceC(jsonObject.getString("choiceC"));
                selManswer.setChoiceD(jsonObject.getString("choiceD"));
                selManswer.setSelected(jsonObject.getString("selected"));
                Log.d(StuAnswerActivity.TAG,"andoridQ:"+selManswer.toString());
                mstulist.add(selManswer);
            }
            Log.d(StuAnswerActivity.TAG,"andoridList:"+mstulist.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
