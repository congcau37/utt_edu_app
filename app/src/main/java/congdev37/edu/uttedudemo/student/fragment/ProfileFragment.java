package congdev37.edu.uttedudemo.student.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import congdev37.edu.uttedudemo.MainActivity;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.Admin;
import congdev37.edu.uttedudemo.model.Student;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static congdev37.edu.uttedudemo.MainActivity.stdCode;

public class ProfileFragment extends Fragment {
    View view;
    SOService mService;
    ArrayList<Student> mDataStudent;
    ArrayList<Admin> mDataAdmin;
    Unbinder unbinder;
    @BindView(R.id.txt_std_name)
    TextView tvName;
    @BindView(R.id.tvstudentCode)
    TextView tvstudentCode;
    @BindView(R.id.tvTecherCode)
    TextView tvTecherCode;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.txt_std_birthday)
    TextView tvBirthday;
    @BindView(R.id.txt_std_phone)
    TextView tvhone;
    @BindView(R.id.txt_std_address)
    TextView tvAddress;
    @BindView(R.id.txt_std_class)
    TextView txtStdClass;
    @BindView(R.id.txt_std_gender)
    TextView tvGender;
    @BindView(R.id.pbLoading)
    FrameLayout pbLoading;
    @BindView(R.id.lnClass)
    LinearLayout lnClass;


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_student, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (MainActivity.stdCode.equals("admin")) {
            loadProfileAdmin();
        } else {
            loadProfileStudent();
        }
    }

    private void initView() {
        if (!stdCode.equals("admin")) {
            tvstudentCode.setVisibility(View.VISIBLE);
            tvTecherCode.setVisibility(View.GONE);
            lnClass.setVisibility(View.VISIBLE);

        } else {
            tvstudentCode.setVisibility(View.GONE);
            tvTecherCode.setVisibility(View.VISIBLE);
            lnClass.setVisibility(View.GONE);
        }
        mDataStudent = new ArrayList<>();
        mDataAdmin = new ArrayList<>();
    }

    private void initData() {
        if (!MainActivity.stdCode.equals("admin")) {
            tvName.setText(mDataStudent.get(0).getStdName());
            tvAddress.setText(mDataStudent.get(0).getAddress());
            txtStdClass.setText(mDataStudent.get(0).getClass_());
            tvBirthday.setText(mDataStudent.get(0).getBirthday());
            tvhone.setText(mDataStudent.get(0).getPhoneNumber());
            tvCode.setText(mDataStudent.get(0).getStdCode());
            tvGender.setText(mDataStudent.get(0).getGender());
        } else {
            tvName.setText(mDataAdmin.get(0).getName());
            tvAddress.setText(mDataAdmin.get(0).getAddress());
            tvBirthday.setText(mDataAdmin.get(0).getBirthday());
            tvhone.setText(mDataAdmin.get(0).getPhoneNumber());
            tvCode.setText(mDataAdmin.get(0).getTeacherCode());
            tvGender.setText(mDataAdmin.get(0).getGender());
        }
        setInvisibleLoading();
    }

    private void loadProfileStudent() {
        mDataStudent.clear();
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("stdCode", stdCode);
        mService.getStudent(params).enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        try {
                            Student item = response.body().get(i);
                            Student student = new Student();
                            student.setID(item.getID());
                            student.setStdName(item.getStdName());
                            student.setStdCode(item.getStdCode());
                            student.setBirthday(item.getBirthday());
                            student.setAddress(item.getAddress());
                            student.setPhoneNumber(item.getPhoneNumber());
                            student.setClass_(item.getClass_());
                            student.setGender(item.getGender());
                            mDataStudent.add(student);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    initData();
                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {

            }
        });
    }

    private void loadProfileAdmin() {
        mDataAdmin.clear();
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("teacherCode", "admin");
        mService.getAdmin(params).enqueue(new Callback<List<Admin>>() {
            @Override
            public void onResponse(Call<List<Admin>> call, Response<List<Admin>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        try {
                            Admin item = response.body().get(i);
                            Admin admin = new Admin();
                            admin.setID(item.getID());
                            admin.setName(item.getName());
                            admin.setTeacherCode(item.getTeacherCode());
                            admin.setBirthday(item.getBirthday());
                            admin.setAddress(item.getAddress());
                            admin.setPhoneNumber(item.getPhoneNumber());
                            admin.setGender(item.getGender());
                            mDataAdmin.add(admin);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    initData();
                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<List<Admin>> call, Throwable t) {

            }
        });
    }

    public void setInvisibleLoading() {
        pbLoading.setVisibility(View.GONE);
    }

    public void setVisibleLoading() {
        pbLoading.setVisibility(View.VISIBLE);
    }
}
