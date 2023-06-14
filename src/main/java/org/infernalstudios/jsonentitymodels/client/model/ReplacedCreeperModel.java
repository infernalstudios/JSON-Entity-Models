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

public class ReplacedCreeperModel extends HeadTurningAnimatedGeoModel {
    public ReplacedCreeperModel() {
        super("creeper");
    }

    @Override
    public void setCustomAnimations(GeoReplacedEntity animatable, long instanceId, AnimationState animationState) {
        if (this.getAnimationResource(animatable) == null) {
            EntityModelData extraData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            CoreGeoBone head = this.getAnimationProcessor().getBone("head");
            CoreGeoBone rightHindLeg = this.getAnimationProcessor().getBone("rightlegback");
            CoreGeoBone leftHindLeg = this.getAnimationProcessor().getBone("leftlegback");
            CoreGeoBone rightFrontLeg = this.getAnimationProcessor().getBone("rightlegfront");
            CoreGeoBone leftFrontLeg = this.getAnimationProcessor().getBone("leftlegfront");

            if (head != null) {
                head.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180F));
                head.setRotX(extraData.headPitch() * ((float) Math.PI / 180F));
            }

            if (rightHindLeg != null) {
                rightHindLeg.setRotX(Mth.cos(animationState.getLimbSwing() * 0.6662F) * 1.4F * animationState.getLimbSwingAmount());
            }

            if (leftHindLeg != null) {
                leftHindLeg.setRotX(Mth.cos(animationState.getLimbSwing() * 0.6662F + (float) Math.PI) * 1.4F * animationState.getLimbSwingAmount());
            }

            if (rightFrontLeg != null) {
                rightFrontLeg.setRotX(Mth.cos(animationState.getLimbSwing() * 0.6662F + (float) Math.PI) * 1.4F * animationState.getLimbSwingAmount());
            }

            if (leftFrontLeg != null) {
                leftFrontLeg.setRotX(Mth.cos(animationState.getLimbSwing() * 0.6662F) * 1.4F * animationState.getLimbSwingAmount());
            }
        } else {
            super.setCustomAnimations(animatable, instanceId, animationState);
        }
    }
}
