package org.infernalstudios.jsonentitymodels.entity;

import org.infernalstudios.jsonentitymodels.client.model.HeadTurningAnimatedGeoModel;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public abstract class ReplacedEntityBase implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    protected boolean isHurt;
    protected boolean isBaby;
    protected boolean isDead;

    protected boolean inWater;

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

    public void setInWater(boolean inWater) {
        this.inWater = inWater;
    }

    public void setModelInstance(HeadTurningAnimatedGeoModel modelInstance) {
        this.modelInstance = modelInstance;
    }

    public HeadTurningAnimatedGeoModel getModelInstance() {
        return this.modelInstance;
    }

    protected abstract <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event);

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
