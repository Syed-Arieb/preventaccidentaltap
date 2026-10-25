package com.purpur.pat.client;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class PreventAccidentalTapConfig {
	public static boolean DEBUG = false;
	public static long COOLDOWN_MS = 500L;

	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("preventaccidentaltap.properties");

	private PreventAccidentalTapConfig() {
	}

	public static void load() {
		if (!Files.exists(FILE)) {
			return;
		}
		Properties p = new Properties();
		try (Reader r = Files.newBufferedReader(FILE)) {
			p.load(r);
		} catch (IOException ignored) {
			return;
		}
		DEBUG = Boolean.parseBoolean(p.getProperty("debug", "false"));
		try {
			COOLDOWN_MS = Long.parseLong(p.getProperty("cooldown", "500"));
		} catch (NumberFormatException ignored) {
		}
	}

	public static void save() {
		Properties p = new Properties();
		p.setProperty("debug", String.valueOf(DEBUG));
		p.setProperty("cooldown", String.valueOf(COOLDOWN_MS));
		try {
			Files.createDirectories(FILE.getParent());
			try (Writer w = Files.newBufferedWriter(FILE)) {
				p.store(w, "PreventAccidentalTap config");
			}
		} catch (IOException ignored) {
		}
	}
}
