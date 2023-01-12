package org.infernalstudios.jsonentitymodels.util;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Map;

public class ResourceUtil {
    private static final IntOpenHashSet resourceReloadedEntities = new IntOpenHashSet();

    public static List<ResourceLocation> fetchModelsForEntity(String namespace, String entityName, boolean isBaby) {
        Map<String, List<ResourceLocation>> modelsMap = isBaby ? ResourceCache.getInstance().getBabyModels() : ResourceCache.getInstance().getAdultModels();
        return modelsMap.get(namespace + ":" + entityName);
    }

    public static List<ResourceLocation> fetchAnimationsForModel(String namespace, String entityName, String modelName, boolean isBaby) {
        Map<String, List<ResourceLocation>> animationsMap = isBaby ? ResourceCache.getInstance().getBabyAnimations() : ResourceCache.getInstance().getAdultAnimations();

        return animationsMap.get(namespace + ":" + entityName + "/" + modelName);
    }

    public static List<ResourceLocation> fetchTexturesForModel(String namespace, String entityName, String modelName, boolean isBaby) {
        Map<String, List<ResourceLocation>> texturesMap = isBaby ? ResourceCache.getInstance().getBabyTextures() : ResourceCache.getInstance().getAdultTextures();

        return texturesMap.get(namespace + ":" + entityName + "/" + modelName);
    }

    public static void addEntityToReloadedHashSet(LivingEntity entity) {
        resourceReloadedEntities.add(entity.hashCode());
    }

    public static boolean isEntityInReloadedHashSet(LivingEntity entity) {
        return resourceReloadedEntities.contains(entity.hashCode());
    }

    public static void removeEntityFromReloadedHashSet(LivingEntity entity) {
        resourceReloadedEntities.remove(entity.hashCode());
    }

    public static void clearHashSet() {
        resourceReloadedEntities.clear();
    }

    public static void registerReloadListener() {
        ReloadableResourceManager reloadable = (ReloadableResourceManager) Minecraft.getInstance()
                .getResourceManager();

        reloadable.registerReloadListener(new ResourceListener());
        reloadable.registerReloadListener(ResourceCache.getInstance()::reload);
    }
}
