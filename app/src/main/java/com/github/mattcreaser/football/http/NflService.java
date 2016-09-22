package com.github.mattcreaser.football.http;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 *
 */
public interface NflService {
  @GET("userdraftranks?format=json")
  Observable<Players> getPlayers(@Query("count") int count, @Query("offset") int offset);
}
