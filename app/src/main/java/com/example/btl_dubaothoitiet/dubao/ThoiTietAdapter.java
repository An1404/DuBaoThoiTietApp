package com.example.btl_dubaothoitiet.dubao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btl_dubaothoitiet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ThoiTietAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ThoiTiet> arrayList;

    public ThoiTietAdapter(Context context, ArrayList<ThoiTiet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
            return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.dong_listview,null );
        ThoiTiet thoiTiet = arrayList.get(i);
        TextView txtTg = (TextView)view.findViewById(R.id.txtTg);
        TextView txtTrangThai7d  = (TextView)view.findViewById(R.id.txtTrangThai7d);
        TextView txtNhietDoMin = (TextView)view.findViewById(R.id.txtNhietDoMin);
        TextView txtNhietDoMax = (TextView)view.findViewById(R.id.txtNhietDoMax);
        ImageView imgTrangThai7d = (ImageView)view.findViewById(R.id.imgIcon7d);

        txtTg.setText(thoiTiet.getDay());
        txtNhietDoMax.setText("Ngày: "+thoiTiet.getMaxTemp() + " °C");
        txtNhietDoMin.setText("Đêm: "+thoiTiet.getMinTemp() +" °C");
        txtTrangThai7d.setText(thoiTiet.getStatus());
        Picasso.with(context).load("https://openweathermap.org/img/wn/" + thoiTiet.getImg() + "@2x.png").into(imgTrangThai7d);
        return view;
    }
}
