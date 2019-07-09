package congdev37.edu.uttedudemo.student.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.admin.adapter.SubjectManagerAdapter;
import congdev37.edu.uttedudemo.model.Question;

public class DetailExerciseAdapter extends RecyclerView.Adapter<DetailExerciseAdapter.ViewHolder> {
    Context mContext;
    List<Question> mDatasets;
    SubjectManagerAdapter.OnClick mOnClick;
    Unbinder unbinder;

    public DetailExerciseAdapter(List<Question> mDatasets) {
        this.mDatasets = mDatasets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_detail_exercise, viewGroup, false);
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
        @BindView(R.id.tvNum)
        TextView tvNum;
        @BindView(R.id.tvQuestion)
        TextView tvQuestion;
        @BindView(R.id.lnTop)
        LinearLayout lnTop;
        @BindView(R.id.ivIcon)
        ImageView ivIcon;
        @BindView(R.id.radA)
        RadioButton radA;
        @BindView(R.id.radB)
        RadioButton radB;
        @BindView(R.id.radC)
        RadioButton radC;
        @BindView(R.id.radD)
        RadioButton radD;
        @BindView(R.id.radGroup)
        RadioGroup radGroup;
        @BindView(R.id.content)
        ScrollView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }

        public void initView(final Question question, int position) {
            tvNum.setText("Câu:" + (position + 1));
            tvQuestion.setText(question.getQuesContent());
            radA.setText(question.getAnsA());
            radB.setText(question.getAnsB());
            radC.setText(question.getAnsC());
            radD.setText(question.getAnsD());

            //kiểm tra câu trả lời và setchecked
            try {
                if (question.getAnswer().equals("null")) {
                    radA.setChecked(false);
                    radB.setChecked(false);
                    radC.setChecked(false);
                    radD.setChecked(false);
                } else {
                    switch (question.getAnswer()) {
                        case "A":
                            radA.setChecked(true);
                            break;
                        case "B":
                            radB.setChecked(true);
                            break;
                        case "C":
                            radC.setChecked(true);
                            break;
                        default:
                            radD.setChecked(true);
                            break;
                    }
                }
                //ko cho người dùng click check
                radA.setClickable(false);
                radB.setClickable(false);
                radC.setClickable(false);
                radD.setClickable(false);

                getCheckAns(question.getAnsCorrect());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void getCheckAns(String ans) {

            switch (ans) {
                case "A":
                    radA.setBackgroundColor(Color.RED);
                    radB.setBackgroundColor(Color.WHITE);
                    radC.setBackgroundColor(Color.WHITE);
                    radD.setBackgroundColor(Color.WHITE);
                    break;
                case "B":
                    radA.setBackgroundColor(Color.WHITE);
                    radB.setBackgroundColor(Color.RED);
                    radC.setBackgroundColor(Color.WHITE);
                    radD.setBackgroundColor(Color.WHITE);
                    break;
                case "C":
                    radA.setBackgroundColor(Color.WHITE);
                    radB.setBackgroundColor(Color.WHITE);
                    radC.setBackgroundColor(Color.RED);
                    radD.setBackgroundColor(Color.WHITE);
                    break;
                default:
                    radA.setBackgroundColor(Color.WHITE);
                    radB.setBackgroundColor(Color.WHITE);
                    radC.setBackgroundColor(Color.WHITE);
                    radD.setBackgroundColor(Color.RED);
                    break;
            }
        }
    }

    public interface OnClick {
        void onItemClick(Question question);
    }
}
