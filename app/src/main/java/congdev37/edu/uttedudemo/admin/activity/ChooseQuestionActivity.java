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
    @BindView(R.id.btDone)

    Button btDone;
    QuestionAdapter mAdapter;
    ArrayList<Question> mDataQuestion, mDataAllQuestion;
    SOService mService;
    boolean checkAll, checkSelected;
    @BindView(R.id.tvNumberQuestion)
    TextView tvNumberQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_question);
        ButterKnife.bind(this);
        initData();
        loadQuestion();
        initView();
    }

    private void initData() {
        mDataQuestion = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mDataQuestion = bundle.getParcelableArrayList("question");
        }
    }

    private void initView() {
        mDataAllQuestion = new ArrayList<>();
        mAdapter = new QuestionAdapter(mDataAllQuestion);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvQuestion.setLayoutManager(layoutManager);
        rcvQuestion.setAdapter(mAdapter);
        tvTitleToolbar.setText("Câu hỏi");

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
                    loadQuestion();
                    tvNumberQuestion.setText(String.format(getString(R.string.question_number),mDataQuestion.size()));
                }
            }
        });

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

        tvNumberQuestion.setText(String.format(getString(R.string.question_number),mDataQuestion.size()));
        mAdapter.setmOnClick(new QuestionAdapter.OnClick() {
            @Override
            public void onItemClick(int numberQuestion) {
                tvNumberQuestion.setText(String.format(getString(R.string.question_number),numberQuestion));
            }
        });
    }

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

    private void loadQuestion() {
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
    }

    private void checkChoose() {
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
    }

    private void checkAll(boolean check) {
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
    }

    private void getListSelected(boolean check) {
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
            loadQuestion();
        }
        mAdapter.notifyDataSetChanged();
    }

    public void returnQuestion() {
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
    }
}
