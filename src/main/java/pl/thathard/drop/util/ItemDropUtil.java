package pl.thathard.drop.util;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import pl.thathard.core.util.InventoryUtil;
import pl.thathard.drop.player.ItemDrop;

public class ItemDropUtil {

  public static String serialize(List<ItemDrop> itemDropList) {
    StringBuilder sb = new StringBuilder();
    for (ItemDrop itemDrop : itemDropList) {
      sb.append(itemDrop.getPickaxe().toString());
      sb.append("!");
      sb.append(itemDrop.isEnabled());
      sb.append("!");
      sb.append(InventoryUtil.serialize(itemDrop.getItem()));
      sb.append("!");
      sb.append(itemDrop.getPoints());
      sb.append("!");
      sb.append(itemDrop.getChance());
      sb.append("!");
      sb.append(itemDrop.getMinY());
      sb.append("@");
    }
    return sb.toString();
  }

  public static List<ItemDrop> deserialize(String string) {
    String[] itemDropList = string.split("@");
    List<ItemDrop> toReturn = new ArrayList<>();
    for (String x : itemDropList) {
      String[] fields = x.split("!");
      ItemDrop itemDrop = new ItemDrop(Material.getMaterial(fields[0]), Boolean.valueOf(fields[1]),
          InventoryUtil.deserialize(fields[2]), Double.valueOf(fields[3]),
          Double.valueOf(fields[4]), Integer.valueOf(fields[5]));
      toReturn.add(itemDrop);
    }
    return toReturn;
  }
}
