package com.github.mattcreaser.football;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mattcreaser.football.http.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 *
 */
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> implements TouchCallback.Adapter {

  private List<Player> mPlayers = new ArrayList<>();
  private Realm mRealm = Realm.getDefaultInstance();

  public PlayerAdapter() {
    setHasStableIds(true);
  }

  public void setPlayers(List<Player> players) {
    mPlayers = new ArrayList<>(players.size());
    mPlayers.addAll(players);
    notifyDataSetChanged();
  }

  @Override
  public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
    return new PlayerViewHolder(view);
  }

  @Override public void onBindViewHolder(PlayerViewHolder holder, int position) {
    Context context = holder.itemView.getContext();

    Player player = mPlayers.get(position);
    holder.mFirstName.setText(player.getFirstName());
    holder.mLastName.setText(player.getLastName());
    holder.mAdp.setText("ADP " + player.getAdp());
    holder.mPosition.setText(player.getPosition());

    if (player.isFavourite()) {
      holder.mFavourite.setImageResource(R.drawable.ic_star_orange_900_18dp);
    } else {
      holder.mFavourite.setImageResource(R.drawable.ic_star_border_black_18dp);
    }

    if (player.isDrafted()) {
      holder.mLastName.setPaintFlags(holder.mLastName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    } else {
      holder.mLastName.setPaintFlags(holder.mLastName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
    }

    Resources resources = context.getResources();
    int resourceId = resources.getIdentifier(player.getTeamAbbr().toLowerCase(), "drawable", context.getPackageName());
    if (resourceId != 0) { // FA players have no team
      holder.mLogo.setImageResource(resourceId);
    } else {
      holder.mLogo.setImageDrawable(null);
    }

    holder.itemView.setBackgroundColor(
        ContextCompat.getColor(context, Position.fromString(player.getPosition()).getColorId()));

    holder.setPlayer(player);
  }

  @Override public int getItemCount() {
    return mPlayers.size();
  }

  @Override public long getItemId(int position) {
    return mPlayers.get(position).getId();
  }

  public Player getPlayer(int position) {
    return mPlayers.get(position);
  }

  @Override public void onPlayerSwiped(int position) {
    Realm realm = Realm.getDefaultInstance();
    realm.beginTransaction();
    Player player = mPlayers.get(position);
    player.setDrafted(true);
    realm.commitTransaction();
    realm.close();
  }

  @Override public void onItemMoved(int fromPosition, int toPosition) {
    Player from = mPlayers.get(fromPosition);
    Player to = mPlayers.get(toPosition);

    int rankFrom = from.getLocalRank();
    int rankTo = to.getLocalRank();

    mRealm.beginTransaction();

    if (rankFrom > rankTo) {
      RealmResults<Player> players = mRealm.where(Player.class).greaterThanOrEqualTo("localRank", rankTo)
          .lessThan("localRank", rankFrom).findAll();
      for (Player player : players) {
        player.setLocalRank(player.getLocalRank() + 1);
      }
    } else {
      RealmResults<Player> players = mRealm.where(Player.class).greaterThan("localRank", rankFrom)
          .lessThanOrEqualTo("localRank", rankTo).findAll();
      for (Player player : players) {
        player.setLocalRank(player.getLocalRank() - 1);
      }
    }
    from.setLocalRank(rankTo);

    if (fromPosition < toPosition) {
      for (int i = fromPosition; i < toPosition; i++) {
        Collections.swap(mPlayers, i, i + 1);
      }
    } else {
      for (int i = fromPosition; i > toPosition; i--) {
        Collections.swap(mPlayers, i, i - 1);
      }
    }
    notifyItemMoved(fromPosition, toPosition);

    mRealm.commitTransaction();
  }

  public static class PlayerViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.player_firstname) TextView mFirstName;
    @BindView(R.id.player_lastname) TextView mLastName;
    @BindView(R.id.player_adp) TextView mAdp;
    @BindView(R.id.player_logo) ImageView mLogo;
    @BindView(R.id.player_position) TextView mPosition;
    @BindView(R.id.player_favourite) ImageView mFavourite;

    private Player mPlayer;

    public PlayerViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.player_favourite)
    public void onClickFavourite() {
      Realm realm = Realm.getDefaultInstance();
      realm.beginTransaction();
      mPlayer.setFavourite(!mPlayer.isFavourite());
      realm.commitTransaction();
      realm.close();
    }

    private void setPlayer(Player player) {
      mPlayer = player;
    }
  }
}
