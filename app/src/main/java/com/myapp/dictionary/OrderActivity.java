package com.myapp.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.myapp.GlobalVariables;
import com.myapp.Main;
import com.myapp.R;
import com.myapp.model.Order;
import com.myapp.model.OrderDetail;
import com.myapp.model.Product;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {
    EditText editTextAmount, editTextShipName, editTextShipAddress, editTextShipPhone, editTextShipNote;
    Button buttonDatHang;
Product product;
TextView textViewProductName, textViewPrice, textViewTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        getSupportActionBar().setElevation(0);
        setTitle("Thông tin nhận hàng");
        product= new Product();
        product = (Product) getIntent().getSerializableExtra("product");

        setControl();
        setEvent();
    }

    private void setEvent() {
        editTextAmount.setText(1 + "");
        textViewProductName.setText(product.getName());
        textViewPrice.setText(product.getPrice()+" ₫");
        textViewTotal.setText(product.getPrice()+" ₫");
        editTextAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                textViewTotal.setText(Integer.parseInt(editTextAmount.getText().toString().trim())*product.getPrice()+" ₫");
            }
        });
        buttonDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postOrderRequest(product.getId());
            }
        });
    }
    private void postOrderRequest(Long productId) {
        //-- dùng json body
        String url = "http://10.0.2.2:7000/orders";
        try {
            Order order = new Order();
            order.setShipAddress(editTextShipAddress.getText().toString().trim());
            order.setShipName(editTextShipName.getText().toString().trim());
            order.setShipPhone(editTextShipPhone.getText().toString().trim());
            order.setShipNote(editTextShipNote.getText().toString().trim());
            order.setUserId(Long.valueOf(GlobalVariables.userId));
            OrderDetail orderDetail = new OrderDetail();

            Product product = new Product();
            product.setId(productId);

            orderDetail.setProduct(product);
            orderDetail.setProductId(productId);
            orderDetail.setQuantity(Integer.parseInt(editTextAmount.getText().toString().trim()));

            List<OrderDetail> orderDetails = new ArrayList<>();
            orderDetails.add(orderDetail);
            order.setOrderDetails(orderDetails);
//
//            SavedWord savedWord = new SavedWord();
//            savedWord.setId(0);
//            savedWord.getEnWord().setId(enWord.getId());
//            savedWord.getUser().setId(Integer.parseInt(GlobalVariables.userId));
            RequestQueue requestQueue = Volley.newRequestQueue(OrderActivity.this);

            Gson gson = new Gson();
            String jsonStr = gson.toJson(order);
            final String mRequestBody = jsonStr;
            System.out.println(mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);

                    if(response.equalsIgnoreCase("200")){
                        Toast.makeText(OrderActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    }else if(!response.isEmpty()){
                        Toast.makeText(OrderActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(getApplication(), Main.class);
                    startActivity(intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_RESPONSE", error.toString());
                    Toast.makeText(OrderActivity.this, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("authorization", "Bearer "+ GlobalVariables.access_token);
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
    private void setControl() {
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextShipName = findViewById(R.id.editTextShipName);
        editTextShipAddress = findViewById(R.id.editTextShipAddress);
        editTextShipPhone = findViewById(R.id.editTextShipPhone);
        editTextShipNote = findViewById(R.id.editTextShipNote);
        buttonDatHang = findViewById(R.id.buttonDatHang);

        textViewProductName= findViewById(R.id.textViewProductName);
        textViewPrice= findViewById(R.id.textViewPrice);
        textViewTotal = findViewById(R.id.textViewTotal);
    }
}