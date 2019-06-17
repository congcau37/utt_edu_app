package congdev37.edu.uttedudemo.admin.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.Question;
import congdev37.edu.uttedudemo.model.Test;
import congdev37.edu.uttedudemo.response.ResponseMessage;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import congdev37.edu.uttedudemo.student.activity.TestActivity;
import congdev37.edu.uttedudemo.student.fragment.SubjectFragment;
import congdev37.edu.uttedudemo.util.ConstantKey;
import congdev37.edu.uttedudemo.util.Converter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTestActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.etTestName)
    EditText etTestName;
    @BindView(R.id.tvSubject)
    TextView tvSubject;
    @BindView(R.id.spnLevel)
    Spinner spnLevel;
    @BindView(R.id.tvQuestionNumber)
    TextView tvQuestionNumber;
    @BindView(R.id.etTime)
    EditText etTime;
    @BindView(R.id.ivSub)
    ImageView ivSub;
    @BindView(R.id.ivPlus)
    ImageView ivPlus;
    @BindView(R.id.tvQuestion)
    TextView tvQuestion;
    @BindView(R.id.spnSubject)
    Spinner spnSubject;
    @BindView(R.id.btnExit)
    Button btnExit;
    @BindView(R.id.btnSave)
    Button btnSave;

    AlertDialog.Builder builderConfirm;

    ArrayList<Question> mDataQuestion;
    ArrayList<Test> mDataTest;
    ArrayList<String> arrLevel;
    String questionID;
    String testName, testID;
    int time = 0, position;
    public static int Level;
    int questionNumber;
    SOService mService;
    String subjectCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_test);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        arrLevel = new ArrayList<>();
        mDataTest = new ArrayList<>();
        mDataQuestion = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mDataQuestion = bundle.getParcelableArrayList("question");
            mDataTest = bundle.getParcelableArrayList("test");
            testName = bundle.getString("test_name");
            testID = bundle.getString("test_id");
            position = bundle.getInt("position");
            time = Integer.parseInt(bundle.getString("timer"));
        }
        arrLevel.add("Dễ");
        arrLevel.add("Trung bình");
        arrLevel.add("Khó");
        questionNumber = mDataQuestion.size();
        questionID = mDataTest.get(position).getQuestionID();
    }

    private void initView() {
        tvSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivPlus.setOnClickListener(this);
        ivSub.setOnClickListener(this);
        tvQuestion.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        //
        tvTitleToolbar.setText("Sửa bài test");
        etTestName.setText(testName);
        setTextQuestion();
        tvQuestionNumber.setText(questionNumber + "");
        tvSubject.setText(SubjectFragment.subName);
        etTime.setText(time + "");
        etTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (time < 0 || s.toString().equals("")) {
                    time = 0;
                    etTime.setText("0");
                } else if (time >= 120) {
                    time = 120;
                    etTime.setText("120");
                } else {
                    time = Integer.parseInt(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //
        spnLevel.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrLevel);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLevel.setAdapter(levelAdapter);
        spnLevel.setSelection(Integer.parseInt(TestActivity.level) - 1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Level = position + 1;
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvQuestion:
                Intent intent = new Intent(this, ChooseQuestionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("question", mDataQuestion);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                break;
            case R.id.btnExit:
            case R.id.ivBack:
                showDialogConfirm();
                break;
            case R.id.btnSave:
            case R.id.tvSave:
                if (validateForm()) {
                    updateTest();
                }
                break;
            case R.id.ivPlus:
                etTime.setText(plusTime() + "");
                break;
            case R.id.ivSub:
                etTime.setText(subTime() + "");
                break;
            default:
        }
    }

    private void showDialogConfirm() {
        builderConfirm = new AlertDialog.Builder(EditTestActivity.this);
        builderConfirm.setMessage("Bạn có muốn hủy bài test không?");
        builderConfirm.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builderConfirm.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builderConfirm.show();
    }

    private void setTextQuestion() {
        String question = "Câu 1 : ";
        for (int i = 0; i < mDataQuestion.size(); i++) {
            question += mDataQuestion.get(i).getQuesContent();
        }
        tvQuestion.setText(question);
    }

    private int plusTime() {
        if (time >= 120) {
            return 120;
        }
        time += 5;
        return time;
    }

    private int subTime() {
        if (time < 0) {
            return 0;
        }
        time -= 5;
        return time;
    }

    private void updateTest() {
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("testID", testID);
        params.put("questionID", questionID);
        params.put("subjectCode", subjectCode);
        params.put("testName", testName);
        params.put("Level", Level);
        params.put("Timer", time);
        params.put("createDay", Converter.setDate());
        mService.updateTest(params).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {

                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {
                        Intent intentBroadCast = new Intent(ConstantKey.ACTION_NOTIFY_DATA);
                        sendBroadcast(intentBroadCast);
                        finish();
                    } else {
                        Toast.makeText(EditTestActivity.this, "Lỗi: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    int statusCode = response.code();
                    if (statusCode == 404) {
                        Toast.makeText(EditTestActivity.this, "Lỗi : Không thể kết nối tới máy chủ ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditTestActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {

            }
        });
    }

    private boolean validateForm() {
        subjectCode = TestActivity.subCode;
        testName = etTestName.getText().toString().trim();
        if (time == 0) {
            Toast.makeText(this, "Bạn chưa thiết lập thời gian", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (testName.equals("")) {
            Toast.makeText(this, "Tên đề thi không được để trống", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tvQuestion.getText().toString().equals("...")) {
            Toast.makeText(this, "Bạn chưa chọn câu hỏi", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle bd = data.getExtras();
            if (bd != null) {
                mDataQuestion.clear();
                mDataQuestion = bd.getParcelableArrayList("question");
                getQuestionID();
                setTextQuestion();
                tvQuestionNumber.setText(mDataQuestion.size() + "");
            }

        }
    }

    private void getQuestionID() {
        questionID = "";
        for (int i = 0; i < mDataQuestion.size(); i++) {
            questionID += mDataQuestion.get(i).getQuestionID() + ",";
        }
    }

    @Override
    public void onBackPressed() {
        showDialogConfirm();
    }
}
