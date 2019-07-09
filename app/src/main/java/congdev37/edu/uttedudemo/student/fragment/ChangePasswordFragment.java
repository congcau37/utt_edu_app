package congdev37.edu.uttedudemo.student.fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import congdev37.edu.uttedudemo.HomeActivity;
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
    Button btnSave;
    SOService mService;
    ArrayList<User> dataUser;
    @BindView(R.id.etOldpassword)
    TextInputEditText etOldpassword;
    @BindView(R.id.textInputLayoutEmail)
    LinearLayout textInputLayoutEmail;
    @BindView(R.id.etOldPasswordReEnter)
    TextInputEditText etOldPasswordReEnter;
    @BindView(R.id.edt_acc_new_password)
    TextInputEditText etNewPassword;
    @BindView(R.id.lnLogin)
    LinearLayoutCompat lnLogin;
    @BindView(R.id.ibt_save)
    AppCompatButton ibtSave;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        stdCode = HomeActivity.stdCode;
        mService = ApiUtils.getSOService();
        initData();
        initView();
        return view;
    }

    private void initData() {
        try {
            dataUser = new ArrayList<>();
            getUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        try {
            btnSave = view.findViewById(R.id.ibt_save);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveChangeAccount();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUser() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveChangeAccount() {
        try {
            String oldPassword = etOldpassword.getText().toString();
            String newPassword = etNewPassword.getText().toString();
            if (oldPassword.equals("")) {
                Toast.makeText(getActivity(), "Vui lòng nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();
                etOldpassword.setError("không được bỏ trống");
                etOldPasswordReEnter.setError("không được bỏ trống");

            }else if(etNewPassword.equals("")){
                etNewPassword.setError("không được bỏ trống");
            }

            else if (oldPassword.equals(dataUser.get(0).getPassword())) {
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
                                Toast.makeText(getActivity(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                initFragment(new SubjectFragment());

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void initFragment(Fragment fragment) {
        try {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, fragment);
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
