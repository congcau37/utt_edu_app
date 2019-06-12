package congdev37.edu.uttedudemo.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.Question;


public class CheckAnswerAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Question> lsData;

    public CheckAnswerAdapter(Context context, int layout, ArrayList<Question> lsData) {
        this.context = context;
        this.layout = layout;
        this.lsData = lsData;
    }

    @Override
    public int getCount() {
        return lsData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
             viewHolder= new ViewHolder();
            view = layoutInflater.inflate(layout, null);
            viewHolder.txtNumberQuestion = (TextView) view.findViewById(R.id.txt_number_question);
            viewHolder.txtYourAnswer = (TextView) view.findViewById(R.id.txt_your_answer);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        int j = i+1;
        Question questionModel = lsData.get(i);
        viewHolder.txtNumberQuestion.setText("Câu:"+j);
        viewHolder.txtYourAnswer.setText(questionModel.getAnswer());
        return view;
    }

    public class ViewHolder {
        TextView txtNumberQuestion, txtYourAnswer;
    }
}
