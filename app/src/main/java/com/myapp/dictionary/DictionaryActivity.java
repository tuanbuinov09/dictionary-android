package com.myapp.dictionary;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.myapp.GlobalVariables;
import com.myapp.R;
import com.myapp.adapter.EnWordRecyclerAdapter;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.model.EnWord;
import com.myapp.model.ExampleDetail;
import com.myapp.model.Meaning;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DictionaryActivity extends AppCompatActivity {
    androidx.appcompat.widget.SearchView searchInput = null;
    public RecyclerView recyclerView;
    public EnWordRecyclerAdapter enWordRecyclerAdapter;
    boolean isScrolling = false;
    LinearLayoutManager manager;
    int currentItems, totalItems, scrollOutItems;
    ProgressBar progressBar;

    ArrayList<EnWord> justFetched = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.myapp.R.layout.activity_dictionary);

        setControl();
        setEvent();

        setTitle("Từ điển Anh-Việt");
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        getSupportActionBar().setElevation(0);
        GlobalVariables.listAllWords.clear();
        GlobalVariables.listFilteredWords.clear();
        GlobalVariables.offset = 0;
        changeSearchView();
    }

    //duwngf scroll truoc khi nhap tu khoa de tranh loi
    private void filter(String text) {

        // if query is empty: return all
        if (text.isEmpty()) {
            GlobalVariables.offset = 0;
            GlobalVariables.listAllWords.clear();
            getNextChunkOfWord(GlobalVariables.offset, GlobalVariables.limit);
            return;
        }

        // khi nhập query mới thì tìm lại, nên để offset 0
        GlobalVariables.listFilteredWords.clear();
        GlobalVariables.offset = 0;

        getNextChunkOfWord(text, GlobalVariables.offset, GlobalVariables.limit);
//        GlobalVariables.listFilteredWords.addAll(justFetched);
//        enWordRecyclerAdapter.notifyDataSetChanged();

//        if (GlobalVariables.listFilteredWords.isEmpty()) {
//            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
//            return;
//        } else {
////            enWordRecyclerAdapter.filterList(GlobalVariables.listFilteredWords);
//            enWordRecyclerAdapter = new EnWordRecyclerAdapter(this, GlobalVariables.listFilteredWords);
//            recyclerView.setAdapter(enWordRecyclerAdapter);
//            manager = new LinearLayoutManager(this);
//            recyclerView.setLayoutManager(manager);
//            enWordRecyclerAdapter.filterList(GlobalVariables.listFilteredWords);
//        }
    }

    private void setControl() {
        searchInput = findViewById(R.id.searchInput);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress_bar);

        this.getNextChunkOfWord(GlobalVariables.offset, GlobalVariables.limit);
        System.out.println("-------------" + GlobalVariables.listAllWords.size());

