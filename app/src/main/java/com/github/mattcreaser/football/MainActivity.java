package com.github.mattcreaser.football;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.github.mattcreaser.football.http.NflService;
import com.github.mattcreaser.football.http.Player;
import com.github.mattcreaser.football.http.Players;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements FilterFragment.Adapter {

  private final PlayerAdapter mPlayerAdapter = new PlayerAdapter();
  private Subscription mPlayerSubscription;

  private String mFilterName = "";
  private boolean mShowDrafted = false;
  private boolean mShowFavouritesOnly;
  private EnumSet<Position> mFilterPositions = EnumSet.noneOf(Position.class);

  @BindView(R.id.list_players) RecyclerView mRecyclerView;
  @BindView(R.id.button_fetch_players) FloatingActionButton mButtonFetch;
  @BindView(R.id.button_filter) FloatingActionButton mButtonFilter;
  @BindView(R.id.button_delete) FloatingActionButton mButtonDelete;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setAdapter(mPlayerAdapter);
    ItemTouchHelper touchHelper = new ItemTouchHelper(new TouchCallback(mPlayerAdapter));
    touchHelper.attachToRecyclerView(mRecyclerView);
    fetchPlayers();
  }

  @OnClick(R.id.button_fetch_players)
  public void onFetchPlayers(FloatingActionButton button) {
    ObjectAnimator animator = ObjectAnimator.ofFloat(button, "rotation", 0, 360)
        .setDuration(1000);

    animator.setInterpolator(new LinearInterpolator());
    animator.setRepeatCount(ValueAnimator.INFINITE);
    animator.start();

    button.setEnabled(false);

    NflService service = FootballApplication.getNflService(this);
    List<Observable<Players>> observables = new ArrayList<>(3);
    for (int i = 0; i < 3; ++i) {
      observables.add(service.getPlayers(100, 100 * i));
    }

    Observable.merge(observables)
        .doOnNext(this::updatePlayers)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(players -> {
          animator.end();
          button.setEnabled(true);
        });
  }

  @OnClick(R.id.button_filter)
  public void showFilters(FloatingActionButton button) {
    FilterFragment fragment = new FilterFragment();
    Bundle bundle = new Bundle();
    bundle.putString("name", mFilterName);
    bundle.putBoolean("drafted", mShowDrafted);
    bundle.putSerializable("positions", mFilterPositions);
    bundle.putBoolean("favourites", mShowFavouritesOnly);
    fragment.setArguments(bundle);

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.add(R.id.main_container, fragment);
    transaction.addToBackStack("showFilters");
    transaction.commit();
  }

  @OnClick(R.id.button_delete)
  public void delete() {
    Realm realm = Realm.getDefaultInstance();
    realm.beginTransaction();
    realm.deleteAll();
    realm.commitTransaction();
    realm.close();
  }

  private void fetchPlayers() {
    if (mPlayerSubscription != null) {
      mPlayerSubscription.unsubscribe();
    }

    Realm realm = Realm.getDefaultInstance();

    RealmQuery<Player> query = realm.where(Player.class);

    if (!mShowDrafted) {
      query.equalTo("isDrafted", false);
    }

    if (mFilterName != null && !mFilterName.isEmpty()) {
      query.beginGroup()
          .contains("firstName", mFilterName, Case.INSENSITIVE)
          .or()
          .contains("lastName", mFilterName, Case.INSENSITIVE)
          .endGroup();
    }

    for (Position position : mFilterPositions) {
      log(position.name());
      query.beginGroup().not().equalTo("position", position.name()).endGroup();
    }

    if (mShowFavouritesOnly) {
      query.equalTo("isFavourite", true);
    }

    mPlayerSubscription = query
        .findAll()
        .sort("localRank")
        .asObservable()
        .subscribe(mPlayerAdapter::setPlayers);
  }

  private void updatePlayers(Players players) {
    Realm realm = Realm.getDefaultInstance();
    realm.beginTransaction();
    int rank = (int) realm.where(Player.class).count();
    for (Player player : players.getPlayers()) {
      Player fromRealm = realm.where(Player.class).equalTo("mId", player.getId()).findFirst();
      if (fromRealm != null) {
        fromRealm.setAdp(player.getAdp());
      } else {
        player.setLocalRank(rank++);
        realm.copyToRealmOrUpdate(player);
      }
    }
    realm.commitTransaction();
    realm.close();
  }

  private static void log(String message) {
    Log.d(MainActivity.class.getSimpleName(), message);
  }

  @Override public void fragmentAttached() {
    mButtonFilter.animate().scaleX(0).scaleY(0).setDuration(200).start();
    mButtonFetch.animate().scaleX(0).scaleY(0).setDuration(200).start();
    mButtonDelete.animate().scaleX(0).scaleY(0).setDuration(200).start();
  }

  @Override public void fragmentDetached() {
    mButtonFilter.animate().scaleX(1).scaleY(1).setDuration(200).start();
    mButtonFetch.animate().scaleX(1).scaleY(1).setDuration(200).start();
    mButtonDelete.animate().scaleX(1).scaleY(1).setDuration(200).start();
  }

  @Override public void playerFilterName(CharSequence name) {
    if (!mFilterName.equals(name)) {
      mFilterName = name.toString();
      fetchPlayers();
    }
  }

  @Override public void showDrafted(boolean drafted) {
    if (drafted != mShowDrafted) {
      mShowDrafted = drafted;
      fetchPlayers();
    }
  }

  @Override public void showFavourites(boolean favouritesOnly) {
    mShowFavouritesOnly = favouritesOnly;
    fetchPlayers();
  }

  @Override public void setPositions(EnumSet<Position> positions) {
    mFilterPositions = positions;
    fetchPlayers();
  }
}
