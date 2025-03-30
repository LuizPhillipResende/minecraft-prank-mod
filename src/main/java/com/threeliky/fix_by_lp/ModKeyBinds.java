package com.threeliky.fix_by_lp;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class ModKeyBinds {
    public static final KeyMapping OPEN_FAKE_CHAT = new KeyMapping(
        "key.fix_by_lp.open_fake_chat",
        GLFW.GLFW_KEY_T,
        "key.categories.misc"
    );

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(OPEN_FAKE_CHAT);
    }
}