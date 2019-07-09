package congdev37.edu.uttedudemo.admin.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import congdev37.edu.uttedudemo.R;
import congdev37.edu.uttedudemo.admin.adapter.StatisticalAdapter;
import congdev37.edu.uttedudemo.model.Statistical;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import congdev37.edu.uttedudemo.util.TimeHelper;
import congdev37.edu.uttedudemo.util.TimeInterval;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticalChartActivity extends AppCompatActivity {

    @BindView(R.id.rbToday)
    RadioButton rbToday;
    @BindView(R.id.rbMonth)
    RadioButton rbMonth;
    @BindView(R.id.rbYear)
    RadioButton rbYear;
    @BindView(R.id.rdg_date)
    RadioGroup rdgDate;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.piechart)
    PieChart pieChart;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    TimeHelper timeHelper;
    TimeInterval timeInterval;
    @BindView(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.pbLoading)
    FrameLayout pbLoading;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.lnNote)
    LinearLayout lnNote;
    StatisticalAdapter mAdapter;


    List<Statistical> mData;
    @BindView(R.id.rcvExercise)
    RecyclerView rcvExercise;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.tvNoExercise)
    TextView tvNoExercise;
    private int[] yData = new int[2];
    String subjectCode, subjectName;
    SOService mService;
    int totalExercise, amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical_chart);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    //ánh xạ
    private void initView() {
        try {
            rbToday.setChecked(true);
            timeHelper = new TimeHelper();
            timeInterval = TimeHelper.getInstance().getToday();
            loadData(subjectCode, TimeHelper.getInstance().getToday().getStartTimeToServer(), TimeHelper.getInstance().getToday().getEndTimeToServer());
            rdgDate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.rbToday:
                            loadData(subjectCode, TimeHelper.getInstance().getToday().getStartTimeToServer(), TimeHelper.getInstance().getToday().getEndTimeToServer());
                            break;
                        case R.id.rbMonth:
                            loadData(subjectCode, TimeHelper.getInstance().getThisMonth().getStartTimeToServer(), TimeHelper.getInstance().getThisMonth().getEndTimeToServer());
                            break;
                        case R.id.rbYear:
                            loadData(subjectCode, TimeHelper.getInstance().getAll().getStartTimeToServer(), TimeHelper.getInstance().getAll().getEndTimeToServer());
                            break;
                    }
                }
            });

            mAdapter = new StatisticalAdapter(mData);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rcvExercise.setLayoutManager(linearLayoutManager);
            rcvExercise.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //khởi tạo dữ liệu
    private void initData() {
        try {
            mData = new ArrayList<>();
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                subjectCode = bundle.getString("sub_code");
                subjectName = bundle.getString("sub_name");
                loadAllData();
            }
            displayStatistical();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //hiển thị dữ liệu lên biểu đồ
    private void displayStatistical() {
        try {
            addDataSet();
            pieChart.setRotationEnabled(true);
            pieChart.setHoleRadius(25);
            pieChart.setTransparentCircleAlpha(0);
            pieChart.setCenterText("Bài làm");
            pieChart.setCenterTextSize(16);
            pieChart.animateXY(1400, 1400);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //thêm dữ liệu
    private void addDataSet() {
        try {
            ArrayList<PieEntry> yEntrys = new ArrayList<>();

            for (int i = 0; i < yData.length; i++) {
                yEntrys.add(new PieEntry(yData[i], i));
            }

            //khởi tạo data set
            PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
            pieDataSet.setSliceSpace(2);
            pieDataSet.setValueTextSize(12);

            //thêm màu
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(ContextCompat.getColor(this, R.color.colorPrimary));
            colors.add(ContextCompat.getColor(this, R.color.colorGrayLight));

            pieDataSet.setColors(colors);

            //add legend to chart
            Legend legend = pieChart.getLegend();
            legend.setForm(Legend.LegendForm.CIRCLE);
            legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

            //create pie data object
            PieData pieData = new PieData(pieDataSet);
            pieData.setValueFormatter(new DefaultValueFormatter(0));
            pieChart.setData(pieData);
            pieChart.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //lấy dữ liệu từ server
    private void loadData(String subjectCode, String startDay, String endDay) {
        try {
            mData.clear();
            amount = 0;
            mService = ApiUtils.getSOService();
            Map<String, Object> params = new HashMap<>();
            params.put("subjectCode", subjectCode);
            params.put("startDay", startDay);
            params.put("endDay", endDay);
            mService.statistical(params).enqueue(new Callback<List<Statistical>>() {
                @Override
                public void onResponse(Call<List<Statistical>> call, Response<List<Statistical>> response) {
                    if (response.isSuccessful()) {
                        for (int i = 0; i < response.body().size(); i++) {
                            Statistical item = response.body().get(i);
                            Statistical statistical = new Statistical();
                            statistical.setStudentCode(item.getStudentCode());
                            statistical.setExDay(item.getExDay());
                            mData.add(statistical);
                        }
                        if (mData.size() == 0) {
                            rcvExercise.setVisibility(View.GONE);
                            tvNoExercise.setVisibility(View.VISIBLE);
                        } else {
                            tvNoExercise.setVisibility(View.GONE);
                            rcvExercise.setVisibility(View.VISIBLE);
                            mAdapter.notifyDataSetChanged();
                        }
                        amount = response.body().size();
                        yData[0] = amount;
                        yData[1] = totalExercise - amount;
                        displayStatistical();
                    } else {
                        int statusCode = response.code();
                    }
                }

                @Override
                public void onFailure(Call<List<Statistical>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAllData() {
        try {
            mService = ApiUtils.getSOService();
            Map<String, Object> params = new HashMap<>();
            params.put("subjectCode", subjectCode);
            params.put("startDay", "");
            params.put("endDay", "");
            mService.statistical(params).enqueue(new Callback<List<Statistical>>() {
                @Override
                public void onResponse(Call<List<Statistical>> call, Response<List<Statistical>> response) {
                    if (response.isSuccessful()) {
                        totalExercise = response.body().size();
                        if (totalExercise == 0) {
                            setInvisibleLoading();
                            tvNoData.setVisibility(View.VISIBLE);
                            lnNote.setVisibility(View.GONE);
                            pieChart.setVisibility(View.GONE);
                        } else {
                            tvNoData.setVisibility(View.GONE);
                            setInvisibleLoading();
                            lnNote.setVisibility(View.VISIBLE);
                            pieChart.setVisibility(View.VISIBLE);
                        }
                    } else {
                        int statusCode = response.code();
                    }
                }

                @Override
                public void onFailure(Call<List<Statistical>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }

    public void setInvisibleLoading() {
        pbLoading.setVisibility(View.GONE);
    }

}
