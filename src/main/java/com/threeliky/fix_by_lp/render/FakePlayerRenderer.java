package com.threeliky.fix_by_lp.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import com.threeliky.fix_by_lp.entity.FakePlayerEntity; // Importação necessária

public class FakePlayerRenderer<T extends LivingEntity> extends LivingEntityRenderer<T, PlayerModel<T>> {

    private static final ResourceLocation DEFAULT_SKIN = new ResourceLocation("textures/entity/steve.png");

    public FakePlayerRenderer(EntityRendererProvider.Context context) {
        // Adicione o parâmetro booleano (false para modelo padrão)
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
        
        // Camada de armadura (ajuste os tipos explicitamente)
        this.addLayer(new HumanoidArmorLayer<>(
            this,
            new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
            new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
            context.getModelManager()
        ));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (entity instanceof FakePlayerEntity fakePlayer) {
            return fakePlayer.getSkinTexture();
        }
        return DEFAULT_SKIN;
    }

    @Override
    protected void scale(T entity, PoseStack matrixStack, float partialTickTime) {
        super.scale(entity, matrixStack, partialTickTime);
    }
}