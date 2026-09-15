package com.mccpvptl.tiertagger;

import net.fabricmc.api.ClientModInitializer;

public class MCCpvpTierTaggerClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Kick off the first fetch of the tier list right away,
        // then keep refreshing it in the background every few minutes.
        TierManager.INSTANCE.fetchAsync();
        TierManager.INSTANCE.startAutoRefresh();
    }
}
