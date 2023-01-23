package org.infernalstudios.jsonentitymodels.util;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.apache.commons.io.FileUtils;
import org.infernalstudios.jsonentitymodels.JSONEntityModels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ResourceUtil {
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
