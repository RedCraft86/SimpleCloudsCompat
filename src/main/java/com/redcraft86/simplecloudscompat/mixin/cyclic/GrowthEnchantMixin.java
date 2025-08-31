package com.redcraft86.simplecloudscompat.mixin.cyclic;

import com.lothrazar.cyclic.enchant.GrowthEnchant;

import com.redcraft86.simplecloudscompat.SCCompat;
import net.minecraft.core.BlockPos;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = GrowthEnchant.class, remap = false)
public class GrowthEnchantMixin {
    // Probably a better way out there that doesn't involve this ThreadLocal, but I have no idea
    // MC itself isn't multithreaded but there's mods that make certain aspects of it be multithreaded
    // So this is just for those until there's a better injection point for this that lets me finish in a single method
    @Unique private ThreadLocal<BlockPos> thisPos = new ThreadLocal<>();

    @Redirect(method = "onEntityUpdate",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraftforge/event/entity/living/LivingEvent$LivingTickEvent;getEntity()Lnet/minecraft/world/entity/LivingEntity;"
            )
    )
    private LivingEntity redirectGetEntity(LivingEvent.LivingTickEvent event) {
        thisPos.set(event.getEntity().blockPosition());
        return event.getEntity();
    }

    @Redirect(method = "onEntityUpdate",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isRaining()Z"
            )
    )
    private boolean redirectIsRaining(Level level) {
        try {
            BlockPos pos = thisPos.get();
            if (pos == null) {
                return level.isRaining();
            }
            return SCCompat.isRaining(level, pos, false);
        } finally {
            System.out.println("Redirect: " + thisPos.get());
            thisPos.remove();
        }
    }
}
