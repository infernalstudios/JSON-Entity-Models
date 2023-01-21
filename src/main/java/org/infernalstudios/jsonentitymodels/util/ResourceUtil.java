package org.infernalstudios.jsonentitymodels.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
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
import org.apache.commons.io.FileUtils;
import org.infernalstudios.jsonentitymodels.JSONEntityModels;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedDefaultModel;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedChickenRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedCowRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedCreeperRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedDefaultRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedEnderManRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedIronGolemRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedPigRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSheepRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSkeletonRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedSpiderRenderer;
import org.infernalstudios.jsonentitymodels.client.render.ReplacedZombieRenderer;
import org.infernalstudios.jsonentitymodels.entity.ReplacedDefaultEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
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

    public static boolean packLoaded = false;
    public static boolean packDeleted = false;

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
        for (EntityType<?> entity : ForgeRegistries.ENTITIES) {
            if (entity.getRegistryName().toString().equals("minecraft:player")) continue;

            if (doesEntityHaveResource(entity)) {
                ResourceLocation registryName = entity.getRegistryName();
                JSONEntityModels.LOGGER.info("JEMs found resource for entity: " + registryName);

                EntityRenderers.register(entity, (EntityRendererProvider<Entity>) RENDERER_MAP.getOrDefault(entity, (context) -> new ReplacedDefaultRenderer(context,
                        new ReplacedDefaultModel(registryName.getNamespace(), registryName.getPath()), new ReplacedDefaultEntity())));
            } else if (DEFAULT_RENDERERS.containsKey(entity)){
                EntityRenderers.register(entity, (EntityRendererProvider<Entity>) DEFAULT_RENDERERS.get(entity));
            }
        }
    }

    private static boolean doesEntityHaveResource(EntityType<?> entityType) {
        ResourceLocation entityTypeRegistry = entityType.getRegistryName();
        ResourceLocation resourceLocation = new ResourceLocation(JSONEntityModels.MOD_ID, "geo/" + entityTypeRegistry.getNamespace() + "/" + entityTypeRegistry.getPath());

        FallbackResourceManager packs = ((MultiPackResourceManager) ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).resources).namespacedManagers.get(JSONEntityModels.MOD_ID);

        if (!resourceLocation.getPath().contains("..")) {
            for (int i = packs.fallbacks.size() - 1; i >= 0; --i) {
                PackResources packresources = packs.fallbacks.get(i);

                if (packresources instanceof FolderPackResources folderPackResources) {
                    String path = String.format("%s/%s/%s", PackType.CLIENT_RESOURCES.getDirectory(), resourceLocation.getNamespace(), resourceLocation.getPath());
                    File tempFile = new File(folderPackResources.file, path);

                    try {
                        if (tempFile.isDirectory() && FolderPackResources.validatePath(tempFile, path)) {
                            return true;
                        }
                    } catch (IOException ignored) {
                    }
                } else {
                    if (packresources.hasResource(PackType.CLIENT_RESOURCES, resourceLocation)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static void loadResourcePacks() {
        packDeleted = deleteOutdatedPacks();
        packLoaded = loadResourcePack("JEMs Base Resources-" + JSONEntityModels.CURR_VERSION + ".zip", "/base_resources.zip");
    }

    private static boolean deleteOutdatedPacks() {
        File dir = new File(".", "resourcepacks");
        Collection<File> packs = FileUtils.listFiles(dir, new String[]{"zip"}, false);

        boolean deleted = false;

        for (File pack : packs) {
            if (pack.getName().startsWith("JEMs Base Resources") && !pack.getName().endsWith(JSONEntityModels.CURR_VERSION + ".zip")) {
                deleted = pack.delete();
            }
        }

        return deleted;
    }

    private static boolean loadResourcePack(String dest, String src) {
        // Creates file location for resource pack
        File dir = new File(".", "resourcepacks");
        File target = new File(dir, dest);

        // If the pack isn't already in the folder, copies the file over from the mod files
        if (!target.exists()) {
            try {
                dir.mkdirs();
                InputStream in = JSONEntityModels.class.getResourceAsStream(src);
                FileOutputStream out = new FileOutputStream(target);

                // The number of bytes here is how many can be read from the resource pack at one time
                // 16kB is the most common disk chunk size, and using this array size
                // reduces latency between reading and actually processing the data.
                // The performance difference is not significant, but it's improved by using a 16kB array.
                byte[] buf = new byte[16384];
                int len;
                while ((len = in.read(buf)) > 0)
                    out.write(buf, 0, len);

                in.close();
                out.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
