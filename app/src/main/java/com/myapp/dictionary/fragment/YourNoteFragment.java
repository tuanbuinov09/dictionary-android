package com.myapp.dictionary.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.myapp.GlobalVariables;
import com.myapp.Main;
import com.myapp.R;
import com.myapp.dictionary.EnWordDetailActivity2;
import com.myapp.model.SavedWord;
import com.myapp.model.UserNote;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YourNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YourNoteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText etYourNote;
    private Button btnSaveNote;
    private Button btnClearNote;
    private int enWordId;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public YourNoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YourNoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YourNoteFragment newInstance(String param1, String param2) {
        YourNoteFragment fragment = new YourNoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        enWordId = getArguments().getInt("enWordId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_your_note, container, false);
        final FragmentActivity c = getActivity();
        etYourNote = view.findViewById(R.id.etYourNote);
        btnSaveNote = view.findViewById(R.id.btnSaveNote);
        btnClearNote = view.findViewById(R.id.btnClearNote);
        getUserNote();
//        etYourNote.setText(GlobalVariables.userNote);
        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserNote(etYourNote.getText().toString().trim());
            }
        });
        btnClearNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserNote("");
                etYourNote.setText("");
            }
        });
//        return inflater.inflate(R.layout.fragment_your_note, container, false);
        return view;
    }

    public void getUserNote() {
        GlobalVariables.userNote="";
        //-- dùng json body
            String url = "http://10.0.2.2:8000/user-note?wordId="+enWordId+"&userId="+GlobalVariables.userId;//+GlobalVariables.userId+"/"+enWord.getId();
        try {

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getResponseData(response);
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Fail to get the data..", Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void getResponseData(String response){
        System.out.println(response);
        try {
                GlobalVariables.userNote= response;
                etYourNote.setText(GlobalVariables.userNote);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    public void saveUserNote(String content) {

        String url = "http://10.0.2.2:8000/user-note";//+GlobalVariables.userId+"/"+enWord.getId();
        try {

            UserNote userNote = new UserNote();
            userNote.setId(0);
            userNote.getEnWord().setId(enWordId);
            userNote.getUser().setId(Integer.parseInt(GlobalVariables.userId));
            userNote.setNote(content);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            Gson gson = new Gson();
            String jsonStr = gson.toJson(userNote);
            final String mRequestBody = jsonStr;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);
                    Toast.makeText(getActivity(),response, Toast.LENGTH_SHORT);
//                    GlobalVariables.userNote= response;
//                    etYourNote.setText(GlobalVariables.userNote);
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
//        GlobalVariables.db.collection("user_note").document(GlobalVariables.userId + enWordId + "").set(map, SetOptions.merge())
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        GlobalVariables.userNote = content;
//
//                        if (content.equalsIgnoreCase("")) {
//                            Toast.makeText(getActivity(), "Clear ghi chú thành công", Toast.LENGTH_LONG).show();
//                            return;
//                        }
//                        Toast.makeText(getActivity(), "Lưu ghi chú thành công", Toast.LENGTH_LONG).show();
//                    }
//
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                if (content.equalsIgnoreCase("")) {
//                    Toast.makeText(getActivity(), "Clear ghi chú thất bại", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                Toast.makeText(getActivity(), "Clear ghi chú thành công", Toast.LENGTH_LONG).show();
//            }
//        });
    }
}