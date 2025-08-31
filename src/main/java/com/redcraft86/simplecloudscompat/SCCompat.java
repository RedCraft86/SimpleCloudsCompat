package com.redcraft86.simplecloudscompat;

import dev.nonamecrackers2.simpleclouds.common.world.CloudManager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.common.MinecraftForge;

@Mod(SCCompat.MOD_ID)
public class SCCompat {
    public static final String MOD_ID = "simplecloudscompat";

    public SCCompat(FMLJavaModLoadingContext context)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static boolean isRaining(Level level, BlockPos pos, boolean checkSides) {
        if (level instanceof ServerLevel serverLevel) {
            BlockPos checkPos = pos.above();
            if (level.isEmptyBlock(checkPos)) {
                return serverLevel.isRainingAt(checkPos);
            }

            if (!checkSides) {
                return false;
            }

            // Down is never checked since it wouldn't make sense for weather
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                checkPos = pos.relative(direction);
                if (level.isEmptyBlock(checkPos)) {
                    return serverLevel.isRainingAt(checkPos);
                }
            }
        }
        return false;
    }

    public static boolean isSnowing(Level level, BlockPos pos, boolean checkSides) {
        if (level instanceof ServerLevel serverLevel) {
            CloudManager<ServerLevel> manager = CloudManager.get(serverLevel);
            BlockPos checkPos = pos.above();

            if (manager.shouldUseVanillaWeather()) {
                return serverLevel.isRainingAt(checkPos);
            }

            if (level.isEmptyBlock(checkPos)) {
                return manager.isSnowingAt(checkPos);
            }

            if (!checkSides) {
                return false;
            }

            // Down is never checked since it wouldn't make sense for weather
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                checkPos = pos.relative(direction);
                if (level.isEmptyBlock(checkPos)) {
                    return manager.isSnowingAt(checkPos);
                }
            }
        }
        return false;
    }

    public static boolean isThundering(Level level, BlockPos pos, boolean checkSides) {
        if (level instanceof ServerLevel serverLevel) {
            CloudManager<ServerLevel> manager = CloudManager.get(serverLevel);
            if (manager.shouldUseVanillaWeather()) {
                return serverLevel.isThundering();
            }

            // SC seems to have a thunderstorm without rain but let's not worry about that right now.
            if (!isRaining(level, pos, checkSides)) {
                return false;
            }

            return manager.getCloudTypeAtPosition(pos.getX(), pos.getZ()).getLeft().weatherType().includesThunder();
        }
        return false;
    }
}
