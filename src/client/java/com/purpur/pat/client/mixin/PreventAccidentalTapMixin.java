package com.purpur.pat.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class PreventAccidentalTapMixin {

	private static final boolean DEBUG = true;
	private static long pat$lastChestAttackMs = 0L;

	@org.spongepowered.asm.mixin.gen.Accessor("missTime")
	public abstract int getLeftClickCounter();

	@Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
	private void pat$throttleHeldChestAttacks(boolean destroying, CallbackInfo ci) {
		if (!destroying) {
			return;
		}
		Minecraft client = (Minecraft) (Object) this;
		if (client.hitResult == null || client.hitResult.getType() != HitResult.Type.BLOCK) {
			return;
		}
		if (client.level == null || client.player == null) {
			return;
		}
		BlockHitResult blockHit = (BlockHitResult) client.hitResult;
		BlockState state = client.level.getBlockState(blockHit.getBlockPos());
		if (!(state.getBlock() instanceof ChestBlock)) {
			return;
		}
		if (!(client.player.getMainHandItem().getItem() instanceof ShearsItem)) {
			return;
		}
		if (DEBUG) {
			client.player.sendSystemMessage(net.minecraft.network.chat.Component.literal("[PAT] Held attack blocked"));
		}
		ci.cancel();
	}

	@Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
	private void pat$throttleChestAttacks(CallbackInfoReturnable<Boolean> cir) {
		Minecraft client = (Minecraft) (Object) this;
		if (client.hitResult == null || client.hitResult.getType() != HitResult.Type.BLOCK) {
			return;
		}
		if (client.level == null || client.player == null) {
			return;
		}
		BlockHitResult blockHit = (BlockHitResult) client.hitResult;
		BlockPos pos = blockHit.getBlockPos();
		BlockState state = client.level.getBlockState(pos);
		if (!(state.getBlock() instanceof ChestBlock)) {
			return;
		}
		if (DEBUG) {
			client.player.sendSystemMessage(net.minecraft.network.chat.Component.literal("[PAT] Chest click detected"));
		}
		Player player = client.player;
		if (!(player.getMainHandItem().getItem() instanceof ShearsItem)) {
			return;
		}
		long now = System.currentTimeMillis();
		if (now - pat$lastChestAttackMs < 800L) {
			if (DEBUG) {
				client.player.sendSystemMessage(net.minecraft.network.chat.Component.literal("[PAT] Throttled (too soon)"));
			}
			cir.setReturnValue(false);
			return;
		}
		if (getLeftClickCounter() > 0) {
			cir.setReturnValue(false);
			return;
		}
		pat$lastChestAttackMs = now;
	}
}
