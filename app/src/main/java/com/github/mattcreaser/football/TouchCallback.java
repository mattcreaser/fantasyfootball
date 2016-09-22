package com.github.mattcreaser.football;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 *
 */
public class TouchCallback extends ItemTouchHelper.Callback {

  private final Adapter mAdapter;

  public TouchCallback(Adapter adapter) {
    mAdapter = adapter;
  }

  @Override
  public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    int swipeFlags = ItemTouchHelper.START;
    return makeMovementFlags(dragFlags, swipeFlags);
  }

  @Override
  public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
    mAdapter.onItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    return true;
  }

  @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    mAdapter.onPlayerSwiped(viewHolder.getAdapterPosition());
  }

  @Override public boolean isItemViewSwipeEnabled() {
    return true;
  }

  @Override public boolean isLongPressDragEnabled() {
    return true;
  }

  public interface Adapter {
    void onPlayerSwiped(int position);

    void onItemMoved(int fromPosition, int toPosition);
  }
}
