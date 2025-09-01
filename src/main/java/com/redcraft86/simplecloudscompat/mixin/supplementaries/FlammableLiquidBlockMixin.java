package com.redcraft86.simplecloudscompat.mixin.supplementaries;

import com.redcraft86.simplecloudscompat.SCCompat;
import net.mehvahdjukaar.supplementaries.common.fluids.FlammableLiquidBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.FireBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// I can't test this properly so I hope it works
@Mixin(value = FlammableLiquidBlock.class, remap = false)
public class FlammableLiquidBlockMixin {
    @Redirect(method = "burnStuffAroundLikeFire",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/FireBlock;isNearRain(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    remap = true
            )
    )
    private static boolean redirectIsNearRain(FireBlock fireBlock, Level level, BlockPos pos) {
        // Skip the original isNearRain check since it checks the surroundings which SCCompat.isRaining can already do
        return SCCompat.isRaining(level, pos, true);
    }

    @Redirect(method = "burnStuffAroundLikeFire",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;isRaining()Z",
                    remap = true
            )
    )
    private static boolean redirectIsRaining(ServerLevel level) {
        // Always return true since logic is handled in isNearRain now
        return true;
    }
}
