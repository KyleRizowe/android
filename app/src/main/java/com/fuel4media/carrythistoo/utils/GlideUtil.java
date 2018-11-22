package com.fuel4media.carrythistoo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;


/**
 * Created by craterzone on 24/9/16.
 */

public class GlideUtil {


    /**
     * @param context
     * @param imageView
     * @param url
     */
    public static void loadImageCirculer(final Context context, final ImageView imageView, String url, int placeholder) {
        Glide.with(context).load(url).asBitmap().centerCrop().placeholder(placeholder).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    public static void loadImageCirculer(final Context context, final ImageView imageView, Uri url, int placeholder) {
        Glide.with(context).load(url).asBitmap().placeholder(placeholder).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    /* public static void loadImageWithDefaultImage(final Context context, final ImageView imageView, String url, int defaultImage) {
     *//* Glide.with(context)
                .load(url)
                .asBitmap()
                .centerCrop()
                .error(defaultImage)
                .fitCenter()
                .placeholder(defaultImage)
                .into(imageView);*//*
        PicassoTrsutAll.getInstance(context).load(url).placeholder(defaultImage).into(imageView);
       // Glide.with(context).load(url).into(imageView);
    }*/


    public static void loadImageWithDefaultImageFromDrawable(final Context context, final ImageView imageView, Drawable drawable, int defaultImage) {
        Glide.with(context)
                .load("")
                .asBitmap()
                .placeholder(drawable)
                .centerCrop()
                .error(defaultImage)
                .fitCenter()
                .placeholder(defaultImage)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    public static void loadImage(final Context context, final ImageView imageView, String url) {

        Glide.with(context)
                .load(url)
                .asBitmap()
                .into(imageView);
    }

    public static void loadImage(final Context context, final ImageView imageView, String url, int placeholder) {

        Glide.with(context)
                .load(url)
                .asBitmap()
                //.skipMemoryCache(true)
                .placeholder(placeholder)
                //.signature(new StringSignature(System.currentTimeMillis() + ""))
                .into(imageView);
    }

    public static void loadImage(final Context context, final ImageView imageView, byte[] bytes) {

        Glide.with(context)
                .load(bytes)
                .asBitmap()
                .into(imageView);
    }

    public static void loadImageWithPlaceHolder(final Context context, final ImageView imageView, byte[] bytes, int placeholder) {

        Glide.with(context)
                .load(bytes)
                .asBitmap()
                .placeholder(placeholder)
                .into(imageView);
    }

    public static void loadImageWithPlaceHolder(final Context context, final ImageView imageView, String url, int placeholder) {

        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(placeholder)
                .into(imageView);
    }

    public static void loadImageWithPlaceHolder(final Context context, final ImageView imageView, Uri uri, int placeholder) {

        Glide.with(context)
                .load(uri)
                .asBitmap()
                .placeholder(placeholder)
                .skipMemoryCache(true)
                .into(imageView);
    }

    public static void loadImage(final Context context, final ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .dontTransform()
                .skipMemoryCache(true)
                .into(imageView);
    }


    public static void loadCircleImage(final Context context, final ImageView imageView, String url, int defaultImage) {
        Glide.with(context).load(url).asBitmap().centerCrop().placeholder(defaultImage).fitCenter().into(imageView);
    }

    public static void loadCircleImage(final Context context, final ImageView imageView, Uri uri, int defaultImage) {
        Glide.with(context).load(uri).asBitmap().centerCrop().placeholder(defaultImage).fitCenter().into(imageView);
    }

    public static void loadCircleImage(final Context context, final ImageView imageView, String url) {
        Glide.with(context).load(url).asBitmap().centerCrop().fitCenter().into(imageView);
    }

    public static void loadImage(final Context context, String url, final View view, int viewWidth, int viewHeight) {
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>(viewWidth, viewHeight) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(drawable);
                }
            }
        });
    }

    public static void loadImage(final Context context, Uri uri, final View view, int viewWidth, int viewHeight) {
        Glide.with(context).load(uri).asBitmap().into(new SimpleTarget<Bitmap>(viewWidth, viewHeight) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(drawable);
                }
            }
        });
    }

    public static void loadImage(final Context context, byte[] bytes, final View view, int viewWidth, int viewHeight) {
        Glide.with(context).load(bytes).asBitmap().into(new SimpleTarget<Bitmap>(viewWidth, viewHeight) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(drawable);
                }
            }
        });
    }


}
