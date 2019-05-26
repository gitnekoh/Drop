package pl.thathard.drop.gui;

import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.thathard.core.util.CC;
import pl.thathard.core.util.ItemBuilder;
import pl.thathard.drop.Drop;
import pl.thathard.drop.player.DropPlayer;

public class DropGUI implements Listener {

  private final DropPlayer dropPlayer;
  private final Inventory inventory;
  private final Drop drop = Drop.getPlugin(Drop.class);
  private ItemStack cobbleDrop;
  private ItemStack inventoryDrop;
  private ItemStack disableWholeDrop;
  private ItemStack enableWholeDrop;
  private ItemStack turboDrop;
  private ItemStack turboExp;

  public DropGUI(DropPlayer dropPlayer) {
    this.dropPlayer = dropPlayer;
    this.inventory = Bukkit.createInventory(null, 36,
        CC.translate(this.drop.getDropConfig().getMainColor() + "&lDROP ZE STONE"));
    Bukkit.getPluginManager().registerEvents(this, this.drop);
  }

  public void openInventory(boolean update) {
    AtomicInteger atomicInteger = new AtomicInteger(0);
    this.dropPlayer.getItemDropList().forEach(itemDrop -> {
      ItemStack itemStack = new ItemBuilder(itemDrop.getItem()).lore(
          CC.translate(
              this.drop.getDropConfig().getMainColor() + "Szansa: " + this.drop.getDropConfig()
                  .getValueColor() + itemDrop.getChance() + "%"),
          CC.translate(
              this.drop.getDropConfig().getMainColor() + "Aktywny: " + this.drop.getDropConfig()
                  .getValueColor() + CC.translateBoolean(itemDrop.isEnabled(), true)),
          CC.translate(
              this.drop.getDropConfig().getMainColor() + "Wystepuje Ponizej: " + this.drop
                  .getDropConfig()
                  .getValueColor() + itemDrop.getMinY()),
          "",
          CC.translate(
              this.drop.getDropConfig().getSecondaryColor() + "(Kliknij aby wlaczyc/wylaczyc)")
      ).build();
      if (itemDrop.isEnabled()) {
        itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
      }
      this.inventory.setItem(atomicInteger.getAndIncrement(), itemStack);
    });

    this.cobbleDrop = new ItemBuilder(Material.COBBLESTONE)
        .name(this.drop.getDropConfig().getMainColor() + "&lDROP COBBLE").lore(
            CC.translate(
                this.drop.getDropConfig().getMainColor() + "Aktywny: " + this.drop.getDropConfig()
                    .getValueColor() + CC.translateBoolean(this.dropPlayer.isCobble(), true)),
            CC.translate(
                this.drop.getDropConfig().getSecondaryColor() + "(Kliknij aby wlaczyc/wylaczyc)"))
        .build();

    this.inventoryDrop = new ItemBuilder(Material.CHEST)
        .name(this.drop.getDropConfig().getMainColor() + "&lDROP DO EKWPIUNKU").lore(
            CC.translate(
                this.drop.getDropConfig().getMainColor() + "Aktywny: " + this.drop.getDropConfig()
                    .getValueColor() + CC
                    .translateBoolean(this.dropPlayer.isInventoryDrop(), true)),
            CC.translate(
                this.drop.getDropConfig().getSecondaryColor() + "(Kliknij aby wlaczyc/wylaczyc)"))
        .build();

    this.disableWholeDrop = new ItemBuilder(Material.REDSTONE_BLOCK)
        .name(this.drop.getDropConfig().getMainColor() + "&lWylacz caly drop").lore(
            CC.translate(
                this.drop.getDropConfig().getSecondaryColor() + "(Kliknij aby wylaczyc)"))
        .build();

    this.enableWholeDrop = new ItemBuilder(Material.COAL_BLOCK)
        .name(this.drop.getDropConfig().getMainColor() + "&lWlacz caly drop").lore(
            CC.translate(
                this.drop.getDropConfig().getSecondaryColor() + "(Kliknij aby wlaczyc)"))
        .build();

    this.turboDrop = new ItemBuilder(Material.DIAMOND_PICKAXE)
        .name(this.drop.getDropConfig().getMainColor() + "&lTurboDrop").lore(
            CC.translate(
                this.drop.getDropConfig().getMainColor() + "Aktywny : " +
                    this.drop.getDropConfig().getValueColor() + (
                    this.dropPlayer.getTurboDrop().isActive() ?
                        this.dropPlayer.getTurboDrop().getStart() + this.dropPlayer.getTurboDrop()
                            .getLenght() : "&cNieaktywny")))
        .build();

    this.turboExp = new ItemBuilder(Material.EXP_BOTTLE)
        .name(this.drop.getDropConfig().getMainColor() + "&lTurboEXP").lore(
            CC.translate(
                this.drop.getDropConfig().getMainColor() + "Aktywny : " +
                    this.drop.getDropConfig().getValueColor() + (
                    this.dropPlayer.getTurboDrop().isExp() ?
                        this.dropPlayer.getTurboDrop().getStart() + this.dropPlayer.getTurboDrop()
                            .getLenght() : "&cNieaktywny")))
        .build();

    this.inventory.setItem(27, this.cobbleDrop);
    this.inventory.setItem(28, this.inventoryDrop);
    this.inventory.setItem(29, this.disableWholeDrop);
    this.inventory.setItem(30, this.enableWholeDrop);
    this.inventory.setItem(31, this.turboDrop);
    this.inventory.setItem(32, this.turboExp);
    if (!update) {
      Bukkit.getPlayer(this.dropPlayer.getUuid()).openInventory(this.inventory);
    } else {
      Bukkit.getPlayer(this.dropPlayer.getUuid()).updateInventory();
    }
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (!event.getInventory().equals(this.inventory)) {
      return;
    }

    if (event.getCurrentItem().getType().equals(Material.AIR) || event.getCurrentItem() == null
        || event.getClickedInventory() == null) {
      return;
    }

    Material item = event.getCurrentItem().getType();

    this.dropPlayer.getItemDropList().stream().filter(itemDrop ->
        itemDrop.getItem().getType().equals(item)
    ).forEach(itemDrop -> itemDrop.setEnabled(!itemDrop.isEnabled()));

    if (item.equals(this.cobbleDrop.getType())) {
      this.dropPlayer.setCobble(!this.dropPlayer.isCobble());
    } else if (item.equals(this.inventoryDrop.getType())) {
      this.dropPlayer.setInventoryDrop(!this.dropPlayer.isInventoryDrop());
    } else if (item.equals(this.disableWholeDrop.getType())) {
      this.dropPlayer.getItemDropList().forEach(itemDrop -> itemDrop.setEnabled(false));
    } else if (item.equals(this.enableWholeDrop.getType())) {
      this.dropPlayer.getItemDropList().forEach(itemDrop -> itemDrop.setEnabled(true));
    }

    this.openInventory(true);
    event.setCancelled(true);
  }
}
