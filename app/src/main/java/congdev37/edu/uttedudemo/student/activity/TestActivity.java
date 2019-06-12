package congdev37.edu.uttedudemo.student.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import congdev37.edu.uttedudemo.MainActivity;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.admin.activity.AddTestActivity;
import congdev37.edu.uttedudemo.admin.activity.EditTestActivity;
import congdev37.edu.uttedudemo.model.Question;
import congdev37.edu.uttedudemo.model.Test;
import congdev37.edu.uttedudemo.response.ResponseMessage;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import congdev37.edu.uttedudemo.student.adapter.TestAdapter;
import congdev37.edu.uttedudemo.student.slide.ScreenSlideActivity;
import congdev37.edu.uttedudemo.util.ConstantKey;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.rbEasy)
    RadioButton rbEasy;
    @BindView(R.id.rbMedium)
    RadioButton rbMedium;
    @BindView(R.id.rbHard)
    RadioButton rbHard;
    @BindView(R.id.rdg_level)
    RadioGroup rdgLevel;
    @BindView(R.id.rcvTest)
    RecyclerView rcvTest;
    @BindView(R.id.tvNoHaveTest)
    TextView tvNoHaveTest;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;

    public static String subCode;
    SOService mService;
    List<Test> mDataTest;
    ArrayList<Question> mDataQuestion;
    ArrayList<Question> lisQuestion;
    ArrayList<String> arrQuesID;
    ArrayList<Boolean> arrTestStatus;
    public static String level = "1";
    public static String testID = "";
    public static String testName = "";
    boolean check;
    TestAdapter mAdapter;
    BroadcastReceiver myBroadCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        initBroadCast();
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                subCode = bundle.getString("sub_code");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        if(!MainActivity.stdCode.equals("admin")){
            ivAdd.setVisibility(View.INVISIBLE);
        }
        tvTitleToolbar.setText("Bài test");
        mDataTest = new ArrayList<>();
        mDataQuestion = new ArrayList<>();
        arrQuesID = new ArrayList<>();
        lisQuestion = new ArrayList<>();
        arrTestStatus = new ArrayList<>();
        mAdapter = new TestAdapter(mDataTest);
        mAdapter.setOnClick(new TestAdapter.OnClick() {
            @Override
            public void onItemClick(String testID, String questionID, String Name, int position) {
                mDataQuestion.clear();
                testName = Name;
                splitQuestionID(questionID);
                for (int i = 0; i < arrQuesID.size(); i++) {
                    loadQuestion(arrQuesID.get(i), testID, position);
                }
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rcvTest.setLayoutManager(gridLayoutManager);
        rcvTest.setAdapter(mAdapter);
        rbEasy.setChecked(true);
        loadTest(level);
        rdgLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mDataTest.clear();
                arrTestStatus.clear();
                if (checkedId == R.id.rbEasy) {
                    level = "1";
                } else if (checkedId == R.id.rbMedium) {
                    level = "2";
                } else {
                    level = "3";
                }
                loadTest(level);
            }
        });
    }

    private void loadTest(String Level) {
        mDataTest.clear();
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("subjectCode", subCode);
        params.put("Level", Level);
        mService.getTest(params).enqueue(new Callback<List<Test>>() {
            @Override
            public void onResponse(Call<List<Test>> call, Response<List<Test>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        Test item = response.body().get(i);
                        Test test = new Test();
                        testID = item.getTestID();
                        test.setTestID(testID);
                        test.setTestName(item.getTestName());
                        test.setLevel(item.getLevel());
                        test.setQuestionID(item.getQuestionID());
                        test.setSubjectCode(item.getSubjectCode());
                        test.setTime(item.getTime());
                        test.setCreateDay(item.getCreateDay());
                        mDataTest.add(test);
                    }
                    if (mDataTest.size() == response.body().size() && !MainActivity.stdCode.equals("admin")) {
                        checkTestStatus();
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                    if (mDataTest.size() == 0) {
                        tvNoHaveTest.setVisibility(View.VISIBLE);
                    } else {
                        tvNoHaveTest.setVisibility(View.GONE);
                    }
                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<List<Test>> call, Throwable t) {

            }
        });
    }

    private void loadQuestion(String questionID, final String testID, final int position) {
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("questionID", questionID);
        mService.getQuestion(params).enqueue(new Callback<List<Question>>() {
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
                        mDataQuestion.add(questionModel);
                    }
                    if (mDataQuestion.size() == arrQuesID.size() && !MainActivity.stdCode.equals("admin")) {
                        check = true;
                        Intent intent = new Intent(TestActivity.this, ScreenSlideActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("question", mDataQuestion);
                        bundle.putString("timer", mDataTest.get(position).getTime());
                        bundle.putInt("num_page", mDataQuestion.size());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (mDataQuestion.size() == arrQuesID.size() && MainActivity.stdCode.equals("admin")) {
                        Intent intent = new Intent(TestActivity.this, EditTestActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("question", mDataQuestion);
                        bundle.putParcelableArrayList("test", (ArrayList<? extends Parcelable>) mDataTest);
                        bundle.putString("test_name", testName);
                        bundle.putString("test_id", testID);
                        bundle.putInt("position", position);
                        bundle.putString("timer", mDataTest.get(position).getTime());
                        intent.putExtras(bundle);
                        startActivity(intent);
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

    public void checkTestStatus() {
        for (int i = 0; i < mDataTest.size(); i++) {
            loadTestStatus(mDataTest.get(i).getTestID(), i);
        }
    }

    private void loadTestStatus(String testID, final int i) {
        mService = ApiUtils.getSOService();
        Map<String, Object> params = new HashMap<>();
        params.put("studentCode", MainActivity.stdCode);
        params.put("testID", testID);
        mService.getExercise(params).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {
                        arrTestStatus.add(true);
                    } else {
                        arrTestStatus.add(false);
                    }
                    if (arrTestStatus.size() == mDataTest.size()) {
                        mDataTest.get(i).setTestStatus(arrTestStatus.get(i));
                        mAdapter.notifyDataSetChanged();
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {

            }
        });
    }

    private void splitQuestionID(String question_ID) {
        arrQuesID.clear();
        String[] arr = question_ID.split(",");
        for (String name : arr)
            arrQuesID.add(name);
    }

    @OnClick({R.id.ivBack, R.id.rbEasy, R.id.rbMedium, R.id.rbHard, R.id.rdg_level,R.id.ivAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivAdd:
                startActivity(new Intent(this, AddTestActivity.class));
                break;
        }
    }

    private void initBroadCast() {
        try {
            myBroadCast = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(ConstantKey.ACTION_NOTIFY_DATA)) {
                        level = EditTestActivity.Level + "";
                        if (level.equals("1")) {
                            rbEasy.setChecked(true);
                        } else if (level.equals("2")) {
                            rbMedium.setChecked(true);
                        } else if (level.equals("3")) {
                            rbHard.setChecked(true);
                        }else {
                            level = AddTestActivity.Level + "";
                            if (level.equals("1")) {
                                rbEasy.setChecked(true);
                            } else if (level.equals("2")) {
                                rbMedium.setChecked(true);
                            } else if (level.equals("3")) {
                                rbHard.setChecked(true);
                            }
                        }
                    }
                }
            };

            final IntentFilter filter = new IntentFilter(ConstantKey.ACTION_NOTIFY_DATA);
            registerReceiver(myBroadCast, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
