package congdev37.edu.uttedudemo.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.admin.adapter.QuestionAdapter;
import congdev37.edu.uttedudemo.model.Question;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import congdev37.edu.uttedudemo.student.activity.TestActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseQuestionActivity extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.rcvQuestion)
    RecyclerView rcvQuestion;
    @BindView(R.id.cbCheckedAll)
    CheckBox cbCheckedAll;
    @BindView(R.id.cbChecked)
    CheckBox cbChecked;
    @BindView(R.id.tvNoSelectQuestion)
    TextView tvNoSelectQuestion;
    @BindView(R.id.tvNumberQuestion)
    TextView tvNumberQuestion;

    SOService mService;
    Button btDone;
    QuestionAdapter mAdapter;
    ArrayList<Question> mDataQuestion, mDataAllQuestion;
    boolean checkAll, checkSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_question);
        ButterKnife.bind(this);
        initData();
        loadAllQuestion();
        initView();
    }

    //khởi tạo và lấy dữ liệu
    private void initData() {
        try {
            mDataQuestion = new ArrayList<>();
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                mDataQuestion = bundle.getParcelableArrayList("question");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // hiển thị
    private void initView() {
        try {
            mDataAllQuestion = new ArrayList<>();
            mAdapter = new QuestionAdapter(mDataAllQuestion);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rcvQuestion.setLayoutManager(layoutManager);
            rcvQuestion.setAdapter(mAdapter);
            tvTitleToolbar.setText("Câu hỏi");

            //sự kiện khi chọn check tất cả
            cbCheckedAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkAll = isChecked;
                    if (checkAll || (checkAll && checkSelected)) {
                        checkAll(checkAll);
                        tvNumberQuestion.setText(String.format(getString(R.string.question_number),mDataAllQuestion.size()));
                    } else if ((!checkAll && checkSelected)) {
                        getListSelected(checkSelected);
                        tvNumberQuestion.setText(String.format(getString(R.string.question_number),mDataQuestion.size()));
                    } else if ((checkAll && !checkSelected)) {
                        checkAll(checkAll);
                        tvNumberQuestion.setText(String.format(getString(R.string.question_number),mDataQuestion.size()));
                    } else if (!checkAll && !checkSelected) {
                        mDataAllQuestion.clear();
                        loadAllQuestion();
                        tvNumberQuestion.setText(String.format(getString(R.string.question_number),mDataQuestion.size()));
                    }
                }
            });

            //sự kiên khi chọn đã check
            cbChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkSelected = isChecked;
                    if (checkAll || (checkAll && checkSelected)) {
                        checkAll(checkAll);
                    } else if ((!checkAll && checkSelected)) {
                        getListSelected(checkSelected);
                    } else if ((checkAll && !checkSelected)) {
                        checkAll(checkAll);
                    } else if (!checkAll && !checkSelected) {
                        getListSelected(checkSelected);
                    }
                }
            });

            //hiển thị số câu đã chọn
            tvNumberQuestion.setText(String.format(getString(R.string.question_number),mDataQuestion.size()));
            //khi click bào check box item thì cộng thêm câu
            mAdapter.setmOnClick(new QuestionAdapter.OnClick() {
                @Override
                public void onItemClick(int numberQuestion) {
                    tvNumberQuestion.setText(String.format(getString(R.string.question_number),numberQuestion));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // onclick
    @OnClick({R.id.ivBack, R.id.btDone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btDone:
                returnQuestion();
                finish();
                break;
        }
    }

    //lấy danh sách toàn bộ câu hỏi từ server
    private void loadAllQuestion() {
        try {
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
                            mDataAllQuestion.add(questionModel);
                            if (mDataAllQuestion.size() == response.body().size()) {
                                checkChoose();
                            }
                        }
                        mAdapter.notifyDataSetChanged();
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

    //kiểm tra các câu check rồi cập nhật list
    private void checkChoose() {
        try {
            for (int i = 0; i < mDataAllQuestion.size(); i++) {
                for (int j = 0; j < mDataQuestion.size(); j++) {
                    if (mDataQuestion.get(j).getQuestionID().equals(mDataAllQuestion.get(i).getQuestionID())) {
                        mDataAllQuestion.get(i).setChoose(true);
                        break;
                    } else {
                        mDataAllQuestion.get(i).setChoose(false);
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //hàm check all
    private void checkAll(boolean check) {
        try {
            tvNoSelectQuestion.setVisibility(View.GONE);
            rcvQuestion.setVisibility(View.VISIBLE);
            if (check) {
                for (int i = 0; i < mDataAllQuestion.size(); i++) {
                    mDataAllQuestion.get(i).setChoose(true);
                }
                if (mDataAllQuestion.size() == 0) {
                    tvNoSelectQuestion.setVisibility(View.VISIBLE);
                    rcvQuestion.setVisibility(View.GONE);
                }
            } else {
                tvNoSelectQuestion.setVisibility(View.VISIBLE);
                rcvQuestion.setVisibility(View.GONE);
                for (int i = 0; i < mDataAllQuestion.size(); i++) {
                    mDataAllQuestion.get(i).setChoose(false);
                }
            }
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //hàm lấy ra list đã check
    private void getListSelected(boolean check) {
        try {
            if (check) {
                for (int i = mDataAllQuestion.size() - 1; i >= 0; i--) {
                    if (!mDataAllQuestion.get(i).isChoose()) {
                        mDataAllQuestion.remove(i);
                    }
                }
                if (mDataAllQuestion.size() == 0) {
                    tvNoSelectQuestion.setVisibility(View.VISIBLE);
                    rcvQuestion.setVisibility(View.GONE);
                }
            } else {
                tvNoSelectQuestion.setVisibility(View.GONE);
                rcvQuestion.setVisibility(View.VISIBLE);
                mDataAllQuestion.clear();
                loadAllQuestion();
            }
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //hàm trả trả về màn hình trước những câu hỏi đã chọn
    public void returnQuestion() {
        try {
            mDataQuestion.clear();
            for (int i = 0; i < mDataAllQuestion.size(); i++) {
                if (mDataAllQuestion.get(i).isChoose()) {
                    mDataQuestion.add(mDataAllQuestion.get(i));
                }
            }
            Intent i = new Intent();
            Bundle bd = new Bundle();
            bd.putParcelableArrayList("question", mDataQuestion);
            i.putExtras(bd);
            setResult(RESULT_OK, i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
