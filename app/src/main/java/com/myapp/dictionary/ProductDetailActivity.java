package com.myapp.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.myapp.GlobalVariables;
import com.myapp.R;
import com.myapp.adapter.SliderAdapter;
import com.myapp.adapter.ViewPagerAdapterProdDetail;
import com.myapp.model.Order;
import com.myapp.model.OrderDetail;
import com.myapp.model.Product;
import com.myapp.model.SavedWord;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    Long productId;
    Product product;
    ViewPagerAdapterProdDetail viewPagerAdapterProdDetail;
    SliderView sliderView;
    TextView textViewProductName, textViewOldPrice, textViewPrice, textViewViews, textViewQuantity;
    Button buttonOrder;
    int [] sliderImages = {R.drawable.dictionary_black, R.drawable.dictionary_blue, R.drawable.dictionary_red};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        productId = getIntent().getLongExtra("productId", -1);
        setControl();
        setEvent();
    }
    public void setControl(){
        sliderView = findViewById(R.id.image_slider);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        textViewProductName = findViewById(R.id.textViewProductName);
        textViewOldPrice= findViewById(R.id.textViewOldPrice);
        textViewPrice= findViewById(R.id.textViewPrice);
        textViewViews= findViewById(R.id.textViewViews);
        textViewQuantity = findViewById(R.id.textViewQuantity);
        buttonOrder= findViewById(R.id.buttonOrder);

    }
    public void setEvent(){
        SliderAdapter sliderAdapter = new SliderAdapter(sliderImages);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postOrderRequest(productId);
            }


        });
        this.getProduct();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.product_dictionary, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_chat:

                break;

        }
        return super.onOptionsItemSelected(item);
    }
    private void postOrderRequest(Long productId) {
        //-- dùng json body
        String url = "http://10.0.2.2:7000/orders";
        try {
            Order order = new Order();
            OrderDetail orderDetail = new OrderDetail();
            Product product = new Product();
            product.setId(productId);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(1);
            List<OrderDetail> orderDetailList = new ArrayList<>();
            orderDetailList.add(orderDetail);
            order.setOrderDetails(orderDetailList);
//
//            SavedWord savedWord = new SavedWord();
//            savedWord.setId(0);
//            savedWord.getEnWord().setId(enWord.getId());
//            savedWord.getUser().setId(Integer.parseInt(GlobalVariables.userId));
            RequestQueue requestQueue = Volley.newRequestQueue(ProductDetailActivity.this);

            Gson gson = new Gson();
            String jsonStr = gson.toJson(order);
            final String mRequestBody = jsonStr;
            System.out.println(mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_RESPONSE", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("authorization", "Bearer "+GlobalVariables.access_token);
//                    params.put("refresh_token", GlobalVariables.refresh_token);
                    return params;
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void getProduct() {

        String url = "http://10.0.2.2:7000/products/"+productId;
        System.out.println("---------------------------------------------------"+url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                getData(response);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductDetailActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetailActivity.this);
        requestQueue.add(request);
    }

    private void getData(JSONObject response) {
        try{
                JSONObject object = response.getJSONObject("data");
                this.product = new Product();

                product.setId(object.getLong("id"));
                product.setName(object.getString("name"));
                product.setQuantity(object.getInt("quantity"));
                product.setUnit(object.getString("unit"));
                product.setPrice((float) object.getDouble("price"));
                product.setDescription(object.getString("description"));
                product.setViews(object.getInt("views"));
            // vì gọi api chạy ngầm nên ph để set adapter ở đây để set sau khi chạy api xong

            textViewProductName.setText(product.getName());
            textViewOldPrice.setText(product.getPrice()+" ₫");
            textViewPrice.setText(product.getPrice()+" ₫");
            textViewViews.setText(product.getViews()+"");
            textViewQuantity.setText(product.getQuantity()+"");

            GlobalVariables.productDescription = "";
            GlobalVariables.productDescription = product.getDescription();

            viewPagerAdapterProdDetail = new ViewPagerAdapterProdDetail(getSupportFragmentManager());
            viewPager.setAdapter(viewPagerAdapterProdDetail);

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}