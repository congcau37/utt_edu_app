package congdev37.edu.uttedudemo.admin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.Question;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Question> mDatasets;
    private OnClick mOnClick;

    public QuestionAdapter(ArrayList<Question> mDatasets) {
        this.mDatasets = mDatasets;
    }

    public void setmOnClick(OnClick mOnClick) {
        this.mOnClick = mOnClick;
    }

    public boolean isChecked(int position) {
        return mDatasets.get(position).isChoose();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_question, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.initView(mDatasets.get(i), i);
    }

    @Override
    public int getItemCount() {
        return mDatasets == null ? 0 : mDatasets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbQuestion;
        TextView tvQuestionName;
        LinearLayout lnRowQuestion;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbQuestion = itemView.findViewById(R.id.cbChecked);
            tvQuestionName = itemView.findViewById(R.id.tvQuestionName);
            lnRowQuestion = itemView.findViewById(R.id.lnRowQuestion);
        }

        public void initView(final Question question, final int position) {
            tvQuestionName.setText(question.getQuesContent());
//            mOnClick.onItemClick(getNumberQuestion());
            cbQuestion.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            mDatasets.get(position).setChoose(b);
                        }
                    });

            cbQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newState = mDatasets.get(position).isChoose();
                    mDatasets.get(position).setChoose(newState);
                    mOnClick.onItemClick(getNumberQuestion());
                }
            });

            cbQuestion.setChecked(isChecked(position));
        }
    }

    public interface OnClick {
        void onItemClick(int numberQuestion);

    }

    public int getNumberQuestion() {
        int number = 0;
        for (int i = 0; i < mDatasets.size(); i++) {
            if (mDatasets.get(i).isChoose()) {
                number += 1;
            }
        }
        return number;
    }

    public ArrayList<Question> getmDatasets() {
        return mDatasets;
    }


}
