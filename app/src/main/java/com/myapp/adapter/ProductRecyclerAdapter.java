package com.myapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.R;
import com.myapp.dictionary.ProductDetailActivity;
import com.myapp.model.Product;
import com.myapp.utils.TTS;

import java.util.ArrayList;

public class ProductRecyclerAdapter extends
        RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Product> productArrayList;
    private TTS tts;

    public ProductRecyclerAdapter(Context mContext, ArrayList<Product> productArrayList) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
    }

    public ProductRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View heroView = inflater.inflate(R.layout.product_item_for_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Product product = productArrayList.get(viewHolder.getAbsoluteAdapterPosition());

        viewHolder.textViewProductName.setText(product.getName());
        viewHolder.textViewPrice.setText(product.getPrice()+" ₫");
        viewHolder.textViewQuantity.setText(product.getQuantity()+"");
        viewHolder.textViewViews.setText(product.getViews()+"");


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), EnWordDetailActivity.class);
                Intent intent = new Intent(view.getContext(), ProductDetailActivity.class);
                Long productId = productArrayList.get(viewHolder.getAbsoluteAdapterPosition()).getId();
                intent.putExtra("productId", productId);
                view.getContext().startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public void filterList(ArrayList<Product> filterllist) {
        if (filterllist.isEmpty()) {
            System.out.println("ket qua tim kiem = 0" + filterllist.size());
            return;
        }
        if (productArrayList.isEmpty()) {
            return;
        }

        productArrayList = filterllist;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewProductName;
        private TextView textViewPrice;
        private TextView textViewQuantity;
        private TextView textViewViews;

//        private LinearLayout enWordItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = (TextView) itemView.findViewById(R.id.textViewProductName);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            textViewQuantity = (TextView) itemView.findViewById(R.id.textViewQuantity);
            textViewViews = (TextView) itemView.findViewById(R.id.textViewViews);
        }
    }
}
