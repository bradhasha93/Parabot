package org.brad.utils;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.input.Mouse;
import org.rev317.api.methods.Bank;
import org.rev317.api.methods.Calculations;
import org.rev317.api.methods.Camera;
import org.rev317.api.methods.Interfaces;
import org.rev317.api.methods.Inventory;
import org.rev317.api.wrappers.hud.Item;
import org.rev317.api.wrappers.scene.SceneObject;

public class BankManager {

	// Bank ids
	private static final int[] BANK_BOOTHS = { 2213 };

	/**
	 * Attempts to open bank
	 * 
	 * @param walkTo
	 *            to tile if to far away
	 * @return true if interacted with bank
	 */
	public static boolean openBank(boolean walkTo) {
		final SceneObject booth = Methods.getObject(BANK_BOOTHS);
		if (isBankOpen())
			return true;
		if (booth != null) {
			if (Calculations.distanceTo(booth.getLocation()) > 5)
				Mouse.getInstance().click(
						Calculations.tileToMinimap(booth.getLocation(), true));
			if (!booth.isOnScreen(true))
				Camera.turnTo(booth);
			if (booth.interact("Use-quickly")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Attempts to withdraw an item from the bank
	 * @param id
	 * @param amnt
	 * @return true if ending count > start count
	 */
	public static boolean withdraw(final int id, final int amnt) {
		final Item item = Bank.getItem(id);
		final int count = Inventory.getCount(true, id);
		if (item != null) {
			switch (amnt) {
			case 1:
				item.interact("Withdraw 1");
				break;
			case 5:
				item.interact("Withdraw 5");
			default:
				if (amnt > 9 && amnt < 28) {
					item.interact("Withdraw X");
					for (int i = 0; i < 21
							&& Interfaces.getChatboxInterface() != null
							&& Interfaces.getChatboxInterfaceId() != 1; i++) {
						Time.sleep(250);
					}
					Keyboard.getInstance().sendKeys("" + amnt);
				} else {
					item.interact("Withdraw All");
				}
			}
		}
		return Inventory.getCount(true, id) > count;
	}

	/**
	 * Checks if bank is open
	 * 
	 * @return true if bank open
	 */
	public static boolean isBankOpen() {
		return Interfaces.get(23350).isVisible();
	}
}
