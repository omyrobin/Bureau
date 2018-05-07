package com.administration.bureau.activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.adapter.InfoWinAdapter;
import com.administration.bureau.utils.AMapUtil;
import com.administration.bureau.utils.DialogUtil;
import com.administration.bureau.utils.ToastUtil;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by omyrobin on 2017/11/3.
 */

public class MapActivity extends BaseActivity implements AMap.OnCameraChangeListener, AMap.OnMyLocationChangeListener,
        DialogUtil.OnDialogCallBack, GeocodeSearch.OnGeocodeSearchListener,Inputtips.InputtipsListener,TextWatcher {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.toolbar_action_tv)
    TextView actionTv;
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.input_edittext)
    AutoCompleteTextView mSearchText;
//    @BindView(R.id.submit_edittext)
//    EditText mSubmitEt;
    @BindView(R.id.poi_detail)
    RelativeLayout mPoiDetail;
    //初始化地图控制器对象
    public AMap aMap;
    public MyLocationStyle myLocationStyle;
    //定义一个UiSettings对象
    private UiSettings mUiSettings;
    private double latitude;
    private double longitude;
    private String keyWord;
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;
    private LatLonPoint lp;
    private PoiResult poiResult; // poi返回的结果
    private List<PoiItem> poiItems;// poi数据
    private GeocodeSearch geocoderSearch;
    private Marker screenMarker;
//    private MarkerOptions otMarkerOptions;
    private double mLocationLatitude, mLocationLongitude;
    private boolean isFirst = true;
    private String addressName;//逆地理编码得到的地址


    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected void initializeToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        titleTv.setText(R.string.address);
        actionTv.setText(R.string.use);
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        initSearch();
        initGeocodeSearch();
        initMapConfig();
    }

    private void initSearch(){
        // 添加文本输入框监听事件
        mSearchText.addTextChangedListener(this);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getFromLocationName(mSearchText.getText().toString());
                    closeInputMethod();
                }
                return false;
            }
        });
    }

    private void closeInputMethod(){
        //得到InputMethodManager的实例
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果开启
        if (imm.isActive()) {
            //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void initGeocodeSearch(){
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    private void getFromLocationAsyn(LatLonPoint lp){
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(lp, 200,GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    private void getFromLocationName(String name) {
        // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
        GeocodeQuery query = new GeocodeQuery(name, "010");
        geocoderSearch.getFromLocationNameAsyn(query);
    }

    private void initMapConfig(){
        //设置希望展示的地图缩放级别
        CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(17);
        aMap.moveCamera(mCameraUpdate);
        //初始化定位蓝点样式
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        myLocationStyle.interval(2000);
        myLocationStyle.strokeWidth(1f);
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);

        myLocationStyle.showMyLocation(true);

        mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomGesturesEnabled(true);
    }

    @Override
    public void submit(String houseNumber) {
        infoEntity = App.getInstance().getInfoEntity();
        infoEntity.setHouse_address(addressName + houseNumber);
        finish();
    }

//    public LatLng getMapCenterPoint() {
//        int left = mMapView.getLeft();
//        int top = mMapView.getTop();
//        int right = mMapView.getRight();
//        int bottom = mMapView.getBottom();
//        // 获得屏幕点击的位置
//        int x = (int) (mMapView.getX() + (right - left) / 2);
//        int y = (int) (mMapView.getY() + (bottom - top) / 2);
//        Projection projection = aMap.getProjection();
//        LatLng pt = projection.fromScreenLocation(new Point(x, y));
//        return pt;
//    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (screenMarker == null) {
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker_normal));
            screenMarker = aMap.addMarker(new MarkerOptions().zIndex(2).icon(bitmapDescriptor));
        }
        LatLng latLng = cameraPosition.target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        screenMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
        screenMarker.setClickable(false);
        screenMarkerJump(aMap, screenMarker);
//        screenMarker.setPosition(latLng);

        //逆地理查询
        lp = new LatLonPoint(latLng.latitude, latLng.longitude);
        getFromLocationAsyn(lp);
        aMap.setInfoWindowAdapter(new InfoWinAdapter());
    }

    public void screenMarkerJump(AMap aMap, Marker screenMarker) {
        if (screenMarker != null) {
            final LatLng latLng = screenMarker.getPosition();
            Point point = aMap.getProjection().toScreenLocation(latLng);
            point.y -= 50;
            LatLng target = aMap.getProjection().fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if (input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(600);
            //设置动画
            screenMarker.setAnimation(animation);
            //开始动画
            screenMarker.startAnimation();
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (location != null) {
            Bundle bundle = location.getExtras();
            if (bundle != null) {
                mLocationLatitude = location.getLatitude();
                mLocationLongitude = location.getLongitude();
                if (isFirst) {
                    if (mLocationLatitude > 0 && mLocationLongitude > 0) {
                        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(mLocationLatitude, mLocationLongitude), 17);
                        aMap.moveCamera(cu);
                    }
                    isFirst = false;
                }
            } else {
                ToastUtil.showShort("定位失败，请检查您的定位权限");
            }
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress();
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(lp), aMap.getCameraPosition().zoom));
                screenMarker.setTitle("位置");
                screenMarker.setSnippet(addressName);
                screenMarker.showInfoWindow();
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                LatLng latLng = new LatLng(address.getLatLonPoint().getLatitude(), address.getLatLonPoint().getLongitude());
                Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
                screenMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(address.getLatLonPoint()), aMap.getCameraPosition().zoom));
                screenMarker.setClickable(false);
                screenMarkerJump(aMap, screenMarker);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String newText = s.toString().trim();
        if (!AMapUtil.IsEmptyOrNullString(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, "010");
            Inputtips inputTips = new Inputtips(this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            List<String> listString = new ArrayList<>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.route_inputs, listString);
            mSearchText.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.btn_search, R.id.toolbar_action_tv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_search:
                getFromLocationName(mSearchText.getText().toString());
                closeInputMethod();
                break;

            default:
                DialogUtil util = new DialogUtil(this, this);
                util.showDilog(addressName);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        aMap.setOnCameraChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
