package github.hqnkuh.plugin.event;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class BreakLimitListener implements Listener {
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Scoreboard scoreboard = player.getScoreboard();

		Objective objective = scoreboard.getObjective("break_limiter");

		if (objective == null) return;

		Score score = objective.getScore(player.getName());
		int count = score.getScore();

		if (count <= 0) {
			player.sendActionBar(Component.text(" × ", NamedTextColor.RED, TextDecoration.BOLD));
			player.playSound(player, Sound.BLOCK_ANVIL_USE, 100, 1);
			event.setCancelled(true);

		} else {
			player.sendActionBar(Component.text("- " + count + " -", NamedTextColor.GRAY, TextDecoration.BOLD));
			score.setScore(count - 1);
		}
	}
}
