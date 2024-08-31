package com.lampro.mymusic.interfaces;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;

public interface IGetBitmapImage {
    Bitmap getBitmapImage(Uri uri);
}
