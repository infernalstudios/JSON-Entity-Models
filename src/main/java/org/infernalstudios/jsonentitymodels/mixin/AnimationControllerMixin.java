package org.infernalstudios.jsonentitymodels.mixin;

import org.infernalstudios.jsonentitymodels.entity.ReplacedEntityBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.controller.AnimationController;

@Mixin(AnimationController.class)
public class AnimationControllerMixin<T extends IAnimatable> {

    @Shadow protected T animatable;

    @ModifyVariable(method = "process",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lsoftware/bernie/geckolib3/core/controller/AnimationController;getModel(Lsoftware/bernie/geckolib3/core/IAnimatable;)Lsoftware/bernie/geckolib3/core/IAnimatableModel;"),
            remap = false)
    private IAnimatableModel<T> jsonentitymodels_getAnimatableModel(IAnimatableModel<T> original) {
        if (this.animatable instanceof ReplacedEntityBase replacedEntityBase) {
            return replacedEntityBase.getModelInstance();
        }

        return original;
    }
}
