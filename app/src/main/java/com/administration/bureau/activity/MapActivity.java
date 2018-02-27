//package com.administration.bureau.activity;
//
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.administration.bureau.BaseActivity;
//import com.administration.bureau.R;
//import com.administration.bureau.utils.AMapUtil;
//import com.administration.bureau.utils.ToastUtil;
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.CameraUpdate;
//import com.amap.api.maps.CameraUpdateFactory;
//import com.amap.api.maps.LocationSource;
//import com.amap.api.maps.MapView;
//import com.amap.api.maps.UiSettings;
//import com.amap.api.maps.model.BitmapDescriptorFactory;
//import com.amap.api.maps.model.CircleOptions;
//import com.amap.api.maps.model.LatLng;
//import com.amap.api.maps.model.MarkerOptions;
//import com.amap.api.maps.model.MyLocationStyle;
//import com.amap.api.services.core.AMapException;
//import com.amap.api.services.core.LatLonPoint;
//import com.amap.api.services.core.PoiItem;
//import com.amap.api.services.core.SuggestionCity;
//import com.amap.api.services.help.Inputtips;
//import com.amap.api.services.help.InputtipsQuery;
//import com.amap.api.services.help.Tip;
//import com.amap.api.services.poisearch.PoiResult;
//import com.amap.api.services.poisearch.PoiSearch;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
///**
// * Created by omyrobin on 2017/11/3.
// */
//
//public class MapActivity extends BaseActivity implements AMap.OnMapClickListener, Inputtips.InputtipsListener,LocationSource,
//        TextWatcher {
//
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.toolbar_title_tv)
//    TextView titleTv;
//    @BindView(R.id.toolbar_action_tv)
//    TextView actionTv;
//    @BindView(R.id.map)
//    MapView mMapView;
//    @BindView(R.id.input_edittext)
//    AutoCompleteTextView mSearchText;
//    @BindView(R.id.poi_detail)
//    RelativeLayout mPoiDetail;
//    //初始化地图控制器对象
//    public AMap aMap;
//    public MyLocationStyle myLocationStyle;
//    //声明AMapLocationClient类对象
//    public AMapLocationClient mLocationClient = null;
//    //声明定位回调监听器
//    public AMapLocationListener mLocationListener;
//    //声明AMapLocationClientOption对象
//    public AMapLocationClientOption mLocationOption = null;
//    //定义一个UiSettings对象
//    private UiSettings mUiSettings;
//    private double latitude;
//    private double longitude;
//    private String keyWord;
//    private int currentPage = 0;// 当前页面，从0开始计数
//    private PoiSearch.Query query;// Poi查询条件类
//    private PoiSearch poiSearch;
//    private LatLonPoint lp;
//    private PoiResult poiResult; // poi返回的结果
//    private List<PoiItem> poiItems;// poi数据
//
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_map;
//    }
//
//    @Override
//    protected void initializeToolbar() {
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//    }
//
//    @Override
//    protected void initializeActivity(Bundle savedInstanceState) {
//        mMapView.onCreate(savedInstanceState);
//        if (aMap == null) {
//            aMap = mMapView.getMap();
//        }
//        mSearchText.addTextChangedListener(this);// 添加文本输入框监听事件
//        initLocationOption();
//        initMapConfig();
//        locationListener();
//    }
//
//    private void initMapConfig(){
//        //设置希望展示的地图缩放级别
//        CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(17);
//        aMap.moveCamera(mCameraUpdate);
//
//        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle = new MyLocationStyle();
//        //连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
//        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        myLocationStyle.interval(2000);
//        //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
//        aMap.setMyLocationEnabled(true);
//
//        myLocationStyle.strokeWidth(1f);
//        //设置定位蓝点的Style
//        aMap.setMyLocationStyle(myLocationStyle);
//
//        myLocationStyle.showMyLocation(true);
//        //设置定位监听
//        aMap.setLocationSource(this);
//
//        aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
//    }
//
//    private void initLocationOption(){
//        //初始化AMapLocationClientOption对象
//        mLocationOption = new AMapLocationClientOption();
//        //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setNeedAddress(true);
//        //设置是否允许模拟位置,默认为true，允许模拟位置
//        mLocationOption.setMockEnable(true);
//        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
//    }
//
//    private void locationListener(){
//        mLocationListener = new AMapLocationListener(){
//
//            @Override
//            public void onLocationChanged(AMapLocation aMapLocation) {
//                if (aMapLocation != null) {
//                    if (aMapLocation.getErrorCode() == 0) {
//                        //可在其中解析amapLocation获取相应内容。
//                        String city = aMapLocation.getCity();//街道信息
//                        ToastUtil.showLong(city);
//                    }else {
//                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                        Log.e("AmapError","location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
//                    }
//                }
//            }
//        };
//    }
//
//    /**
//     * 激活定位
//     */
//    @Override
//    public void activate(OnLocationChangedListener onLocationChangedListener) {
//        if (mLocationClient == null) {
//            //初始化定位
//            mLocationClient = new AMapLocationClient(getApplicationContext());
//            //设置定位回调监听
//            mLocationClient.setLocationListener(mLocationListener);
//            //给定位客户端对象设置定位参数
//            mLocationClient.setLocationOption(mLocationOption);
//            //启动定位
//            mLocationClient.startLocation();
//            //实例化UiSettings类对象
//            mUiSettings = aMap.getUiSettings();
//            //显示默认的定位按钮
//            mUiSettings.setMyLocationButtonEnabled(true);
//            //可触发定位并显示当前位置
//            aMap.setMyLocationEnabled(true);
//        }
//    }
//
//    /**
//     * 停止定位
//     */
//    @Override
//    public void deactivate() {
//        if (mLocationClient != null) {
//            mLocationClient.stopLocation();
//            mLocationClient.onDestroy();
//        }
//        mLocationClient = null;
//    }
//
//    //地图点击事件
//    @Override
//    public void onMapClick(LatLng latLng) {
//        //点击地图后清理图层插上图标，在将其移动到中心位置
//        aMap.clear();
//        latitude = latLng.latitude;
//        longitude = latLng.longitude;
//        MarkerOptions otMarkerOptions = new MarkerOptions();
//        otMarkerOptions.position(latLng);
//        aMap.addMarker(otMarkerOptions);
//        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
//        ToastUtil.showShort(latitude +  "  :   " +  longitude);
//        lp = new LatLonPoint(latitude, longitude);
//    }
//
//    @OnClick(R.id.btn_search)
//    public void onClick(View view){
//        switch (view.getId()){
//            case R.id.btn_search:
//
//                break;
//
//            default:
//
//                break;
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mMapView.onResume();
//    }
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//        String newText = s.toString().trim();
//        if (!AMapUtil.IsEmptyOrNullString(newText)) {
//            InputtipsQuery inputquery = new InputtipsQuery(newText, "北京");
//            Inputtips inputTips = new Inputtips(this, inputquery);
//            inputTips.setInputtipsListener(this);
//            inputTips.requestInputtipsAsyn();
//        }
//    }
//
//    @Override
//    public void onGetInputtips(List<Tip> tipList, int rCode) {
//        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
//            List<String> listString = new ArrayList<String>();
//            for (int i = 0; i < tipList.size(); i++) {
//                listString.add(tipList.get(i).getName());
//            }
//            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.route_inputs, listString);
//            mSearchText.setAdapter(aAdapter);
//            aAdapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mMapView.onPause();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mMapView.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mMapView.onDestroy();
//    }
//}
