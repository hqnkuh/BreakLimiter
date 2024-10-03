package github.hqnkuh.plugin;

import github.hqnkuh.plugin.command.LimitCommand;
import github.hqnkuh.plugin.event.BreakLimitListener;
import org.bukkit.plugin.java.JavaPlugin;

public class BreakLimiter extends JavaPlugin {

	public static BreakLimiter instance;

	public static BreakLimiter getInstance() {
		return instance;
	}

	@Override
	public void onDisable() {
		instance = this;
	}

	@Override
	public void onEnable() {
		instance = this;

		getServer().getCommandMap().register(getName(), new LimitCommand());
		getServer().getPluginManager().registerEvents(new BreakLimitListener(), this);
	}
}
