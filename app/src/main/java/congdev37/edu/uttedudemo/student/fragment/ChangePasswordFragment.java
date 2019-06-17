package congdev37.edu.uttedudemo.student.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import congdev37.edu.uttedudemo.MainActivity;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.User;
import congdev37.edu.uttedudemo.response.ResponseMessage;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {

    View view;
    String stdCode;
    EditText edtAccOldPass, edtAccNewPass;
    Button btnSave;
    SOService mService;
    ArrayList<User> dataUser;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);
        stdCode = MainActivity.stdCode;
        mService = ApiUtils.getSOService();
        initData();
        initView();
        return view;
    }

    private void initData() {
        dataUser = new ArrayList<>();
        getUser();
    }

    private void initView() {

        edtAccOldPass = view.findViewById(R.id.edt_acc_password);
        edtAccNewPass = view.findViewById(R.id.edt_acc_new_password);
        btnSave = view.findViewById(R.id.ibt_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChangeAccount();
            }
        });
    }

    private void getUser() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", stdCode);
        mService.getUser(params).enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        User item = response.body().get(i);
                        User user = new User();
                        user.setId(item.getId());
                        user.setName(item.getName());
                        user.setPassword(item.getPassword());
                        dataUser.add(user);
                    }
                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

            }
        });
    }

    private void saveChangeAccount() {
        String oldPassword = edtAccOldPass.getText().toString();
        String newPassword = edtAccNewPass.getText().toString();
        if (oldPassword.equals("")) {
            Toast.makeText(getActivity(), "Xin nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();

        } else if (oldPassword.equals(dataUser.get(0).getPassword())) {
            mService = ApiUtils.getSOService();
            Map<String, Object> params = new HashMap<>();
            params.put("updateUser", oldPassword);
            params.put("old_password", oldPassword);
            params.put("new_password", newPassword);
            params.put("name", stdCode);
            mService.updateUser(params).enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {
                            Toast.makeText(getActivity(), "Lưu thành công!", Toast.LENGTH_SHORT).show();

                        } else if (response.body().getSuccess() == 0) {
                            Toast.makeText(getActivity(), "Lỗi: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        int statusCode = response.code();
                        if (statusCode == 404) {
                            Toast.makeText(getActivity(), "Lỗi : Không thể kết nối tới máy chủ ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {

                }
            });

        } else {
            Toast.makeText(getActivity(), "Mật khẩu cũ không chính xác", Toast.LENGTH_SHORT).show();

        }
    }

    void initFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, fragment);
        ft.commit();

    }
}
