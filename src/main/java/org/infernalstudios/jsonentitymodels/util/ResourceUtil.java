package org.infernalstudios.jsonentitymodels.util;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.List;

public class ResourceUtil {
    private static final IntOpenHashSet resourceReloadedEntities = new IntOpenHashSet();

    public static List<ResourceLocation> fetchModelsForEntity(String namespace, String entityName, boolean isBaby) {
        String path = "geo/" + namespace + "/" + entityName + (isBaby ? "/baby" : "/adult");

        Collection<ResourceLocation> map = Minecraft.getInstance().getResourceManager().listResources(path,
                filename -> filename.endsWith(".json"));
        return map.stream().toList();
    }

    public static List<ResourceLocation> fetchAnimationsForModel(String namespace, String entityName, String modelName, boolean isBaby) {
        String path = "animations/" + namespace + "/" + entityName + (isBaby ? "/baby/" : "/adult/") + modelName;

        Collection<ResourceLocation> map = Minecraft.getInstance().getResourceManager().listResources(path,
                filename -> filename.endsWith(".json"));
        return map.stream().toList();
    }

    public static List<ResourceLocation> fetchTexturesForModel(String namespace, String entityName, String modelName, boolean isBaby) {
        String path = "textures/entity/" + namespace + "/" + entityName  + (isBaby ? "/baby/" : "/adult/") + modelName;

        Collection<ResourceLocation> map = Minecraft.getInstance().getResourceManager().listResources(path,
                filename -> filename.endsWith(".png") && !filename.endsWith("_glow.png"));
        return map.stream().toList();
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
    }
}
