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
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class ReplacedChickenModel extends HeadTurningAnimatedGeoModel {
    public ReplacedChickenModel() {
        super("chicken");
    }

    @Override
    public void setCustomAnimations(GeoReplacedEntity animatable, long instanceId, AnimationState animationState) {
        if (this.getAnimationResource(animatable) == null) {
            EntityModelData extraData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            CoreGeoBone head = this.getAnimationProcessor().getBone("head");
            CoreGeoBone rightLeg = this.getAnimationProcessor().getBone("rightleg");
            CoreGeoBone leftLeg = this.getAnimationProcessor().getBone("leftleg");
            CoreGeoBone rightWing = this.getAnimationProcessor().getBone("wingright");
            CoreGeoBone leftWing = this.getAnimationProcessor().getBone("wingleft");

            if (head != null) {
                head.setRotX(extraData.headPitch() * ((float) Math.PI / 180F));
                head.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180F));
            }

            if (rightLeg != null) {
                rightLeg.setRotX(Mth.cos(animationState.getLimbSwing() * 0.6662F) * 1.4F * animationState.getLimbSwingAmount());
            }

            if (leftLeg != null) {
                leftLeg.setRotX(Mth.cos(animationState.getLimbSwing() * 0.6662F + (float) Math.PI) * 1.4F * animationState.getLimbSwingAmount());
            }

            if (rightWing != null) {
                rightWing.setRotZ(this.getBob((Chicken) this.getCurrentEntity(), animationState.getPartialTick()));
            }

            if (leftWing != null) {
                leftWing.setRotZ(-this.getBob((Chicken) this.getCurrentEntity(), animationState.getPartialTick()));
            }
        } else {
            super.setCustomAnimations(animatable, instanceId, animationState);
        }
    }

    protected float getBob(Chicken chicken, float partialTick) {
        float f = Mth.lerp(partialTick, chicken.oFlap, chicken.flap);
        float f1 = Mth.lerp(partialTick, chicken.oFlapSpeed, chicken.flapSpeed);
        return (Mth.sin(f) + 1.0F) * f1;
    }
}
