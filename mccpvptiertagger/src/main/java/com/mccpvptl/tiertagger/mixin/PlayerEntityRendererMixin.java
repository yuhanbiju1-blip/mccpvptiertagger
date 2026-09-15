package com.mccpvptl.tiertagger.mixin;

import com.mccpvptl.tiertagger.TierColors;
import com.mccpvptl.tiertagger.TierManager;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Rewrites a player's nametag text to prepend their Crystal PvP tier,
 * e.g. "Steve" becomes "[HT1] Steve" in the tier's color.
 *
 * If you'd rather see it as a suffix ("Steve [HT1]"), swap the order in
 * buildTaggedName() below.
 */
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

    @ModifyVariable(
            method = "renderLabelIfPresent",
            at = @At("HEAD"),
            argsOnly = true
    )
    private Text mccpvptl$addTier(Text text, PlayerEntityRenderState state) {
        String username = text.getString();
        String tier = TierManager.INSTANCE.getTier(username);

        if (tier == null || tier.isBlank()) {
            return text;
        }

        return buildTaggedName(tier, text);
    }

    private static Text buildTaggedName(String tier, Text originalName) {
        Text tierLabel = Text.literal("[" + tier.toUpperCase() + "] ")
                .formatted(TierColors.colorFor(tier));

        return Text.empty().append(tierLabel).append(originalName);
    }
}
