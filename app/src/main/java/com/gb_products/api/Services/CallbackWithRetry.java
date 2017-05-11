package com.gb_products.api.Services;

import android.os.Handler;
import android.util.Log;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import timber.log.Timber;

/**
 * Created by sotra on 5/9/2017.
 */
public abstract class CallbackWithRetry<T> implements Callback<T> {

    private  final int TOTAL_RETRIES ;
    private  final int INTERVAL  ;
    private static final String TAG = CallbackWithRetry.class.getSimpleName();
    private final Call<T> call;
    private int retryCount = 0;


    public CallbackWithRetry(int TOTAL_RETRIES, int INTERVAL, Call<T> call) {
        this.TOTAL_RETRIES = TOTAL_RETRIES;
        this.INTERVAL = INTERVAL;
        this.call = call;
    }

    @Override
    public void onFailure(Call<T> call , Throwable t) {



        if (t instanceof SocketTimeoutException){
        Log.e(TAG, t.getMessage() + "");
        if (retryCount++ < TOTAL_RETRIES) {
            Timber.e( t,"Retrying... (" + retryCount + " out of " + TOTAL_RETRIES + ")");
        }else {
            retry();

        }
        }

    }

    private void retry() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                call.clone().enqueue(CallbackWithRetry.this);
            }
        },INTERVAL);
    }
}
