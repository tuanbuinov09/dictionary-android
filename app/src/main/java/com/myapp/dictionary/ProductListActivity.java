package com.myapp.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.myapp.GlobalVariables;
import com.myapp.Main;
import com.myapp.R;
import com.myapp.adapter.ProductRecyclerAdapter;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.model.EnWord;
import com.myapp.model.ExampleDetail;
import com.myapp.model.Meaning;
import com.myapp.model.Product;
import com.myapp.utils.ChangeSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductListActivity extends AppCompatActivity {
    androidx.appcompat.widget.SearchView searchInput = null;
    ArrayList<Product> listProduct = new ArrayList<>();
    ArrayList<Product> filterList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductRecyclerAdapter productRecyclerAdapter;
    private ArrayList<EnWord> list;
    boolean isScrolling = false;
    LinearLayoutManager manager;
    int currentItems, totalItems, scrollOutItems;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        setControl();
        setEvent();

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        getSupportActionBar().setElevation(0);
        setTitle("Sản phẩm");
        ChangeSearchView.change(searchInput, this);
    }

    protected void onResume() {
        super.onResume();
        //để khi lưu hay bỏ lưu ở word detail thì cái nàfy đc cậpj nhật
        try{
            productRecyclerAdapter.notifyDataSetChanged();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void filter(String text) {

        // if query is empty: return all
        if (text.isEmpty()) {
            productRecyclerAdapter.filterList(this.listProduct);
            return;
        }
        GlobalVariables.listFilteredWords.clear();
        for (EnWord en : GlobalVariables.listAllSavedWords) {
            if (en.getWord().startsWith(text)) {
                GlobalVariables.listFilteredWords.add(en);
            }
        }

        if (GlobalVariables.listFilteredWords.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
            return;
        } else {
            productRecyclerAdapter.filterList(this.filterList);
        }
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
//                    fetchData();
                }
            }
        });
    }

    // k dùng trong yourword nữa
    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                databaseAccess.open();
                GlobalVariables.offset = GlobalVariables.offset + GlobalVariables.limit;

                ArrayList<EnWord> justFetched = new ArrayList<>();
                justFetched = databaseAccess.getFakeSavedWord_NoPopulateWithOffsetLimit(GlobalVariables.offset, GlobalVariables.limit);

                databaseAccess.close();

                GlobalVariables.listAllSavedWords.addAll(justFetched);
                productRecyclerAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);

            }
        }, 3000);
    }
//        try {
//            enWordList = (ArrayList<EnWord>) new UserDAO().getSavedWordlist("username");
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

//        YourWordAdapter adapter = new
//                YourWordAdapter(this, this.listEnWord, R.layout.yourword_listview_item, ttobj);
//        listViewYourWord.setAdapter(adapter);
//        listViewYourWord.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position,
//                                    long id) {
//                Toast.makeText(YourWordActivity.this, "Bạn chọn " +
//                        listEnWord.get(position).getId(), Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(view.getContext(), EnWordDetailActivity.class);
//                intent.putExtra("enWord", listEnWord.get(position));
//                view.getContext().startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//            }
//        });
//    }

    private void setControl() {
        searchInput = findViewById(R.id.searchInput);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress_bar);

        this.getProducts();
//        System.out.println("-------------" + GlobalVariables.listAllSavedWords.size());
        
    }
    public void setUpRecyclerViewProduct(){
        productRecyclerAdapter = new ProductRecyclerAdapter(this, this.listProduct);
        recyclerView.setAdapter(productRecyclerAdapter);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
    }
    public void search(View view) {
    }

    public void backToMain(View view) {
        Intent mainIntent = new Intent(this, Main.class);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

// lấy từ api
        private void getProducts() {

        String url = "http://10.0.2.2:7000/products";
        System.out.println("---------------------------------------------------"+url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                getData(response);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductListActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("authorization", "Bearer "+GlobalVariables.access_token);
//                    params.put("refresh_token", GlobalVariables.refresh_token);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProductListActivity.this);
        requestQueue.add(request);
    }

    private void getData(JSONObject response) {
        try{
            JSONArray array = response.getJSONArray("data");
            for(int i=0; i<array.length(); i=i+1){
                JSONObject object = array.getJSONObject(i);
                Product product = new Product();

                product.setId(object.getLong("id"));
                product.setName(object.getString("name"));
                product.setQuantity(object.getInt("quantity"));
                product.setUnit(object.getString("unit"));
                product.setPrice((float) object.getDouble("price"));
                product.setDescription(object.getString("description"));
                product.setViews(object.getInt("views"));

//                JSONArray meaningArray = object.getJSONArray("meanings");
//                ArrayList<Meaning> listMeaning = new ArrayList<>();
//
//                for(int j=0; j<meaningArray.length(); j=j+1) {
//                    JSONObject objectMeaning = meaningArray.getJSONObject(j);
//                    JSONObject objectPartOfSpeech = objectMeaning.getJSONObject("partOfSpeech");
//                    Meaning meaning = new Meaning();
//
//                    meaning.setMeaning(objectMeaning.getString("meaning"));
//                    meaning.setId(objectMeaning.getInt("id"));
//                    meaning.setPartOfSpeechName(objectPartOfSpeech.getString("name"));
//
//                    // bắst trường hợp k có example
//                    JSONArray exampleArray = new JSONArray();
//                    ArrayList<ExampleDetail> listExample = new ArrayList<>();
//                    try{
//                        exampleArray = objectMeaning.getJSONArray("examples");
//                    }catch (Exception ex){
//                        ex.printStackTrace();
//                    }
//                    for(int k=0; k<exampleArray.length(); k=k+1) {
//                        JSONObject exampleObject = exampleArray.getJSONObject(k);
//                        ExampleDetail exampleDetail = new ExampleDetail();
//                        exampleDetail.setId(exampleObject.getInt("id"));
//                        exampleDetail.setMeaningId(exampleObject.getInt("meaningId"));
//                        exampleDetail.setExample(exampleObject.getString("example"));
//                        exampleDetail.setExampleMeaning(exampleObject.getString("exampleMeaning"));
//
//                        listExample.add(exampleDetail);
//                    }
//
//                    meaning.setListExampleDetails(listExample);
//
//                    listMeaning.add(meaning);
//                }

//                enWord.setListMeaning(listMeaning);
                this.listProduct.add(product);
            }
//            for(EnWord en : GlobalVariables.listAllSavedWords){
//                System.out.println("----"+en.toString());
//
//                GlobalVariables.listSavedWordId.add(en.getId());
//            }
            // vì gọi api chạy ngầm nên ph để set adapter ở đây để set sau khi chạy api xong
            setUpRecyclerViewProduct();
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}




