package congdev37.edu.uttedudemo.admin.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.Admin;
import congdev37.edu.uttedudemo.model.Student;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentInformation extends AppCompatActivity {

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
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.appBar)
    AppBarLayout appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_information);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String stdCode = bundle.getString("stdCode");
            loadProfileStudent(stdCode);
        }
    }

    private void initView() {
        tvstudentCode.setVisibility(View.VISIBLE);
        tvTecherCode.setVisibility(View.GONE);
        lnClass.setVisibility(View.VISIBLE);

        mDataStudent = new ArrayList<>();
        mDataAdmin = new ArrayList<>();
    }

    private void initData() {
        tvName.setText(mDataStudent.get(0).getStdName());
        tvAddress.setText(mDataStudent.get(0).getAddress());
        txtStdClass.setText(mDataStudent.get(0).getClass_());
        tvBirthday.setText(mDataStudent.get(0).getBirthday());
        tvhone.setText(mDataStudent.get(0).getPhoneNumber());
        tvCode.setText(mDataStudent.get(0).getStdCode());
        tvGender.setText(mDataStudent.get(0).getGender());
        setInvisibleLoading();
    }

    private void loadProfileStudent(String stdCode) {
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

    public void setInvisibleLoading() {
        pbLoading.setVisibility(View.GONE);
    }

    public void setVisibleLoading() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }
}
