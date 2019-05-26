package pl.thathard.drop.manager;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import pl.thathard.core.Core;
import pl.thathard.drop.player.DropPlayer;

public class PlayerManager {

  @Getter
  public static final LoadingCache<UUID, DropPlayer> players = CacheBuilder.newBuilder()
      .maximumSize(Core.getPlugin(Core.class).getCoreConfig().getPlayerCacheSize())
      .expireAfterWrite(15, TimeUnit.SECONDS)
      .build(new CacheLoader<UUID, DropPlayer>() {
        public DropPlayer load(@NotNull UUID uuid) {
          DropPlayer dropPlayer = new DropPlayer(uuid);
          dropPlayer.load();
          return dropPlayer;
        }
      });

}
