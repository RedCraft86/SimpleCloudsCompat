package com.redcraft86.simplecloudscompat.mixin.supplementaries;

import com.redcraft86.simplecloudscompat.SCCompat;
import net.mehvahdjukaar.supplementaries.common.misc.effects.FlammableEffect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// I can't test this properly so I hope it works
@Mixin(value = FlammableEffect.class)
public class FlammableEffectMixin {
    @Redirect(method = "applyEffectTick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/FireBlock;isNearRain(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    private boolean redirectIsNearRain(FireBlock fireBlock, Level level, BlockPos pos) {
        // Skip the original isNearRain check since it checks the surroundings which SCCompat.isRaining can already do
        return SCCompat.isRaining(level, pos, true);
    }

    @Redirect(method = "applyEffectTick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isRaining()Z"
            )
    )
    private boolean redirectIsRaining(Level level) {
        // Always return true since logic is handled in isNearRain now
        return true;
    }
}
