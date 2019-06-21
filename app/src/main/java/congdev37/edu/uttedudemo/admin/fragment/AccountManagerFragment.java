package congdev37.edu.uttedudemo.admin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.admin.adapter.AccountAdapter;
import congdev37.edu.uttedudemo.model.User;
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

    public AccountManagerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account_manager, container, false);
        unbinder = ButterKnife.bind(this, view);
        loadDataAccount();
        mAdapter = new AccountAdapter(getContext(), R.layout.item_account, mDataAccount);
        lvAccount.setAdapter(mAdapter);
        return view;
    }

    public void loadDataAccount() {
        mDataAccount = new ArrayList<>();
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("type",2);
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
                        user.setType(item.getType());
                        mDataAccount.add(user);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
