package congdev37.edu.uttedudemo.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.User;

public class AccountAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<User> mData;

    public AccountAdapter(Context context, int layout, ArrayList<User> mData) {
        this.context = context;
        this.layout = layout;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            viewHolder= new ViewHolder();
            view = layoutInflater.inflate(layout, null);
            viewHolder.tvAccCode = (TextView) view.findViewById(R.id.tvAccCode);
            viewHolder.imgDel = (ImageView) view.findViewById(R.id.img_del);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        User user = mData.get(i);
        viewHolder.tvAccCode.setText(user.getName());
        viewHolder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mData.remove(i);
                notifyDataSetChanged();
            }
        });
        return view;
    }

    public class ViewHolder {
        TextView tvAccCode;
        ImageView imgEdit,imgDel;
    }
}
