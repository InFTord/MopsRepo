package ml.mops.base;

import ml.mops.base.game.GameSession;
import ml.mops.utils.Translation;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.*;

import ml.mops.utils.Option;

import java.util.logging.Logger;

public class MopsPlugin extends JavaPlugin {
	private List<GameSession> gameSessions;
	protected FileConfiguration config;
	public final FileConfiguration translation = new YamlConfiguration();
	protected List<Method> doOnEnable = new LinkedList<>();
	public Logger logger;
	protected Map<String, Object> asjfdkafj;
	protected Map<String, Option> hardcodedSettings = new HashMap<String, Option>();

	@Override
	public void onEnable() {
		this.logger = getLogger();
		logger.info("§aS§at§2a§ar§2t§ai§2n§ag §2M§ao§2p§as§2P§al§2u§ag§2i§an§2!");
		for(Method m : doOnEnable) {
			//m.invoke();
		}
	}

	//public void registerUtilCommands() {
	//	this.getCommand("cname").setExecutor(new UtilCommands(this));
	//}

	public TextComponent getByLang(String lang, String string) {
		getLogger().info("MopsPlugin | getByLang: \n" + lang + "\n" + string);
		return new Translation(translation, getLogger(), "mopsgeneral").getTranslation(lang, string.replaceFirst("mopsgeneral.", ""));
	}
	public TextComponent getByLang(String lang, String string, Map<String, String> formatV) {
		getLogger().info("MopsPlugin | getByLang: \n" + lang + "\n" + string + "\n" + formatV.toString());
		return new Translation(translation, getLogger(), "mopsgeneral").getTranslation(lang, string.replaceFirst("mopsgeneral.", ""));
	}

	protected void loadConfigurableData(@Nullable List<String> optionalConfigPaths, @Nullable List<String> optionalTranslationsPaths) {
		this.saveDefaultConfig();
		this.config = this.getConfig();
		logger.info("config: \n" + config.saveToString() );
		logger.info("default config: \n" + ((FileConfiguration) Objects.requireNonNull(config.getDefaults())).saveToString());

		String data;

		StringBuilder stringBuilder = new StringBuilder();

		try (Scanner reader = new Scanner(getResource("translations.yml"))) {
			data = "";
			while (reader.hasNextLine()) {
				stringBuilder.append("\n" + data);
			}
		}

		try {
			this.translation.loadFromString(data);
		} catch (InvalidConfigurationException e) {
			logger.warning(Arrays.toString(e.getStackTrace()));
		}

		logger.info("Loaded translations: \n" + translation.saveToString());
	}

	protected void clearConfigurableData() {
		//this.config = null;
		//this.translation = null;
	}
}
