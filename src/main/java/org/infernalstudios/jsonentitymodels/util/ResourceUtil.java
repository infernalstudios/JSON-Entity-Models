package org.infernalstudios.jsonentitymodels.util;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Map;

public class ResourceUtil {
    private static final IntOpenHashSet resourceReloadedEntities = new IntOpenHashSet();

    public static List<ResourceLocation> fetchModelsForEntity(String namespace, String entityName, boolean isBaby) {
        String path = "geo/" + namespace + "/" + entityName + (isBaby ? "/baby" : "/adult");

        Map<ResourceLocation, Resource> map = Minecraft.getInstance().getResourceManager().listResources(path,
                filename -> filename.getNamespace().equals("jsonentitymodels") && filename.toString().endsWith(".json"));
        return map.keySet().stream().toList();
    }

    public static List<ResourceLocation> fetchAnimationsForModel(String namespace, String entityName, String modelName, boolean isBaby) {
        String path = "animations/" + namespace + "/" + entityName + (isBaby ? "/baby/" : "/adult/") + modelName;

        Map<ResourceLocation, Resource> map = Minecraft.getInstance().getResourceManager().listResources(path,
                filename -> filename.getNamespace().equals("jsonentitymodels") && filename.toString().endsWith(".json"));
        return map.keySet().stream().toList();
    }

    public static List<ResourceLocation> fetchTexturesForModel(String namespace, String entityName, String modelName, boolean isBaby) {
        String path = "textures/entity/" + namespace + "/" + entityName  + (isBaby ? "/baby/" : "/adult/") + modelName;

        Map<ResourceLocation, Resource> map = Minecraft.getInstance().getResourceManager().listResources(path,
                filename -> filename.getNamespace().equals("jsonentitymodels") && filename.toString().endsWith(".png") &&
                        !filename.toString().endsWith("_glow.png") && !filename.toString().contains("crackiness"));
        return map.keySet().stream().toList();
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
