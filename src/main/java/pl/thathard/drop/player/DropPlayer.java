package pl.thathard.drop.player;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.thathard.drop.Drop;
import pl.thathard.drop.gui.DropGUI;
import pl.thathard.drop.turbo.TurboDrop;
import pl.thathard.drop.util.ItemDropUtil;
import pl.thathard.drop.util.TurboUtil;

@Data
public class DropPlayer {

  private final UUID uuid;
  private TurboDrop turboDrop;
  private List<ItemDrop> itemDropList;
  private DropGUI dropGUI;
  private boolean inventoryDrop;
  private boolean fullyLoaded;
  private boolean chatInfo;
  private boolean cobble;
  private double points;

  public DropPlayer(UUID uuid) {
    this.uuid = uuid;
    this.itemDropList = new ArrayList<>();
    this.dropGUI = new DropGUI(this);
  }

  public void load() {
    Drop drop = Drop.getPlugin(Drop.class);

    drop.getDataExecutor().execute(() -> {
      Document found = drop.getDataService().getPlayers()
          .find(Filters.eq("uuid", this.uuid.toString())).first();

      if (found == null) {
        this.turboDrop = new TurboDrop(false, false, 0, 0);
        this.inventoryDrop = true;
        this.chatInfo = true;
        this.cobble = true;
        this.points = 0;

        ItemDrop diamondDrop = new ItemDrop(Material.IRON_PICKAXE, true,
            new ItemStack(Material.DIAMOND), 20, 1.0, 24);
        ItemDrop goldDrop = new ItemDrop(Material.IRON_PICKAXE, true,
            new ItemStack(Material.GOLD_ORE), 15, 0.8, 32);
        ItemDrop ironDrop = new ItemDrop(Material.STONE_PICKAXE, true,
            new ItemStack(Material.IRON_ORE), 10, 1.6, 48);
        ItemDrop emeraldDrop = new ItemDrop(Material.IRON_PICKAXE, true,
            new ItemStack(Material.EMERALD), 25, 0.75, 28);
        ItemDrop coalDrop = new ItemDrop(Material.STONE_PICKAXE, true,
            new ItemStack(Material.COAL), 7, 2.3, 60);
        ItemDrop slimeDrop = new ItemDrop(Material.STONE_PICKAXE, true,
            new ItemStack(Material.SLIME_BALL), 16, 0.85, 30);
        ItemDrop redstoneDrop = new ItemDrop(Material.STONE_PICKAXE, true,
            new ItemStack(Material.REDSTONE), 12, 2.0, 42);
        ItemDrop gunpowderDrop = new ItemDrop(Material.STONE_PICKAXE, true,
            new ItemStack(Material.SULPHUR), 25, 0.5, 54);

        this.itemDropList.addAll(Arrays
            .asList(diamondDrop, goldDrop, ironDrop, emeraldDrop, coalDrop, slimeDrop, redstoneDrop,
                gunpowderDrop));

        found = new Document("uuid", this.uuid.toString());
        found.append("turboDrop", TurboUtil.serialize(this.turboDrop)).append("itemDropList",
            ItemDropUtil.serialize(this.itemDropList)).append("inventoryDrop", this.inventoryDrop)
            .append("chatInfo", this.chatInfo).append("cobble", this.cobble)
            .append("points", this.points);

        drop.getDataService().getPlayers().insertOne(found);
      } else {
        this.turboDrop = TurboUtil.deserialize(found.getString("turboDrop"));
        this.itemDropList = ItemDropUtil.deserialize(found.getString("itemDropList"));
        this.inventoryDrop = found.getBoolean("inventoryDrop");
        this.chatInfo = found.getBoolean("chatInfo");
        this.cobble = found.getBoolean("cobble");
        this.points = found.getDouble("points");
      }
      Bukkit.getScheduler().runTask(drop, () -> this.fullyLoaded = true);
    });
  }

  public void save() {
    Drop drop = Drop.getPlugin(Drop.class);

    drop.getDataExecutor().execute(() -> {
      Document doc = new Document("uuid", this.uuid.toString());

      doc.append("turboDrop", TurboUtil.serialize(this.turboDrop)).append("itemDropList",
          ItemDropUtil.serialize(this.itemDropList)).append("inventoryDrop", this.inventoryDrop)
          .append("chatInfo", this.chatInfo).append("cobble", this.cobble)
          .append("points", this.points);

      drop.getDataService().getPlayers().replaceOne(Filters.eq("uuid", this.uuid.toString()), doc,
          new ReplaceOptions().upsert(true));
    });
  }
}
