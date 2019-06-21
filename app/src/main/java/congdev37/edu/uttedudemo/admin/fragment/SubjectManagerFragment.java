package congdev37.edu.uttedudemo.admin.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.admin.adapter.SubjectListAdapter;
import congdev37.edu.uttedudemo.model.Subject;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import congdev37.edu.uttedudemo.student.activity.TestActivity;
import congdev37.edu.uttedudemo.student.adapter.SubjectAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static congdev37.edu.uttedudemo.student.activity.TestActivity.subCode;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectManagerFragment extends Fragment {

    View view;
    @BindView(R.id.rcvSubject)
    RecyclerView rcvSubject;
    Unbinder unbinder;

    SOService mService;
    public ArrayList<Subject> mDataSubject;
    SubjectListAdapter mAdapter;

    public SubjectManagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_manager_subject, container, false);
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
        mAdapter = new SubjectListAdapter(mDataSubject);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcvSubject.setLayoutManager(layoutManager);
        rcvSubject.setAdapter(mAdapter);
        mAdapter.setOnClick(new SubjectListAdapter.OnClick() {
            @Override
            public void onItemClick() {

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
//                    setInvisibleLoading();
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

    public void setInvisibleLoading() {
//        pbLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
