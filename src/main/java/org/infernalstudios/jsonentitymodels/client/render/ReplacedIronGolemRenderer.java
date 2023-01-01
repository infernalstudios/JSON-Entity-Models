package org.infernalstudios.jsonentitymodels.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedIronGolemModel;
import org.infernalstudios.jsonentitymodels.entity.ReplacedIronGolemEntity;
import software.bernie.geckolib3.renderers.geo.GeoReplacedEntityRenderer;

public class ReplacedIronGolemRenderer extends GeoReplacedEntityRenderer<ReplacedIronGolemEntity> {

    public ReplacedIronGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedIronGolemModel(), new ReplacedIronGolemEntity());
    }

}
