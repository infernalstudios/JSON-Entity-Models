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
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class ReplacedSpiderModel extends HeadTurningAnimatedGeoModel {
    public ReplacedSpiderModel() {
        super("spider");
    }

    public ReplacedSpiderModel(String entityTypeName) {
        super(entityTypeName);
    }

    @Override
    public void setCustomAnimations(GeoReplacedEntity animatable, long instanceId, AnimationState animationState) {
        if (this.getAnimationResource(animatable) == null) {
            EntityModelData extraData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            CoreGeoBone head = this.getAnimationProcessor().getBone("head");
            CoreGeoBone rightFrontLeg = this.getAnimationProcessor().getBone("rightleg1");
            CoreGeoBone leftFrontLeg = this.getAnimationProcessor().getBone("leftleg1");
            CoreGeoBone rightMiddleFrontLeg = this.getAnimationProcessor().getBone("rightleg2");
            CoreGeoBone leftMiddleFrontLeg = this.getAnimationProcessor().getBone("leftleg2");
            CoreGeoBone rightMiddleHindLeg = this.getAnimationProcessor().getBone("rightleg3");
            CoreGeoBone leftMiddleHindLeg = this.getAnimationProcessor().getBone("leftleg3");
            CoreGeoBone rightHindLeg = this.getAnimationProcessor().getBone("rightleg4");
            CoreGeoBone leftHindLeg = this.getAnimationProcessor().getBone("leftleg4");

            if (head != null) {
                head.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180F));
                head.setRotX(extraData.headPitch() * ((float) Math.PI / 180F));
            }

            float f3 = -(Mth.cos(animationState.getLimbSwing() * 0.6662F * 2.0F + 0.0F) * 0.4F) * animationState.getLimbSwingAmount();
            float f4 = -(Mth.cos(animationState.getLimbSwing() * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * animationState.getLimbSwingAmount();
            float f5 = -(Mth.cos(animationState.getLimbSwing() * 0.6662F * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * animationState.getLimbSwingAmount();
            float f6 = -(Mth.cos(animationState.getLimbSwing() * 0.6662F * 2.0F + ((float)Math.PI * 1.5F)) * 0.4F) * animationState.getLimbSwingAmount();
            float f7 = Math.abs(Mth.sin(animationState.getLimbSwing() * 0.6662F + 0.0F) * 0.4F) * animationState.getLimbSwingAmount();
            float f8 = Math.abs(Mth.sin(animationState.getLimbSwing() * 0.6662F + (float)Math.PI) * 0.4F) * animationState.getLimbSwingAmount();
            float f9 = Math.abs(Mth.sin(animationState.getLimbSwing() * 0.6662F + ((float)Math.PI / 2F)) * 0.4F) * animationState.getLimbSwingAmount();
            float f10 = Math.abs(Mth.sin(animationState.getLimbSwing() * 0.6662F + ((float)Math.PI * 1.5F)) * 0.4F) * animationState.getLimbSwingAmount();

            if (rightHindLeg != null) {
                rightHindLeg.setRotY(f3);
                rightHindLeg.setRotZ(f7);
            }

            if (leftHindLeg != null) {
                leftHindLeg.setRotY(-f3);
                leftHindLeg.setRotZ(-f7);
            }

            if (rightMiddleHindLeg != null) {
                rightMiddleHindLeg.setRotY(f4);
                rightMiddleHindLeg.setRotZ(f8);
            }

            if (leftMiddleHindLeg != null) {
                leftMiddleHindLeg.setRotY(-f4);
                leftMiddleHindLeg.setRotZ(-f8);
            }

            if (rightMiddleFrontLeg != null) {
                rightMiddleFrontLeg.setRotY(f5);
                rightMiddleFrontLeg.setRotZ(f9);
            }

            if (leftMiddleFrontLeg != null) {
                leftMiddleFrontLeg.setRotY(-f5);
                leftMiddleFrontLeg.setRotZ(-f9);
            }

            if (rightFrontLeg != null) {
                rightFrontLeg.setRotY(f6);
                rightFrontLeg.setRotZ(f10);
            }

            if (leftFrontLeg != null) {
                leftFrontLeg.setRotY(-f6);
                leftFrontLeg.setRotZ(-f10);
            }
        } else {
            super.setCustomAnimations(animatable, instanceId, animationState);
        }
    }
}
