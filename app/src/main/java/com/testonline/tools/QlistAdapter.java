package com.testonline.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.testonline.R;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QlistAdapter extends ArrayAdapter {
        private final int resourceId;
        public static final Map<Integer, String> map = new HashMap<Integer, String>();
        public QlistAdapter(Context context, int textViewResourceId, List<Question> objects) {
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Question question= (Question) getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象

            TextView que=view.findViewById(R.id.question);
            RadioGroup group=view.findViewById(R.id.radioGroup);
            RadioButton A=view.findViewById(R.id.choice_A);
            RadioButton B=view.findViewById(R.id.choice_B);
            RadioButton C=view.findViewById(R.id.choice_C);
            RadioButton D=view.findViewById(R.id.choice_D);
            que.setText(question.getqDescription());
            A.setText(question.getChoiceA());
            B.setText(question.getChoiceB());
            C.setText(question.getChoiceC());
            D.setText(question.getChoiceD());
            //final int qid=question.getQid();
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rbtn = group.findViewById(checkedId);
                    map.put(position, rbtn.getText().toString());
                }
            });
            return view;
        }
}
