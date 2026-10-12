package cn.wyceden.mixin.interfaces;

import net.minecraft.util.Hand;

public interface IMixinLivingEntity {
	void fakeSwingHand(Hand hand);
}
