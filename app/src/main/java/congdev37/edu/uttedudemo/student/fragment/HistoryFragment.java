package congdev37.edu.uttedudemo.student.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import congdev37.edu.uttedudemo.HomeActivity;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.ResponseHistory;
import congdev37.edu.uttedudemo.model.Subject;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import congdev37.edu.uttedudemo.student.activity.DetailExerciseActivity;
import congdev37.edu.uttedudemo.student.adapter.ExerciseAdapter;
import congdev37.edu.uttedudemo.util.ConstantKey;
import congdev37.edu.uttedudemo.util.TimeHelper;
import congdev37.edu.uttedudemo.util.TimeInterval;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    @BindView(R.id.rbToday)
    RadioButton rbToday;
    @BindView(R.id.rbMonth)
    RadioButton rbMonth;
    @BindView(R.id.rbYear)
    RadioButton rbYear;
    @BindView(R.id.rdg_date)
    RadioGroup rdgDate;
    @BindView(R.id.pbLoading)
    FrameLayout pbLoading;
    Unbinder unbinder;
    @BindView(R.id.tvNoHaveTest)
    TextView tvNoHaveTest;

    View view;
    ListView lvHistory;
    SOService mService;
    ExerciseAdapter mAdapter;
    TimeHelper timeHelper;
    TimeInterval timeInterval;
    ArrayList<ResponseHistory> mData;
    ArrayList<Subject> mSubject;
    BroadcastReceiver myBroadCast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history_test, container, false);
        unbinder = ButterKnife.bind(this, view);
        initBroadCast();
        initView();
        return view;
    }

    private void initView() {
        try {
            lvHistory = view.findViewById(R.id.lv_history);
            mData = new ArrayList<>();
            mSubject = new ArrayList<>();
            mAdapter = new ExerciseAdapter(getActivity(), R.layout.item_history, mData);
            lvHistory.setAdapter(mAdapter);
            rbToday.setChecked(true);
            timeHelper = new TimeHelper();
            timeInterval = TimeHelper.getInstance().getToday();
            loadDataHistory();
            //chọn thời gian
            rdgDate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.rbToday) {
                        timeInterval = TimeHelper.getInstance().getToday();
                    } else if (checkedId == R.id.rbMonth) {
                        timeInterval = TimeHelper.getInstance().getThisMonth();
                    } else {
                        timeInterval = TimeHelper.getInstance().getAll();

                    }
                    loadDataHistory();
                }
            });

            lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    sendDataExercise(position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //gửi dữ liệu bài làm qua màn hình chi tiết bài làm
    private void sendDataExercise(int position) {
        try {
            Intent intent = new Intent(getContext(), DetailExerciseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("exercise",mData.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //lấy lịch sử làm bài từ server
    private void loadDataHistory() {
        try {
            mData.clear();
            mService = ApiUtils.getSOService();
            Map<String, Object> params = new HashMap<>();
            params.put("studentCode", HomeActivity.stdCode);
            params.put("startDay", timeInterval.getStartTimeToServer());
            params.put("endDay", timeInterval.getEndTimeToServer());
            mService.getHistory(params).enqueue(new Callback<ArrayList<ResponseHistory>>() {
                @Override
                public void onResponse(Call<ArrayList<ResponseHistory>> call, Response<ArrayList<ResponseHistory>> response) {
                    if (response.isSuccessful()) {
                        for (int i = 0; i < response.body().size(); i++) {
                            ResponseHistory item = response.body().get(i);
                            ResponseHistory history = new ResponseHistory();
                            history.setExID(item.getExID());
                            history.setLevel(item.getLevel());
                            history.setQuestionID(item.getQuestionID());
                            history.setTestName(item.getTestName());
                            history.setAnswer(item.getAnswer());
                            history.setExDay(item.getExDay());
                            history.setScore(item.getScore());
                            history.setStudentCode(item.getStudentCode());
                            history.setSubjectCode(item.getSubjectCode());
                            history.setSubjectName(item.getSubjectName());
                            mData.add(history);
                        }
                        try {
                            if (mData.size() == 0) {
                                setInvisibleLoading();
                                tvNoHaveTest.setVisibility(View.VISIBLE);
                            }else {
                                Collections.reverse(mData);
                                tvNoHaveTest.setVisibility(View.GONE);
                                setInvisibleLoading();
                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        int statusCode = response.code();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ResponseHistory>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBroadCast() {
        try {
            myBroadCast = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(ConstantKey.ACTION_NOTIFY_DATA)) {
                        if(intent.getStringExtra("screen").equals("save_test")){
                            loadDataHistory();
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            };

            final IntentFilter filter = new IntentFilter(ConstantKey.ACTION_NOTIFY_DATA);
            getActivity().registerReceiver(myBroadCast, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setInvisibleLoading() {
        pbLoading.setVisibility(View.GONE);
    }

    public void setVisibleLoading() {
        pbLoading.setVisibility(View.VISIBLE);
    }
}
