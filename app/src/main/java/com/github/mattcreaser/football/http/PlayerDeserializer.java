package com.github.mattcreaser.football.http;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 *
 */
public class PlayerDeserializer implements JsonDeserializer<Player> {
  @Override
  public Player deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    Player player = new Gson().fromJson(json, Player.class);
    player.initId();
    return player;
  }
}
