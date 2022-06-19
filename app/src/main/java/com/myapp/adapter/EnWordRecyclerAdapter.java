package com.myapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.myapp.GlobalVariables;
import com.myapp.R;
import com.myapp.model.SavedWord;
import com.myapp.model.User;
import com.myapp.dictionary.DictionaryActivity;
import com.myapp.dictionary.EnWordDetailActivity2;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.model.EnWord;
import com.myapp.utils.FileIO2;
import com.myapp.utils.TTS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnWordRecyclerAdapter extends
        RecyclerView.Adapter<EnWordRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<EnWord> enWordArrayList;
    private TTS tts;

    public EnWordRecyclerAdapter(Context mContext, ArrayList<EnWord> enWordArrayList) {
        this.mContext = mContext;
        this.enWordArrayList = enWordArrayList;
        this.tts = new TTS(mContext);
    }

    public EnWordRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View heroView = inflater.inflate(R.layout.en_word_item_for_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        EnWord enWord = enWordArrayList.get(viewHolder.getAbsoluteAdapterPosition());

        viewHolder.textViewWord.setText(enWord.getWord().trim());
        viewHolder.textViewPronunciation.setText(enWord.getPronunciation().trim());
        try {
            viewHolder.textViewMeaning.setText(enWord.getListMeaning().get(0).getMeaning().trim());

        } catch (Exception ex) {
        }

//        viewHolder.buttonWordMenu.setVisibility(View.GONE);
        viewHolder.buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(enWordArrayList.get(viewHolder.getAbsoluteAdapterPosition()).getWord());
            }
        });
        // neu trong danh sach da luu thi to mau vang
        if (GlobalVariables.listSavedWordId.contains(enWord.getId())) {
            viewHolder.unsave = true;
            viewHolder.btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
        } else {
            viewHolder.unsave = false;
            viewHolder.btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_bookmark_outline_32px);
        }
        if (GlobalVariables.userId.equalsIgnoreCase("") || GlobalVariables.userId == null) {
            viewHolder.btnSave_UnsaveWord.setVisibility(View.GONE);
        } else {
            viewHolder.btnSave_UnsaveWord.setVisibility(View.VISIBLE);
        }
        viewHolder.btnSave_UnsaveWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sau này check trong saved word
                if (viewHolder.unsave == true) {
                    //---run unsave code -- dùng path variable
                        String url = "http://10.0.2.2:7000/savedwords/"+GlobalVariables.userId+"/"+enWord.getId();
                        System.out.println("---------------------------------------------------"+url);
                        JsonArrayRequest request = new JsonArrayRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
//                                Toast.makeText(mContext, "Bỏ lưu từ thành công..", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mContext, "Bỏ lưu từ thành công..", Toast.LENGTH_SHORT).show();
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

                        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                        requestQueue.add(request);


                    while(GlobalVariables.listSavedWordId.indexOf(enWord.getId())!=-1){
                        GlobalVariables.listSavedWordId.remove(GlobalVariables.listSavedWordId.indexOf(enWord.getId()));

                    }
                    viewHolder.btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_bookmark_outline_32px);
                    viewHolder.unsave = !viewHolder.unsave;
                } else {
                    //-- dùng json body
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
                        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

                        Gson gson = new Gson();
                        String jsonStr = gson.toJson(savedWord);

                        Log.i("body", jsonStr);

                        final String mRequestBody = jsonStr;
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("LOG_RESPONSE", response);
                                Toast.makeText(mContext, "Lưu từ thành công", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());
                                Toast.makeText(mContext, "Lưu từ thất bại", Toast.LENGTH_SHORT).show();
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


//                    //them ca vao trong nay cho de dung
                    GlobalVariables.listSavedWordId.add(enWord.getId());

                    viewHolder.btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
                    viewHolder.unsave = !viewHolder.unsave;
                }

            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), EnWordDetailActivity.class);
                Intent intent = new Intent(view.getContext(), EnWordDetailActivity2.class);
                Integer enWordId = Math.toIntExact(enWordArrayList.get(viewHolder.getAbsoluteAdapterPosition()).getId());
                intent.putExtra("enWordId", enWordId);
                view.getContext().startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public int getItemCount() {
        return enWordArrayList.size();
    }

    public void filterList(ArrayList<EnWord> filterllist) {
        if (filterllist.isEmpty()) {
            System.out.println("ket qua tim kiem = 0" + filterllist.size());
            return;
        }
        if (enWordArrayList.isEmpty()) {
            return;
        }

        enWordArrayList = filterllist;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewWord;
        private TextView textViewPronunciation;
        private TextView textViewMeaning;
        private ImageButton buttonSpeak;
        private ImageButton buttonWordMenu;
        private ImageButton btnSave_UnsaveWord;
        private boolean unsave;
//        private LinearLayout enWordItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWord = (TextView) itemView.findViewById(R.id.textViewWord);
            textViewPronunciation = (TextView) itemView.findViewById(R.id.textViewPronunciation);
            textViewMeaning = (TextView) itemView.findViewById(R.id.textViewMeaning);
            buttonSpeak = (ImageButton) itemView.findViewById(R.id.buttonSpeak);
            buttonWordMenu = (ImageButton) itemView.findViewById(R.id.buttonWordMenu);
            btnSave_UnsaveWord = (ImageButton) itemView.findViewById(R.id.btnSave_UnsaveWord);
//            enWordItemLayout = (LinearLayout) itemView.findViewById(R.id.enWordItemLayout);
            //lấy dữ liệu thật sau

            unsave = true;
        }
    }
}
