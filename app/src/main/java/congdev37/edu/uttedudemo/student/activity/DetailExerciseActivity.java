package congdev37.edu.uttedudemo.student.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.Exercise;
import congdev37.edu.uttedudemo.model.Question;
import congdev37.edu.uttedudemo.model.ResponseHistory;
import congdev37.edu.uttedudemo.response.ResponseMessage;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import congdev37.edu.uttedudemo.student.adapter.DetailExerciseAdapter;
import congdev37.edu.uttedudemo.util.ConstantKey;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailExerciseActivity extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.rcvDetailExercise)
    RecyclerView rcvDetailExercise;
    @BindView(R.id.pbLoading)
    FrameLayout pbLoading;
    @BindView(R.id.ivDelete)
    ImageView ivDelete;

    SOService mService;
    DetailExerciseAdapter mAdapter;
    Dialog dialogDelete;
    String questionID, Answer, testName;
    List<Question> mData;
    ArrayList<String> arrQuesID;
    ResponseHistory mExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_exercise);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        try {
            mAdapter = new DetailExerciseAdapter(mData);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rcvDetailExercise.setLayoutManager(linearLayoutManager);
            rcvDetailExercise.setAdapter(mAdapter);

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogDelete(mExercise.getExID());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //khởi tạo và lấy dữ liệu
    private void initData() {
        try {
            mData = new ArrayList<>();
            arrQuesID = new ArrayList<>();
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                mExercise = (ResponseHistory) bundle.getSerializable("exercise");
                questionID = mExercise.getQuestionID();
                Answer = mExercise.getAnswer();
                testName = mExercise.getTestName();
                tvTitleToolbar.setText(testName);
                splitQuestionID(questionID);
                for (int i = 0; i < arrQuesID.size(); i++) {
                    loadQuestion(arrQuesID.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //lấy danh sách câu hỏi với questionID
    private void loadQuestion(String questionID) {
        try {
            mData.clear();
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
                            mData.add(questionModel);
                        }
                        if (mData.size() == arrQuesID.size()) {
                            setInvisibleLoading();
                            addAnswer(Answer);
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

    //xóa bài làm trong lịch sử
    private void deleteExercise(String exID) {
        try {
            mService = ApiUtils.getSOService();
            Map<String, Object> params = new HashMap<>();
            params.put("exerciseID", exID);
            params.put("status", 1);
            mService.deleteExercise(params).enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {
                            Intent intentBroadCast = new Intent(ConstantKey.ACTION_NOTIFY_DATA);
                            intentBroadCast.putExtra("screen","save_test");
                            sendBroadcast(intentBroadCast);
                            dialogDelete.dismiss();
                            finish();
                        }
                    } else {
                        int statusCode = response.code();
                        if (statusCode == 404) {
                            Toast.makeText(DetailExerciseActivity.this, "Lỗi : Không thể kết nối tới máy chủ ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailExerciseActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //dialog xóa
    private void showDialogDelete(final String exID) {
        try {
            dialogDelete = new Dialog(this, R.style.Theme_Dialog);
            dialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogDelete.setContentView(R.layout.dialog_delete);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialogDelete.setCancelable(true);
            dialogDelete.setCanceledOnTouchOutside(true);

            //ánh xạ
            Button btnNo = dialogDelete.findViewById(R.id.btnNo);
            Button btnYes = dialogDelete.findViewById(R.id.btnYes);
            ImageView btnTitleClose = dialogDelete.findViewById(R.id.btnTitleClose);
            final TextView tvConfirm = dialogDelete.findViewById(R.id.tvConfirm);

            //Hiển thị text theo đúng định dạng bằng html
            tvConfirm.setText(getString(R.string.you_can_delete_exercise));

            // Chọn có xóa đơn vị
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDelete.dismiss();
                }
            });
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteExercise(exID);
                }
            });
            // Chọn không để đóng dialog
            btnTitleClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dialogDelete.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            dialogDelete.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //tách chuổi mã câu hỏi thành mảng
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

    //thêm câu trả lời cho mảng câu hỏi
    private void addAnswer(String Answer) {
        try {
            String[] arr = Answer.split(",");
            for (int i = 0; i < mData.size(); i++) {
                mData.get(i).setAnswer(arr[i]);
                if (i == mData.size() - 1) {
                    mAdapter.notifyDataSetChanged();
                }
            }
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

    //trờ về
    @OnClick({R.id.ivBack})
    public void onViewClicked() {
        finish();
    }
}
