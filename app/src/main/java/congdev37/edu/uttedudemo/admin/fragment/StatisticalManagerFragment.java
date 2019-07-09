package congdev37.edu.uttedudemo.admin.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.admin.activity.StatisticalChartActivity;
import congdev37.edu.uttedudemo.admin.adapter.SubjectStatisticalAdapter;
import congdev37.edu.uttedudemo.model.Subject;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticalManagerFragment extends Fragment {

    Unbinder unbinder;
    SOService mService;
    public ArrayList<Subject> mDataSubject;
    SubjectStatisticalAdapter mAdapter;
    @BindView(R.id.rcvSubject)
    RecyclerView rcvSubject;

    @BindView(R.id.pbLoading)
    FrameLayout pbLoading;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    @BindView(R.id.tvNoHaveTest)
    TextView tvNoHaveTest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistical_manager, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        try {
            mDataSubject = new ArrayList<>();
            mAdapter = new SubjectStatisticalAdapter(mDataSubject);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
            rcvSubject.setLayoutManager(linearLayoutManager);
            rcvSubject.setAdapter(mAdapter);
            //click adapter
            mAdapter.setOnClick(new SubjectStatisticalAdapter.OnClick() {
                @Override
                public void onItemClick(Subject subject) {
                    Intent intent = new Intent(getContext(), StatisticalChartActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("sub_code", subject.getSubjectCode());
                    bundle.putString("sub_name", subject.getSubjectName());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            //sự kiện khi reload
            swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadAllSubject();
                            swiperefresh.setRefreshing(false);
                        }
                    }, 1500);
                }
            });
            //lấy dữ liệu
            loadAllSubject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //lấy toàn bộ môn học từ server
    private void loadAllSubject() {
        try {
            mDataSubject.clear();
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
                        if (mDataSubject.size() == response.body().size()) {
                            setInvisibleLoading();
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
        pbLoading.setVisibility(View.INVISIBLE);
    }
}

