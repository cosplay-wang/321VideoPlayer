package com.demo.duke.videoplayer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.demo.duke.videoplayer.domain.SplashBean;
import com.demo.duke.videoplayer.http.HttpUtils;
import com.demo.duke.videoplayer.util.JsonUtil;
import com.demo.duke.videoplayer.util.PreferencesUtil;
import com.demo.duke.videoplayer.util.UrlUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "spalshThreadName";
    private Handler handler = new Handler();
    private boolean isStartMain = false;
    ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemBarTransparent();
        setContentView(R.layout.activity_splash);
        splashImage = findViewById(R.id.splash_image);
        PreferencesUtil.init(this);
        doData();

    }

    private void setSystemBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP解决方案
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // KITKAT解决方案
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void doData() {
        DisposableObserver<SplashBean> observer = new DisposableObserver<SplashBean>() {
            @Override
            public void onNext(SplashBean value) {
                String url = value.images.get(0).url;
                url = url.substring(0, url.length() - 13);
                url = url + "1080x1920.jpg";
                Glide.with(SplashActivity.this).load(UrlUtil.SPLASH_BASEURL + url).error(R.drawable.default_splash_1).into(splashImage);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO: 2017/12/25  三秒跳主页
                        startMainActivity();
                        Log.e(TAG, "当前线程名字：" + Thread.currentThread().getName());
                    }
                }, 3 * 1000);
            }

            @Override
            public void onError(Throwable e) {
                //progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {
                // progressBar.setVisibility(View.GONE);
            }
        };
        Observable<SplashBean> observable = Observable.create(
                new ObservableOnSubscribe<SplashBean>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<SplashBean> rx) throws Exception {
                        getdata(UrlUtil.SPLASH_URL, rx);
                    }
                });
        observable.onTerminateDetach()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);

    }

    public void getdata(String url, final ObservableEmitter<SplashBean> rx) throws Exception {

        Response response = null;
        response = HttpUtils.okHttpRequest(url, null, null);
        if (response.isSuccessful()) {
            String responses = response.body().string();
            SplashBean splashBean = JsonUtil.GsonToBean(responses, SplashBean.class);
            rx.onNext(splashBean);

        } else {
            rx.onError(new NullPointerException("错误返回码：" + response.code()));
        }

    }

    public SplashBean getdata(String url) throws Exception {

        Response response = null;
        SplashBean splashBean = null;
        response = HttpUtils.okHttpRequest(url, null, null);
        if (response.isSuccessful()) {
            String responses = response.body().string();
            splashBean = JsonUtil.GsonToBean(responses, SplashBean.class);
        }
        return splashBean;

    }


    /**
     * 跳转mainactivity  ，并且关闭当前activity
     */
    private void startMainActivity() {

        if (!isStartMain) {
            isStartMain = true;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startMainActivity();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
