package com.gb_products.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.Target;
import com.gb_products.R;
import com.gb_products.api.Models.ProductModel;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<ProductModel> data;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean showLoader = true;

    public ProductsAdapter(Context context , ArrayList<ProductModel> data) {
        this.context = context;
        this.data =data ;
    }

    public ArrayList<ProductModel> getData() {
        return data;
    }

    public void setData(ArrayList<ProductModel> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.prod_item, parent, false);
        viewHolder = new ProductViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("type",getItemViewType(position)+"") ;


        switch (getItemViewType(position)) {
            case ITEM:
                final ProductModel productModel = data.get(position);
                final ProductViewHolder ProductViewHolder = (ProductViewHolder) holder;
                ProductViewHolder.mProductImg.setImageDrawable(null);
                ProductViewHolder.mProductImg.setImageBitmap(null);
                ProductViewHolder.mProductImg.post(new Runnable() {
                    @Override
                    public void run() {
                        ViewGroup.LayoutParams layoutParams =  ProductViewHolder.mProductImg.getLayoutParams() ;
                        layoutParams.height = (int) (productModel.getImgModel().getHeight() * Resources.getSystem().getDisplayMetrics().density);
                        ProductViewHolder.mProductImg.setLayoutParams(layoutParams);
                    }
                });


                ProductViewHolder.mProductPrice.setText(productModel.getPrice());
                ProductViewHolder.mProductDesc.setText(productModel.getProductDescription());

                Log.e("model" , productModel.getId() +"   ");
                  Glide
                          .with(context)
                          .load(productModel.getImgModel().getUrl())
                          .diskCacheStrategy(DiskCacheStrategy.ALL)
                          .centerCrop()
                          .into(ProductViewHolder.mProductImg);

                break;

            case LOADING:
                final LoadingViewHolder loaderViewHolder = (LoadingViewHolder) holder;
                if (showLoader) {
                    loaderViewHolder.mProgressBar.setVisibility(View.VISIBLE);
                } else {
                    loaderViewHolder.mProgressBar.setVisibility(View.GONE);
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        // If no items are present, there's no need for loader
        if (data == null || data.size() == 0) {
            return 0;
        }

        // +1 for loader
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position != 0 && position == getItemCount() - 1  ? LOADING : ITEM ;
    }


    public void remove(ProductModel r) {
        int position = data.indexOf(r);
        if (position > -1) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void showLoading(boolean status) {
        showLoader = status;
    }
    public ProductModel getItem(int position) {
        return data.get(position);
    }


   /*
   View Holders
    */

    /**
     * Main ArrayList's content ViewHolder
     */
    protected class ProductViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.prod_price)
         TextView mProductPrice;
        @BindView(R.id.prod_desc)
         TextView mProductDesc;
        @BindView(R.id.prod_img)
         ImageView mProductImg;

        public ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this , itemView);
        }
    }


    protected class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.loadmore_progress)
        View mProgressBar ;
        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
