package org.infernalstudios.jsonentitymodels.entity;

import org.infernalstudios.jsonentitymodels.client.model.HeadTurningAnimatedGeoModel;

public class ReplacedEntityBase {
    protected boolean isHurt;
    protected boolean isBaby;
    protected boolean isDead;

    private HeadTurningAnimatedGeoModel modelInstance;

    public void setHurt(boolean isHurt) {
        this.isHurt = isHurt;
    }

    public void setBaby(boolean isBaby) {
        this.isBaby = isBaby;
    }

    public boolean getBaby() {
        return this.isBaby;
    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }

    public void setModelInstance(HeadTurningAnimatedGeoModel modelInstance) {
        this.modelInstance = modelInstance;
    }

    public HeadTurningAnimatedGeoModel getModelInstance() {
        return this.modelInstance;
    }
}
