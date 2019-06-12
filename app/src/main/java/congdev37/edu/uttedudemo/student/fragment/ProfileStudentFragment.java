package congdev37.edu.uttedudemo.student.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.Student;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static congdev37.edu.uttedudemo.MainActivity.stdCode;

public class ProfileStudentFragment extends Fragment {

    View view;
    TextView txtName,txtCode,txtPhone,txtClass,txtBirthday,txtAdd,txtGender;
    SOService mService;
    ArrayList<Student> dataStudent;


    public static ProfileStudentFragment newInstance() {
        ProfileStudentFragment fragment = new ProfileStudentFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_student, container, false);
        initView();
        loadProfileStudent();
        return view;
    }

    private void initView() {
        txtName = view.findViewById(R.id.txt_std_name);
        txtCode = view.findViewById(R.id.txt_std_code);
        txtPhone = view.findViewById(R.id.txt_std_phone);
        txtBirthday = view.findViewById(R.id.txt_std_birthday);
        txtClass = view.findViewById(R.id.txt_std_class);
        txtAdd = view.findViewById(R.id.txt_std_address);
        txtGender = view.findViewById(R.id.txt_std_gender);
        dataStudent = new ArrayList<>();

    }
    private void initData(){
        txtName.setText(dataStudent.get(0).getStdName());
        txtAdd.setText(dataStudent.get(0).getAddress());
        txtClass.setText(dataStudent.get(0).getClass_());
        txtBirthday.setText(dataStudent.get(0).getBirthday());
        txtPhone.setText(dataStudent.get(0).getPhoneNumber());
        txtCode.setText(dataStudent.get(0).getStdCode());
        txtGender.setText(dataStudent.get(0).getGender());
    }

    private void loadProfileStudent() {
        dataStudent = new ArrayList<>();
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
                            dataStudent.add(student);
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

}
