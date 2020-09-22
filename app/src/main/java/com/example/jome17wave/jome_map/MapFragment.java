package com.example.jome17wave.jome_map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jome17wave.Common;
import com.example.jome17wave.MainActivity;
import com.example.jome17wave.R;
import com.example.jome17wave.task.CommonTask;
import com.example.jome17wave.task.ImageTask;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {
    private static final int PER_ACCESS_LOCATION = 0;
    private static final String TAG = "TAG_MainFragment";
    private MainActivity activity;
    private RecyclerView rvMap;
    private GoogleMap map;
    private CommonTask mapGetAllTask;
    private List<Map> maps;
    private List<ImageTask> imageTasks;
    private double latitude;
    private double longitude;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        setHasOptionsMenu(true);
        imageTasks = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        getActivity().setTitle("地圖");
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("地圖");
        activity.setSupportActionBar(toolbar);

        final MapView mapView = view.findViewById(R.id.mapView);
        rvMap = view.findViewById(R.id.rvMap);

        // RecyclerView
        rvMap.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        maps = getMaps();
        showMaps(maps);

        // show出地圖Marker
        mapView.onCreate(savedInstanceState);
        mapView.onStart();
        mapView.getMapAsync( new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                showMyLocation();

                final List<Marker> mapMarkers = new ArrayList<>();

                maps = getMaps();
                for (Map map: maps){
                    Log.d(TAG,"Level"+map.getLevel());
                    LatLng latLng = new LatLng(map.getLatitude(), map.getLongitude());
                    Marker mapMarker = addMarker(latLng, map.getName());
                    mapMarkers.add(mapMarker);
                }

                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        int index = mapMarkers.indexOf(marker);
//                        Log.d(TAG, String.valueOf(index));
                        rvMap.smoothScrollToPosition(index);
                        return false;
                    }
                });
            }
        });


        // 搜尋欄
        final SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    showMaps(maps);
                } else {
                    List<Map> searchMaps = new ArrayList<>();
                    for (Map map : maps) {
                        if (map.getName().toUpperCase().contains(newText.toUpperCase())) {
                            searchMaps.add(map);
                        }
                    }
                    showMaps(searchMaps);
                }

                map.clear();
