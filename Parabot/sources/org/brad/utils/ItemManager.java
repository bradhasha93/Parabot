package org.brad.utils;

import java.awt.event.KeyEvent;

import org.parabot.environment.input.Keyboard;
import org.rev317.api.methods.Inventory;
import org.rev317.api.wrappers.hud.Item;
import org.rev317.api.wrappers.hud.Tab;

public class ItemManager {

	public static Item getItem(final int... ids) {
		final Item[] items = Inventory.getItems(ids);
		return items.length > 0 ? items[0] : null;
	}

	public static boolean hasAllItems(final int... ids) {
		for (int i = 0; i < ids.length; i++) {
			if (Inventory.getCount(true, ids[i]) > 0)
				continue;
			else
				return false;
		}
		return true;
	}

	public static boolean hasItem(final int... ids) {
		return Inventory.getItems(ids).length > 0;
	}

	public static void interactWithItem(final Item item, final String action) {
		if (Tab.INVENTORY.isOpen()) {
			if (item != null)
				item.interact(action);
		} else {
			Keyboard.getInstance().pressKey(KeyEvent.VK_F1);
			Keyboard.getInstance().releaseKey(KeyEvent.VK_F1);
		}
	}

}
