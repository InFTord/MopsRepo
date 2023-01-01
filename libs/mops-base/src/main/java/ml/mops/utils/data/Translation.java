package ml.mops.utils.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Logger;

import static net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand;

public class Translation <LANG extends CharSequence> {
    private final TextComponent invalidString = Component.text("Invalid String", NamedTextColor.RED);
    private final TextComponent invalidLanguage = Component.text("Invalid Language", NamedTextColor.RED);
    protected List<Map<?, ?>> translation;
    protected Logger logger;
    public HashMap<String, String> colors = new HashMap<>();
    protected String base;


    public Translation(FileConfiguration fc, Logger logger, String base) {
        this.translation = fc.getMapList("");
        this.logger = logger;
        this.base = base;
        logger.info(base + " translation:\n" + Arrays.toString(translation.toArray()));
    }

    public TextComponent getTranslation(LANG lang, String pathString, @Nullable Map<String, String> formatValues, boolean pathFromRoot) {
//        logger.info("Translation.getTranslation (1): " + lang + "\n" + string + "\n" + formatValues.toString());
        TextComponent tc;

        if (!LANGUAGE.ENGLISH.isCorrectLanguage(lang)) {
           return invalidLanguage;
        }

        if (pathFromRoot) {
            pathString = base + "." + pathString;
        }

        String[] path = pathString.split(".");
        String s = "";
        Object value = new HashMap<Object, Object>();

//            logger.info("woolbattleTranslation: " + Arrays.toString(woolbattleTranslation.toArray()));
        int i = 0;
        int max_i = path.length;
        for (String pString : path) {
            if (i == 1 && i < max_i) {
                for (Map<?, ?> mp : translation) {
                    if (mp.containsKey(path[0])) {
                        value = mp.get(pString);
                        break;
                    }
                }
            } else if (i < max_i) {
                for (Map<?, ?> mp : value) {
                    if (mp.containsKey(value)) {
                        value = ((Map<?, ?>) mp.get(pString));
                        break;
                    }
                }
            }
            i += 1;
        }

        if (value != null && value.containsKey(lang.toString())) {
            s = value.get(lang.toString()).toString();
        }

        if (s.isBlank()) {
            tc = invalidString;
//                logger.warning("invalid string");
        } else if (formatValues != null) {
            for (String K : formatValues.keySet()) {
                s = s.replaceAll("§" + K + "§", formatValues.get(K));
            }
        }
        tc = (legacyAmpersand().deserialize("&r" + s));
        return tc;

    }

//    public TextComponent getTranslation(String lang, String string) {
//        logger.info("Translation.getTranslation (1): " + lang + "\n" + string + "\n" + colors);
//        return getTranslation(lang, string, colors);
//    }
}