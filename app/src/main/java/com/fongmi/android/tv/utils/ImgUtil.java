package com.fongmi.android.tv.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.fongmi.android.tv.App;
import com.fongmi.android.tv.R;

import java.io.ByteArrayOutputStream;

public class ImgUtil {

    public static void load(String url, ImageView view) {
        view.setScaleType(ImageView.ScaleType.CENTER);
        if (TextUtils.isEmpty(url)) view.setImageResource(R.drawable.ic_img_error);
        else Glide.with(App.get()).asBitmap().load(getUrl(Utils.checkProxy(url))).skipMemoryCache(true).dontAnimate().sizeMultiplier(Prefers.getThumbnail()).signature(new ObjectKey(url + "_" + Prefers.getQuality())).placeholder(R.drawable.ic_img_loading).listener(getListener(view)).into(view);
    }

    public static void loadKeep(String url, ImageView view) {
        view.setScaleType(ImageView.ScaleType.CENTER);
        Glide.with(App.get()).asBitmap().load(Utils.checkProxy(url)).error(R.drawable.ic_img_error).placeholder(R.drawable.ic_img_loading).listener(getListener(view)).into(view);
    }

    public static void loadHistory(String url, ImageView view) {
        view.setScaleType(ImageView.ScaleType.CENTER);
        Glide.with(App.get()).asBitmap().load(Utils.checkProxy(url)).error(R.drawable.ic_img_error).placeholder(R.drawable.ic_img_loading).listener(getListener(view)).into(view);
    }

    public static void loadLive(String url, ImageView view) {
        view.setVisibility(TextUtils.isEmpty(url) ? View.GONE : View.VISIBLE);
        if (TextUtils.isEmpty(url)) view.setImageResource(R.drawable.ic_img_empty);
        else Glide.with(App.get()).asBitmap().load(url).skipMemoryCache(true).dontAnimate().signature(new ObjectKey(url)).error(R.drawable.ic_img_empty).into(view);
    }

    public static GlideUrl getUrl(String url) {
        String param = null;
        LazyHeaders.Builder builder = new LazyHeaders.Builder();
        if (url.contains("@Cookie=")) builder.addHeader("Cookie", param = url.split("@Cookie=")[1].split("@")[0]);
        if (url.contains("@Referer=")) builder.addHeader("Referer", param = url.split("@Referer=")[1].split("@")[0]);
        if (url.contains("@User-Agent=")) builder.addHeader("User-Agent", param = url.split("@User-Agent=")[1].split("@")[0]);
        return new GlideUrl(param == null ? url : url.split("@")[0], builder.build());
    }

    private static RequestListener<Bitmap> getListener(ImageView view) {
        return new RequestListener<>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                view.setScaleType(ImageView.ScaleType.CENTER);
                view.setImageResource(R.drawable.ic_img_error);
                return true;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return false;
            }
        };
    }

    public static byte[] resize(byte[] bytes) {
        int width = ResUtil.getScreenWidthPx();
        int height = ResUtil.getScreenHeightPx();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if (bitmap.getWidth() < width && bitmap.getHeight() < height) return bytes;
        Matrix matrix = new Matrix();
        boolean land = bitmap.getWidth() > bitmap.getHeight();
        matrix.postScale((float) width / bitmap.getWidth(), (float) height / bitmap.getHeight());
        bitmap = Bitmap.createBitmap(bitmap, land ? bitmap.getWidth() / 2 - bitmap.getHeight() / 2 : 0, 0, bitmap.getHeight(), bitmap.getHeight(), matrix, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
}
