package pl.thathard.drop.config;

import lombok.Getter;
import pl.thathard.drop.Drop;

@Getter
public class DropConfig {

  private final int playerCacheSize;
  private final String mongoLink;
  private final String mainColor;
  private final String secondaryColor;
  private final String valueColor;


  public DropConfig(Drop drop) {
    playerCacheSize = drop.getConfig().getInt("player_cache_size");
    mongoLink = drop.getConfig().getString("mongo_link");
    mainColor = drop.getConfig().getString("main_color");
    secondaryColor = drop.getConfig().getString("secondary_color");
    valueColor = drop.getConfig().getString("value_color");
  }
}
