package org.brad.utils;

import org.parabot.environment.input.Mouse;
import org.rev317.api.methods.Calculations;
import org.rev317.api.methods.Camera;
import org.rev317.api.methods.Interfaces;
import org.rev317.api.wrappers.scene.SceneObject;

public class BankManager {

	private static final int[] BANK_BOOTHS = { 2213 };

	public static boolean openBank(boolean walkTo) {
		final SceneObject booth = Methods.getObject(BANK_BOOTHS);
		if (isBankOpen())
			return true;
		if (booth != null) {
			if (Calculations.distanceTo(booth.getLocation()) > 5)
				Mouse.getInstance().click(Calculations.tileToMinimap(booth.getLocation(), true));
			if (!booth.isOnScreen(true))
				Camera.turnTo(booth);
			if (booth.interact("Use-quickly")) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isBankOpen() {
		return Interfaces.get(23350).isVisible();
	}
}
