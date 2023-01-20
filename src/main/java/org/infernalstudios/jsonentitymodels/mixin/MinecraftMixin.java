package org.infernalstudios.jsonentitymodels.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.repository.PackRepository;
import org.infernalstudios.jsonentitymodels.JSONEntityModels;
import org.infernalstudios.jsonentitymodels.util.ResourceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow public abstract PackRepository getResourcePackRepository();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void jsonentitymodels_enableDefaultPacks(GameConfig config, CallbackInfo ci) {
        if (ResourceUtil.packLoaded) {
            PackRepository repository = this.getResourcePackRepository();
            Collection<String> selectedPacks = new ArrayList<>(repository.getSelectedIds());
            if (!ResourceUtil.packDeleted && !selectedPacks.contains("file/JEMs Base Resources-" + JSONEntityModels.CURR_VERSION + ".zip")) {
                selectedPacks.add("file/JEMs Base Resources-" + JSONEntityModels.CURR_VERSION + ".zip");
                repository.setSelected(selectedPacks);
                repository.openAllSelected();
            }
        }
    }
}
