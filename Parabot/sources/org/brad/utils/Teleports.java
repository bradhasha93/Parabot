package org.brad.utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import org.parabot.core.ui.components.LogArea;
import org.parabot.environment.api.utils.Random;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.input.Mouse;
import org.rev317.api.methods.BotMouse;
import org.rev317.api.methods.Calculations;
import org.rev317.api.methods.Interfaces;
import org.rev317.api.methods.Magic;
import org.rev317.api.wrappers.hud.Tab;
import org.rev317.api.wrappers.scene.Tile;

public class Teleports {

	public enum CombatTeleport {
		LUMBRIDGE(new Rectangle(191, 376, 137, 8), new Tile(3239, 3281)),
		RELLEKA(new Rectangle(199, 394, 124, 8), new Tile(2713, 3711)),
		WARRIORS_GUILD(new Rectangle(199, 410, 137, 8), new Tile(2866, 3570)),
		APE_ATOLL(new Rectangle(199, 426, 131, 8), new Tile(2797, 2798)),
		NEVER_MIND(new Rectangle(199, 441, 131, 8), null);

		private final Rectangle AREA;
		private final Tile SUCCESS;

		/**
		 * Enum constructor
		 * 
		 * @param AREA
		 * @param SUCCESS
		 */
		private CombatTeleport(final Rectangle AREA, final Tile SUCCESS) {
			this.AREA = AREA;
			this.SUCCESS = SUCCESS;
		}

		/**
		 * Returns area of click space.
		 * 
		 * @return Area of click space
		 */
		public final Rectangle getArea() {
			return this.AREA;
		}

		/**
		 * Returns successful tile for teleport
		 * 
		 * @return Tile of teleport
		 */
		public final Tile getSuccessTile() {
			return this.SUCCESS;
		}

		/**
		 * Attempts to teleport to the location
		 * 
		 * @return
		 */
		public final boolean teleport() {
			Mouse mouse = Mouse.getInstance();
			final Point botMouse = new Point(BotMouse.getMouseX(),
					BotMouse.getMouseY());
			if (this.AREA.contains(botMouse)) {
				mouse.click(botMouse, true);
				return true;
			} else {
				mouse.moveMouse(
						this.AREA.x + Random.between(2, this.AREA.width - 2),
						this.AREA.y + Random.between(2, this.AREA.height - 2));
			}
			return false;
		}

		/**
		 * Compares player location to success tile
		 * 
		 * @return Returns true if player is < 15 tiles from success tile.
		 */
		public final boolean isTeleportSuccessful() {
			return Calculations.distanceTo(SUCCESS) < 21;
		}
	}

	public static boolean clickCombatTeleport() {
		if (openMagicTab()) {
			Magic.clickSpell(Magic.StandardMagic377.TELEKINETIC_GRAB);
			return true;
		}
		return false;
	}

	public static boolean openMagicTab() {
		if (Tab.MAGIC.isOpen()) {
			return true;
		}
		Keyboard.getInstance().pressKey(KeyEvent.VK_F4);
		Keyboard.getInstance().releaseKey(KeyEvent.VK_F4);
		return false;
	}

	/**
	 * Checks to see if combat teleports is visible
	 * 
	 * @return True if is visible
	 */
	public static boolean isCombatTeleportsOpen() {
		return Interfaces.get(2492).getChild(0).isVisible();
	}

}
