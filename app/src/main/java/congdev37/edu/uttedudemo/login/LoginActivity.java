package congdev37.edu.uttedudemo.login;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import congdev37.edu.uttedudemo.MainActivity;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.User;
import congdev37.edu.uttedudemo.response.ResponseMessage;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import congdev37.edu.uttedudemo.util.ConstantKey;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.logo_utt)
    ImageView logoUtt;
    @BindView(R.id.cbSaveLogin)
    CheckBox cbSaveLogin;
    @BindView(R.id.lnLogin)
    LinearLayoutCompat lnLogin;
    @BindView(R.id.nestedScrollView)
    LinearLayout nestedScrollView;
    @BindView(R.id.etAccName)
    TextInputEditText etAccName;
    @BindView(R.id.ivBarcode)
    ImageView ivBarcode;
    @BindView(R.id.textInputLayoutEmail)
    LinearLayout textInputLayoutEmail;
    @BindView(R.id.etAccPassword)
    TextInputEditText etAccPassword;
    @BindView(R.id.appCompatButtonLogin)
    AppCompatButton appCompatButtonLogin;

    static TextInputEditText etPassword;
    Button btnLogin;
    ArrayList<User> list;
    boolean check = false;
    String permission = "";
    SOService mService;
    private static final int RESULT_OK = 1;
    BroadcastReceiver receiver;
    ProgressDialog dialog;
    String shfKey = "save login";// key để lưu thông tin đăng nhập

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initEvent();
        initReceiver();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initView() {
        etAccName = findViewById(R.id.etAccName);
        etPassword = findViewById(R.id.etAccPassword);
        btnLogin = findViewById(R.id.appCompatButtonLogin);
        list = new ArrayList<>();
    }

    void initEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAppWithText();
            }
        });
        ivBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ScanBarcodeActivity.class);
                startActivity(i);
            }
        });
    }

    private void loginAppWithText() {
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("acc_user", etAccName.getText().toString());
        params.put("acc_pwd", etPassword.getText().toString());

        String accName = etAccName.getText().toString();
        String accPass = etPassword.getText().toString();
        if (accName.equals("")) {
            etAccName.setError("Không được để trống");
        }else if(accPass.equals("")){
            etPassword.setError("Không được để trống");
        }
        else {
            showDialogLoading();
            mService.login(params).enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {
                            if (Integer.parseInt(response.body().getType()) == 0) {
                                permission = "admin";
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("permission", permission);
                                bundle.putString("code", etAccName.getText().toString());
                                intent.putExtras(bundle);
                                hideDialogLoading();
                                startActivity(intent);
                            } else {
                                permission = "student";
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("permission", permission);
                                bundle.putString("code", etAccName.getText().toString());
                                intent.putExtras(bundle);
                                hideDialogLoading();
                                startActivity(intent);
                            }
                        } else {
                            hideDialogLoading();
                            Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        int statusCode = response.code();
                        if (statusCode == 404) {
                            hideDialogLoading();
                            Toast.makeText(LoginActivity.this, "Lỗi : Không thể kết nối tới máy chủ ", Toast.LENGTH_SHORT).show();
                        } else {
                            hideDialogLoading();
                            Toast.makeText(LoginActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                    t.printStackTrace();
                    hideDialogLoading();
                    Toast.makeText(LoginActivity.this, "Server lỗi", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loginAppWithScanBarcode() {
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("acc_user", etAccName.getText().toString());
        params.put("acc_pwd", "scan_barcode");

        String accName = etAccName.getText().toString();
        if (accName.equals("")) {
            etAccName.setError("Không được để trống");
        }
        else {
            showDialogLoading();
            mService.login(params).enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {
                            if (Integer.parseInt(response.body().getType()) == 0) {
                                permission = "admin";
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("permission", permission);
                                bundle.putString("code", etAccName.getText().toString());
                                intent.putExtras(bundle);
                                hideDialogLoading();
                                startActivity(intent);
                            } else {
                                permission = "student";
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("permission", permission);
                                bundle.putString("code", etAccName.getText().toString());
                                intent.putExtras(bundle);
                                hideDialogLoading();
                                startActivity(intent);
                            }
                        } else {
                            hideDialogLoading();
                            Toast.makeText(LoginActivity.this,  response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        int statusCode = response.code();
                        if (statusCode == 404) {
                            hideDialogLoading();
                            Toast.makeText(LoginActivity.this, "Lỗi : Không thể kết nối tới máy chủ ", Toast.LENGTH_SHORT).show();
                        } else {
                            hideDialogLoading();
                            Toast.makeText(LoginActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                    t.printStackTrace();
                    hideDialogLoading();
                    Toast.makeText(LoginActivity.this, "Server lỗi", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void receiveBarcode(String barcode) {
        MediaPlayer song = MediaPlayer.create(LoginActivity.this, R.raw.beep);
        song.start();
        etAccName.setText(barcode);

    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantKey.ACTION_RECEIVE_BARCODE);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ConstantKey.ACTION_RECEIVE_BARCODE)) {
                    String barcode = intent.getStringExtra(ConstantKey.KEY_PASS_BARCODE);
                    receiveBarcode(barcode);
                    loginAppWithScanBarcode();
                }
            }
        };
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    public void showDialogLoading() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Đang đăng nhập");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
    }

    public void hideDialogLoading() {
        dialog.hide();
    }

    public void savingPreferences() {
        try {
            SharedPreferences pre = getSharedPreferences
                    (shfKey, MODE_PRIVATE);
            SharedPreferences.Editor editor = pre.edit();
            String user = etAccName.getText().toString();
            String pwd = etPassword.getText().toString();
            boolean bchk = cbSaveLogin.isChecked();
            if (!bchk) {
                editor.clear();
            } else {
                editor.putString("user", user);
                editor.putString("pwd", pwd);
                editor.putBoolean("checked", bchk);
            }
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restoringPreferences() {
        try {
            SharedPreferences pre = getSharedPreferences
                    (shfKey, MODE_PRIVATE);
            boolean bchk = pre.getBoolean("checked", false);
            if (bchk) {
                String user = pre.getString("user", "");
                String pwd = pre.getString("pwd", "");
                etAccName.setText(user);
                etPassword.setText(pwd);
            }
            cbSaveLogin.setChecked(bchk);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            restoringPreferences();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();
            savingPreferences();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
