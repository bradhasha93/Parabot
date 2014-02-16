package utils;

import java.util.Collection;
import java.util.HashMap;

import org.parabot.core.ui.components.LogArea;
import org.parabot.environment.api.utils.Filter;
import org.parabot.environment.input.Mouse;
import org.rev317.api.methods.Calculations;
import org.rev317.api.methods.Camera;
import org.rev317.api.methods.GroundItems;
import org.rev317.api.methods.Players;
import org.rev317.api.methods.Walking;
import org.rev317.api.wrappers.scene.GroundItem;
import org.rev317.api.wrappers.scene.Tile;

public class Looting {

	private static final HashMap<Integer, String> LOOT = new HashMap<>();

	private static int[] itemIds;
	private static String[] itemNames;

	/**
	 * Sets up loot hashmap with starting items/ids.
	 * 
	 * @param names
	 * @param ids
	 */
	public static void setupLootMap(final String[] names, final int[] ids) {
		for (int index = 0; index < names.length && index < ids.length; index++) {
			LOOT.put(ids[index], names[index]);
		}
		itemIds = convertIntegerCollection(LOOT.keySet());
		itemNames = convertStringCollection(LOOT.values());
	}

	/**
	 * Attempts to find a valid ground item matching ids.
	 * 
	 * @param ids
	 * @return Valid ground item if exists
	 */
	public static GroundItem getLoot() {
		final GroundItem[] items = GroundItems
				.getNearest(new Filter<GroundItem>() {
					@Override
					public boolean accept(GroundItem g) {
						for (int id : itemIds) {
							if (g != null
									&& g.getId() == id
									&& Calculations.distanceBetween(Players.getLocal().getLocation(), g
											.getLocation()) < 15)
								return true;
						}
						return false;
					}

				});
		return items.length > 0 ? items[0] : null;
	}

	/**
	 * Attempts to take item from the ground
	 * 
	 * @param item
	 * @return True if item was successfully interacted with
	 */
	public static boolean takeLoot(final GroundItem item) {
		if (item != null) {
			if (!item.isOnScreen())
				Camera.turnTo(item);
			if (item.isOnScreen()) {
				final String name = LOOT.get(item.getId());
				LogArea.log(name);
				if (Calculations.distanceBetween(Players.getLocal()
						.getLocation(), item.getLocation()) > 5)
					Mouse.getInstance().click(
							Calculations.tileToMinimap(item.getLocation()),
							true);	
				if (name == null) {
					return item.interact("Take");
				} else {
					return item.interact("Take " + name);
				}
			}
		}
		return false;
	}

	/**
	 * Converts integer collection to int[] array
	 * 
	 * @param coll
	 * @return converted collection in int[] array
	 */
	public static int[] convertIntegerCollection(Collection<Integer> coll) {
		int[] collection = new int[coll.size()];
		for (int index = 0; index < collection.length; index++) {
			collection[index] = (int) coll.toArray()[index];
		}
		return collection;
	}

	/**
	 * Converts String collection to String[] array
	 * 
	 * @param coll
	 * @return converted collection in String[] array
	 */
	public static String[] convertStringCollection(Collection<String> coll) {
		String[] collection = new String[coll.size()];
		for (int index = 0; index < collection.length; index++) {
			collection[index] = (String) coll.toArray()[index];
		}
		return collection;
	}

	/**
	 * Adds a new item to the loot list.
	 * 
	 * @param name
	 * @param id
	 */
	public static void addLoot(String name, int id) {
		LOOT.put(id, name);
		itemNames = convertStringCollection(LOOT.values());
		itemIds = convertIntegerCollection(LOOT.keySet());
		LogArea.log(name + " has been added to loot list.");
	}

	/**
	 * Removes specified item from loot list.s
	 * 
	 * @param name
	 */
	public static void removeLoot(String name) {
		LOOT.remove(name);
		itemNames = convertStringCollection(LOOT.values());
		itemIds = convertIntegerCollection(LOOT.keySet());
		LogArea.log(name + " has been removed from loot list.");
	}
}
