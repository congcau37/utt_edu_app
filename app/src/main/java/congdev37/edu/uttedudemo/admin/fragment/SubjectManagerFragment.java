package congdev37.edu.uttedudemo.admin.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import congdev37.edu.uttedudemo.MainActivity;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.admin.adapter.SubjectListAdapter;
import congdev37.edu.uttedudemo.model.Subject;
import congdev37.edu.uttedudemo.response.ResponseMessage;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    Dialog dialogDelete;


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
            public void onItemClick(Subject subject) {
                ((MainActivity) getActivity()).showDialogAddSubject(subject);
            }

            @Override
            public void longclick(Subject subject) {
                showDialogDelete(subject);
            }
        });
    }

    private void initData() {
        loadAllSubject();
    }


    private void showDialogDelete(final Subject subject) {
        dialogDelete = new Dialog(getContext(), R.style.Theme_Dialog);
        dialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDelete.setContentView(R.layout.dialog_delete);
        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogDelete.setCancelable(true);
        dialogDelete.setCanceledOnTouchOutside(true);

        //ánh xạ
        Button btnNo = dialogDelete.findViewById(R.id.btnNo);
        Button btnYes = dialogDelete.findViewById(R.id.btnYes);
        ImageView btnTitleClose = dialogDelete.findViewById(R.id.btnTitleClose);
        final TextView tvConfirm = dialogDelete.findViewById(R.id.tvConfirm);

        //Hiển thị text theo đúng định dạng bằng html
        tvConfirm.setText(getString(R.string.you_can_delete_subject));

        // Chọn có xóa đơn vị
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDelete.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSubject(subject);
            }
        });
        // Chọn không để đóng dialog
        btnTitleClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogDelete.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialogDelete.show();
    }

    public void loadAllSubject() {
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

    private void deleteSubject(Subject subject) {
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("subjectCode", subject.getSubjectCode());
        mService.deleteSubject(params).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {

                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else if(response.body().getSuccess() == 2){
                        loadAllSubject();
                        dialogDelete.dismiss();
                    }else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    int statusCode = response.code();
                    if (statusCode == 404) {
                        Toast.makeText(getContext(), "Lỗi : Không thể kết nối tới máy chủ ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
