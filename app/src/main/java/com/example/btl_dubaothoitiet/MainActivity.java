package com.example.btl_dubaothoitiet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.btl_dubaothoitiet.dubao.BlurBuilder;
import com.example.btl_dubaothoitiet.dubao.ThoiTiet;
import com.example.btl_dubaothoitiet.dubao.ThoiTietAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    //tab1
    private EditText txtSearch;
    private TextView txtDoAm, txtGio, txtMay, txtTP, txtQG, txtNhietDo, txtTrangThai, txtNgayUpdate,txtTenTP;
    private ImageButton btnSearch,btnMap;
    private ImageView imgTrangThai;
    private TabHost tabHost;
    //tab2
    private TextView txtTPMai,txtNhietDoMai,txtNDTB,txtNgay,txtDem,txtTrangThaiMai;
    private ImageView imgTTMai;
    //tab3
    private ListView lv;
    private ThoiTietAdapter adapter;
    private ArrayList<ThoiTiet> mangThoiTiets;
    private String tenTP;
    private int backGround;
    private double viTriLat;
    private double viTriLon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ẩn ActionBar
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.hide();*/
        addControls();

        addEvents();




    }


    private void addEvents() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabHost.setCurrentTab(0);
               timThoiTiet();
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,MapsActivity.class);
               /* Bundle bundle = new Bundle();
                bundle.putDouble("lat",viTriLat);
                bundle.putDouble("lon",viTriLon);
                i.putExtras(bundle);*/
                startActivity(i);
            }
        });
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s.equals("tab1")){
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + tenTP
                            + "&units=metric&appid=1a51a81870ce5b0232dc34accccbe3ec";
                    layTTHienTai(url);

                }
                else if(s.equals("tab2")){
                    Log.d("test",tenTP);
                    Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), backGround);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        // làm mờ background
                        Bitmap blurredBitmap = BlurBuilder.blur( MainActivity.this, originalBitmap );
                        linearLayout.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
                    }
                    //lấy 2
                    txtTPMai.setText(R.string.tp);
                    txtTPMai.append(": " + tenTP);
                    String url= "https://api.openweathermap.org/data/2.5/forecast/daily?q="+ tenTP +"&units=metric&cnt=2&appid=53fbf527d52d4d773e828243b90c1f8e";
                    duBaoNgayMai(url);
                }
                else if(s.equals("tab3")){
                    Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), backGround);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        Bitmap blurredBitmap = BlurBuilder.blur( MainActivity.this, originalBitmap );
                        linearLayout.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
                    }
                    mangThoiTiets.clear();
                    String url= "https://api.openweathermap.org/data/2.5/forecast/daily?q="+ tenTP +"&units=metric&cnt=8&appid=53fbf527d52d4d773e828243b90c1f8e";
                    DuBao7d(url);

                }

            }
        });

    }

    private void duBaoNgayMai(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            txtTPMai.setText(tenTP);
                            JSONArray listArray = jsonObject.getJSONArray("list");

                            JSONObject jsonArray = listArray.getJSONObject(1);
                            //nhiệt độ
                            JSONObject objectNhietDo = jsonArray.getJSONObject("temp");
                            String nhietDo = objectNhietDo.getString("eve");
                            txtNhietDoMai.setText(nhietDo + "°C");
                            JSONObject objectNhietDoCamThay = jsonArray.getJSONObject("feels_like");
                            String nhietDoTB= objectNhietDoCamThay.getString("eve");
                            txtNDTB.setText("Cảm thấy như: "+nhietDoTB + "°C");
                            String nhietDoNgay = objectNhietDoCamThay.getString("day");
                            txtNgay.setText(nhietDoNgay + "°C");
                            String nhietDoDem = objectNhietDoCamThay.getString("eve");
                            txtDem.setText(nhietDoDem + "°C");
                            //thời tiết
                            JSONArray objectWeather = jsonArray.getJSONArray("weather");
                            JSONObject weather = objectWeather.getJSONObject(0);
                            String status = weather.getString("description");


                            String icon = weather.getString("icon");
                            Picasso.with(MainActivity.this).load("https://openweathermap.org/img/wn/" + icon + "@2x.png").into(imgTTMai);
                            txtTrangThaiMai.setText(layTrangThaiThoiTiet(icon,status));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("loi",error.toString());
            }
        });
        requestQueue.add(stringRequest);
    }

    private void DuBao7d(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //tên tp
                            JSONObject objectCity = jsonObject.getJSONObject("city");
                            String city = objectCity.getString("name");
                            txtTenTP.setText(city);
                            //
                            JSONArray listArray = jsonObject.getJSONArray("list");
                            for(int i=1;i<listArray.length();i++){
                                JSONObject jsonArray = listArray.getJSONObject(i);
                                //lấy tg
                                String objectTG = jsonArray.getString("dt");
                                long day = Long.valueOf(objectTG);
                                Date date = new Date(day*1000L);
                                SimpleDateFormat format = new SimpleDateFormat("EEEE dd-MM-yyyy");
                                String dayText = format.format(date);
                                JSONObject objectNhietDo = jsonArray.getJSONObject("temp");
                                String nhietDoMax = objectNhietDo.getString("day");
                                String nhietDoMin = objectNhietDo.getString("night");
                                double tMax = Double.valueOf(nhietDoMax);
                                double tMin = Double.valueOf(nhietDoMin);
                                JSONArray objectWeather = jsonArray.getJSONArray("weather");
                                JSONObject weather = objectWeather.getJSONObject(0);
                                String status = weather.getString("description");
                                String icon = weather.getString("icon");
                                mangThoiTiets.add(new ThoiTiet(dayText,layTrangThaiThoiTiet(icon,status),icon,""+(int)tMax,""+(int)tMin));

                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("loi",error.toString());
            }
        });
        requestQueue.add(stringRequest);

    }

    private void timThoiTiet() {
        String data = txtSearch.getText().toString();
        if(data!=null){
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data
                    + "&units=metric&appid=1a51a81870ce5b0232dc34accccbe3ec";

            layTTHienTai(url);
        }
        else
            getCurrentLocation();

    }

    private void addControls() {
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        // tab
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1");
        TextView text = new TextView(MainActivity.this);
        text.setText(R.string.tabHomNay);
        text.setTextSize(20);
        text.setTextColor(Color.WHITE);
        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        tab1.setIndicator(text);
        tab1.setContent(R.id.tab1);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2");
        TextView text1 = new TextView(MainActivity.this);
        text1.setText(R.string.tabNgayMai);
        text1.setTextSize(20);
        text1.setTextColor(Color.WHITE);
        text1.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        tab2.setIndicator(text1);
        tab2.setContent(R.id.tab2);
        tabHost.addTab(tab2);

        TabHost.TabSpec tab3 = tabHost.newTabSpec("tab3");
        TextView text2 = new TextView(MainActivity.this);
        text2.setText(R.string.tab7day);
        text2.setTextSize(20);
        text2.setTextColor(Color.WHITE);
        text2.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        tab3.setIndicator(text2);
        tab3.setContent(R.id.tab3);
        tabHost.addTab(tab3);


        txtDoAm = (TextView) findViewById(R.id.txtDoAm);
        txtGio = (TextView) findViewById(R.id.txtLuongGio);
        txtMay = (TextView) findViewById(R.id.txtMay);
        txtTP = (TextView) findViewById(R.id.txtTP);
        txtNgayUpdate = (TextView) findViewById(R.id.txtNgayUpdate);
        txtNhietDo = (TextView) findViewById(R.id.txtNhietDo);
        txtQG = (TextView) findViewById(R.id.txtQG);
        txtTrangThai = (TextView) findViewById(R.id.txtTrangThai);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        btnMap = (ImageButton) findViewById(R.id.btnMap);
        imgTrangThai = (ImageView) findViewById(R.id.imgIcon);
        txtTenTP = (TextView)findViewById(R.id.txtTenTP);
        lv = (ListView)findViewById(R.id.lvThoiTiet);
        mangThoiTiets = new ArrayList<ThoiTiet>();
        adapter = new ThoiTietAdapter(MainActivity.this,mangThoiTiets);
        lv.setAdapter(adapter);
        txtTPMai = (TextView)findViewById(R.id.txtTPNgayMai);
        txtNhietDoMai = (TextView)findViewById(R.id.txtNhietDoMai) ;
        txtNDTB =(TextView)findViewById(R.id.txtNhietDoTB) ;
        txtNgay = (TextView)findViewById(R.id.txtNhietDoNgay) ;
        txtDem = (TextView)findViewById(R.id.txtNhietDodem) ;
        txtTrangThaiMai = (TextView)findViewById(R.id.txtTrangThaiMai);
        imgTTMai =(ImageView) findViewById(R.id.imgTTMai);





    }

    public void layTTHienTai(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            layTTThoiTiet(jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("loi", error.toString());
                    }
                });

        requestQueue.add(stringRequest);
    }

    private void layTTThoiTiet(JSONObject jsonObject) throws JSONException {
        //lấy tên tp
        String tenTP = jsonObject.getString("name");
        this.tenTP = tenTP;
        txtTP.setText(R.string.tp);
        txtTP.append(": "+ tenTP);
        //lấy ngày update
        String day = jsonObject.getString("dt");
        long dayLong = Long.valueOf(day);
        Date date = new Date(dayLong * 1000L);//milisecond
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy HH:mm:ss");
        String dayText = simpleDateFormat.format(date);
        txtNgayUpdate.setText(dayText);
        //lấy thời tiết
        JSONArray jsonArray = jsonObject.getJSONArray("weather");
        JSONObject weatherArray = jsonArray.getJSONObject(0);
        String iconThoiTiet = weatherArray.getString("icon");
        // lấy icon
        Picasso.with(MainActivity.this).load("https://openweathermap.org/img/wn/" + iconThoiTiet + "@2x.png").into(imgTrangThai);


        //lấy nhiệt độ
        JSONObject objectMain = jsonObject.getJSONObject("main");
        String nhietDo = objectMain.getString("temp");
        txtNhietDo.setText(nhietDo + " °C");
        //lấy độ ẩm
        String doAm = objectMain.getString("humidity");
        txtDoAm.setText(doAm + "%");
        //lấy mây
        JSONObject objectMay = jsonObject.getJSONObject("clouds");
        String may = objectMay.getString("all");
        txtMay.setText(may + "%");
        //lấy gio
        JSONObject objectGio = jsonObject.getJSONObject("wind");
        String gio = objectGio.getString("speed");
        txtGio.setText(gio + " m/s");
        //lấy quốc gia
        JSONObject objectQG = jsonObject.getJSONObject("sys");
        String qg = objectQG.getString("country");
        txtQG.setText(R.string.quocgia);
        txtQG.append(": "+ qg);
        String descriptionThoiTiet = weatherArray.getString("description");
        //lấy trạng thái
        txtTrangThai.setText(layTrangThaiThoiTiet(iconThoiTiet,descriptionThoiTiet));;

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout);

        if (descriptionThoiTiet.equals("clear sky") || descriptionThoiTiet.equals("few clouds")){
            linearLayout.setBackgroundResource(R.drawable.back_nang);
            backGround = R.drawable.back_nang;
            if(iconThoiTiet.contains("n")){
                linearLayout.setBackgroundResource(R.drawable.back_dem_sao);
                backGround = R.drawable.back_dem_sao;
            }
        }


        else if (descriptionThoiTiet.equals("overcast clouds") || descriptionThoiTiet.equals("broken clouds")){
            backGround = R.drawable.back_amu;
            linearLayout.setBackgroundResource(R.drawable.back_amu);
            if(iconThoiTiet.contains("n")){
                linearLayout.setBackgroundResource(R.drawable.back_dem_may);
                backGround = R.drawable.back_dem_may;
            }
        }

        else if (descriptionThoiTiet.contains("rain")) {
            if (descriptionThoiTiet.contains("thunderstorm")){
                linearLayout.setBackgroundResource(R.drawable.back_thunderstorm_rain);
                backGround = R.drawable.back_thunderstorm_rain;
            }

            else{
                linearLayout.setBackgroundResource(R.drawable.back_mua);
                backGround = R.drawable.back_mua;
            }

        } else if (descriptionThoiTiet.contains("thunderstorm")){
            linearLayout.setBackgroundResource(R.drawable.back_samset);
            backGround = R.drawable.back_samset;
        }

        else if (descriptionThoiTiet.contains("snow")){
            linearLayout.setBackgroundResource(R.drawable.back_snow);
            backGround = R.drawable.back_snow;
        }



    }

    private int layTrangThaiThoiTiet(String icon,String chitiet) {
        String iconn = icon.substring(0,2);
        int kq = 0;
        switch (iconn){
            case "01":
                 kq = R.string.clear_sky;
                break;
            case "02":
                kq =  R.string.few_clouds;
                break;
            case "03":
                kq = R.string.scattered_clouds;
                break;
            case "04":
                kq = R.string.broken_clouds;
                break;
            case "09":
                if(chitiet.equals("light intensity shower rain") || chitiet.equals("shower rain"))
                    kq = R.string.light_rain;
                else if(chitiet.equals("heavy intensity shower rain"))
                    kq = R.string.moderate_rain;
                else
                    kq = R.string.heavy_intensity_rain;
                break;
            case "10":
                if(chitiet.equals("light rain"))
                    kq = R.string.light_rain;
                else if(chitiet.equals("moderate rain"))
                    kq = R.string.moderate_rain;
                else
                    kq = R.string.heavy_intensity_rain;
                break;
            case "11":
                if(chitiet.equals("thunderstorm"))
                    kq = R.string.thunderstorm;
                else
                    kq = R.string.thunderstorm_rain;
                break;
            case "13":
                kq = R.string.snow;
                break;
            case "50":
                kq = R.string.mist;
                break;
        }
        return  kq;
    }

    //lấy địa điểm hiện tại
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                makeRequest();

            return;
        }
        Location lastLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        //ta lấy được thì truyền vĩ độ, kinh độ để xem thời tiết
        //Log.d("eee"," "+lastLocation.getLatitude() );
        this.viTriLat = lastLocation.getLatitude();
        this.viTriLon = lastLocation.getLongitude();
        String url = "https://api.openweathermap.org/data/2.5/weather?lat="+viTriLat+"&lon="+viTriLon+"&units=metric&appid=1a51a81870ce5b0232dc34accccbe3ec";
        layTTHienTai(url);



    }
    protected void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                 Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }
   @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            String url = "https://api.openweathermap.org/data/2.5/weather?lat="+bundle.getDouble("1", 0)+"&lon="+bundle.getDouble("2", 0)
                    +"&units=metric&appid=1a51a81870ce5b0232dc34accccbe3ec";
            layTTHienTai(url);
        }
        else
         getCurrentLocation();
    }


}