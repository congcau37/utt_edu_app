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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.Question;
import congdev37.edu.uttedudemo.model.Subject;
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
    @BindView(R.id.etQuestionNumber)
    EditText etQuestionNumber;
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
    ArrayList<Question> mDataQuestion, listAllQuestion, mListAllQuestion, listQuestionRandom, listQuestionSelect;
    SOService mService;
    public static int Level;
    int time = 0, questionNumber = 0;
    String subjectCode;
    String questionID;
    @BindView(R.id.btnExit)
    Button btnExit;
    @BindView(R.id.btnSave)
    Button btnSave;

    String testName;
    String Error = "";
    @BindView(R.id.ivSubQues)
    ImageView ivSubQues;
    @BindView(R.id.ivPlusQues)
    ImageView ivPlusQues;
    @BindView(R.id.swAutoChoose)
    Switch swAutoChoose;
    @BindView(R.id.btnDelete)
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_test);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        listAllQuestion = new ArrayList<>();
        mListAllQuestion = new ArrayList<>();
        arrLevel = new ArrayList<>();
        arrSubject = new ArrayList<>();
        mListSubject = new ArrayList<>();
        mDataQuestion = new ArrayList<>();
        listQuestionSelect = new ArrayList<>();
        listQuestionRandom = new ArrayList<>();
        arrLevel.add("Dễ");
        arrLevel.add("Trung bình");
        arrLevel.add("Khó");
        //
        mListSubject = SubjectFragment.mDataSubject;
        loadQuestion();
    }

    private void initView() {
        btnDelete.setVisibility(View.GONE);
        btnExit.setVisibility(View.VISIBLE);
        tvTitleToolbar.setText("Thêm bài test");
        spnLevel.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrLevel);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLevel.setAdapter(levelAdapter);
        tvSubject.setText(SubjectFragment.subName);
        etQuestionNumber.setHint("0");
        etTime.setHint("0");
        //
        etTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() < 0 || s.toString().equals("")) {
                    time = 0;
                    etTime.setHint("0");
                } else {
                    time = Integer.parseInt(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (time < 0) {
                    time = 0;
                    etTime.setHint("0");
                }
                if (time > 120) {
                    time = 120;
                    etTime.setText("120");
                }

            }
        });

        etQuestionNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() < 0 || s.toString().equals("")) {
                    questionNumber = 0;
                    etQuestionNumber.setHint("0");
                } else {
                    questionNumber = Integer.parseInt(String.valueOf(s));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (questionNumber < 0) {
                    questionNumber = 0;
                    etQuestionNumber.setHint("0");
                }
                if (questionNumber > mListAllQuestion.size()) {
                    questionNumber = mListAllQuestion.size();
                    etQuestionNumber.setText(mListAllQuestion.size() + "");
                }
                if (questionNumber != 0 || questionNumber != mListAllQuestion.size()) {
                    if (swAutoChoose.isChecked()) {
                        setRandomQuestion(questionNumber);
                    }
                }
            }
        });

        //
        swAutoChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setRandomQuestion(questionNumber);
                } else {
                    mDataQuestion.clear();
                    mDataQuestion.addAll(listQuestionSelect);
                    setTextQuestion(mDataQuestion);
                    etQuestionNumber.setText(mDataQuestion.size() + "");
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Level = position + 1;
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @OnClick({R.id.ivBack, R.id.tvSave, R.id.ivSub, R.id.ivPlus, R.id.tvQuestion, R.id.btnExit, R.id.btnSave, R.id.ivPlusQues, R.id.ivSubQues})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnExit:
            case R.id.ivBack:
                showDialogConfirm();
                break;
            case R.id.btnSave:
            case R.id.tvSave:
                if (validateForm()) {
                    saveNewTest();
                }
                break;
            case R.id.ivSub:
                etTime.setText(subTime() + "");
                break;
            case R.id.ivPlus:
                etTime.setText(plusTime() + "");
                break;
            case R.id.ivPlusQues:
                etQuestionNumber.setText(plusQuestion() + "");
                if (swAutoChoose.isChecked()) {
                    setRandomQuestion(questionNumber);
                }
                break;
            case R.id.ivSubQues:
                etQuestionNumber.setText(subQuestion() + "");
                if (swAutoChoose.isChecked()) {
                    setRandomQuestion(questionNumber);
                }
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

    private void loadQuestion() {
        listAllQuestion.clear();
        mListAllQuestion.clear();
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("subCode", TestActivity.subCode);
        mService.getAllQuestion(params).enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        Question item = response.body().get(i);
                        Question questionModel = new Question();
                        questionModel.setQuestionID(item.getQuestionID());
                        questionModel.setQuesContent(item.getQuesContent());
                        questionModel.setSubCode(item.getSubCode());
                        questionModel.setAnsA(item.getAnsA());
                        questionModel.setAnsB(item.getAnsB());
                        questionModel.setAnsC(item.getAnsC());
                        questionModel.setAnsD(item.getAnsD());
                        questionModel.setAnsCorrect(item.getAnsCorrect());
                        mListAllQuestion.add(questionModel);
                        listAllQuestion.add(questionModel);
                    }
                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {

            }
        });
    }

    private void saveNewTest() {
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("questionID", getQuestionID());
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
                        intentBroadCast.putExtra("screen", "add_test");
                        intentBroadCast.putExtra("level", Level);
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

    private boolean validateForm() {
        subjectCode = TestActivity.subCode;
        testName = etTestName.getText().toString().trim();
        if (testName.equals("")) {
            Toast.makeText(this, "Tên đề thi không được để trống", Toast.LENGTH_SHORT).show();
            etTestName.setError("Không được để trống");
            return false;
        }
        if (time == 0) {
            Toast.makeText(this, "Bạn chưa thiết lập thời gian", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tvQuestion.getText().toString().equals("")) {
            Toast.makeText(this, "Bạn chưa chọn câu hỏi", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Integer.parseInt(etQuestionNumber.getText().toString()) < mDataQuestion.size() || Integer.parseInt(etQuestionNumber.getText().toString()) > mDataQuestion.size()) {
            Toast.makeText(this, "Tổng số câu phải bằng số câu đã chọn", Toast.LENGTH_SHORT).show();
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
                setTextQuestion(mDataQuestion);
                etQuestionNumber.setText(mDataQuestion.size() + "");
            }

        }
    }

    //conver mảng question sang chuỗi
    private String getQuestionID() {
        questionID = "";
        for (int i = 0; i < mDataQuestion.size(); i++) {
            questionID += mDataQuestion.get(i).getQuestionID() + ",";
        }
        return questionID;
    }

    private void setTextQuestion(ArrayList<Question> lisQues) {
        StringBuilder question = new StringBuilder("");
        for (int i = 0; i < lisQues.size(); i++) {
            question.append("Câu " + (i + 1) + ": ");
            question.append(lisQues.get(i).getQuesContent());
        }
        tvQuestion.setText(question.toString());
    }

    private int plusTime() {
        if (time >= 120) {
            return 120;
        }
        time += 5;
        return time;
    }

    private int subTime() {
        if (time <= 0) {
            return 0;
        }
        time -= 5;
        return time;
    }

    private int plusQuestion() {
        if (questionNumber >= mListAllQuestion.size()) {
            return mListAllQuestion.size();
        } else {
            questionNumber += 1;
        }
        return questionNumber;
    }

    private int subQuestion() {
        if (questionNumber <= 0) {
            return 0;
        } else {
            questionNumber -= 1;
        }
        return questionNumber;
    }

    private void setRandomQuestion(int numberQuestion) {
        mDataQuestion.clear();
        listQuestionRandom.clear();
        Random rand = new Random();
        for (int i = 0; i < numberQuestion; i++) {
            Question question = new Question();
            try {
                int randomIndex = rand.nextInt(listAllQuestion.size());
                question = listAllQuestion.get(randomIndex);
                listAllQuestion.remove(randomIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
            listQuestionRandom.add(question);
        }
        listAllQuestion.clear();
        mDataQuestion.addAll(listQuestionRandom);
        listAllQuestion.addAll(mListAllQuestion);
        setTextQuestion(mDataQuestion);
    }

    @Override
    public void onBackPressed() {
        showDialogConfirm();
    }
}
