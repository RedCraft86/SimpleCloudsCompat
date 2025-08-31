package com.redcraft86.simplecloudscompat.mixin.supplementaries;

import com.redcraft86.simplecloudscompat.SCCompat;
import net.mehvahdjukaar.supplementaries.common.block.blocks.WindVaneBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WindVaneBlock.class, remap = false)
public class WindVaneBlockMixin {
    @Inject(method = "updatePower", at = @At("HEAD"), cancellable = true)
    private static void onUpdatePower(BlockState blockstate, Level level, BlockPos pos, CallbackInfo ci) {
        int weather = 0;
        if (SCCompat.isThundering(level, pos, true)) {
            weather = 2;
        } else if (SCCompat.isRaining(level, pos, true)) {
            weather = 1;
        }

        if (weather != blockstate.getValue(WindVaneBlock.WIND_STRENGTH)) {
            level.setBlock(pos, blockstate.setValue(WindVaneBlock.WIND_STRENGTH, weather), 3);
            level.updateNeighborsAt(pos.below(), blockstate.getBlock());
        }

        ci.cancel();
    }
}
