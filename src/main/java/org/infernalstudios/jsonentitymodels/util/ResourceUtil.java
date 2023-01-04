package org.infernalstudios.jsonentitymodels.util;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.util.List;
import java.util.Map;

public class ResourceUtil {

    public static List<ResourceLocation> fetchModelsForEntity(String entityName, boolean isBaby) {
        String path = "geo/" + entityName + (isBaby ? "/baby" : "/adult");

        Map<ResourceLocation, Resource> map = Minecraft.getInstance().getResourceManager().listResources(path,
                filename -> filename.toString().endsWith(".json"));
        return map.keySet().stream().toList();
    }

    public static List<ResourceLocation> fetchAnimationsForModel(String entityName, String modelName, boolean isBaby) {
        String path = "animations/" + entityName + (isBaby ? "/baby/" : "/adult/") + modelName;

        Map<ResourceLocation, Resource> map = Minecraft.getInstance().getResourceManager().listResources(path,
                filename -> filename.toString().endsWith(".json"));
        return map.keySet().stream().toList();
    }

    public static List<ResourceLocation> fetchTexturesForModel(String entityName, String modelName, boolean isBaby) {
        String path = "textures/entity/" + entityName  + (isBaby ? "/baby/" : "/adult/") + modelName;

        Map<ResourceLocation, Resource> map = Minecraft.getInstance().getResourceManager().listResources(path,
                filename -> filename.toString().endsWith(".png"));
        return map.keySet().stream().toList();
    }
}
