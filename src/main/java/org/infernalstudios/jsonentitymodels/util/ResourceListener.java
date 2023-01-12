package org.infernalstudios.jsonentitymodels.util;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class ResourceListener implements ResourceManagerReloadListener {
    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        ResourceUtil.clearHashSet();
    }
}
