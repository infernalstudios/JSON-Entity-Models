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
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class ReplacedIronGolemModel extends HeadTurningAnimatedGeoModel {
    public ReplacedIronGolemModel() {
        super("iron_golem");
    }

    @Override
    public void setCustomAnimations(GeoReplacedEntity animatable, long instanceId, AnimationState animationState) {
        if (this.getAnimationResource(animatable) == null) {
            EntityModelData extraData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            CoreGeoBone head = this.getAnimationProcessor().getBone("head");
            CoreGeoBone rightLeg = this.getAnimationProcessor().getBone("rightleg");
            CoreGeoBone leftLeg = this.getAnimationProcessor().getBone("leftleg");
            CoreGeoBone rightArm = this.getAnimationProcessor().getBone("rightarm");
            CoreGeoBone leftArm = this.getAnimationProcessor().getBone("leftarm");

            if (head != null) {
                head.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180F));
                head.setRotX(extraData.headPitch() * ((float) Math.PI / 180F));
            }

            if (rightLeg != null) {
                rightLeg.setRotX(-1.5F * Mth.triangleWave(animationState.getLimbSwing(), 13.0F) * animationState.getLimbSwingAmount());
            }

            if (leftLeg != null) {
                leftLeg.setRotX(1.5F * Mth.triangleWave(animationState.getLimbSwing(), 13.0F) * animationState.getLimbSwingAmount());
            }

            int i = ((IronGolem) this.getCurrentEntity()).getAttackAnimationTick();
            if (i > 0) {
                if (rightArm != null) {
                    rightArm.setRotX(2.0F - 1.5F * Mth.triangleWave((float) i - animationState.getPartialTick(), 10.0F));
                }

                if (leftArm != null) {
                    leftArm.setRotX(2.0F - 1.5F * Mth.triangleWave((float) i - animationState.getPartialTick(), 10.0F));
                }
            } else {
                int j = ((IronGolem) this.getCurrentEntity()).getOfferFlowerTick();
                if (j > 0) {
                    if (rightArm != null) {
                        rightArm.setRotX(-0.8F + 0.025F * Mth.triangleWave((float) j, 70.0F));
                    }

                    if (leftArm != null) {
                        leftArm.setRotX(0.0F);
                    }
                } else {
                    if (rightArm != null) {
                        rightArm.setRotX((-0.2F + 1.5F * Mth.triangleWave(animationState.getLimbSwing(), 13.0F)) * animationState.getLimbSwingAmount());
                    }

                    if (leftArm != null) {
                        leftArm.setRotX((-0.2F - 1.5F * Mth.triangleWave(animationState.getLimbSwing(), 13.0F)) * animationState.getLimbSwingAmount());
                    }
                }
            }
        } else {
            super.setCustomAnimations(animatable, instanceId, animationState);
        }
    }
}
