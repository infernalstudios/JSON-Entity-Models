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

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;

public class ReplacedEnderManModel extends HumanoidAnimatedGeoModel {
    private float defaultHeadY;
    public ReplacedEnderManModel() {
        super("enderman");

        if (this.getAnimationProcessor().getBone("head") != null) {
            this.defaultHeadY = this.getAnimationProcessor().getBone("head").getPosY();
        }
    }

    @Override
    public void setCustomAnimations(GeoReplacedEntity animatable, long instanceId, AnimationState animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (this.getAnimationResource(animatable) == null) {
            LivingEntity livingEntity = this.getCurrentEntity();
            
            CoreGeoBone skull = this.getAnimationProcessor().getBone("skull");
            CoreGeoBone body = this.getAnimationProcessor().getBone("body");
            CoreGeoBone rightArm = this.getAnimationProcessor().getBone("rightarm");
            CoreGeoBone leftArm = this.getAnimationProcessor().getBone("leftarm");
            CoreGeoBone bothArms = this.getAnimationProcessor().getBone("botharms");
            CoreGeoBone rightLeg = this.getAnimationProcessor().getBone("rightleg");
            CoreGeoBone leftLeg = this.getAnimationProcessor().getBone("leftleg");

            if (body != null) {
                body.setRotX(0.0F);
            }

            if (rightArm != null) {
                rightArm.setRotX(rightArm.getRotX() * 0.5F);
            }

            if (leftArm != null) {
                leftArm.setRotX(leftArm.getRotX() * 0.5F);
            }

            if (rightLeg != null) {
                rightLeg.setRotX(rightLeg.getRotX() * 0.5F);
            }

            if (leftLeg != null) {
                leftLeg.setRotX(leftLeg.getRotX() * 0.5F);
            }

            if (rightArm != null && rightArm.getRotX() > 0.4F) {
                rightArm.setRotX(0.4F);
            }

            if (leftArm != null && leftArm.getRotX() > 0.4F) {
                leftArm.setRotX(0.4F);
            }

            if (rightArm != null && rightArm.getRotX() < -0.4F) {
                rightArm.setRotX(-0.4F);
            }

            if (leftArm != null && leftArm.getRotX() < -0.4F) {
                leftArm.setRotX(-0.4F);
            }

            if (rightLeg != null && rightLeg.getRotX() > 0.4F) {
                rightLeg.setRotX(0.4F);
            }

            if (leftLeg != null && leftLeg.getRotX() > 0.4F) {
                leftLeg.setRotX(0.4F);
            }

            if (rightLeg != null && rightLeg.getRotX() < -0.4F) {
                rightLeg.setRotX(-0.4F);
            }

            if (leftLeg != null && leftLeg.getRotX() < -0.4F) {
                leftLeg.setRotX(-0.4F);
            }

            if (((EnderMan) livingEntity).getCarriedBlock() != null) {
                if (bothArms != null) {
                    bothArms.setRotX(0.5F);

                    if (rightArm != null) {
                        rightArm.setRotX(0.0F);
                    }

                    if (leftArm != null) {
                        leftArm.setRotX(0.0F);
                    }
                } else {
                    if (rightArm != null) {
                        rightArm.setRotX(0.5F);
                        rightArm.setRotZ(-0.05F);
                    }

                    if (leftArm != null) {
                        leftArm.setRotX(0.5F);
                        leftArm.setRotZ(0.05F);
                    }
                }
            }

            if (((EnderMan) livingEntity).isCreepy() && skull != null) {
                skull.setPosY(this.defaultHeadY + 5.0F);
            } else if (skull != null) {
                skull.setPosY(this.defaultHeadY);
            }
        }
    }
}
