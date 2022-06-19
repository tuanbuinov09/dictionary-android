package com.myapp.dictionary.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.myapp.GlobalVariables;
import com.myapp.R;
import com.myapp.adapter.ImageAdapter;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.model.EnWord;
import com.myapp.model.Image;
import com.myapp.utils.CustomSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductDescriptionFragment extends Fragment {

    View convertView;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager layoutManager;
    int enWordId;
    TextView textViewProductDescription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enWordId = getArguments().getInt("enWordId");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.fragment_product_description, container, false);
        recyclerView = convertView.findViewById(R.id.recyclerView);
        textViewProductDescription = convertView.findViewById(R.id.textViewProductDescription);

        return convertView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewProductDescription.setText(GlobalVariables.productDescription);
        if(GlobalVariables.productDescription==null||GlobalVariables.productDescription.equalsIgnoreCase("null")){
            textViewProductDescription.setText("");
        }
    }

    public void changeDes(){
        textViewProductDescription.setText(GlobalVariables.productDescription);
    }
}
