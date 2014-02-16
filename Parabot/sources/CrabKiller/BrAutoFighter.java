package CrabKiller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.parabot.core.ui.components.LogArea;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Random;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.LoopTask;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.rev317.api.events.MessageEvent;
import org.rev317.api.events.listeners.MessageListener;
import org.rev317.api.methods.Bank;
import org.rev317.api.methods.BotMouse;
import org.rev317.api.methods.Calculations;
import org.rev317.api.methods.Inventory;
import org.rev317.api.methods.Magic;
import org.rev317.api.methods.Players;
import org.rev317.api.methods.Skill;
import org.rev317.api.wrappers.scene.GroundItem;
import org.rev317.api.wrappers.scene.Tile;

import utils.BankManager;
import utils.Looting;
import utils.Methods;
import utils.NpcManager;
import utils.Teleports;
import utils.Teleports.CombatTeleport;

@ScriptManifest(author = "Bradsta", category = Category.COMBAT, description = "Fights and loots!", name = "BrAutoFighter", servers = { "PkHonor" }, version = 1.00)
public class BrAutoFighter extends Script implements Paintable, LoopTask,
		MessageListener {

	// Food & pots
	private final int[] foodIds = { 379, 385 };
	private final int[] prayerPots = { 2434, 139, 141, 143 };
	private final int[] overloadPotions = { 14592, 14593, 14594, 14595 };

	private final DateFormat formatter = new SimpleDateFormat("kk:mm:ss");

	private final CombatTeleport CRABS = CombatTeleport.RELLEKA;
	private final Tile HOME = new Tile(3213, 3437);

	// Looting
	private final int[] lootIds = { 6571, 11732, 4151, 556, 1675, 561, 536,
			537, 6129, 6130, 995 };
	private final String[] names = { "Uncut onyx", "Dragon boots",
			"Abyssal whip", "Dragon plateskirt", "Ancient staff",
			"Nature rune", "Dragon bones", "Dragon bones", "Rock-shell plate",
			"Rock-shell legs", "Coins" };

	// Bot states
	private enum State {
		EAT, ATTACK, LOOT, DRINK_PRAYER_POT, BANK, TELEPORT, WAIT;
	}

	@Override
	public boolean onExecute() {
		// Setup loot hashmap
		Looting.setupLootMap(names, lootIds);

		// Submit tasks for execution
		Vars.setupCombatStart();
		Vars.startTime = System.currentTimeMillis();
		return true;
	}

	@Override
	public void onFinish() {
		LogArea.log("Thank you for using BrAutoFighter " + Vars.VERSION
				+ " by Bradsta!");
	}

	/**
	 * Returns current bot state based on conditions
	 * 
	 * @return Bot state
	 */
	private State getState() {
		if (Methods.hasItem(foodIds)) {
			if (BankManager.isBankOpen()) {
				Bank.close();
				return State.WAIT;
			}
			if (CRABS.isTeleportSuccessful()) {
				if (Skill.HITPOINTS.getLevel() < 15) {
					return State.EAT;
				} else if (Skill.PRAYER.getLevel() < 25
						&& Methods.hasItem(prayerPots)) {
					return State.DRINK_PRAYER_POT;
				} else if (Methods.inCombat()) {
					return State.WAIT;
				} else if (Looting.getLoot() != null) {
					return State.LOOT;
				} else {
					return State.ATTACK;
				}
			} else {
				return State.TELEPORT;
			}
		} else {
			return State.BANK;
		}
	}

	/**
	 * Attempts to eat food
	 */
	private void eat() {
		if (Methods.hasItem(foodIds))
			Methods.interactWithItem(Methods.getItem(foodIds), "Eat");
	}

	/**
	 * Attempts to attack target npcs
	 */
	private void attack() {
		if (NpcManager.attackNpc(NpcManager.getNpc(new int[] { 1265, 1267 }))) {
			sleep(new SleepCondition() {

				@Override
				public boolean isValid() {
					return Players.getLocal().isInCombat();
				}
			}, Random.between(3000, 3500));
		}
	}

	/**
	 * Attempts to loot items
	 */
	private void loot() {
		final GroundItem loot = Looting.getLoot();
		final int id = loot.getId();
		final int start = Inventory.getCount(true, id);

		if (Inventory.isFull() && Methods.hasItem(foodIds))
			Methods.interactWithItem(Methods.getItem(foodIds), "Eat");

		if (Looting.takeLoot(loot)) {

			sleep(new SleepCondition() {

				@Override
				public boolean isValid() {
					return Inventory.getCount(true, id) > start;
				}
			}, Random.between(2500, 3000));
		}
	}

	/**
	 * Attempts to teleport
	 */
	private void teleport() {
		if (Teleports.isCombatTeleportsOpen()) {
			if (CRABS.teleport()) {
				sleep(new SleepCondition() {

					@Override
					public boolean isValid() {
						return CRABS.isTeleportSuccessful();
					}
				}, Random.between(2000, 3000));
			}
		} else if (Teleports.clickCombatTeleport()) {
			sleep(new SleepCondition() {

				@Override
				public boolean isValid() {
					return Teleports.isCombatTeleportsOpen();
				}
			}, Random.between(2000, 3000));
		}
	}

	/**
	 * Attempts to bank
	 */
	private void bank() {
		if (Calculations.distanceTo(HOME) > 15) {
			if (Teleports.openMagicTab()) {
				Magic.clickSpell(Magic.StandardMagic377.WIND_STRIKE);
				sleep(Random.between(1500, 2500));
			}
		} else if (BankManager.isBankOpen()) {
			Bank.depositAllExcept(foodIds);
			Bank.withdraw(foodIds[0], 28);
		} else if (BankManager.openBank(true)) {
			sleep(new SleepCondition() {

				@Override
				public boolean isValid() {
					return BankManager.isBankOpen();
				}
			}, Random.between(3000, 3500));
		}
	}

	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Methods.plusMouse(g, Color.RED, new Point(BotMouse.getMouseX(),
				BotMouse.getMouseY()));
		Vars.xpGained = Vars.getXpGained();
		g.drawString("Combat Xp/Hr: " + Methods.formatNumber(Vars.xpGained)
				+ " / " + Methods.perHour(Vars.xpGained, Vars.startTime), 276,
				356);
		g.drawString("Time Ran: " + Methods.getTimeRunning(Vars.startTime), 276, 370);
	}

	@Override
	public int loop() {
		try {
			switch (getState()) {
			case ATTACK:
				attack();
				break;
			case EAT:
				eat();
				break;
			case LOOT:
				loot();
				break;
			case DRINK_PRAYER_POT:
				Methods.interactWithItem(Methods.getItem(prayerPots), "Drink");
				sleep(Random.between(700, 800));
			case WAIT:
				if (Methods.hasItem(229))
					Methods.interactWithItem(Methods.getItem(229), "Drop");
				break;
			case TELEPORT:
				teleport();
				break;
			case BANK:
				bank();
				break;
			default:
				break;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 350;
	}

	@Override
	public void messageReceived(MessageEvent arg0) {
		if (arg0.getMessage().contains("normal again.")) {
			if (Methods.hasItem(overloadPotions))
				Methods.interactWithItem(Methods.getItem(overloadPotions),
						"Drink");
		}

	}

}
