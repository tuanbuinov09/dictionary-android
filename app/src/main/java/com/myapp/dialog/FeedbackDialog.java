package com.myapp.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.myapp.GlobalVariables;
import com.myapp.R;
import com.myapp.model.Feedback;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class FeedbackDialog extends BottomSheetDialogFragment {
    public enum Type {
        NOTIFICATION, CONFIRM
    }

    public enum Result {
        OK, CANCEL
    }

    public interface Listener {
        void sendDialogResult(Result result, String request);
    }

    View convertView = null;

    private Listener listener;
    private Type type;
    private String title, content;
    private String request;

    public FeedbackDialog(Type type, String title, String content, String request) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.request = request;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (Listener) getActivity();
        } catch (ClassCastException e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (type == Type.NOTIFICATION) {
//            convertView = inflater.inflate(R.layout.dialog_feedback, null);
//
//            EditText textEditContent = convertView.findViewById(R.id.textEditContent);
//            Button btnOK = convertView.findViewById(R.id.btnOK);
//            Button btnCancel = convertView.findViewById(R.id.btnCancel);
//            textEditContent.setText("");
//            btnOK.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.sendDialogResult(Result.OK, request);
//                    dismiss();
//                }
//            });
//        } else
        if (type == Type.CONFIRM) {
            convertView = inflater.inflate(R.layout.dialog_feedback, null);
            EditText textEditContent = convertView.findViewById(R.id.textEditContent);
            EditText textEditEmail = convertView.findViewById(R.id.textEditEmail);
            TextView tvTitle = convertView.findViewById(R.id.tvTitle);
            Button btnOK = convertView.findViewById(R.id.btnOK);
            Button btnCancel = convertView.findViewById(R.id.btnCancel);

            tvTitle.setText(this.title);
            textEditContent.setText("");
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "http://10.0.2.2:7000/feedbacks";//+GlobalVariables.userId+"/"+enWord.getId();
                    try {

                        Feedback feedback = new Feedback();
                        feedback.setUserId((long) Integer.parseInt(GlobalVariables.userId));
                        feedback.setContent(textEditContent.getText().toString().trim());
                        feedback.setEmail(textEditEmail.getText().toString().trim());
                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

                        Gson gson = new Gson();
                        String jsonStr = gson.toJson(feedback);

                        Log.i("body", jsonStr);

                        final String mRequestBody = jsonStr;
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("LOG_RESPONSE", response);
                                //
                                listener.sendDialogResult(Result.OK, request);
                                dismiss();
                                //
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //
                                listener.sendDialogResult(Result.OK, request);
                                dismiss();
                                //
                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("authorization", "Bearer " + GlobalVariables.access_token);
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
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.sendDialogResult(Result.CANCEL, request);
                    dismiss();
                }
            });
        }
        return convertView;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }
}
