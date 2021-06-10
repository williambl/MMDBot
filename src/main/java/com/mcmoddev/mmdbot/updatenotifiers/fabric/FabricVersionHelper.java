package com.mcmoddev.mmdbot.updatenotifiers.fabric;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mcmoddev.mmdbot.MMDBot;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author
 *
 */
public final class FabricVersionHelper {

    /**
     *
     */
    private static final String YARN_URL = "https://meta.fabricmc.net/v2/versions/yarn";

    /**
     *
     */
    private static final String LOADER_URL = "https://meta.fabricmc.net/v2/versions/loader";

    /**
     *
     */
    private static final String API_URL = "https://maven.fabricmc.net/net/fabricmc/fabric-api/fabric-api/maven-metadata.xml";

    /**
     *
     */
    private static final Map<String, String> LATEST_YARNS = new HashMap<>();

    /**
     *
     */
    private static final Duration TIME_UNTIL_OUTDATED = Duration.ofMinutes(20);

    /**
     *
     */
    private static String latestLoader;

    /**
     *
     */
    private static String latestApi;

    /**
     *
     */
    private static Instant lastUpdated;

    static {
        update();
    }

    /**
	 *
	 */
   private FabricVersionHelper() {
       throw new IllegalStateException("Utility class");
   }

    /**
     *
     * @param mcVersion
     * @return
     */
    public static String getLatestYarn(final String mcVersion) {
        if (LATEST_YARNS.isEmpty() || isOutdated()) {
            update();
        }
        return LATEST_YARNS.get(mcVersion);
    }

    /**
     *
     * @return
     */
    public static String getLatestLoader() {
        if (latestLoader == null || isOutdated()) {
            update();
        }
        return latestLoader;
    }

    /**
     *
     * @return
     */
    public static String getLatestApi() {
        if (latestApi == null || isOutdated()) {
            update();
        }
        return latestApi;
    }

    /**
     *
     * @return
     */
    private static boolean isOutdated() {
        return lastUpdated.plus(TIME_UNTIL_OUTDATED).isBefore(Instant.now());
    }

    /**
     *
     */
    public static void update() {
        updateYarn();
        updateLoader();
        updateApi();
        lastUpdated = Instant.now();
    }

    /**
     *
     */
    private static void updateYarn() {
    	final InputStreamReader reader = getReader(YARN_URL);
        if (reader == null) {
            return;
        }
        final TypeToken<List<YarnVersionInfo>> token = new TypeToken<List<YarnVersionInfo>>() {
        };
        final List<YarnVersionInfo> versions = new Gson().fromJson(reader, token.getType());

        LATEST_YARNS.clear();
        final Map<String, List<YarnVersionInfo>> map = versions.stream()
            .distinct()
            .collect(Collectors.groupingBy(it -> it.gameVersion));
        map.keySet().forEach(it -> LATEST_YARNS.put(it, map.get(it).get(0).version));
    }

    /**
     *
     */
    private static void updateLoader() {
    	final InputStreamReader reader = getReader(LOADER_URL);
        if (reader == null) {
            return;
        }
        final TypeToken<List<LoaderVersionInfo>> token = new TypeToken<List<LoaderVersionInfo>>() {
        };
        final List<LoaderVersionInfo> versions = new Gson().fromJson(reader, token.getType());

        latestLoader = versions.get(0).version;
    }

    /**
     *
     */
    private static void updateApi() {
    	final InputStream stream = getStream(API_URL);
        if (stream == null) {
            return;
        }
        try {
        	final Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(stream);
        	final XPathExpression expr = XPathFactory.newInstance()
                .newXPath()
                .compile("/metadata/versioning/latest/text()");
            latestApi = expr.evaluate(doc);
        } catch (SAXException | XPathExpressionException | ParserConfigurationException | IOException ex) {
            MMDBot.LOGGER.error("Failed to resolve latest loader version", ex);
        }
    }

    /**
     *
     * @param urlString
     * @return
     */
    private static InputStreamReader getReader(final String urlString) {
    	final InputStream stream = getStream(urlString);
        if (stream == null) {
            return null;
        } else {
            return new InputStreamReader(stream, Charsets.UTF_8);
        }
    }

    /**
     *
     * @param urlString
     * @return
     */
    private static InputStream getStream(final String urlString) {
        try {
        	final URL url = new URL(urlString);
            return url.openStream();
        } catch (IOException ex) {
            MMDBot.LOGGER.error("Failed to get minecraft version", ex);
            return null;
        }
    }

    /**
     *
     * @author
     *
     */
    private static class YarnVersionInfo {

    	/**
    	 *
    	 */
        public String gameVersion;

        /**
         *
         */
        public String separator;

        /**
         *
         */
        public int build;

        /**
         *
         */
        public String maven;

        /**
         *
         */
        public String version;

        /**
         *
         */
        public boolean stable;
    }

    /**
     *
     * @author
     *
     */
    private static class LoaderVersionInfo {

    	/**
    	 *
    	 */
        public String separator;

        /**
         *
         */
        public int build;

        /**
         *
         */
        public String maven;

        /**
         *
         */
        public String version;

        /**
         *
         */
        public boolean stable;
    }
}
