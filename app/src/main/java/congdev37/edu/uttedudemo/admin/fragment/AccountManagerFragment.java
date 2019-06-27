package congdev37.edu.uttedudemo.admin.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.admin.activity.StudentInformation;
import congdev37.edu.uttedudemo.admin.adapter.AccountAdapter;
import congdev37.edu.uttedudemo.model.User;
import congdev37.edu.uttedudemo.response.ResponseMessage;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountManagerFragment extends Fragment {

    View view;
    ArrayList<User> mDataAccount;
    SOService mService;
    AccountAdapter mAdapter;
    @BindView(R.id.lvAccount)
    ListView lvAccount;
    Unbinder unbinder;
    @BindView(R.id.imgSubject)
    ImageButton imgSubject;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.lnTop)
    LinearLayout lnTop;

    Dialog dialogDelete;

    public AccountManagerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account_manager, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        //
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.filter(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        lvAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), StudentInformation.class);
                Bundle bundle = new Bundle();
                bundle.putString("stdCode",mDataAccount.get(position).getName());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initData() {
        loadDataAccount();
    }

    public void loadDataAccount() {
        mDataAccount = new ArrayList<>();
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("type", 2);
        mService.getAllAccount(params).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        User item = response.body().get(i);
                        User user = new User();
                        user.setId(item.getId());
                        user.setName(item.getName());
                        user.setPassword(item.getPassword());
                        user.setPermission(item.getPermission());
                        mDataAccount.add(user);
                        if (i == response.body().size() - 1) {
                            mAdapter = new AccountAdapter(getContext(), R.layout.item_account, mDataAccount);
                            lvAccount.setAdapter(mAdapter);

                            mAdapter.setmItemClick(new AccountAdapter.ItemClick() {
                                @Override
                                public void onDeleteUser(User user) {
                                    showDialogDelete(user);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    private void showDialogDelete(final User user) {
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
        tvConfirm.setText(getString(R.string.you_can_delete_user));

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
                deleteUser(user);
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

    private void deleteUser(User user) {
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("name", user.getName());
        mService.deleteUser(params).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {

                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {
                        loadDataAccount();
                        dialogDelete.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Lỗi: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
