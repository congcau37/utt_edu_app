package congdev37.edu.uttedudemo.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.ResponseHistory;
import congdev37.edu.uttedudemo.util.Converter;

public class ExerciseAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<ResponseHistory> data;

    public ExerciseAdapter(Context context, int layout, ArrayList<ResponseHistory> data) {
        this.context = context;
        this.layout = layout;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
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
            viewHolder.txtSubjectName = (TextView) view.findViewById(R.id.txt_subject_name);
            viewHolder.txtScore = (TextView) view.findViewById(R.id.tvScore);
            viewHolder.txtExDay = (TextView) view.findViewById(R.id.tvExDay);
            viewHolder.txtLevel = (TextView) view.findViewById(R.id.tvLevel);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ResponseHistory history = data.get(i);
        viewHolder.txtSubjectName.setText(history.getSubjectName());
        viewHolder.txtScore.setText(history.getScore());
        viewHolder.txtExDay.setText(Converter.setDate(history.getExDay()));
        viewHolder.txtLevel.setText(Converter.convertLevel(history.getLevel()));
        return view;
    }

    public class ViewHolder {
        TextView txtSubjectName, txtScore, txtExDay, txtLevel;
    }
}