//                maps = getMaps();
                if (newText.isEmpty()){
                    for (Map map: maps){
                        LatLng latLng = new LatLng(map.getLatitude(), map.getLongitude());
                        addMarker(latLng, map.getName());
                    }
                } else {
//                    List<Map> searchMaps = new ArrayList<>();
                    for (Map map : maps) {
                        if (map.getName().toUpperCase().contains(newText.toUpperCase())) {
//                            searchMaps.add(map);
                            LatLng latLng = new LatLng(map.getLatitude(), map.getLongitude());
                            addMarker(latLng, map.getName());
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.surf_side_menu, menu);
    }

    // menu選擇浪點
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.northSide:
                map.clear();
                maps = getMaps();
                for (int i = 0; i < 3; i++){
                    LatLng latLng = new LatLng(maps.get(i).getLatitude(), maps.get(i).getLongitude());
                    addMarker(latLng, maps.get(i).getName());
                    rvMap.smoothScrollToPosition(i);
                }
                break;
            case R.id.eastSide:
                map.clear();
                maps = getMaps();
                for (int i = 3; i < 6; i++) {
                    LatLng latLng = new LatLng(maps.get(i).getLatitude(), maps.get(i).getLongitude());
                    addMarker(latLng, maps.get(i).getName());
                    rvMap.smoothScrollToPosition(i);
                }
                break;
            case R.id.southSide:
                map.clear();
                maps = getMaps();
                for (int i = 6; i < 8; i++) {
                    LatLng latLng = new LatLng(maps.get(i).getLatitude(), maps.get(i).getLongitude());
                    addMarker(latLng, maps.get(i).getName());
                    rvMap.smoothScrollToPosition(i);
                }
                break;
            case R.id.westSide:
                map.clear();
                maps = getMaps();
                for (int i = 8; i < 10; i++) {
                    LatLng latLng = new LatLng(maps.get(i).getLatitude(), maps.get(i).getLongitude());
                    addMarker(latLng, maps.get(i).getName());
                    rvMap.smoothScrollToPosition(i);
                }
                break;
            case R.id.allSurfPoint:
                maps = getMaps();
                for (Map map: maps){
                    Log.d(TAG,"Level"+map.getLevel());
                    LatLng latLng = new LatLng(map.getLatitude(), map.getLongitude());
                    addMarker(latLng, map.getName());
                    rvMap.smoothScrollToPosition(0);
                }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 取得後端資料
    private List<Map> getMaps() {
        List<Map> maps = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "SURF_POINTServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            mapGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = mapGetAllTask.execute().get();
                Type listType = new TypeToken<List<Map>>() {
                }.getType();
                maps = new Gson().fromJson(jsonIn, listType);
                Log.d(TAG,"maps:"+jsonIn);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return maps;
    }


    // 資料show在RecyclerView
    private void showMaps(List<Map> maps) {
        if (maps == null || maps.isEmpty()) {
            Common.showToast(activity, R.string.textNoMapsFound);
        }
        MapAdapter mapAdapter = (MapAdapter) rvMap.getAdapter();
        if (mapAdapter == null) {
            rvMap.setAdapter(new MapAdapter(activity, maps));
        } else {
            mapAdapter.setMaps(maps);
            mapAdapter.notifyDataSetChanged();

        }
    }

    private class MapAdapter extends RecyclerView.Adapter<MapAdapter.MyViewHolder>{
        private LayoutInflater layoutInflater;
        private List<Map> maps;
        private int imageSize;

        MapAdapter(Context context, List<Map> maps) {
            layoutInflater = LayoutInflater.from(context);
            this.maps = maps;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        void setMaps(List<Map> maps) {
            this.maps = maps;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView tvName, tvSide, tvType, tvLevel;

            MyViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.ivMap);
                tvName = itemView.findViewById(R.id.tvName);
                tvSide = itemView.findViewById(R.id.tvSide);
                tvType = itemView.findViewById(R.id.tvType);
                tvLevel = itemView.findViewById(R.id.tvLevel);
            }
        }

        @Override
        public int getItemCount() {
            return maps == null ? 0 : maps.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_map, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            final Map map = maps.get(position);
            String url = Common.URL_SERVER + "SURF_POINTServlet";
            int id = map.getId();
            ImageTask imageTask = new ImageTask(url, id, imageSize, myViewHolder.imageView);
            imageTask.execute();
            imageTasks.add(imageTask);
            myViewHolder.tvName.setText(map.getName());
            myViewHolder.tvSide.setText(map.getSide());
            myViewHolder.tvType.setText(map.getType());
            myViewHolder.tvLevel.setText(map.getLevel());
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("map", map);
                    Navigation.findNavController(v).navigate(R.id.action_mapsFragment_to_mapDetailFragment, bundle);
                }
            });
        }
    }

    private Marker addMarker(LatLng latLng, String title) {
        Address address = reverseGeocode(latLng.latitude, latLng.longitude);
//        String title = address.getThoroughfare();
        String snippet = address.getAddressLine(0);
        Marker marker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title)
                .snippet(snippet));
        moveMap(latLng);
        return marker;
    }

    private void moveMap(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(7).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);

    }

    private Address reverseGeocode(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(activity);
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (addressList == null || addressList.isEmpty()) {
            return null;
        } else {
            return addressList.get(0);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        askAccessLocationPermission();
    }

    // 詢問使否取用位置
    private void askAccessLocationPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        int result = ActivityCompat.checkSelfPermission(activity, permissions[0]);
        if(result == PackageManager.PERMISSION_DENIED){
            requestPermissions(permissions, PER_ACCESS_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        showMyLocation();
    }

    // show出使用者現在位置
    private void showMyLocation() {
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
    }

}