package congdev37.edu.uttedudemo.admin.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.Statistical;
import congdev37.edu.uttedudemo.util.Converter;

public class StatisticalAdapter extends RecyclerView.Adapter<StatisticalAdapter.ViewHolder> {

    private List<Statistical> mData;

    public StatisticalAdapter(List<Statistical> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_statistical, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.initView(mData.get(i));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDate)
        TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        private void initView(Statistical statistical) {
            tvName.setText(statistical.getStudentCode());
            tvDate.setText(Converter.setDate(statistical.getExDay()));
        }
    }
}
