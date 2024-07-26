package com.lampro.mymusic.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomItemSongDecoration extends RecyclerView.ItemDecoration {
    private final int valueTop,valueBottom;

    public CustomItemSongDecoration(int valueTop,int valueBottom) {
        this.valueTop= valueTop;
        this.valueBottom= valueBottom;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = valueTop;
        }

        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom= valueBottom;
        }
    }
}
