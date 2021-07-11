/*
 * Copyright (C) isXander [2019 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package co.uk.isxander.evergreenhud.config.convert;

import co.uk.isxander.evergreenhud.elements.ElementManager;
import co.uk.isxander.xanderlib.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public abstract class ConfigConverter implements Constants {

    private final File configFolder;
    private final Logger logger;

    public ConfigConverter(File configFolder) {
        this.configFolder = configFolder;
        this.logger = LogManager.getLogger("EvergreenHUD Converter");
    }

    public abstract void process(ElementManager manager);

    public File getConfigFolder() {
        return configFolder;
    }

    public Logger getLogger() {
        return logger;
    }

}
