package org.infernalstudios.jsonentitymodels.mixin;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import org.infernalstudios.jsonentitymodels.util.ResourceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(EntityRenderers.class)
public class EntityRenderersMixin {

    @Inject(method = "createEntityRenderers", at = @At("HEAD"))
    private static void jsonentitymodels_storeDefaultRenderers(EntityRendererProvider.Context context, CallbackInfoReturnable<Map<EntityType<?>, EntityRenderer<?>>> cir) {
        ResourceUtil.storeDefaultRenderers();
    }

}
