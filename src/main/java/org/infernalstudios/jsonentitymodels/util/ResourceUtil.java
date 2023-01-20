package org.infernalstudios.jsonentitymodels.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.FolderPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.infernalstudios.jsonentitymodels.JSONEntityModels;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedDefaultModel;
import org.infernalstudios.jsonentitymodels.client.render.*;
import org.infernalstudios.jsonentitymodels.entity.ReplacedDefaultEntity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceUtil {
    private static Map<EntityType<?>, EntityRendererProvider<?>> DEFAULT_RENDERERS = new HashMap<>();

    private static final Map<EntityType<? extends Entity>, EntityRendererProvider<? extends Entity>> RENDERER_MAP = new HashMap();

    static {
        RENDERER_MAP.put(EntityType.CREEPER, ReplacedCreeperRenderer::new);
        RENDERER_MAP.put(EntityType.SPIDER, ReplacedSpiderRenderer::new);
        RENDERER_MAP.put(EntityType.ZOMBIE, ReplacedZombieRenderer::new);
        RENDERER_MAP.put(EntityType.SKELETON, ReplacedSkeletonRenderer::new);
        RENDERER_MAP.put(EntityType.IRON_GOLEM, ReplacedIronGolemRenderer::new);
        RENDERER_MAP.put(EntityType.SHEEP, ReplacedSheepRenderer::new);
        RENDERER_MAP.put(EntityType.PIG, ReplacedPigRenderer::new);
        RENDERER_MAP.put(EntityType.COW, ReplacedCowRenderer::new);
        RENDERER_MAP.put(EntityType.CHICKEN, ReplacedChickenRenderer::new);
        RENDERER_MAP.put(EntityType.ENDERMAN, ReplacedEnderManRenderer::new);
    }

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

    public static void registerReloadListener() {
        ReloadableResourceManager reloadable = (ReloadableResourceManager) Minecraft.getInstance()
                .getResourceManager();

        reloadable.registerReloadListener(ResourceCache.getInstance()::reload);
    }

    public static void storeDefaultRenderers() {
        if (DEFAULT_RENDERERS.isEmpty()) {
            DEFAULT_RENDERERS = new HashMap<>(EntityRenderers.PROVIDERS);
            replaceRenderers();
        }
    }


    public static void replaceRenderers() {
        for (Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>> entityEntry : ForgeRegistries.ENTITY_TYPES.getEntries()) {
            ResourceLocation entityResourceLocation = entityEntry.getKey().location();
            if (entityResourceLocation.toString().equals("minecraft:player")) continue;

            if (doesEntityHaveResource(entityResourceLocation)) {

                JSONEntityModels.LOGGER.info("JEMs found resource for entity: " + entityResourceLocation);

                EntityRenderers.register(entityEntry.getValue(), (EntityRendererProvider<Entity>) RENDERER_MAP.getOrDefault(entityEntry.getValue(), (context) -> new ReplacedDefaultRenderer(context,
                        new ReplacedDefaultModel(entityResourceLocation.getNamespace(), entityResourceLocation.getPath()), new ReplacedDefaultEntity())));
            } else if (DEFAULT_RENDERERS.containsKey(entityEntry.getValue())) {
                EntityRenderers.register(entityEntry.getValue(), (EntityRendererProvider<Entity>) DEFAULT_RENDERERS.get(entityEntry.getValue()));
            }
        }
    }

    private static boolean doesEntityHaveResource(ResourceLocation entityResourceLocation) {
        ResourceLocation resourceLocation = new ResourceLocation(JSONEntityModels.MOD_ID, "geo/" + entityResourceLocation.getNamespace() + "/" + entityResourceLocation.getPath());

        FallbackResourceManager packs = ((MultiPackResourceManager) ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).resources).namespacedManagers.get(JSONEntityModels.MOD_ID);

        if (!resourceLocation.getPath().contains("..") && packs != null) {
            for (PackResources packResources : packs.listPacks().toList()) {
                if (packResources instanceof FolderPackResources folderPackResources) {
                    String path = String.format("%s/%s/%s", PackType.CLIENT_RESOURCES.getDirectory(), resourceLocation.getNamespace(), resourceLocation.getPath());
                    File tempFile = new File(folderPackResources.file, path);

                    try {
                        if (tempFile.isDirectory() && FolderPackResources.validatePath(tempFile, path)) {
                            return true;
                        }
                    } catch (IOException ignored) {
                    }
                } else {
                    if (packResources.hasResource(PackType.CLIENT_RESOURCES, resourceLocation)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
