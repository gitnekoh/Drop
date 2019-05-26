package pl.thathard.drop.player;

import java.util.concurrent.ThreadLocalRandom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
@Setter
public class ItemDrop {

  Material pickaxe;
  boolean enabled;
  ItemStack item;
  double points;
  double chance;
  int minY;

  public boolean drop(boolean turbo) {
    int rand = ThreadLocalRandom.current().nextInt(0, 10001);
    return (turbo ? this.chance * 3 : this.chance) * 100 >= rand;
  }

  public boolean pickaxe(Material material) {
    if (material.equals(Material.WOOD_PICKAXE)) {
      return false;
    } else if (material.equals(Material.STONE_PICKAXE)) {
      return this.pickaxe.equals(Material.STONE_PICKAXE) || this.pickaxe
          .equals(Material.WOOD_PICKAXE);
    } else if (material.equals(Material.IRON_PICKAXE) || material.equals(Material.GOLD_PICKAXE)) {
      return this.pickaxe.equals(Material.STONE_PICKAXE) || this.pickaxe
          .equals(Material.WOOD_PICKAXE) || this.pickaxe.equals(Material.IRON_PICKAXE)
          || this.pickaxe.equals(Material.GOLD_PICKAXE);
    } else {
      return material.equals(Material.DIAMOND_PICKAXE);
    }
  }
}
