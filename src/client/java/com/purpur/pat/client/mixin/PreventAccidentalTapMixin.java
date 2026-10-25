package com.purpur.pat.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class PreventAccidentalTapMixin {

	@org.spongepowered.asm.mixin.gen.Accessor("missTime")
	public abstract int getLeftClickCounter();

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
		Player player = client.player;
		if (player.getMainHandItem().isEmpty()) {
			return;
		}
		if (getLeftClickCounter() > 0) {
			cir.setReturnValue(false);
		}
	}
}
