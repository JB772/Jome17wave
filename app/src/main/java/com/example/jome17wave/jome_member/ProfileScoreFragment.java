package com.example.jome17wave.jome_member;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jome17wave.Common;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.R;
import com.example.jome17wave.task.CommonTask;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ProfileScoreFragment extends Fragment {
    private static final String TAG = "ProfileScoreFragment";
    private MainActivity activity;
    private ImageView imageStar5, imageStar4, imageStar3, imageStar2, imageStar1;
    private TextView tvDisplayScoreCount;
    private CommonTask scoreTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity)getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_score, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("評價");
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        imageStar5 = view.findViewById(R.id.imageStar5);
        imageStar4 = view.findViewById(R.id.imageStar4);
        imageStar3 = view.findViewById(R.id.imageStar3);
        imageStar2 = view.findViewById(R.id.imageStar2);
        imageStar1 = view.findViewById(R.id.imageStar1);
        tvDisplayScoreCount = view.findViewById(R.id.tvDisplayScoreCount);
        imageStar5.setVisibility(View.GONE);
        imageStar4.setVisibility(View.GONE);
        imageStar3.setVisibility(View.GONE);
        imageStar2.setVisibility(View.GONE);
        imageStar1.setVisibility(View.GONE);
        tvDisplayScoreCount.setVisibility(View.GONE);

        PieChart scorePieChart = view.findViewById(R.id.scorePieChart);
        /* 設定可否旋轉 */
        scorePieChart.setRotationEnabled(true);
        /* 設定圓心文字 */
        scorePieChart.setCenterText("UrScore");
        /* 設定圓心文字大小 */
        scorePieChart.setCenterTextSize(25);

        Description description = new Description();
        description.setText("評價統計圖");
        description.setTextSize(20);
        scorePieChart.setDescription(description);
        
        scorePieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry scorePieEntry = (PieEntry) e;
                imageStar5.setVisibility(View.GONE);
                imageStar4.setVisibility(View.GONE);
                imageStar3.setVisibility(View.GONE);
                imageStar2.setVisibility(View.GONE);
                imageStar1.setVisibility(View.GONE);
                tvDisplayScoreCount.setVisibility(View.GONE);
                switch (scorePieEntry.getLabel()){
                    case "5星":
                        imageStar5.setVisibility(View.VISIBLE);
                        tvDisplayScoreCount.setText(String.valueOf(scorePieEntry.getValue()) + "次");
                        break;
                    case "4星":
                        imageStar4.setVisibility(View.VISIBLE);
                        tvDisplayScoreCount.setText(String.valueOf(scorePieEntry.getValue()) + "次");
                        break;
                    case "3星":
                        imageStar3.setVisibility(View.VISIBLE);
                        tvDisplayScoreCount.setText(String.valueOf(scorePieEntry.getValue()) + "次");
                        break;
                    case "2星":
                        imageStar2.setVisibility(View.VISIBLE);
                        tvDisplayScoreCount.setText(String.valueOf(scorePieEntry.getValue()) + "次");
                        break;
                    case "1星":
                        imageStar1.setVisibility(View.VISIBLE);
                        tvDisplayScoreCount.setText(String.valueOf(scorePieEntry.getValue()) + "次");
                        break;
                    default:
                        break;
                }
                tvDisplayScoreCount.setVisibility(View.VISIBLE);
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
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
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
            Navigation.findNavController(tvDisplayScoreCount).popBackStack();
        }
        return true;
    }

    public List<PieEntry> getScoreEntries() {
        List<Map<String, String>> scoreMaps = new ArrayList<>();
        if(Common.networkConnected(activity)){
            String url = Common.URL_SERVER + "jome_member/LoginServlet";
            String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
            JomeMember mainMember = new Gson().fromJson(memberStr, JomeMember.class);
            String memberId = mainMember.getMemberId();
            String jsonIn = "";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "scoreCounts");
            jsonObject.addProperty("memberId", memberId);
            scoreTask = new CommonTask(url, jsonObject.toString());
            try {
               jsonIn =  scoreTask.execute().get();
               jsonObject = new Gson().fromJson(jsonIn, JsonObject.class);
               jsonIn = jsonObject.get("scoreCounts").getAsString();
                Type listType = new TypeToken<List<Map<String, String>>>(){}.getType();
                scoreMaps = new Gson().fromJson(jsonIn, listType);
            } catch (ExecutionException e) {
                Log.e(TAG, e.toString());
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }
        }
        List<PieEntry> scoreEntries = new ArrayList<>();
        for (Map<String, String> scoreMap: scoreMaps){
            for (String key : scoreMap.keySet()){
                String labelTitle = "";
                switch (key){
                    case "5":
                        labelTitle = "5星";
                        break;
                    case "4":
                        labelTitle = "4星";
                        break;
                    case "3":
                        labelTitle = "3星";
                        break;
                    case "2":
                        labelTitle = "2星";
                        break;
                    case "1":
                        labelTitle = "1星";
                        break;
                }
                scoreEntries.add(new PieEntry(Integer.valueOf(scoreMap.get(key)), labelTitle));
            }
//            scoreEntries.add(new PieEntry(10 , "1Star"));
        }

        return scoreEntries;
    }

}