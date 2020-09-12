package com.example.jome17wave.jome_member;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jome17wave.MainActivity;
import com.example.jome17wave.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ProfileScoreFragment extends Fragment {
    private static final String TAG = "ProfileScoreFragment";
    private MainActivity activity;
    private TextView star5Count, star4Count, star3Count, star2Count, star1Count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity)getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_score, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("累積評價");
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        star5Count = view.findViewById(R.id.star5Count);
        star4Count = view.findViewById(R.id.star4Count);
        star3Count = view.findViewById(R.id.star3Count);
        star2Count = view.findViewById(R.id.star2Count);
        star1Count = view.findViewById(R.id.star1Count);

        PieChart scorePieChart = view.findViewById(R.id.scorePieChart);
        /* 設定可否旋轉 */
        scorePieChart.setRotationEnabled(true);
        /* 設定圓心文字 */
        scorePieChart.setCenterText("UrScore");
        /* 設定圓心文字大小 */
        scorePieChart.setCenterTextSize(25);

        Description description = new Description();
//        description.setText("Car Sales in Taiwan");
        description.setTextSize(25);
        scorePieChart.setDescription(description);
        
        scorePieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                
            }

            @Override
            public void onNothingSelected() {

            }
        });

        /* 取得各評等資料 */
        List<PieEntry> pieEntries = getScoreEntries();

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Score");
        pieDataSet.setValueTextColor(Color.BLUE);
        pieDataSet.setValueTextSize(20);
        //圓餅各塊之間的距離
        pieDataSet.setSliceSpace(5);

        /* 使用官訂顏色範本，顏色不能超過5種，否則官定範本要加顏色 */
        //setColors可以自己建立int[]來放顏色的id
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        scorePieChart.setData(pieData);
        scorePieChart.invalidate();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.member_center_tool_bar, menu);
        menu.findItem(R.id.member_settin_item).setVisible(false);
        menu.findItem(R.id.member_check_item).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Navigation.findNavController(star5Count).popBackStack();
        }
        return true;
    }

    public List<PieEntry> getScoreEntries() {
        List<PieEntry> scoreEntries = new ArrayList<>();
        scoreEntries.add(new PieEntry(10, "5Star"));
        scoreEntries.add(new PieEntry(8, "4Star"));
        scoreEntries.add(new PieEntry(5, "3Star"));
        scoreEntries.add(new PieEntry(2, "2Star"));
        scoreEntries.add(new PieEntry(1 , "1Star"));
        return scoreEntries;
    }

}