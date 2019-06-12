package congdev37.edu.uttedudemo.student.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.Subject;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import congdev37.edu.uttedudemo.student.activity.TestActivity;
import congdev37.edu.uttedudemo.student.adapter.SubjectAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectFragment extends Fragment {
    Unbinder unbinder;

    SOService mService;
    public static ArrayList<Subject> mDataSubject;
    SubjectAdapter mAdapter;
    @BindView(R.id.rcvSubject)
    RecyclerView rcvTest;

    public static String subName;
    public SubjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initView(View view) {
        mDataSubject = new ArrayList<>();
        mAdapter = new SubjectAdapter(mDataSubject);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        rcvTest.setLayoutManager(gridLayoutManager);
        rcvTest.setAdapter(mAdapter);
        mAdapter.setOnClick(new SubjectAdapter.OnClick() {
            @Override
            public void onItemClick(String subCode) {
                Intent intent = new Intent(getContext(), TestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("sub_code",subCode);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        loadAllSubject();
    }

    private void loadAllSubject() {
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        mService.getAllSubject(params).enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(Call<List<Subject>> call, Response<List<Subject>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        Subject item = response.body().get(i);
                        Subject subject = new Subject();
                        subject.setID(item.getID());
                        subject.setSubjectCode(item.getSubjectCode());
                        subject.setSubjectName(item.getSubjectName());
                        mDataSubject.add(subject);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    int statusCode = response.code();
                }
            }
            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
