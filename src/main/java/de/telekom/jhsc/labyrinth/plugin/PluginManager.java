package de.telekom.jhsc.labyrinth.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class PluginManager {

	@SuppressWarnings("resource")
    public static Plugin loadPlugin(File file) throws FileNotFoundException {
		if (!file.exists())
			throw new FileNotFoundException();

		try {
		    URLClassLoader loader = new URLClassLoader(new URL[] { file.toURI().toURL() },
	                PluginManager.class.getClassLoader());
		    
			PluginProperties properties = loadPluginProperties(loader.findResource("plugin.properties").openStream());

			if (properties.getMainClass().startsWith("de.telekom.jhsc.labyrinth"))
				throw new IllegalArgumentException(
						"Plugin package can not be 'de.telekom.jhsc.labyrinth' or a subpackage of it!");

			Class<?> clazz = loader.loadClass(properties.getMainClass());
			Plugin instance;
			if (!Plugin.class.isAssignableFrom(clazz)) {
				loader.close();
				throw new IllegalArgumentException();
			}

			instance = (Plugin) clazz.getConstructor().newInstance();
			return instance;
		} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			System.out.println("Something went wrong loading the plugin!");
			e.printStackTrace();
			return null;
		}
	}

	private static PluginProperties loadPluginProperties(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		Map<String, String> map = new HashMap<>();

		String line;
		while ((line = reader.readLine()) != null) {
			String[] array = line.split("=", 2);
			if (array.length != 2)
				continue;
			map.put(array[0], array[1]);
		}

		return new PluginProperties(map);
	}

}