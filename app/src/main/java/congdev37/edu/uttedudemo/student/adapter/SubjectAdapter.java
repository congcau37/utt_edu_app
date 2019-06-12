package congdev37.edu.uttedudemo.student.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.Subject;
import congdev37.edu.uttedudemo.student.fragment.SubjectFragment;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    Context mContext;
    List<Subject> mDatasets;
    OnClick mOnClick;

    public void setOnClick(OnClick mOnClick) {
        this.mOnClick = mOnClick;
    }

    public SubjectAdapter(List<Subject> mDatasets) {
        this.mDatasets = mDatasets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_subject_test,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.initView(mDatasets.get(i));
    }

    @Override
    public int getItemCount() {
        return mDatasets == null ? 0 : mDatasets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubName,tvTimerQuestion,tvTimeQuesRear;
        LinearLayout llSubject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubName = itemView.findViewById(R.id.tvName);
            llSubject = itemView.findViewById(R.id.llSubjectTest);
            tvTimeQuesRear = itemView.findViewById(R.id.tvTimeQuestion);
            tvTimerQuestion = itemView.findViewById(R.id.tvTimerAndQuestion);
        }

        public void initView(final Subject subject) {
            tvSubName.setText(subject.getSubjectName());
            tvTimerQuestion.setVisibility(View.GONE);
            tvTimeQuesRear.setVisibility(View.GONE);
            llSubject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClick.onItemClick(subject.getSubjectCode());
                    SubjectFragment.subName = subject.getSubjectName();
                }
            });
        }
    }

    public interface OnClick{
        void onItemClick(String subCode);
    }
}
