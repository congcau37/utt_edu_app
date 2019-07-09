package congdev37.edu.uttedudemo.student.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.Test;
import congdev37.edu.uttedudemo.student.activity.TestActivity;
import congdev37.edu.uttedudemo.util.Converter;
import congdev37.edu.uttedudemo.util.Helper;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    Context mContext;
    List<Test> mDatasets;
    OnClick mOnClick;

    public void setOnClick(OnClick mOnClick) {
        this.mOnClick = mOnClick;
    }

    public TestAdapter(List<Test> mDatasets) {
        this.mDatasets = mDatasets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_subject_test, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.initView(mDatasets.get(i),i);
    }

    @Override
    public int getItemCount() {
        return mDatasets == null ? 0 : mDatasets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvTimerQuestion;
        LinearLayout llSubjectTest;
        ImageView ivIcon,ivTestStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvTimerQuestion = itemView.findViewById(R.id.tvTimerAndQuestion);
            llSubjectTest = itemView.findViewById(R.id.llSubjectTest);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            ivTestStatus = itemView.findViewById(R.id.ivStatus);
        }

        public void initView(final Test test, final int position) {
            tvName.setText(test.getTestName());
            tvTimerQuestion.setText(Converter.splitQuestionID(test.getQuestionID()).size()+"/"+test.getTime());
            if(Helper.getActivity(mContext) instanceof TestActivity){
                ivIcon.setImageResource(R.drawable.ic_test);
            }
            if(test.isTestStatus())
            {
                ivTestStatus.setVisibility(View.VISIBLE);
            }else {
                ivTestStatus.setVisibility(View.GONE);
            }
            llSubjectTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClick.onItemClick(test.getTestID(),test.getQuestionID(),test.getTestName(),position);
                }
            });
        }
    }

    public interface OnClick {
        void onItemClick(String testID, String questionID, String testName,int position);
    }
}
