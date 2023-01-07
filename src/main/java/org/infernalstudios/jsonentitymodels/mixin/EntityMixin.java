package org.infernalstudios.jsonentitymodels.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.infernalstudios.jsonentitymodels.util.ResourceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "remove", at = @At("HEAD"))
    private void jsonentitymodels_removeEntityFromCache(Entity.RemovalReason reason, CallbackInfo ci) {
        if (((Entity) (Object) this) instanceof LivingEntity livingEntity) {
            ResourceUtil.removeEntityFromReloadedHashSet(livingEntity);
        }
    }
}
