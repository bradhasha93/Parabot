package utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

import org.parabot.environment.api.utils.Filter;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.input.Mouse;
import org.rev317.api.methods.Inventory;
import org.rev317.api.methods.Players;
import org.rev317.api.methods.SceneObjects;
import org.rev317.api.wrappers.hud.Item;
import org.rev317.api.wrappers.hud.Tab;
import org.rev317.api.wrappers.scene.SceneObject;

public class Methods {

	static Mouse mouse = Mouse.getInstance();

	public static Item getItem(int... ids) {
		final Item[] items = Inventory.getItems(ids);
		return items.length > 0 ? items[0] : null;
	}

	public static boolean hasItem(int... ids) {
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

	public static boolean inCombat() {
		return Players.getLocal().getInteractingCharacter() != 0;
	}

	public static String perHour(double gained, long startTime) {
		return formatNumber((int) ((gained) * 3600000D / (System
				.currentTimeMillis() - startTime)));
	}

	public static String formatNumber(int start) {
		DecimalFormat nf = new DecimalFormat("#,###");
		return nf.format(start);
	}
	
	public static String getTimeRunning(long startTime) {
		final long duration = System.currentTimeMillis() - startTime;
		int seconds = (int) ((duration / 1000) % 60), minutes = (int) ((duration / (1000 * 60)) % 60), hours = (int) ((duration / (1000 * 60 * 60)) % 24);

		
		String hours1, minutes1, seconds1;
		hours1 = (hours < 10) ? "0" + hours : ""+hours;
		minutes1 = (minutes < 10) ? "0" + minutes : ""+minutes;
		seconds1 = (seconds < 10) ? "0" + seconds : ""+seconds;

		return hours1 + ":" + minutes1 + ":" + seconds1;
	}

	public static SceneObject getObject(final int...ids) {
		final SceneObject[] objects = SceneObjects.getNearest(new Filter<SceneObject>() {

			@Override
			public boolean accept(SceneObject o) {
				if (o != null) {
					for (int i : ids) {
						if (o.getId() == i) {
							return true;
						}
					}
				}
						return false;
			}
		});
		return objects.length > 0 ? objects[0] : null;
	}

	private final static int CIRCLE_RADIUS = 15;
	private final static int CIRCLE_DIAMETER = CIRCLE_RADIUS * 2;

	public static void plusMouse(Graphics g, Color color, final Point m) {
		g.setColor(color);
		g.drawOval(m.x - CIRCLE_RADIUS / 3, m.y - CIRCLE_RADIUS / 3,
				CIRCLE_DIAMETER / 3, CIRCLE_DIAMETER / 3);
		g.drawLine(m.x - CIRCLE_RADIUS, m.y + CIRCLE_RADIUS, m.x
				+ CIRCLE_RADIUS, m.y - CIRCLE_RADIUS);
		g.drawLine(m.x - CIRCLE_RADIUS, m.y - CIRCLE_RADIUS, m.x
				+ CIRCLE_RADIUS, m.y + CIRCLE_RADIUS);
	}
}
