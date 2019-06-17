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

    private static int NUM_PAGES;
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
    private CounterClass timer;
    private int numExam, chekAns = 0;
    private int totalTimemer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        ButterKnife.bind(this);
        initControls();
        initEvent();
    }

    private void initEvent() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogExit();
            }
        });
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        final Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        listQuestion = new ArrayList<Question>();
        listQuestion = bundle.getParcelableArrayList("question");
        totalTimemer = Integer.parseInt(bundle.getString("timer"));
        tvKiemTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
            }
        });
        tvXemDiem = (TextView) findViewById(R.id.tvScore);
        tvXemDiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent1 = new Intent(ScreenSlideActivity.this, TestDoneActivity.class);
                startActivity(intent1);
            }
        });

        timer = new CounterClass(totalTimemer * 60 * 1000, 1000);
        tvTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        timer.start();
    }

    private void initControls() {
        mPager = (ViewPager) findViewById(R.id.pager);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        tvKiemTra = (TextView) findViewById(R.id.tvKiemTra);
        imgBack = (ImageView) findViewById(R.id.ivBack);
    }

    public ArrayList<Question> getData() {
        return listQuestion;
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            dialogExit();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void dialogExit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ScreenSlideActivity.this);
//        builder.setIcon(R.drawable.exit);
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
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position, chekAns);
        }

        @Override
        public int getCount() {
            final Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            int numpage = bundle.getInt("num_page");
            return numpage;
        }
    }

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

    public void checkAnswer() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.check_dialog_choice);
        dialog.setTitle("Danh sách câu trả lời");
        gvCheckAnswer = (GridView) dialog.findViewById(R.id.grv_choice);
        btnCloseGrv = (Button) dialog.findViewById(R.id.btn_close_grv);
        btnFinishGrv = (Button) dialog.findViewById(R.id.btn_finish_grv);
        checkAnswerAdapter = new CheckAnswerAdapter(getApplication(), R.layout.item_dialog_choice, listQuestion);
        gvCheckAnswer.setAdapter(checkAnswerAdapter);
        gvCheckAnswer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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
                result();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void result() {
        chekAns = 1;
        mPager.setCurrentItem(0);
        tvKiemTra.setVisibility(View.GONE);
        tvXemDiem.setVisibility(View.VISIBLE);
        tvStatus.setText("Kết thúc");
    }

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
            tvTimer.setText("00:00");
            Intent intent = new Intent(ScreenSlideActivity.this, TestDoneActivity.class);
            startActivity(intent);

        }
    }


}
