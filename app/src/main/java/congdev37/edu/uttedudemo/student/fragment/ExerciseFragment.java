package congdev37.edu.uttedudemo.student.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import congdev37.edu.uttedudemo.MainActivity;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.ResponseHistory;
import congdev37.edu.uttedudemo.model.Subject;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import congdev37.edu.uttedudemo.student.adapter.ExerciseAdapter;
import congdev37.edu.uttedudemo.util.TimeHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseFragment extends Fragment {

    View view;
    ListView lvHistory;
    SOService mService;
    ExerciseAdapter adapter;

    ArrayList<ResponseHistory> mData;
    ArrayList<Subject> mSubject;
    String testID;
    @BindView(R.id.rbToday)
    RadioButton rbToday;
    @BindView(R.id.rbMonth)
    RadioButton rbMonth;
    @BindView(R.id.rbYear)
    RadioButton rbYear;
    @BindView(R.id.rdg_date)
    RadioGroup rdgDate;
    Unbinder unbinder;
    TimeHelper timeHelper;

    public ExerciseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history_test, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        loadDataHistory();
        return view;
    }

    private void initView() {
        lvHistory = view.findViewById(R.id.lv_history);
        mData = new ArrayList<>();
        mSubject = new ArrayList<>();
        timeHelper = new TimeHelper();
        adapter = new ExerciseAdapter(getActivity(), R.layout.item_history, mData);
        lvHistory.setAdapter(adapter);

        rbToday.setChecked(true);

        rdgDate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.rbToday){
                    TimeHelper.getInstance().getToday();
                }else if(checkedId==R.id.rbMonth){
                    TimeHelper.getInstance().getThisMonth();
                }else {

                }
            }
        });
    }

    private void loadDataHistory() {
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("studentCode", MainActivity.stdCode);
        mService.getHistory(params).enqueue(new Callback<ArrayList<ResponseHistory>>() {
            @Override
            public void onResponse(Call<ArrayList<ResponseHistory>> call, Response<ArrayList<ResponseHistory>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        ResponseHistory item = response.body().get(i);
                        ResponseHistory history = new ResponseHistory();
                        testID = item.getTestID();
                        history.setTestID(testID);
                        history.setExID(item.getExID());
                        history.setLevel(item.getLevel());
                        history.setTestName(item.getTestName());
                        history.setAnswer(item.getAnswer());
                        history.setExDay(item.getExDay());
                        history.setScore(item.getScore());
                        history.setStudentCode(item.getStudentCode());
                        history.setSubjectCode(item.getSubjectCode());
                        history.setSubjectName("");
                        mData.add(history);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ResponseHistory>> call, Throwable t) {

            }
        });
    }

    public void loadDataSubject(String subjectCode) {
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("subjectCode", subjectCode);
        mService.getSubject(params).enqueue(new Callback<ArrayList<Subject>>() {
            @Override
            public void onResponse(Call<ArrayList<Subject>> call, Response<ArrayList<Subject>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        Subject item = response.body().get(i);
                        Subject subject = new Subject();
                        subject.setSubjectCode(item.getSubjectCode());
                        subject.setSubjectName(item.getSubjectName());
                        mSubject.add(subject);
                    }
                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Subject>> call, Throwable t) {

            }
        });
    }

    private void addSubjectNameInHistoryTest() {
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).setSubjectName(mSubject.get(i).getSubjectName());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
