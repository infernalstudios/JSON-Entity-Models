package org.infernalstudios.jsonentitymodels.util;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.util.List;
import java.util.Map;

public class ResourceUtil {

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
                filename -> filename.getNamespace().equals("jsonentitymodels") && filename.toString().endsWith(".png"));
        return map.keySet().stream().toList();
    }
}
