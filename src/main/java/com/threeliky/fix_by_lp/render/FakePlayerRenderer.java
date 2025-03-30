package com.threeliky.fix_by_lp.entity.render;

import com.threeliky.fix_by_lp.entity.FakePlayerEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class FakePlayerRenderer<T extends FakePlayerEntity> extends LivingEntityRenderer<T, PlayerModel<T>> {
    private static final ResourceLocation DEFAULT_SKIN = ResourceLocation.parse("minecraft:textures/entity/steve.png");

    public FakePlayerRenderer(EntityRendererProvider.Context context, boolean slim) {
        super(context, new PlayerModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this,
            new PlayerModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR), slim),
            new PlayerModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR), slim),
            context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return entity.getSkinTexture();
    }
}