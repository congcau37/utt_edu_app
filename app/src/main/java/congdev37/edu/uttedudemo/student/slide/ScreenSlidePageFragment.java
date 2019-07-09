package congdev37.edu.uttedudemo.student.slide;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.Question;

import static congdev37.edu.uttedudemo.util.ConstantKey.ARG_CHECK_ANSWER;
import static congdev37.edu.uttedudemo.util.ConstantKey.KEY_PAGE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenSlidePageFragment extends Fragment {

    public static ArrayList<Question> arr_Ques;
    @BindView(R.id.tvNum)
    TextView tvNum;
    @BindView(R.id.tvQuestion)
    TextView tvQuestion;
    @BindView(R.id.lnTop)
    LinearLayout lnTop;
    @BindView(R.id.ivIcon)
    ImageView ivIcon;
    @BindView(R.id.radA)
    RadioButton radA;
    @BindView(R.id.radB)
    RadioButton radB;
    @BindView(R.id.radC)
    RadioButton radC;
    @BindView(R.id.radD)
    RadioButton radD;
    @BindView(R.id.radGroup)
    RadioGroup radGroup;
    @BindView(R.id.content)
    ScrollView content;

    Unbinder unbinder;
    private int mPageNumber;
    private int checkAnswwer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        try {
            arr_Ques = new ArrayList<>();
            ScreenSlideActivity screenSlideActivity = (ScreenSlideActivity) getActivity();
            arr_Ques = screenSlideActivity.getData();
            mPageNumber = getArguments().getInt(KEY_PAGE);
            checkAnswwer = getArguments().getInt(ARG_CHECK_ANSWER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ScreenSlidePageFragment create(int pageNumber, int checkAnswer) {
        ScreenSlidePageFragment screenSlidePageFragment = new ScreenSlidePageFragment();
        Bundle bd = new Bundle();
        bd.putInt(KEY_PAGE, pageNumber);
        bd.putInt(ARG_CHECK_ANSWER, checkAnswer);
        screenSlidePageFragment.setArguments(bd);
        return screenSlidePageFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //
        try {
            tvNum.setText("Câu:" + (mPageNumber + 1));
            tvQuestion.setText(getItem(mPageNumber).getQuesContent());
            radA.setText(getItem(mPageNumber).getAnsA());
            radB.setText(getItem(mPageNumber).getAnsB());
            radC.setText(getItem(mPageNumber).getAnsC());
            radD.setText(getItem(mPageNumber).getAnsD());

            //chọn đáp án
            radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
    //                getItem(mPageNumber).choiceID = i;
                    //gán câu trả lời
                    getItem(mPageNumber).setAnswer(getChoiceFromID(i));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //lấy ra đối tượng câu hỏi hiện tại
    private Question getItem(int i) {
        return arr_Ques.get(i);
    }

    private String getChoiceFromID(int ID) {
        if (ID == R.id.radA) {
            return "A";
        } else if (ID == R.id.radB) {
            return "B";
        } else if (ID == R.id.radC) {
            return "C";
        } else if (ID == R.id.radD) {
            return "D";
        } else
            return "";
    }

    //set background cho câu trả lời đúng
    private void getCheckAns(String ans) {
        switch (ans) {
            case "A":
                radA.setBackgroundColor(Color.RED);
                break;
            case "B":
                radB.setBackgroundColor(Color.RED);
                break;
            case "C":
                radC.setBackgroundColor(Color.RED);
                break;
            default:
                radD.setBackgroundColor(Color.RED);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //kiểm tra nếu khác 0 thì đã kết thúc
        if (checkAnswwer != 0) {
            radA.setClickable(false);
            radB.setClickable(false);
            radC.setClickable(false);
            radD.setClickable(false);
            //gọi hàm set background câu trả lời đúng
            getCheckAns(getItem(mPageNumber).getAnsCorrect());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
