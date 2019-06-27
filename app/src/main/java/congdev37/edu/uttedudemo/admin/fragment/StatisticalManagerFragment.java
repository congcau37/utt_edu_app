package congdev37.edu.uttedudemo.admin.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import congdev37.edu.uttedudemo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticalManagerFragment extends Fragment {

    Unbinder unbinder;
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

    private int[] yData = {12, 30};
    private String[] xData = {"bài làm của môn", "tổng bài làm"};


    public StatisticalManagerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistical_manager, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Bài làm");
        pieChart.setCenterTextSize(16);
        addDataSet();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
//                int pos1 = e.toString().indexOf("(sum): ");
//                String quantity = e.toString().substring(pos1 + 1);
//
//                for (int i = 0; i < yData.length; i++) {
//                    if (yData[i] == Integer.parseInt(quantity)) {
//                        pos1 = i;
//                        break;
//                    }
//                }
//                String employee = xData[pos1 + 1];
//                Toast.makeText(getContext(), "Employee " + employee + "\n" + "Sales: $" + quantity + "K", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void addDataSet() {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
//        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry(yData[i], i));
        }

//        for (int i = 1; i < xData.length; i++) {
//            xEntrys.add(xData[i]);
//        }

        //khởi tạo data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "số lượng bài làm");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //thêm màu
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
