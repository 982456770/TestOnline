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

import androidx.annotation.NonNull;

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

public class StuAnswerAdapter extends ArrayAdapter {
    private final int resourceId;
    public StuAnswerAdapter(Context context, int textViewResourceId, List<SelManswer> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SelManswer selManswer= (SelManswer) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView que=view.findViewById(R.id.mstu_question);
        TextView A=view.findViewById(R.id.mstu_choiceA);
        TextView B=view.findViewById(R.id.mstu_choiceB);
        TextView C=view.findViewById(R.id.mstu_choiceC);
        TextView D=view.findViewById(R.id.mstu_choiceD);
        TextView answer=view.findViewById(R.id.mstu_answer);
        TextView selected=view.findViewById(R.id.mstu_selected);
        que.setText(selManswer.getqDescription());
        A.setText(selManswer.getChoiceA());
        B.setText(selManswer.getChoiceB());
        C.setText(selManswer.getChoiceC());
        D.setText(selManswer.getChoiceD());
        answer.setText(selManswer.getqAnswer());
        selected.setText(selManswer.getSelected());
        return view;
    }
}
