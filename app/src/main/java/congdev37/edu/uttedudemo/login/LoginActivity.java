package congdev37.edu.uttedudemo.login;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
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

    @BindView(R.id.textInputEditTextEmail)
    TextInputEditText etAccName;
    @BindView(R.id.ivBarcode)
    ImageView ivBarcode;
    @BindView(R.id.textInputLayoutEmail)
    LinearLayout textInputLayoutEmail;
    @BindView(R.id.textInputEditTextPassword)
    TextInputEditText textInputEditTextPassword;
    @BindView(R.id.appCompatButtonLogin)
    AppCompatButton appCompatButtonLogin;

    TextInputEditText etAccount;
    static TextInputEditText edtPassword;
    Button btnLogin;
    ArrayList<User> list;
    boolean check = false;
    String permission = "";
    SOService mService;
    private static final int RESULT_OK = 1;
    BroadcastReceiver receiver;

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
        etAccount = findViewById(R.id.textInputEditTextEmail);
        edtPassword = findViewById(R.id.textInputEditTextPassword);
        btnLogin = findViewById(R.id.appCompatButtonLogin);
        list = new ArrayList<>();
    }

    void initEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAccount();
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

    private void checkAccount() {
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("acc_user", etAccount.getText().toString());
        params.put("acc_pwd", edtPassword.getText().toString());
        mService.login(params).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {
//                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (Integer.parseInt(response.body().getType()) == 0) {
                            permission = "admin";
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("permission", permission);
                            bundle.putString("code", etAccount.getText().toString());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else {
                            permission = "student";
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("permission", permission);
                            bundle.putString("code", etAccount.getText().toString());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Lỗi: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    int statusCode = response.code();
                    if (statusCode == 404) {
                        Toast.makeText(LoginActivity.this, "Lỗi : Không thể kết nối tới máy chủ ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "Server lỗi", Toast.LENGTH_SHORT).show();
            }
        });
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
}
