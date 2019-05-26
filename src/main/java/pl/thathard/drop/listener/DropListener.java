package pl.thathard.drop.listener;

import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import pl.thathard.core.util.CC;
import pl.thathard.drop.Drop;
import pl.thathard.drop.manager.PlayerManager;
import pl.thathard.drop.player.DropPlayer;

@RequiredArgsConstructor
public class DropListener implements Listener {

  private final Drop drop;

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    if (event.isCancelled()) {
      return;
    }

    if (event.getBlock().getType().toString().contains("ORE")) {
      event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
    }

    if (event.getBlock().getType() != Material.STONE) {
      return;
    }

    Player player = event.getPlayer();
    DropPlayer dropPlayer = PlayerManager.getPlayers().getUnchecked(player.getUniqueId());

    if (!dropPlayer.isCobble()) {
      event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
    } else {
      if (dropPlayer.isInventoryDrop()) {
        event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation())
            .setType(Material.AIR);
        player.getInventory().addItem(new ItemStack(Material.COBBLESTONE));
      }
    }
    int exp = 10;
    if (dropPlayer.getTurboDrop() != null) {
      if (dropPlayer.getTurboDrop().isExp() && dropPlayer.getTurboDrop().isActive()) {
        exp *= 3;
      }
    }

    player.setTotalExperience(player.getTotalExperience() + exp);

    dropPlayer.getItemDropList().stream().filter(itemDrop ->
        itemDrop.isEnabled() && itemDrop.getMinY() >= player.getLocation().getY() && itemDrop
            .pickaxe(player.getItemInHand().getType()) && itemDrop
            .drop(dropPlayer.getTurboDrop().isActive())
    ).forEach(itemDrop -> {
      dropPlayer.setPoints(dropPlayer.getPoints() + itemDrop.getPoints());

      int dropAmount = 1;
      if (player.getItemInHand().getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
        dropAmount *= ThreadLocalRandom.current()
            .nextInt(0,
                player.getItemInHand().getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS));
      }
      ItemStack drop = itemDrop.getItem().clone();

      drop.setAmount(dropAmount);
      drop.removeEnchantment(Enchantment.DURABILITY);

      if (dropPlayer.isInventoryDrop() && !this.isFull(player, itemDrop.getItem().getType())) {
        player.getInventory().addItem(drop);
      } else {
        player.getWorld().dropItem(player.getLocation().add(0.1, 0.2, 0.1), drop);
      }
      if (dropPlayer.isChatInfo()) {
        player.sendMessage(CC.translate(
            this.drop.getDropConfig().getMainColor() + "Wykopales " + this.drop.getDropConfig()
                .getValueColor() + itemDrop.getItem().getType().toString() + this.drop
                .getDropConfig().getSecondaryColor() + "(+" + itemDrop.getPoints() + ")"));
      }
    });
  }

  private boolean isFull(Player player, Material material) {
    if (player.getInventory().firstEmpty() == -1) {
      for (ItemStack itemStack : player.getInventory().getContents()) {
        if (itemStack.getType().equals(material)) {
          if (itemStack.getMaxStackSize() > itemStack.getAmount()) {
            return true;
          }
        }
      }
      return false;
    } else {
      return false;
    }
  }
}
