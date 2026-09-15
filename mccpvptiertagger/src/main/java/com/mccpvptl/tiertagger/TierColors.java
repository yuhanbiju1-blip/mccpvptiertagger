package com.mccpvptl.tiertagger;

import net.minecraft.util.Formatting;

import java.util.Locale;

public final class TierColors {

    private TierColors() {
    }

    /** Picks a nametag color for a given tier string, e.g. "HT1", "LT3". */
    public static Formatting colorFor(String tier) {
        if (tier == null) return Formatting.GRAY;

        String normalized = tier.toUpperCase(Locale.ROOT);

        if (normalized.startsWith("HT1")) return Formatting.DARK_RED;
        if (normalized.startsWith("HT2")) return Formatting.RED;
        if (normalized.startsWith("HT3")) return Formatting.GOLD;
        if (normalized.startsWith("HT4")) return Formatting.YELLOW;
        if (normalized.startsWith("HT5")) return Formatting.LIGHT_PURPLE;
        if (normalized.startsWith("LT1")) return Formatting.DARK_AQUA;
        if (normalized.startsWith("LT2")) return Formatting.GREEN;
        if (normalized.startsWith("LT3")) return Formatting.DARK_GREEN;
        if (normalized.startsWith("LT4")) return Formatting.BLUE;
        if (normalized.startsWith("LT5")) return Formatting.DARK_BLUE;

        return Formatting.GRAY;
    }
}
