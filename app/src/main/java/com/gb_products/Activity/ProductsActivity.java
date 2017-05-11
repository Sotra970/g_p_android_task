package com.gb_products.Activity;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.gb_products.Adapters.ProductsAdapter;
import com.gb_products.R;
import com.gb_products.Utils.EndlessRecyclerViewScrollListener;
import com.gb_products.api.Models.ProductModel;
import com.gb_products.api.Services.CallbackWithRetry;
import com.gb_products.api.Services.Injector;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ProductsActivity extends AppCompatActivity {
    @BindView(R.id.products_recycler_view)
    RecyclerView products_list;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    ProductsAdapter adapter ;
    ArrayList<ProductModel> products = new ArrayList<>() ;
    private String TAG = "Products_Activity";
    private int currentPage = 0 ;
    private int pageFactor = 10 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);
        product_list_setup() ;
    }

    void product_list_setup(){
        adapter = new ProductsAdapter(this,products);
        getdata(0);
        setupRefreshLayout();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        products_list.setLayoutManager(staggeredGridLayoutManager);

        products_list.setItemAnimator(new DefaultItemAnimator());

        products_list.setAdapter(adapter);

        products_list.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(final int page, final int totalItemsCount, RecyclerView view) {
                Log.e("page",page +"  "+totalItemsCount);
                adapter.showLoading(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ;
                            currentPage = page;
                            getdata(page);


                        }
                    }, 2000);
                }

        });



        }


    private void setupRefreshLayout(){
        swipeRefresh.setColorSchemeColors(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                      products.clear();
                      adapter.notifyDataSetChanged();
                      getdata(0);
                  }
              },300) ;
            }
        });
    }

    private void endLoading() {
        if ( swipeRefresh.isRefreshing()){
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(false);
                }
            });
        }
    }


    void getdata( int page ){

            int from = page == 0 ? 1 : page*pageFactor ;
        Call<ArrayList<ProductModel>> call = Injector.provideProductService().getProducts(pageFactor , from)  ;
        call.enqueue(new CallbackWithRetry<ArrayList<ProductModel>>(5,1000,call) {
            @Override
            public void onResponse(Call<ArrayList<ProductModel>> call, Response<ArrayList<ProductModel>> response) {
                for (ProductModel ProductModel : response.body()) {
                    products.add(ProductModel);
                    adapter.notifyItemInserted(products.size()-1);
                }
                adapter.showLoading(false);
                endLoading();
            }
        });




    }




}
