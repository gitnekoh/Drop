package pl.thathard.drop;

import com.qrakn.honcho.Honcho;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.thathard.drop.command.DropCommand;
import pl.thathard.drop.config.DropConfig;
import pl.thathard.drop.data.DataService;
import pl.thathard.drop.listener.ConnectionListener;
import pl.thathard.drop.listener.DropListener;

@Getter
public final class Drop extends JavaPlugin {

  private final ExecutorService dataExecutor = Executors
      .newFixedThreadPool(Runtime.getRuntime().availableProcessors());
  private DataService dataService;
  private DropConfig dropConfig;

  @Override
  public void onEnable() {
    this.saveDefaultConfig();
    this.dropConfig = new DropConfig(this);
    this.dataService = new DataService(this);
    this.registerHoncho();
    Bukkit.getPluginManager().registerEvents(new ConnectionListener(), this);
    Bukkit.getPluginManager().registerEvents(new DropListener(this), this);
  }

  @Override
  public void onDisable() {
    this.dataService.getMongoClient().close();
    this.dataExecutor.shutdown();
  }

  private void registerHoncho() {
    Honcho manager = new Honcho(this);
    manager.registerCommand(new DropCommand());
  }
}
