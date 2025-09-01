package com.redcraft86.simplecloudscompat.mixin.cyclic;

import com.redcraft86.simplecloudscompat.SCCompat;
import com.lothrazar.cyclic.block.detectweather.TileWeather;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileWeather.class, remap = false)
public class TileWeatherMixin {
    @Unique
    private final BlockPos thisPos = ((TileWeather)(Object)this).getBlockPos();

    @Redirect(method = "tick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isThundering()Z",
                    remap = true
            )
    )
    private boolean redirectIsThundering(Level level) {
        return SCCompat.isThundering(level, thisPos, true);
    }

    @Redirect(method = "tick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isRaining()Z",
                    remap = true
            )
    )
    private boolean redirectIsRaining(Level level) {
        return SCCompat.isRaining(level, thisPos, true);
    }
}
