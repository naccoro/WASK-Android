package com.naccoro.wask.ui.splash;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.naccoro.wask.R;
import com.naccoro.wask.preferences.SettingPreferenceManager;
import com.naccoro.wask.ui.main.MainActivity;
import com.naccoro.wask.utils.LanguageUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LanguageUtil.initLanguage(this);

        ImageView splashImageView = findViewById(R.id.imageview_splash);

        Glide.with(this)
                .load(R.drawable.wask_splash)
                .into(new DrawableImageViewTarget(splashImageView) {
                    @Override
                    public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        if (resource instanceof GifDrawable) {
                            //애니메이션 1번 반복
                            ((GifDrawable) resource).setLoopCount(1);

                            //애니메이션이 끝나면 Callback 등록
                            Animatable2Compat.AnimationCallback callback = new Animatable2Compat.AnimationCallback() {
                                @Override
                                public void onAnimationEnd(Drawable drawable) {
                                    super.onAnimationEnd(drawable);
                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            };
                            ((GifDrawable) resource).registerAnimationCallback(callback);
                        }

                        super.onResourceReady(resource, transition);
                    }
                });
    }

    private String getLanguageIdentifier(Context context, int languageIndex) {
        return context.getResources().getStringArray(R.array.LANGUASE_IDENTIFIER)[languageIndex];
    }
}