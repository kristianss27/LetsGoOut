package com.codepathgroup5.adapters;


public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position,int direction);
}
