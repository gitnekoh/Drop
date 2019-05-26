package pl.thathard.drop.command;

import com.qrakn.honcho.command.CommandMeta;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.thathard.core.util.CC;
import pl.thathard.drop.manager.PlayerManager;
import pl.thathard.drop.player.DropPlayer;

@RequiredArgsConstructor
@CommandMeta(label = {"drop",
    "stone"}, permission = "drop.drop", noPermissionMessage = "Nie masz pozwolenia do uzycia tej komendy.")
public class DropCommand {

  public void execute(CommandSender sender) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(CC.translate("&cTylko konsola."));
      return;
    }
    Player player = (Player) sender;

    DropPlayer dropPlayer = PlayerManager.getPlayers().getUnchecked(player.getUniqueId());

    dropPlayer.getDropGUI().openInventory(false);
  }
}
