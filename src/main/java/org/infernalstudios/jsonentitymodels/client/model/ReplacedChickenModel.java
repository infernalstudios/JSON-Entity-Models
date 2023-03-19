/*
 * Copyright 2023 Infernal Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.infernalstudios.jsonentitymodels.client.model;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Chicken;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ReplacedChickenModel extends HeadTurningAnimatedGeoModel {
    public ReplacedChickenModel() {
        super("chicken");
    }

    @Override
    public void setCustomAnimations(IAnimatable animatable, int instanceId, AnimationEvent animationEvent) {
        if (this.getAnimationResource(animatable) == null) {
            EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);

            IBone head = this.getAnimationProcessor().getBone("head");
            IBone rightLeg = this.getAnimationProcessor().getBone("rightleg");
            IBone leftLeg = this.getAnimationProcessor().getBone("leftleg");
            IBone rightWing = this.getAnimationProcessor().getBone("wingright");
            IBone leftWing = this.getAnimationProcessor().getBone("wingleft");

            if (head != null) {
                head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
                head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
            }

            if (rightLeg != null) {
                rightLeg.setRotationX(Mth.cos(animationEvent.getLimbSwing() * 0.6662F) * 1.4F * animationEvent.getLimbSwingAmount());
            }

            if (leftLeg != null) {
                leftLeg.setRotationX(Mth.cos(animationEvent.getLimbSwing() * 0.6662F + (float) Math.PI) * 1.4F * animationEvent.getLimbSwingAmount());
            }

            if (rightWing != null) {
                rightWing.setRotationZ(this.getBob((Chicken) this.getCurrentEntity(), animationEvent.getPartialTick()));
            }

            if (leftWing != null) {
                leftWing.setRotationZ(-this.getBob((Chicken) this.getCurrentEntity(), animationEvent.getPartialTick()));
            }
        } else {
            super.setCustomAnimations(animatable, instanceId, animationEvent);
        }
    }

    protected float getBob(Chicken chicken, float partialTick) {
        float f = Mth.lerp(partialTick, chicken.oFlap, chicken.flap);
        float f1 = Mth.lerp(partialTick, chicken.oFlapSpeed, chicken.flapSpeed);
        return (Mth.sin(f) + 1.0F) * f1;
    }
}
