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
import net.minecraft.world.entity.animal.IronGolem;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ReplacedIronGolemModel extends HeadTurningAnimatedGeoModel {
    public ReplacedIronGolemModel() {
        super("iron_golem");
    }

    @Override
    public void setCustomAnimations(IAnimatable animatable, int instanceId, AnimationEvent animationEvent) {
        if (this.getAnimationResource(animatable) == null) {
            EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);

            IBone head = this.getAnimationProcessor().getBone("head");
            IBone rightLeg = this.getAnimationProcessor().getBone("rightleg");
            IBone leftLeg = this.getAnimationProcessor().getBone("leftleg");
            IBone rightArm = this.getAnimationProcessor().getBone("rightarm");
            IBone leftArm = this.getAnimationProcessor().getBone("leftarm");

            if (head != null) {
                head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
                head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            }

            if (rightLeg != null) {
                rightLeg.setRotationX(-1.5F * Mth.triangleWave(animationEvent.getLimbSwing(), 13.0F) * animationEvent.getLimbSwingAmount());
            }

            if (leftLeg != null) {
                leftLeg.setRotationX(1.5F * Mth.triangleWave(animationEvent.getLimbSwing(), 13.0F) * animationEvent.getLimbSwingAmount());
            }

            int i = ((IronGolem) this.getCurrentEntity()).getAttackAnimationTick();
            if (i > 0) {
                if (rightArm != null) {
                    rightArm.setRotationX(2.0F - 1.5F * Mth.triangleWave((float) i - animationEvent.getPartialTick(), 10.0F));
                }

                if (leftArm != null) {
                    leftArm.setRotationX(2.0F - 1.5F * Mth.triangleWave((float) i - animationEvent.getPartialTick(), 10.0F));
                }
            } else {
                int j = ((IronGolem) this.getCurrentEntity()).getOfferFlowerTick();
                if (j > 0) {
                    if (rightArm != null) {
                        rightArm.setRotationX(-0.8F + 0.025F * Mth.triangleWave((float) j, 70.0F));
                    }

                    if (leftArm != null) {
                        leftArm.setRotationX(0.0F);
                    }
                } else {
                    if (rightArm != null) {
                        rightArm.setRotationX((-0.2F + 1.5F * Mth.triangleWave(animationEvent.getLimbSwing(), 13.0F)) * animationEvent.getLimbSwingAmount());
                    }

                    if (leftArm != null) {
                        leftArm.setRotationX((-0.2F - 1.5F * Mth.triangleWave(animationEvent.getLimbSwing(), 13.0F)) * animationEvent.getLimbSwingAmount());
                    }
                }
            }
        } else {
            super.setCustomAnimations(animatable, instanceId, animationEvent);
        }
    }
}
