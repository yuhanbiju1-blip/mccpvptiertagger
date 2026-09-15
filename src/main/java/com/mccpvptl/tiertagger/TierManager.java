package com.mccpvptl.tiertagger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Downloads the MC Cpvp TL tier list from a GitHub raw JSON URL and keeps it
 * cached in memory. The nametag mixin reads from {@link #getTier(String)}.
 *
 * TODO: Replace RAW_URL below with your own GitHub raw file link once you've
 * created your repo, e.g.
 * https://raw.githubusercontent.com/<your-username>/<your-repo>/main/tiers.json
 */
public final class TierManager {

    public static final TierManager INSTANCE = new TierManager();

    private static final Logger LOGGER = LoggerFactory.getLogger("mccpvptiertagger");

    // ↓↓↓ CHANGE THIS to your own GitHub raw JSON URL ↓↓↓
    private static final String RAW_URL =
            "https://raw.githubusercontent.com/yuhanbiju1-blip/mccpvptl-tiers/refs/heads/main/tiers.json";

    private static final Duration REFRESH_INTERVAL = Duration.ofMinutes(5);

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final Gson gson = new Gson();

    // Usernames are stored lowercase so lookups are case-insensitive.
    private volatile Map<String, String> tiersByUsername = Collections.emptyMap();

    private Timer refreshTimer;

    private TierManager() {
    }

    /** Looks up a player's tier by their Minecraft username. Returns null if untested. */
    public String getTier(String username) {
        if (username == null) return null;
        return tiersByUsername.get(username.toLowerCase());
    }

    public void startAutoRefresh() {
        if (refreshTimer != null) return;
        refreshTimer = new Timer("mccpvptiertagger-refresh", true);
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchAsync();
            }
        }, REFRESH_INTERVAL.toMillis(), REFRESH_INTERVAL.toMillis());
    }

    public void fetchAsync() {
        Thread thread = new Thread(this::fetchNow, "mccpvptiertagger-fetch");
        thread.setDaemon(true);
        thread.start();
    }

    private void fetchNow() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(RAW_URL))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                LOGGER.warn("Tier list fetch failed with status {}", response.statusCode());
                return;
            }

            Type mapType = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> parsed = gson.fromJson(response.body(), mapType);

            if (parsed == null) {
                LOGGER.warn("Tier list JSON parsed to null, ignoring");
                return;
            }

            ConcurrentHashMap<String, String> normalized = new ConcurrentHashMap<>();
            parsed.forEach((name, tier) -> normalized.put(name.toLowerCase(), tier));

            tiersByUsername = normalized;
            LOGGER.info("Loaded {} tiered players", normalized.size());
        } catch (IOException | InterruptedException e) {
            LOGGER.warn("Could not fetch tier list: {}", e.getMessage());
        } catch (Exception e) {
            LOGGER.warn("Could not parse tier list JSON: {}", e.getMessage());
        }
    }
}
