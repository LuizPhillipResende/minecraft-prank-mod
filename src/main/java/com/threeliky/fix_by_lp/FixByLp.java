package com.threeliky.fix_by_lp;
import com.threeliky.fix_by_lp.entity.ModEntities;
import com.threeliky.fix_by_lp.entity.FakePlayerEntity; // Adicione esta linha
import com.threeliky.fix_by_lp.entity.render.FakePlayerRenderer; // Importação faltante

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraft.server.level.ServerPlayer; // Importação faltante
import net.minecraft.server.level.ServerLevel; // Para ServerLevel
import java.util.concurrent.TimeUnit; // Para TimeUnit (usado no delay)
import net.minecraft.core.BlockPos;
import net.minecraftforge.event.TickEvent;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component; // Importação faltante
import net.minecraftforge.client.event.EntityRenderersEvent; // Importação do evento

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FixByLp.MOD_ID)
public class FixByLp {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "fix_by_lp";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Lista para armazenar tarefas agendadas
    private static final List<DelayedTask> delayedTasks = new ArrayList<>();

    public FixByLp(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModEntities.ENTITIES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
      
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (ModKeyBinds.OPEN_FAKE_CHAT.consumeClick()) {
            Minecraft.getInstance().setScreen(new ChatScreen("/"));
        }
    }
    // Classe auxiliar para armazenar tarefas
    private static class DelayedTask {
        long targetTick;
        Runnable action;

        public DelayedTask(long targetTick, Runnable action) {
            this.targetTick = targetTick;
            this.action = action;
        }
    }

    // Evento de tick do servidor
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            long currentTick = event.getServer().getTickCount();
            List<DelayedTask> toRemove = new ArrayList<>();

            for (DelayedTask task : delayedTasks) {
                if (currentTick >= task.targetTick) {
                    task.action.run();
                    toRemove.add(task);
                }
            }

            delayedTasks.removeAll(toRemove);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event) {
        if (event.getRawText().equalsIgnoreCase("vaca amarela")) {
            ServerPlayer player = event.getPlayer();
            ServerLevel level = (ServerLevel) player.serverLevel();

            // Agenda a criação do FakePlayer após 200 ticks (10 segundos)
            long targetTick = level.getServer().getTickCount() + 200;
            delayedTasks.add(new DelayedTask(targetTick, () -> {
                FakePlayerEntity fakePlayer = ModEntities.FAKE_PLAYER.get().create(level);
                fakePlayer.setPos(player.getX(), player.getY(), player.getZ());
                level.addFreshEntity(fakePlayer);
                fakePlayer.addToPlayerList(player);
            }));
        }
    }

    @SubscribeEvent
    public void onChatSent(ServerChatEvent event) {
        if (event.getRawText().startsWith("/")) {
            ServerPlayer player = event.getPlayer();
            // Verifica se o jogador é o FakePlayer (lógica adicional pode ser necessária)
            if (player.getName().getString().equals("Notch")) {
                event.setCanceled(true);
                // Envia a mensagem como o FakePlayer
                String message = event.getRawText().substring(1);
                player.serverLevel().players().forEach(p ->  
                    p.sendSystemMessage(Component.literal("<Notch> " + message))
                );
            }
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.FAKE_PLAYER.get(), FakePlayerRenderer::new);
        }
    }
}
