package pl.thathard.drop.listener;

import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.thathard.drop.manager.PlayerManager;

@RequiredArgsConstructor
public class ConnectionListener implements Listener {

  @EventHandler
  public void onPreLogin(AsyncPlayerPreLoginEvent event) {
    try {
      PlayerManager.getPlayers().get(event.getUniqueId());
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    try {
      PlayerManager.getPlayers().get(event.getPlayer().getUniqueId()).save();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
  }
}
