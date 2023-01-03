package org.infernalstudios.jsonentitymodels.entity;

public class ReplacedEntityBase {
    protected boolean isHurt;
    protected boolean isBaby;

    public void setHurt(boolean isHurt) {
        this.isHurt = isHurt;
    }

    public void setBaby(boolean isBaby) {
        this.isBaby = isBaby;
    }

    public boolean getBaby() {
        return this.isBaby;
    }
}
