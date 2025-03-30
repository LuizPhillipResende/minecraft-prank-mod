package com.threeliky.fix_by_lp.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.List;
import java.util.UUID;

public class FakePlayerEntity extends Monster {
    private GameProfile gameProfile;
    private ServerPlayer targetPlayer;
    private static final double MAX_DISTANCE = 10.0; // Distância máxima antes de teleportar

    public FakePlayerEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.gameProfile = new GameProfile(UUID.randomUUID(), "Notch");
    }

    /**
     * Define a skin do Fake Player baseada em um jogador real.
     */
    public void setSkinFromPlayer(ServerPlayer player) {
        this.gameProfile = player.getGameProfile();
    }

    /**
     * Retorna a textura da skin do Fake Player.
     */
    public ResourceLocation getSkinTexture() {
        if (gameProfile.getProperties().containsKey("textures")) {
            Property property = gameProfile.getProperties().get("textures").iterator().next();
            return ResourceLocation.parse("minecraft:textures/entity/" + property.getValue() + ".png");
        }
        return ResourceLocation.parse("minecraft:textures/entity/steve.png");
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            if (targetPlayer == null || targetPlayer.distanceTo(this) > MAX_DISTANCE) {
                findNewTarget();
                if (targetPlayer != null) {
                    // Teleporta para o jogador alvo
                    this.teleportTo(targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ());
                    // Envia mensagem no chat simulando /tp
                    level().players().forEach(p -> 
                        p.sendSystemMessage(Component.literal("§e[Servidor] Notch teleportou-se para " + targetPlayer.getName().getString()))
                    );
                }
            }

            // Olha para o jogador alvo
            if (targetPlayer != null) {
                this.lookAt(targetPlayer, 30.0F, 30.0F);
            }
        }
    }

    /**
     * Encontra um jogador aleatório para ser o alvo da entidade.
     */
    private void findNewTarget() {
        if (level() instanceof ServerLevel serverLevel) {
            List<ServerPlayer> players = serverLevel.players();
            if (!players.isEmpty()) {
                this.targetPlayer = players.get(random.nextInt(players.size()));
            }
        }
    }

    /**
     * Adiciona o Fake Player à lista de jogadores visível ao pressionar TAB.
     */
    public void addToPlayerList(ServerPlayer target) {
        if (target.connection != null) {
            ClientboundPlayerInfoUpdatePacket packet = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, target);
            target.connection.send(packet);
        }
    }
}
