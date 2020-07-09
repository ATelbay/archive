package com.example.smqpr.a2hi_tech;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private int likes;
    private int amount_of_comments;

    private RecyclerView recyclerView;
    private List<News> newsList;
    NewsAdapter adapter = new NewsAdapter(newsList);
    ShowNewsFragment showNewsFragment = new ShowNewsFragment();

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



//            adapter = new NewsAdapter(newsList);
//            adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
//            recyclerView.setAdapter(adapter);

    }

    public class ParseText extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder str = new StringBuilder(" ");

            try {
                org.jsoup.nodes.Document document = Jsoup.connect(params[0]).get();
                Elements elements = document.select("article > div.td-post-content > p");

                for (Element element : elements){
                    str.append("\n");

                    str.append(element.text());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return str.toString();
        }
    }

    public class ParseTitle extends AsyncTask<Void, Void, HashMap<String, String>> {

        @Override
        protected HashMap<String, String> doInBackground(Void... voids) {
            HashMap<String, String> hashMap = new HashMap<>();

            try {
                org.jsoup.nodes.Document document = Jsoup.connect("http://hitechnewsdaily.com/").get();
                Elements elements = document.select("#td_uid_6_5c0cd28d267a9");
                for (Element element : elements) {
                    Elements divs = element.select("div");

                    for (Element div : divs) {
                        Element element1 = div.select("div > .item-details > h3 > a[href]").first();
                        // Log.d("NewsFragment", String.valueOf(element1));
                        if (element1 != null)
                            hashMap.put(element1.text(), element1.attr("abs:href"));
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (Map.Entry<String, String> stringStringEntry : hashMap.entrySet()) {
                Log.d("NewsFragment", stringStringEntry.toString());
            }

            return hashMap;
        }
    }

}