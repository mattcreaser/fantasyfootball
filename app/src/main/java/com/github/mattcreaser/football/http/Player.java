package com.github.mattcreaser.football.http;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@Generated("org.jsonschema2pojo")
public class Player extends RealmObject {

  @SerializedName("firstName") @Expose private String firstName;

  @SerializedName("lastName") @Expose private String lastName;

  @SerializedName("esbid") @Expose private String esbid;

  @SerializedName("gsisPlayerId") @Expose private String gsisPlayerId;

  @SerializedName("rank") @Expose private String adp;

  @SerializedName("aav") @Expose private String aav;

  @SerializedName("teamAbbr") @Expose private String teamAbbr;

  @SerializedName("position") @Expose private String position;

  @PrimaryKey private long mId;
  private boolean isDrafted;
  private boolean isFavourite;
  private int localRank;

  /**
   * @return The firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName The firstName
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return The lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName The lastName
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return The esbid
   */
  public String getEsbid() {
    return esbid;
  }

  /**
   * @param esbid The esbid
   */
  public void setEsbid(String esbid) {
    this.esbid = esbid;
  }

  /**
   * @return The gsisPlayerId
   */
  public String getGsisPlayerId() {
    return gsisPlayerId;
  }

  /**
   * @param gsisPlayerId The gsisPlayerId
   */
  public void setGsisPlayerId(String gsisPlayerId) {
    this.gsisPlayerId = gsisPlayerId;
  }

  /**
   * @return The adp
   */
  public String getAdp() {
    return adp;
  }

  /**
   * @param adp The adp
   */
  public void setAdp(String adp) {
    this.adp = adp;
  }

  /**
   * @return The aav
   */
  public String getAav() {
    return aav;
  }

  /**
   * @param aav The aav
   */
  public void setAav(String aav) {
    this.aav = aav;
  }

  /**
   * @return The teamAbbr
   */
  public String getTeamAbbr() {
    return teamAbbr;
  }

  /**
   * @param teamAbbr The teamAbbr
   */
  public void setTeamAbbr(String teamAbbr) {
    this.teamAbbr = teamAbbr;
  }

  /**
   * @return The position
   */
  public String getPosition() {
    return position;
  }

  /**
   * @param position The position
   */
  public void setPosition(String position) {
    this.position = position;
  }

  public long getId() {
    return mId;
  }

  public boolean isDrafted() {
    return isDrafted;
  }

  public void setDrafted(boolean drafted) {
    isDrafted = drafted;
  }

  public void setFavourite(boolean favourite) {
    isFavourite = favourite;
  }

  public boolean isFavourite() {
    return isFavourite;
  }

  public void setLocalRank(int localRank) {
    this.localRank = localRank;
  }

  public int getLocalRank() {
    return localRank;
  }

  void initId() {
    mId = (firstName + lastName + teamAbbr + position).hashCode();
  }

}