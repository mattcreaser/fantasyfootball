package com.github.mattcreaser.football.http;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Players {

  @SerializedName("lastUpdated")
  @Expose
  private String lastUpdated;
  @SerializedName("players")
  @Expose
  private List<Player> players = new ArrayList<Player>();

  /**
   *
   * @return
   * The lastUpdated
   */
  public String getLastUpdated() {
    return lastUpdated;
  }

  /**
   *
   * @param lastUpdated
   * The lastUpdated
   */
  public void setLastUpdated(String lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  /**
   *
   * @return
   * The players
   */
  public List<Player> getPlayers() {
    return players;
  }

  /**
   *
   * @param players
   * The players
   */
  public void setPlayers(List<Player> players) {
    this.players = players;
  }

}