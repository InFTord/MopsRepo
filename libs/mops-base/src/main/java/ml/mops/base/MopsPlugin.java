package ml.mops.base;

import ml.mops.utils.data.Translation;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
//import ml.mops.base.game.GameSession;

import org.jetbrains.annotations.Nullable;
import java.lang.reflect.Method;
import java.util.*;;

import java.util.logging.Logger;

public class MopsPlugin extends JavaPlugin {
//	private List<GameSession> gameSessions;
	protected FileConfiguration config;
	public final FileConfiguration translation = new YamlConfiguration();
	protected Map<Object, String> doOnEnableMethodName = new HashMap<Object, String>() {{
		put(this.getClass(), "defaultOnEnable");
	}};
	protected List<Method> doOnEnable = new LinkedList<Method>();
	protected List<String> logOnEnable = new LinkedList<String>();
	public Logger logger;

	@Override
	public void onEnable() {
		for (Object o: doOnEnableMethodName.keySet()) {
			try {
				this.doOnEnable.add(o.getClass().getDeclaredMethod(doOnEnableMethodName.get(o)));
			} catch (NoSuchMethodException | SecurityException e) {
				logOnEnable.add("couldn't find \"defaultOnEnable\" method" + Arrays.toString(e.getStackTrace()));
			}
		}
//		this.logger = getLogger();
//		logger.info("§aS§at§2a§ar§2t§ai§2n§ag §2M§ao§2p§as§2P§al§2u§ag§2i§an§2!");
		for (String s : logOnEnable) {
			logger.info("logging on enable: " + s);
		}
		for(Method m : doOnEnable) {
			//m.invoke();
			try {
				m.invoke(this);
			} catch (Exception e) {
				logger.warning("onEnable (custom doOnEnable method invocation" + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	public void defaultOnEnable() {
		this.logger = getLogger();
		logger.info("§aS§at§2a§ar§2t§ai§2n§ag §2M§ao§2p§as§2P§al§2u§ag§2i§an§2!");
	}

	//public void registerUtilCommands() {
	//	this.getCommand("cname").setExecutor(new UtilCommands(this));
	//}

	public TextComponent getByLang(String lang, String string, @Nullable Object customFormat, @Nullable boolean noCustomFormat, @Nullable Object fromRoot) {
	getLogger().info("WoolBattle:Plugin | getByLang: \n" + lang + "\n" + string);
		return translator.getTranslation(lang, string.replaceFirst("woolbattle.", "")).decoration(TextDecoration.ITALIC, false);
	}
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
