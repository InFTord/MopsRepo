package ml.mopsbase;

import ml.mopsbase.game.GameSession;
import ml.mopsutils.Translation;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.concurrent.Callable;

import java.util.List;
import java.util.Map;

public class MopsPlugin extends JavaPlugin {
	private List<GameSession> gameSessions;
	protected FileConfiguration config;
	public final FileConfiguration translation = new YamlConfiguration();
	protected List<Method> doOnEnable = new LinkedList<Method>();
	public Logger logger;
	protected Map<String, Object>;
	protected Map<String> hardcodedSettings = new HashMap<>();

	@Override
	public void onEnable() {
		this.logger = getLogger();
		logger.info("§aS§at§2a§ar§2t§ai§2n§ag §2M§ao§2p§as§2P§al§2u§ag§2i§an§2!");
		for(Method m : doOnEnable) {
			m.invoke();
		}
	}

	public void registerUtilCommands() {
		this.getCommand("cname").setExecutor(new UtilCommands(this))
	}

	public TextComponent getByLang(String lang, String string) {
		getLogger().info("MopsPlugin | getByLang: \n" + lang + "\n" + string);
		return new Translation(translation, getLogger(), "mopsgeneral").getTranslation(lang, string.replaceFirst("mopsgeneral.", ""));
	}
	public TextComponent getByLang(String lang, String string, Map<String, String> formatV) {
		getLogger().info("MopsPlugin | getByLang: \n" + lang + "\n" + string + "\n" + formatV.toString());
		return new Translation(translation, getLogger(), "mopsgeneral").getTranslation(lang, string.replaceFirst("mopsgeneral.", ""));
	}

	protected void loadConfigurableData(@Nullable List<String> optionalConfigPaths, @Nullable List<string> optionalTranslationsPaths) {
		this.saveDefaultConfig();
		this.config = this.getConfig();
		logger.info("config: \n" + config.saveToString() );
		logger.info("default config: \n" + ((FileConfiguration) Objects.requireNonNull(config.getDefaults())).saveToString());

		String data;

		try (Scanner reader = new Scanner(getResource("translations.yml"))) {
			data = "";
			while (reader.hasNextLine()) {
				data = data + "\n" + reader.nextLine();
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
		this.config = null;
		this.translation = null;
	}
}
