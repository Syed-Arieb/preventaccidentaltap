package com.purpur.pat.client;

import com.purpur.pat.client.mixin.PreventAccidentalTapMixin;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;

import net.minecraft.network.chat.Component;

public class PreventAccidentalTapClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) -> {
			dispatcher.register(ClientCommands.literal("pat-debug")
				.executes(ctx -> {
					PreventAccidentalTapMixin.DEBUG = !PreventAccidentalTapMixin.DEBUG;
					ctx.getSource().sendFeedback(Component.literal("PAT debug: " + PreventAccidentalTapMixin.DEBUG));
					return 1;
				}));
			dispatcher.register(ClientCommands.literal("pat-cooldown")
				.then(ClientCommands.argument("ms", com.mojang.brigadier.arguments.IntegerArgumentType.integer(50, 5000))
					.executes(ctx -> {
						int ms = com.mojang.brigadier.arguments.IntegerArgumentType.getInteger(ctx, "ms");
						PreventAccidentalTapMixin.COOLDOWN_MS = ms;
						ctx.getSource().sendFeedback(Component.literal("PAT cooldown: " + ms + "ms"));
						return 1;
					})));
		});
	}
}
