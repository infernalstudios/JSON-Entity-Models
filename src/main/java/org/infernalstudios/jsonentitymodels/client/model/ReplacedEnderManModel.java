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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

public class ReplacedEnderManModel extends HumanoidAnimatedGeoModel {
    private float defaultHeadY;
    public ReplacedEnderManModel() {
        super("enderman");

        if (this.getAnimationProcessor().getBone("head") != null) {
            this.defaultHeadY = this.getAnimationProcessor().getBone("head").getPositionY();
        }
    }

    @Override
    public void setCustomAnimations(IAnimatable animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);

        if (this.getAnimationResource(animatable) == null) {
            LivingEntity livingEntity = this.getCurrentEntity();
            
            IBone skull = this.getAnimationProcessor().getBone("skull");
            IBone body = this.getAnimationProcessor().getBone("body");
            IBone rightArm = this.getAnimationProcessor().getBone("rightarm");
            IBone leftArm = this.getAnimationProcessor().getBone("leftarm");
            IBone bothArms = this.getAnimationProcessor().getBone("botharms");
            IBone rightLeg = this.getAnimationProcessor().getBone("rightleg");
            IBone leftLeg = this.getAnimationProcessor().getBone("leftleg");

            if (body != null) {
                body.setRotationX(0.0F);
            }

            if (rightArm != null) {
                rightArm.setRotationX(rightArm.getRotationX() * 0.5F);
            }

            if (leftArm != null) {
                leftArm.setRotationX(leftArm.getRotationX() * 0.5F);
            }

            if (rightLeg != null) {
                rightLeg.setRotationX(rightLeg.getRotationX() * 0.5F);
            }

            if (leftLeg != null) {
                leftLeg.setRotationX(leftLeg.getRotationX() * 0.5F);
            }

            if (rightArm != null && rightArm.getRotationX() > 0.4F) {
                rightArm.setRotationX(0.4F);
            }

            if (leftArm != null && leftArm.getRotationX() > 0.4F) {
                leftArm.setRotationX(0.4F);
            }

            if (rightArm != null && rightArm.getRotationX() < -0.4F) {
                rightArm.setRotationX(-0.4F);
            }

            if (leftArm != null && leftArm.getRotationX() < -0.4F) {
                leftArm.setRotationX(-0.4F);
            }

            if (rightLeg != null && rightLeg.getRotationX() > 0.4F) {
                rightLeg.setRotationX(0.4F);
            }

            if (leftLeg != null && leftLeg.getRotationX() > 0.4F) {
                leftLeg.setRotationX(0.4F);
            }

            if (rightLeg != null && rightLeg.getRotationX() < -0.4F) {
                rightLeg.setRotationX(-0.4F);
            }

            if (leftLeg != null && leftLeg.getRotationX() < -0.4F) {
                leftLeg.setRotationX(-0.4F);
            }

            if (((EnderMan) livingEntity).getCarriedBlock() != null) {
                if (bothArms != null) {
                    bothArms.setRotationX(0.5F);

                    if (rightArm != null) {
                        rightArm.setRotationX(0.0F);
                    }

                    if (leftArm != null) {
                        leftArm.setRotationX(0.0F);
                    }
                } else {
                    if (rightArm != null) {
                        rightArm.setRotationX(0.5F);
                        rightArm.setRotationZ(-0.05F);
                    }

                    if (leftArm != null) {
                        leftArm.setRotationX(0.5F);
                        leftArm.setRotationZ(0.05F);
                    }
                }
            }

            if (((EnderMan) livingEntity).isCreepy() && skull != null) {
                skull.setPositionY(this.defaultHeadY + 5.0F);
            } else if (skull != null) {
                skull.setPositionY(this.defaultHeadY);
            }
        }
    }
}
