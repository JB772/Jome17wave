package com.example.jome17wave.jome_message;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jome17wave.Common;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.FriendListBean;
import com.example.jome17wave.jome_Bean.JoGroupBean;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.jome_Bean.ScoreBean;
import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.task.CommonTask;
import com.example.jome17wave.task.GroupImageTask;
import com.example.jome17wave.task.MemberImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RatingPageFragment extends Fragment {
    private String TAG = "TAG_RatingPage";
    private MainActivity activity;
    private ImageView ivRatedGroupImage;
    private TextView tvRatedGroupName;
    private Button btRatingSubmit;
    private Bitmap bitmap;
    private RecyclerView rvRatingList;
    private List<MemberImageTask> imageTasks;
    private JoGroupBean groupBean;
    private List<ScoreBean> ratings, ratingsResult;
    private Bitmap groupImageBitmap;
    private CommonTask getRatingsTask, submitRatedTask, getGroupBeanTask;


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
        return inflater.inflate(R.layout.fragment_rating_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.ratingPageTitle);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //左上角返回箭頭

        ivRatedGroupImage = view.findViewById(R.id.ivRatedGroupImage);
        tvRatedGroupName = view.findViewById(R.id.tvRatedGroupName);
        btRatingSubmit = view.findViewById(R.id.btRatingSubmit);

        //取得相關資料
        groupBean = getGroupBean();
        showGroupInfo(groupBean);
        ratings = getRatings(groupBean.getGroupId());
        showRatings(ratings);



        //按下"確認送出"按鈕
        btRatingSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Common.URL_SERVER + "/jome_member/GroupOperateServlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "updateScoreList");
                jsonObject.addProperty("ratingsResult", ratingsResult.toString());
                String jsonOut = jsonObject.toString();
                submitRatedTask = new CommonTask(url, jsonOut);
                try {
                    String inStr = submitRatedTask.execute().get();
                    JsonObject jsonIn = new Gson().fromJson(inStr, JsonObject.class);
                    int resultCode = jsonIn.get("resultCode").getAsInt();
                    if (resultCode == 1){
                        Navigation.findNavController(v).popBackStack();
                        Common.showToast(activity, R.string.ratingsFinished);
                    }
                }catch (Exception e){

                }
            }
        });

    }

    private void showRatings(List<ScoreBean> ratings) {
        if (ratings == null || ratings.isEmpty()){
            Common.showToast(activity, R.string.no_information_found);
        }else {
            RatingPageFragment.RatingPageAdapter ratingPageAdapter =
                    (RatingPageFragment.RatingPageAdapter)rvRatingList.getAdapter();
            // 如果bookAdapter不存在就建立新的，否則續用舊有的
            if (ratingPageAdapter == null){
                rvRatingList.setAdapter(new RatingPageFragment.RatingPageAdapter(activity, ratings));
            }else {
                ratingPageAdapter.setRatings(ratings);
                ratingPageAdapter.notifyDataSetChanged();
            }
        }
    }

    private List<ScoreBean> getRatings(String groupId) {
        List<ScoreBean> ratings = new ArrayList<>();
        if (Common.networkConnected(activity)){
            String url = Common.URL_SERVER + "/jome_member/GroupOperateServlet";
            JsonObject jsonObject = new JsonObject();

            String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
            JomeMember member = new Gson().fromJson(memberStr,JomeMember.class);
            String memberId = member.getMember_id();

            if (memberId != null){
                jsonObject.addProperty("action", "getRatings");
                jsonObject.addProperty("memberId", memberId);
                jsonObject.addProperty("groupId", groupId);
            }
            String jsonOut = jsonObject.toString();
            getRatingsTask = new CommonTask(url, jsonOut);
            try {
                String inStr = getRatingsTask.execute().get();
                Log.d(TAG, "inStr: " + inStr);
                JsonObject jsonIn = new Gson().fromJson(inStr, JsonObject.class);
                String ratingsStr = jsonIn.get("ratings").getAsString();
                Type listType = new TypeToken<List<ScoreBean>>(){}.getType();
                ratings = new Gson().fromJson(ratingsStr, listType);
                return ratings;
            }catch (Exception e){
                Log.e(TAG, e.toString());
            }
        }else {
            Common.showToast(activity, R.string.no_network_connection_available);
        }

        return  null;
    }

    private void showGroupInfo(JoGroupBean groupBean) {
        String url = Common.URL_SERVER + "jome_member/GroupOperateServlet";
        int imageSize = getResources().getDisplayMetrics().widthPixels / 2;
        if (Common.networkConnected(activity)){
            //抓揪團的圖片
            try {
                groupImageBitmap = new GroupImageTask(url, groupBean.getGroupId(), imageSize).execute().get();
                if (groupImageBitmap != null){
                    ivRatedGroupImage.setImageBitmap(groupImageBitmap);
                }else {
                    ivRatedGroupImage.setImageResource(R.drawable.no_image);
                }
            }catch (Exception e){
                Log.e(TAG, e.toString());
            }

            tvRatedGroupName.setText(groupBean.getGroupName());
        }
    }

    private JoGroupBean getGroupBean() {
        Bundle bundle = getArguments();
        if (bundle != null){
            String groupId = (String) bundle.getSerializable("groupId");
            String url = Common.URL_SERVER + "jome_member/GroupOperateServlet";

            if (Common.networkConnected(activity)){
                //抓group物件
                JsonObject jsonObject = new JsonObject();
                if (groupId != null && !groupId.isEmpty()){
                    jsonObject.addProperty("action", "getAGroup");
                    jsonObject.addProperty("groupId", groupId);
                }
                String jsonOut = jsonObject.toString();
                getGroupBeanTask = new CommonTask(url, jsonOut);
                try {
                   String inStr = getGroupBeanTask.execute().get();
                   JsonObject jsonIn = new Gson().fromJson(inStr, JsonObject.class);
                   groupBean = new Gson().fromJson(jsonIn.get("getAResult").toString(), JoGroupBean.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                ivRatedGroupImage.setImageResource(R.drawable.no_image);
                Common.showToast(activity, R.string.no_network_connection_available);
            }
            return groupBean;
        }
        return null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.creat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Navigation.findNavController(rvRatingList).popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Adapter的相關們
    public class RatingPageAdapter extends RecyclerView.Adapter<RatingPageFragment.MyViewHolder> {
        private  List <ScoreBean> ratings;
        private LayoutInflater layoutInflater;
        private  int imageSize;

        public RatingPageAdapter(Context context, List<ScoreBean> ratings) {
            layoutInflater = LayoutInflater.from(context);
            this.ratings = ratings;
            imageSize = getResources().getDisplayMetrics().widthPixels/4;
        }

        @NonNull
        @Override
        public RatingPageFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_rating_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RatingPageFragment.MyViewHolder holder, int position) {
            final ScoreBean scoreBean = ratings.get(position);

            /*
             *  取照片們
             */
            String url = Common.URL_SERVER + "jome_member/LoginServlet";
            String memberID = scoreBean.getBeRatedId();
            MemberImageTask imageTask = new MemberImageTask(url, memberID, imageSize, holder.ivRatedImage);
            imageTask.execute();
            imageTasks.add(imageTask);

            holder.tvRatedName.setText(scoreBean.getBeRatedName());

            //評分數
            holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    String ratedScore = String.valueOf(rating) ;
                    holder.tvRatedScore.setText(ratedScore);

                    //還要寫更新資料
                    ScoreBean ratingResult = new ScoreBean();
                    ratingResult.setMemberId(scoreBean.getMemberId());
                    ratingResult.setBeRatedId(scoreBean.getBeRatedId());
                    ratingResult.setBeRatedName(scoreBean.getBeRatedName());
                    ratingResult.setGroupId(scoreBean.getGroupId());
                    ratingResult.setScore(Integer.valueOf(ratedScore));

                    ratingsResult.add(ratingResult);

                }
            });
//
        }

        @Override
        public int getItemCount() {
            return ratings == null ? 0 : ratings.size();
        }

        public void setRatings(List<ScoreBean> ratings) {
            ratings = this.ratings;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private  ImageView ivRatedImage;
        private TextView tvRatedName, tvRatedScore;
        private RatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRatedGroupImage = itemView.findViewById(R.id.ivRatedGroupImage);
            tvRatedName = itemView.findViewById(R.id.tvRatedGroupName);
            tvRatedScore = itemView.findViewById(R.id.tvRatedScore);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getRatingsTask != null){
            getRatingsTask.cancel(true);
            getRatingsTask = null;
        }

        if (imageTasks != null && imageTasks.size() > 0){
            for (MemberImageTask imageTask : imageTasks){
                imageTask.cancel(true);
            }
            imageTasks.clear();
        }
    }
}