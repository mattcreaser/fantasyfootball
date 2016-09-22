package com.github.mattcreaser.football;

import android.app.Application;
import android.content.Context;

import com.github.mattcreaser.football.http.NflService;
import com.github.mattcreaser.football.http.Player;
import com.github.mattcreaser.football.http.PlayerDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 */
public class FootballApplication extends Application {

  private NflService mNflService;

  @Override public void onCreate() {
    super.onCreate();

    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Player.class, new PlayerDeserializer())
        .excludeFieldsWithoutExposeAnnotation()
        .create();

    Retrofit retrofit = new Retrofit.Builder()
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("http://api.fantasy.nfl.com/v1/players/")
        .build();

    mNflService = retrofit.create(NflService.class);

    RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
        .deleteRealmIfMigrationNeeded()
        .build();
    Realm.setDefaultConfiguration(realmConfiguration);
  }

  public static NflService getNflService(Context context) {
    FootballApplication application = (FootballApplication) context.getApplicationContext();
    return application.mNflService;
  }
}
