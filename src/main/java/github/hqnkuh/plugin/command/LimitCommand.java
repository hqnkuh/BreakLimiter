package github.hqnkuh.plugin.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LimitCommand extends Command {
	public LimitCommand() {
		super("limit");
		setDescription("block break limiter settings command");
		setUsage("/limit [count] <player>");
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
		// OP権限持ちのみ実行可能
		if (sender.isOp()) {
			int count;
			try {
				count = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				sender.sendMessage(Component.text("Invalid number: ", NamedTextColor.RED).append(Component.text(args[0])));
				return false;
			}
			Player player = Bukkit.getPlayer(args[1]);
			if (player == null) {
				sender.sendMessage(Component.text("Invalid player: ", NamedTextColor.RED).append(Component.text(args[1])));
				return false;
			}

			Scoreboard scoreboard = initScoreboard(player, count);
			player.setScoreboard(scoreboard);
			if (scoreboard.getObjective("break_limiter") == null) {
				sender.sendMessage(Component.text("制限が上書きされました", NamedTextColor.YELLOW));
			}
			sender.sendMessage(player.displayName().append(Component.text(" の制限を ", NamedTextColor.GREEN).append(Component.text(count, NamedTextColor.GREEN, TextDecoration.BOLD).append(Component.text(" に設定しました", NamedTextColor.GREEN)))));

			return true;
		}
		return false;
	}

	private Scoreboard initScoreboard(Player player, int count) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard scoreboard = manager.getNewScoreboard();
		Objective objective = scoreboard.registerNewObjective("break_limiter", Criteria.DUMMY, Component.empty());

		Score score = objective.getScore(player.getName());
		score.setScore(count);

		return scoreboard;
	}

	@Override
	public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
		List<String> completer = new ArrayList<>();
		String currency = args[args.length - 1];

		if (args.length == 1) {
			for (int i = 0; i < 9999; i++) {
				if (StringUtil.startsWithIgnoreCase(Integer.toString(i), currency)) {
					completer.add(Integer.toString(i));
				}
			}
		}

		if (args.length == 2) {
			Bukkit.getOnlinePlayers().forEach(player -> {
				if (StringUtil.startsWithIgnoreCase(player.getName(), currency)) {
					completer.add(player.getName());
				}
			});
		}

		return completer;
	}
}
