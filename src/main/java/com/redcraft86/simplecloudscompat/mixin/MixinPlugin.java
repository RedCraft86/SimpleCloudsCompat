package com.redcraft86.simplecloudscompat.mixin;

import java.util.Set;
import java.util.List;
import java.util.HashSet;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.loading.FMLLoader;

import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MixinPlugin implements IMixinConfigPlugin {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Set<String> loadedMods = new HashSet<>();

    @Override
    public boolean shouldApplyMixin(String targetClass, String mixinClass) {
        boolean hasDependency = true;
        if (mixinClass.contains(".cyclic.")) {
            hasDependency = isModIncluded("cyclic");
        }
        if (mixinClass.contains(".supplementaries.")) {
            hasDependency = isModIncluded("supplementaries");
        }

        LOGGER.info("[Simple Clouds Compat] {} Mixin: {}", hasDependency ? "Loading" : "Skipping", mixinClass);
        return hasDependency;
    }

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {}

    @Override
    public List<String> getMixins() { return null; }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}

    private static boolean isModIncluded(String modID) {
        if (loadedMods.contains(modID)) {
            return true;
        }

        if (FMLLoader.getLoadingModList().getModFileById(modID) != null) {
            loadedMods.add(modID);
            return true;
        }

        return false;
    }
}
