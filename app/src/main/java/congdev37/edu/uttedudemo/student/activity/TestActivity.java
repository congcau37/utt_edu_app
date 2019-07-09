package congdev37.edu.uttedudemo.student.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
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
import congdev37.edu.uttedudemo.HomeActivity;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.admin.activity.AddTestActivity;
import congdev37.edu.uttedudemo.admin.activity.EditTestActivity;
import congdev37.edu.uttedudemo.model.Question;
import congdev37.edu.uttedudemo.model.Test;
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
    @BindView(R.id.pbLoading)
    FrameLayout pbLoading;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;

    public static String subCode;
    SOService mService;
    TestAdapter mAdapter;
    List<Test> mDataTest;
    ArrayList<Question> mDataQuestion;
    ArrayList<Question> lisQuestion;
    ArrayList<String> arrQuesID;
    ArrayList<Test> arrTestStatus;
    public static String level = "1";
    public static String testName = "";
    boolean check;
    BroadcastReceiver myBroadCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        initData();
        initView();
        initBroadCast();
    }

    private void initData() {
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                try {
                    level = "1";
                    subCode = bundle.getString("sub_code");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        try {
            if (!HomeActivity.stdCode.equals("admin")) {
                ivAdd.setVisibility(View.INVISIBLE);
            }
            tvTitleToolbar.setText("Bài test");
            mDataTest = new ArrayList<>();
            mDataQuestion = new ArrayList<>();
            arrQuesID = new ArrayList<>();
            lisQuestion = new ArrayList<>();
            arrTestStatus = new ArrayList<>();
            mAdapter = new TestAdapter(mDataTest);
            //click bào bài test
            mAdapter.setOnClick(new TestAdapter.OnClick() {
                @Override
                public void onItemClick(String test_ID, String questionID, String Name, int position) {
                    mDataQuestion.clear();
                    testName = Name;
                    //tách chuỗi
                    splitQuestionID(questionID);
                    //lấy lần lượt từng câu hỏi theo id rồi thêm vào mảng
                    for (int i = 0; i < arrQuesID.size(); i++) {
                        loadQuestion(arrQuesID.get(i), test_ID, position);
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
                    if (checkedId == R.id.rbEasy) {
                        level = "1";
                    } else if (checkedId == R.id.rbMedium) {
                        level = "2";
                    } else {
                        level = "3";
                    }
                    setVisibleLoading();
                    loadTest(level);
                }
            });

            //
            swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadTest(level);
                            swiperefresh.setRefreshing(false);
                        }
                    }, 1500);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mục đính của methob: lấy ra danh sach bài test thuộc cấp độ đã chọn
     *
     * @param Level:cấp độ
     * @Create_by: trand
     * @Date: 7/3/2019
     */
    private void loadTest(String Level) {
        try {
            mDataTest.clear();
            mService = ApiUtils.getSOService();
            Map<String, Object> params = new HashMap<>();
            params.put("subjectCode", subCode);
            params.put("studentCode", HomeActivity.stdCode);
            params.put("Level", Level);
            mService.getTest(params).enqueue(new Callback<List<Test>>() {
                @Override
                public void onResponse(Call<List<Test>> call, Response<List<Test>> response) {
                    if (response.isSuccessful()) {
                        for (int i = 0; i < response.body().size(); i++) {
                            Test item = response.body().get(i);
                            Test test = new Test();
                            test.setTestID(item.getTestID());
                            test.setTestName(item.getTestName());
                            test.setTime(item.getTime());
                            test.setQuestionID(item.getQuestionID());
                            test.setStudentCode(item.getStudentCode());
                            test.setStudentName(item.getStudentName());
                            test.setExerciseStatus(item.getExerciseStatus());
                            mDataTest.add(test);
                        }
                        //nếu là sinh viên
                        if (mDataTest.size() == response.body().size() && !HomeActivity.stdCode.equals("admin")) {
                            if (mDataTest.size() == 0) {
                                setInvisibleLoading();
                                tvNoHaveTest.setVisibility(View.VISIBLE);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                //gọi hàm kiểm tra trạng thái bài test
                                checkTestStatus();
                                tvNoHaveTest.setVisibility(View.GONE);
                            }
                            //admin
                        } else {
                            setInvisibleLoading();
                            if (mDataTest.size() == 0) {
                                tvNoHaveTest.setVisibility(View.VISIBLE);
                            } else {
                                tvNoHaveTest.setVisibility(View.GONE);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        int statusCode = response.code();
                    }
                }

                @Override
                public void onFailure(Call<List<Test>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //kiểm tra trạng thái đề thi đã làm hay chưa
    public void checkTestStatus() {
        try {
            for (int i = 0; i < mDataTest.size(); i++) {
                if (mDataTest.get(i).getStudentCode() != null) {
                    if (mDataTest.get(i).getExerciseStatus().equals("0")){
                        mDataTest.get(i).setTestStatus(true);
                    }
                }
            }
            setInvisibleLoading();
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadQuestion(String questionID, final String test_ID, final int position) {
        try {
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
                        //quyền sinh viên
                        //lấy xong câu hỏi chuyển qua màn hình làm bài
                        if (mDataQuestion.size() == arrQuesID.size() && !HomeActivity.stdCode.equals("admin")) {
                            check = true;
                            Intent intent = new Intent(TestActivity.this, ScreenSlideActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("question", mDataQuestion);
                            bundle.putString("timer", mDataTest.get(position).getTime());
                            bundle.putString("test_name", testName);
                            bundle.putString("test_id", test_ID);
                            bundle.putString("list_question_ID", mDataTest.get(position).getQuestionID());
                            bundle.putInt("num_page", mDataQuestion.size());
                            intent.putExtras(bundle);
                            startActivity(intent);
                            //quyền admin
                            //chuyển qua màn hình sửa
                        } else if (mDataQuestion.size() == arrQuesID.size() && HomeActivity.stdCode.equals("admin")) {
                            Intent intent = new Intent(TestActivity.this, EditTestActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("question", mDataQuestion);
                            bundle.putParcelableArrayList("test", (ArrayList<? extends Parcelable>) mDataTest);
                            bundle.putString("test_name", testName);
                            bundle.putString("test_id", test_ID);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void splitQuestionID(String question_ID) {
        try {
            arrQuesID.clear();
            String[] arr = question_ID.split(",");
            for (String name : arr)
                arrQuesID.add(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ivBack, R.id.rbEasy, R.id.rbMedium, R.id.rbHard, R.id.rdg_level, R.id.ivAdd})
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

    //khởi tạo BroadCast
    private void initBroadCast() {
        try {
            myBroadCast = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(ConstantKey.ACTION_NOTIFY_DATA)) {
                        mDataTest.clear();
                        //lấy ra cấp độ
                        int level_current = intent.getIntExtra("level", 0);
                        //lấy ra màn hình
                        String screen = intent.getStringExtra("screen");
                        //kiểm tra màn hình nào trả về
                        if (screen.equals("add_test")) {
                            level_current = AddTestActivity.Level;
                        } else if (screen.equals("edit_test")) {
                            level_current = EditTestActivity.Level;
                        } else {
                            level_current = Integer.parseInt(level); //level màn hình hiện tại
                        }
                        if (!String.valueOf(level_current).equals(level)) {
                            switch (level_current + "") {
                                case "1":
                                    rbEasy.setChecked(true);
                                    break;
                                case "2":
                                    rbMedium.setChecked(true);
                                    break;
                                case "3":
                                    rbHard.setChecked(true);
                                    break;
                            }
                        } else {
                            setVisibleLoading();
                            loadTest(String.valueOf(level_current));
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

    public void setInvisibleLoading() {
        pbLoading.setVisibility(View.GONE);
    }

    public void setVisibleLoading() {
        pbLoading.setVisibility(View.VISIBLE);
    }
}
