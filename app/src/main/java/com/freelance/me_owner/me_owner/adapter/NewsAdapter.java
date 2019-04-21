package com.freelance.me_owner.me_owner.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.freelance.me_owner.me_owner.R;
import com.freelance.me_owner.me_owner.app.AppController;
import com.freelance.me_owner.me_owner.module.NewsData;

import java.util.List;


public class NewsAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<NewsData> newsItems;
    ImageLoader imageLoader;

    public NewsAdapter(Activity activity, List<NewsData> newsItems) {
        this.activity = activity;
        this.newsItems = newsItems;
    }

    @Override
    public int getCount() {
        return newsItems.size();
    }

    @Override
    public Object getItem(int location) {
        return newsItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.gambar);
        TextView nama = (TextView) convertView.findViewById(R.id.nama);
        TextView alamat = (TextView) convertView.findViewById(R.id.alamat);
        TextView jam_operasional = (TextView) convertView.findViewById(R.id.jam_operasional);
        TextView jenis_usaha = (TextView) convertView.findViewById(R.id.jenis_usaha);

        NewsData news = newsItems.get(position);

        thumbNail.setImageUrl(news.getGambar(), imageLoader);
        nama.setText(news.getNama());
        alamat.setText(news.getAlamat());
        jam_operasional.setText(news.getJam_operasional());
        jenis_usaha.setText(news.getJenis_usaha());

        return convertView;
    }

}