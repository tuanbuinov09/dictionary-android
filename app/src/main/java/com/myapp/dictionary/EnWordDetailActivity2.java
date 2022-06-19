package com.myapp.dictionary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.myapp.GlobalVariables;
import com.myapp.R;
import com.myapp.adapter.ViewPagerAdapter;
import com.myapp.dictionary.fragment.EnWordDetailFragment;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.model.EnWord;
import com.myapp.model.ExampleDetail;
import com.myapp.model.Meaning;
import com.myapp.model.SavedWord;
import com.myapp.utils.FileIO2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnWordDetailActivity2 extends AppCompatActivity {
    public Long enWordId;
    EnWord enWord;
    BottomNavigationView topNavigation;
    FragmentManager fragmentManager;
    public ImageButton btnSave_UnsaveWord;
    public ImageButton btnBackToSavedWord;

    TextView textViewTitle;
    boolean unsave;

    private final int REQUEST_WRITE = 888;

    MenuItem menuSave_unSaveWord;
    Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_word_detail2);
        setControl();
        setEvent();
    }


    private void setEvent() {

        btnBackToSavedWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), YourWordActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


//        // luu tu xoa tu
//        btnSave_UnsaveWord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (unsave == true) {
//                    //---run unsave code
//                    GlobalVariables.db.collection("saved_word").document(GlobalVariables.userId + enWord.getId() + "")
//                            .delete()
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Toast.makeText(EnWordDetailActivity2.this, "Xoa từ khoi danh sach thanh cong", Toast.LENGTH_LONG).show();
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(EnWordDetailActivity2.this, "Xoa từ khoi danh sach that bai", Toast.LENGTH_LONG).show();
//
//                                }
//                            });
//                    GlobalVariables.listSavedWordId.remove(GlobalVariables.listSavedWordId.indexOf(enWord.getId()));
//
//                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
//                    databaseAccess.open();
//                    databaseAccess.unSaveOneWord(GlobalVariables.userId, enWord.getId());
//                    databaseAccess.close();
//
//                    btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_bookmark_outline_32px);
//                    unsave = !unsave;
//                } else {
//                    //---run save code
//                    HashMap<String, Object> map = new HashMap<>();
//                    map.put("user_id", GlobalVariables.userId);
//                    map.put("word_id", enWord.getId());
//                    GlobalVariables.db.collection("saved_word")
//                            .document(GlobalVariables.userId + enWord.getId() + "").set(map, SetOptions.merge())
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    Toast.makeText(EnWordDetailActivity2.this, "Lưu từ thành công", Toast.LENGTH_LONG).show();
//                                    //them ca vao trong nay cho de dung
//                                }
//
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(EnWordDetailActivity2.this, "Lưu từ khong thành công", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    GlobalVariables.listSavedWordId.add((enWord.getId()));
//
//                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
//                    databaseAccess.open();
//                    databaseAccess.saveOneWord(GlobalVariables.userId, enWord.getId());
//                    databaseAccess.close();
//
//                    btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
//                    unsave = !unsave;
//                }
//            }
//        });

    }

    private void setControl() {

        topNavigation = findViewById(R.id.topNavigation);
        fragmentManager = getSupportFragmentManager();
        enWordId = Long.valueOf(getIntent().getIntExtra("enWordId", -1));
//        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
//        databaseAccess.open();
//        enWord = databaseAccess.getOneEnWord(enWordId);
//        databaseAccess.close();
        textViewTitle = findViewById(R.id.textViewTitle);
        btnSave_UnsaveWord = findViewById(R.id.btnSave_UnsaveWord);
        btnBackToSavedWord = findViewById(R.id.imgBtnBackToSavedWord);
        unsave = true;
    }
    public void getOneEnWord(int enWordId){
        String url = "http://10.0.2.2:7000/enwords/"+enWordId;
        System.out.println("---------------------------------------------------"+url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                getData(response);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EnWordDetailActivity2.this, error.toString()+"Fail to get the data..", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(EnWordDetailActivity2.this);
        requestQueue.add(request);
    }

    public void getData(JSONObject response) {
        try{
                JSONObject object = response.getJSONObject("data");
                EnWord enWord = new EnWord();
                enWord.setWord(object.getString("word"));
                enWord.setId(object.getLong("id"));
                enWord.setViews(object.getInt("views"));
                enWord.setPronunciation(object.getString("pronunciation"));

                JSONArray meaningArray = object.getJSONArray("meanings");
                ArrayList<Meaning> listMeaning = new ArrayList<>();

                for(int j=0; j<meaningArray.length(); j=j+1) {
                    JSONObject objectMeaning = meaningArray.getJSONObject(j);
                    JSONObject objectPartOfSpeech = objectMeaning.getJSONObject("partOfSpeech");
                    Meaning meaning = new Meaning();

                    meaning.setMeaning(objectMeaning.getString("meaning"));
                    meaning.setId((int) objectMeaning.getLong("id"));
                    meaning.setPartOfSpeechName(objectPartOfSpeech.getString("name"));

                    // bắst trường hợp k có example
                    JSONArray exampleArray = new JSONArray();
                    ArrayList<ExampleDetail> listExample = new ArrayList<>();
                    try{
                        exampleArray = objectMeaning.getJSONArray("examples");
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    for(int k=0; k<exampleArray.length(); k=k+1) {
                        JSONObject exampleObject = exampleArray.getJSONObject(k);
                        ExampleDetail exampleDetail = new ExampleDetail();
                        exampleDetail.setId((int) exampleObject.getLong("id"));
                        exampleDetail.setMeaningId(exampleObject.getInt("meaningId"));
                        exampleDetail.setExample(exampleObject.getString("example"));
                        exampleDetail.setExampleMeaning(exampleObject.getString("exampleMeaning"));

                        listExample.add(exampleDetail);
                    }

                    meaning.setListExampleDetails(listExample);
                    listMeaning.add(meaning);
                }
                enWord.setListMeaning(listMeaning);
            this.enWord = enWord;
            
            textViewTitle.setText(this.enWord.getWord().trim());

            setTitle(this.enWord.getWord().trim());

            assert getSupportActionBar() != null;   //null check
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
            getSupportActionBar().setElevation(0);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new EnWordDetailFragment(enWord)).commit();

            askPermission();

            TabLayout tabLayout = findViewById(R.id.tabLayout);
            ViewPager2 viewPager2 = findViewById(R.id.pager);

            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, Math.toIntExact(enWordId), enWord);
            viewPager2.setAdapter(viewPagerAdapter);
            new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                switch (position) {
                    case 0:
                        tab.setText("ENG-VIET");
                        break;
                    case 1:
                        tab.setText("HÌNH ẢNH");
                        break;
                    case 2:
                        tab.setText("GHI CHÚ");
                        break;

                }
            }).attach();

            //GHI LẠI LỊCH SỬ TRA TỪ
            List<Integer> wordList = FileIO2.readFromFile(this);
            if (wordList.contains(enWordId)) {
                wordList.remove(wordList.indexOf(enWordId));
            }
            wordList.add(0, Math.toIntExact(enWordId));
            FileIO2.writeToFile(wordList, this);
            // neu trong danh sach da luu thi to mau vang
            if (GlobalVariables.listSavedWordId.contains(enWord.getId())) {
                unsave = true;
                btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
            } else {
                unsave = false;
                btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_bookmark_outline_32px);
            }

            if (GlobalVariables.listSavedWordId.contains(enWord.getId())) {
                unsave = true;
                menuSave_unSaveWord.setIcon(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
            } else {
                unsave = false;
                menuSave_unSaveWord.setIcon(R.drawable.icons8_bookmark_outline_32px);
            }

            if (GlobalVariables.userId.equalsIgnoreCase("") || GlobalVariables.userId == null) {
                menuSave_unSaveWord.setVisible(false);
            } else {
                menuSave_unSaveWord.setVisible(true);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.enword_detail_2_menu, menu);
        optionsMenu = menu;
        menuSave_unSaveWord = (MenuItem) optionsMenu.findItem(R.id.menu_save_unsave);
        // chuyển xuống đây do get nhanh quá menusave_unsave còn chưa được map
        this.getOneEnWord(Math.toIntExact(enWordId));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:

                break;
            case R.id.menu_note:

                break;
            case R.id.menu_save_unsave:
                if (unsave == true) {
                    //---run unsave code -- dùng path variable
                    String url = "http://10.0.2.2:7000/savedwords/"+GlobalVariables.userId+"/"+ enWord.getId();
                    System.out.println("---------------------------------------------------"+url);
                    JsonArrayRequest request = new JsonArrayRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Toast.makeText(EnWordDetailActivity2.this, "Bỏ lưu từ thành công..", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(EnWordDetailActivity2.this, "Bỏ lưu từ thành công..", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(EnWordDetailActivity2.this, "Bỏ lưu từ thất bại..", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(mContext, "Fail to get the data..", Toast.LENGTH_SHORT).show();
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

                    RequestQueue requestQueue = Volley.newRequestQueue(EnWordDetailActivity2.this);
                    requestQueue.add(request);
                    
                    while(GlobalVariables.listSavedWordId.indexOf(enWordId)!=-1){
                        GlobalVariables.listSavedWordId.remove(GlobalVariables.listSavedWordId.indexOf(enWordId));
                    }

                    item.setIcon(R.drawable.icons8_bookmark_outline_32px);
                    unsave = !unsave;

                } else {
                    //-- luwu dùng json body
                    String url = "http://10.0.2.2:7000/savedwords";//+GlobalVariables.userId+"/"+enWord.getId();
                    try {

                        SavedWord savedWord = new SavedWord();
                        savedWord.setId(0L);
                        //
                        savedWord.setUserId(Long.valueOf(GlobalVariables.userId));
                        savedWord.setWordId(enWord.getId());
                        //
                        savedWord.getEnWord().setId(enWord.getId());
                        savedWord.getUser().setId((long) Integer.parseInt(GlobalVariables.userId));
                        RequestQueue requestQueue = Volley.newRequestQueue(EnWordDetailActivity2.this);

                        Gson gson = new Gson();
                        String jsonStr = gson.toJson(savedWord);
                        final String mRequestBody = jsonStr;
                        Log.i("Body",mRequestBody);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("LOG_RESPONSE", response);
                                Toast.makeText(EnWordDetailActivity2.this,"Lưu từ thành công", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                                Toast.makeText(EnWordDetailActivity2.this,"Lưu từ thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("authorization", "Bearer "+GlobalVariables.access_token);
//                    params.put("refresh_token", GlobalVariables.refresh_token);
                                return params;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
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

//                    //them ca vao trong nay cho de dung
                    GlobalVariables.listSavedWordId.add((enWord.getId()));
//
                    item.setIcon(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
                    unsave = !unsave;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_WRITE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(EnWordDetailActivity2.this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}