//        enWordRecyclerAdapter = new EnWordRecyclerAdapter(this, GlobalVariables.listAllWords);
//        recyclerView.setAdapter(enWordRecyclerAdapter);
//        manager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(manager);
////        enWordRecyclerAdapter = new EnWordRecyclerAdapter(this, GlobalVariables.listAllWords);
////        recyclerView.setAdapter(enWordRecyclerAdapter);
////        manager = new LinearLayoutManager(this);
////        recyclerView.setLayoutManager(manager);
    }

    private void setEvent() {
        searchInput.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();
                if (isScrolling && currentItems + scrollOutItems == totalItems) {
                    // lấy thêm data
                    fetchData();
                }
            }
        });
    }

    public void setEnWordAdapter() {


    }

    private void changeSearchView() {

        TextView textView = (TextView) searchInput.findViewById(androidx.appcompat.R.id.search_src_text);
        textView.setTextColor(getColor(R.color.space_cadet));
        textView.setHintTextColor(getColor(R.color.azureish_white));

    }

    @Override
    protected void onResume() {
        super.onResume();
        //để khi lưu hay bỏ lưu ở word detail thì cái nàfy đc cậpj nhật
        try {
            enWordRecyclerAdapter.notifyDataSetChanged();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!searchInput.getQuery().toString().trim().equalsIgnoreCase("")) {

                    GlobalVariables.offset = GlobalVariables.offset + GlobalVariables.limit;
                    getNextChunkOfWord(searchInput.getQuery().toString().trim(), GlobalVariables.offset, GlobalVariables.limit);
//                    GlobalVariables.listFilteredWords.addAll(justFetched);
//                    enWordRecyclerAdapter.notifyDataSetChanged();
//                    progressBar.setVisibility(View.GONE);
                } else {
                    GlobalVariables.offset = GlobalVariables.offset + GlobalVariables.limit;
                    getNextChunkOfWord(GlobalVariables.offset, GlobalVariables.limit);
//                enWordRecyclerAdapter.notifyDataSetChanged();

//                progressBar.setVisibility(View.GONE);
                }
                return;

            }
        }, 1000);
    }

    private void getNextChunkOfWord(int offset, int limit) {

        String url = "http://10.0.2.2:7000/enwords/simplified?offset=" + offset + "&limit=" + limit;
        System.out.println("---------------------------------------------------" + url);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                getDataJustFetchedWords(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DictionaryActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + GlobalVariables.access_token);
//                    params.put("refresh_token", GlobalVariables.refresh_token);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(DictionaryActivity.this);
        requestQueue.add(request);
    }

    private void getNextChunkOfWord(String query, int offset, int limit) {

        String url = "http://10.0.2.2:7000/enwords/simplified/search?query=" + query + "&offset=" + offset + "&limit=" + limit;
        System.out.println("---------------------------------------------------" + url);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                getDataJustFetchedWords(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DictionaryActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("authorization", "Bearer "+GlobalVariables.access_token);
////                    params.put("refresh_token", GlobalVariables.refresh_token);
//                return params;
//            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(DictionaryActivity.this);
        requestQueue.add(request);
    }

    private void getDataJustFetchedWords(JSONArray array) {
        try {
            this.justFetched.clear();
//            JSONArray array = response;
            for (int i = 0; i < array.length(); i = i + 1) {
                JSONObject object = array.getJSONObject(i);

                EnWord enWord = new EnWord();
                enWord.setWord(object.getString("word"));
                enWord.setId(object.getLong("id"));
                enWord.setViews(object.getInt("views"));
                enWord.setPronunciation(object.getString("pronunciation"));

                JSONArray meaningArray = object.getJSONArray("meanings");
                ArrayList<Meaning> listMeaning = new ArrayList<>();

                for (int j = 0; j < meaningArray.length(); j = j + 1) {
                    JSONObject objectMeaning = meaningArray.getJSONObject(j);
                    Meaning meaning = new Meaning();
                    try{
                        JSONObject objectPartOfSpeech = objectMeaning.getJSONObject("partOfSpeech");
                        meaning.setPartOfSpeechName(objectPartOfSpeech.getString("name"));

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                    meaning.setMeaning(objectMeaning.getString("meaning"));

                    // bắst trường hợp k có example
                    JSONArray exampleArray = new JSONArray();
                    ArrayList<ExampleDetail> listExample = new ArrayList<>();
                    try {
                        exampleArray = objectMeaning.getJSONArray("examples");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    for (int k = 0; k < exampleArray.length(); k = k + 1) {
                        JSONObject exampleObject = exampleArray.getJSONObject(k);
                        ExampleDetail exampleDetail = new ExampleDetail();
                        exampleDetail.setId(exampleObject.getInt("id"));
                        exampleDetail.setMeaningId(exampleObject.getInt("meaningId"));
                        exampleDetail.setExample(exampleObject.getString("example"));
                        exampleDetail.setExampleMeaning(exampleObject.getString("exampleMeaning"));

                        listExample.add(exampleDetail);
                    }

                    meaning.setListExampleDetails(listExample);

                    listMeaning.add(meaning);
                }

                enWord.setListMeaning(listMeaning);
                this.justFetched.add(enWord);

            }
            for (EnWord en : this.justFetched) {
                System.out.println("----" + en.toString());
            }

            if (!searchInput.getQuery().toString().trim().equalsIgnoreCase("")) {// nếu là fetch cho search
                GlobalVariables.listFilteredWords.addAll(justFetched);
                if (GlobalVariables.listFilteredWords.isEmpty()) {
                    Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //offset 0: mới search lần đầu, thì setadapter, nếu không thì chỉ notify change
                    if (GlobalVariables.offset == 0) {
                        enWordRecyclerAdapter = new EnWordRecyclerAdapter(this, GlobalVariables.listFilteredWords);
                        recyclerView.setAdapter(enWordRecyclerAdapter);
                        manager = new LinearLayoutManager(this);
                        recyclerView.setLayoutManager(manager);
                    } else {
                        enWordRecyclerAdapter.filterList(GlobalVariables.listFilteredWords);
//                        enWordRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            } else {// nếu là fetch không search
                GlobalVariables.listAllWords.addAll(justFetched);
                if (GlobalVariables.listAllWords.isEmpty()) {
                    Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //offset 0: mới lần đầu, thì setadapter, nếu không thì chỉ notify change
                    if (GlobalVariables.offset == 0) {
                        enWordRecyclerAdapter = new EnWordRecyclerAdapter(this, GlobalVariables.listAllWords);
                        recyclerView.setAdapter(enWordRecyclerAdapter);
                        manager = new LinearLayoutManager(this);
                        recyclerView.setLayoutManager(manager);
                    } else {
                        enWordRecyclerAdapter.filterList(GlobalVariables.listAllWords);
//                        enWordRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
            progressBar.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    public void setUpRecyclerViewYourWord(){
//        enWordRecyclerAdapter = new EnWordRecyclerAdapter(this, GlobalVariables.listAllWords);
//        recyclerView.setAdapter(enWordRecyclerAdapter);
//        manager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(manager);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}