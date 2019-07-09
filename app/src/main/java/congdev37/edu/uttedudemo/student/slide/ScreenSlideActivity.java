package congdev37.edu.uttedudemo.student.slide;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.model.Question;
import congdev37.edu.uttedudemo.student.activity.TestDoneActivity;
import congdev37.edu.uttedudemo.student.adapter.CheckAnswerAdapter;

public class ScreenSlideActivity extends FragmentActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private ArrayList<Question> listQuestion;
    private TextView tvKiemTra, tvTimer, tvXemDiem;
    private CheckAnswerAdapter checkAnswerAdapter;
    private GridView gvCheckAnswer;
    private Button btnCloseGrv, btnFinishGrv;
    private ImageView imgBack;

    public static String testID = "";
    public static String testName = "";
    public static String questionID = "";
    private CounterClass timer;
    private int chekAns = 0;
    private int totalTimemer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initView() {
        try {
            mPager = (ViewPager) findViewById(R.id.pager);
            tvTimer = (TextView) findViewById(R.id.tvTimer);
            tvKiemTra = (TextView) findViewById(R.id.tvKiemTra);
            imgBack = (ImageView) findViewById(R.id.ivBack);
            tvXemDiem = (TextView) findViewById(R.id.tvScore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvent() {
        try {
            //trờ về
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogExit();
                }
            });
            //khởi tạo pager adapter
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);
            mPager.setPageTransformer(true, new ZoomOutPageTransformer());
            //gọi hàm lấy danh sách câu hỏi chuyển sang
            getDataQuestion();
            //kiểm tra
            tvKiemTra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //gọi hàm hiển thị dilog danh sach câu đã làm và chưa làm
                    checkAnswer();
                }
            });
            //xem điểm đóng màn hình hiện tại và đi tới kết quả
            tvXemDiem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    Intent intent1 = new Intent(ScreenSlideActivity.this, TestDoneActivity.class);
                    startActivity(intent1);
                }
            });

            //khởi tạo thời gian đếm ngược
            timer = new CounterClass(totalTimemer * 60 * 1000, 1000);
            //bắt đầu
            timer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //lấy danh sách câu hỏi gửi sang
    private void getDataQuestion() {
        try {
            final Intent intent = getIntent();
            final Bundle bundle = intent.getExtras();
            listQuestion = new ArrayList<Question>();
            listQuestion = bundle.getParcelableArrayList("question");
            totalTimemer = Integer.parseInt(bundle.getString("timer"));
            testID = bundle.getString("test_id");
            testName = bundle.getString("test_name");
            questionID = bundle.getString("list_question_ID");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    //trả về danh sach câu hỏi
    public ArrayList<Question> getData() {
        return listQuestion;
    }

    //trờ về
    @Override
    public void onBackPressed() {
        try {
            if (mPager.getCurrentItem() == 0) {
                dialogExit();
            } else {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //dialog thoát
    public void dialogExit() {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(ScreenSlideActivity.this);
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn có muốn hủy bài không");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    timer.cancel();
                    finish();
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * class adapter pager
     * @Create_by: trand
     * @Date: 7/3/2019
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //lấy ra item trả về là fragment
        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position, chekAns);
        }

        //lấy số trang
        @Override
        public int getCount() {
            final Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            int numpage = bundle.getInt("num_page");
            return numpage;
        }
    }

    //hiệu ứng chuyển tiếp
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }
                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    //danh sách câu trả lời
    public void checkAnswer() {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.check_dialog_choice);
            dialog.setTitle("Danh sách câu trả lời");
            gvCheckAnswer = (GridView) dialog.findViewById(R.id.grv_choice);
            btnCloseGrv = (Button) dialog.findViewById(R.id.btn_close_grv);
            btnFinishGrv = (Button) dialog.findViewById(R.id.btn_finish_grv);
            checkAnswerAdapter = new CheckAnswerAdapter(getApplication(), R.layout.item_dialog_choice, listQuestion);
            gvCheckAnswer.setAdapter(checkAnswerAdapter);
            //click vào item câu
            gvCheckAnswer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //trở về page tại câu đã chọn
                    mPager.setCurrentItem(i);
                    dialog.dismiss();
                }
            });
            btnCloseGrv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            btnFinishGrv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timer.cancel();
                    endTest();
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //kết thúc
    public void endTest() {
        try {
            chekAns = 1;
            mPager.setCurrentItem(0);
            tvKiemTra.setVisibility(View.GONE);
            tvXemDiem.setVisibility(View.VISIBLE);
            tvStatus.setText("Kết thúc");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //lớp xử lý đếm ngược
    public class CounterClass extends CountDownTimer {

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String countTime = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
            tvTimer.setText(countTime); //SetText cho textview hiện thị thời gian.
        }

        @Override
        public void onFinish() {
            try {
                tvTimer.setText("00:00");
                Intent intent = new Intent(ScreenSlideActivity.this, TestDoneActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
