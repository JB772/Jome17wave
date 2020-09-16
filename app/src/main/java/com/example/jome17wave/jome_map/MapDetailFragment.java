package com.example.jome17wave.jome_map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.jome17wave.MainActivity;
import com.example.jome17wave.R;
import com.google.android.gms.maps.MapView;

public class MapDetailFragment extends Fragment {
    private MainActivity activity;
    private TextView tvName, tvType, tvTidal, tvDirection, tvLevel;
    private NavController navController;
    private WebView webView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        setHasOptionsMenu(true);
        navController = Navigation.findNavController(activity, R.id.fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        activity.setTitle("浪點資訊");
        final View view = inflater.inflate(R.layout.fragment_map_detail, container, false);
        webView = view.findViewById(R.id.webView);
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    webView = view.findViewById(R.id.webView);
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (webView.canGoBack()) {
                                webView.goBack();
                                return true;
                            }
                            break;
                    }
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("浪點資訊");
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final NavController navController = Navigation.findNavController(view);
        Bundle bundle = getArguments();
        Map map = (Map) bundle.getSerializable("map");
        tvName = view.findViewById(R.id.tvName);
        tvType = view.findViewById(R.id.tvType);
        tvTidal = view.findViewById(R.id.tvTidal);
        tvDirection = view.findViewById(R.id.tvDirection);
        tvLevel = view.findViewById(R.id.tvLevel);



        tvName.setText(map.getName());
        tvType.setText(map.getType());
        tvTidal.setText(map.getTidal());
        tvDirection.setText(map.getDirection());
        tvLevel.setText(map.getLevel());

        webView = view.findViewById(R.id.webView);
        handleViews();
        webView.loadUrl("https://www.windy.com/?23.403,120.910,7");
//        webView.loadUrl("https://safesee.cwb.gov.tw/");

    }

    private void handleViews() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navController.popBackStack();
                return true;
            default:
                break;
        }
        return true;
    }

}