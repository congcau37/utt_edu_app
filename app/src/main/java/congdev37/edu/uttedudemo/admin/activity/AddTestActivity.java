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
import butterknife.OnClick;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.admin.adapter.CustomAdapter;
import congdev37.edu.uttedudemo.model.Question;
import congdev37.edu.uttedudemo.model.Subject;
import congdev37.edu.uttedudemo.response.ResponseMessage;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import congdev37.edu.uttedudemo.student.activity.TestActivity;
import congdev37.edu.uttedudemo.student.activity.TestDoneActivity;
import congdev37.edu.uttedudemo.student.fragment.SubjectFragment;
import congdev37.edu.uttedudemo.util.ConstantKey;
import congdev37.edu.uttedudemo.util.Converter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTestActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
    @BindView(R.id.spnSubject)
    Spinner spnSubject;
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

    AlertDialog.Builder builderConfirm;
    ArrayList<String> arrLevel;
    ArrayList<String> arrSubject;
    ArrayList<Subject> mListSubject;
    ArrayList<Question> mDataQuestion;
    SOService mService;
    public static int Level;
    int time = 0;
    String subjectCode;
    String questionID;

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
        arrSubject = new ArrayList<>();
        mListSubject = new ArrayList<>();
        mDataQuestion = new ArrayList<>();
        arrLevel.add("Dễ");
        arrLevel.add("Trung bình");
        arrLevel.add("Khó");
        //
        mListSubject = SubjectFragment.mDataSubject;
    }

    private void initView() {
        tvTitleToolbar.setText("Thêm bài test");
        spnLevel.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrLevel);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLevel.setAdapter(levelAdapter);
        //adapter spiner môn học
//        CustomAdapter subjectAdapter = new CustomAdapter(this, R.layout.customspinneritem, mListSubject);
//        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                subjectCode = mListSubject.get(pos).getSubjectCode();
//
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//        spnSubject.setAdapter(subjectAdapter);
        tvSubject.setText(SubjectFragment.subName);
        //
        etTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(null) || s.toString().equals("")) {
                    time = 0;
                    etTime.setText("0");
                } else {
                    time = Integer.parseInt(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Level = position + 1;
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @OnClick({R.id.ivBack, R.id.tvSave, R.id.ivSub, R.id.ivPlus, R.id.tvQuestion})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                showDialogConfirm();
                break;
            case R.id.tvSave:
                saveNewTest();
                break;
            case R.id.ivSub:
                etTime.setText(subTime() + "");
                break;
            case R.id.ivPlus:
                etTime.setText(plusTime() + "");
                break;
            case R.id.tvQuestion:
                Intent intent = new Intent(this, ChooseQuestionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("question", mDataQuestion);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                break;
        }
    }

    private void showDialogConfirm() {
        builderConfirm = new AlertDialog.Builder(AddTestActivity.this);
        builderConfirm.setMessage("Bạn có muốn hủy bài test");
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

    private void saveNewTest() {
        subjectCode = TestActivity.subCode;
        String testName = etTestName.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        if (time.equals("0")) {
            Toast.makeText(this, "Bạn chưa thiết lập thời gian", Toast.LENGTH_SHORT).show();
        }
        if (testName.equals("")) {
            Toast.makeText(this, "Tên đề thi không được để trống", Toast.LENGTH_SHORT).show();
        }
        if (tvQuestion.getText().toString().equals("...")) {
            Toast.makeText(this, "Bạn chưa chọn câu hỏi", Toast.LENGTH_SHORT).show();
        }
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("questionID", questionID);
        params.put("subjectCode", subjectCode);
        params.put("testName", testName);
        params.put("Level", Level);
        params.put("Timer", time);
        params.put("createDay", Converter.setDate());
        mService.saveNewTest(params).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {

                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {
                        Intent intentBroadCast = new Intent(ConstantKey.ACTION_NOTIFY_DATA);
                        sendBroadcast(intentBroadCast);
                        finish();
                    } else {
                        Toast.makeText(AddTestActivity.this, "Lỗi: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    int statusCode = response.code();
                    if (statusCode == 404) {
                        Toast.makeText(AddTestActivity.this, "Lỗi : Không thể kết nối tới máy chủ ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddTestActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {

            }
        });
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

    private void setTextQuestion() {
        String question = "Câu 1 : ";
        for (int i = 0; i < mDataQuestion.size(); i++) {
            question += mDataQuestion.get(i).getQuesContent();
        }
        tvQuestion.setText(question);
    }

    private int plusTime() {
        time += 5;
        return time;
    }

    private int subTime() {
        if (time == 0) {
            return 0;
        }
        time -= 5;
        return time;
    }
}
