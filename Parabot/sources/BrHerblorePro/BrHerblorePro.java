package BrHerblorePro;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;

import org.brad.enums.Potions;
import org.brad.randoms.MysteriousOldMan;
import org.brad.randoms.SandwichLady;
import org.brad.utils.BankManager;
import org.brad.utils.ItemManager;
import org.brad.utils.Methods;
import org.brad.utils.Teleports;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Random;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.LoopTask;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.api.methods.Bank;
import org.rev317.api.methods.BotMouse;
import org.rev317.api.methods.Inventory;
import org.rev317.api.methods.Skill;
import org.rev317.api.wrappers.hud.Item;

import com.sun.java_cup.internal.runtime.virtual_parse_stack;

import sun.java2d.loops.DrawGlyphListAA.General;

@ScriptManifest(author = "Bradsta", category = Category.HERBLORE, description = "Trains the herblore skill!", name = "BrHerblorePro", servers = { "PkHonor" }, version = 1.000)
public class BrHerblorePro extends Script implements LoopTask, Paintable {

	// Strategies for randoms
	private final ArrayList<Strategy> tasks = new ArrayList<>();

	// Script info
	public static final String VERSION = "1.000";

	// User set vars
	public static Potions potion = Potions.ATTACK;
	public static boolean fullProcess = false;
	public static boolean makeUnfinished = false;
	public static boolean makeFinished = false;
	public static boolean startScript = false;

	// Start stats
	private String Status = "Waiting for setup..";
	private int startLvl = 0;
	private int startXp = 0;
	private long startTime = 0;
	private final Skill skill = Skill.HERBLORE;

	// Misc ids
	private final int VIAL_OF_WATER = 227;

	@Override
	public boolean onExecute() {

		UI.main(null);

		for (int i = 0; i < 31 && !startScript; i++)
			sleep(500);

		// Starting stats
		startTime = System.currentTimeMillis();
		startLvl = skill.getRealLevel();
		startXp = skill.getExperience();

		// Add randoms
		tasks.add(new SandwichLady());
		tasks.add(new MysteriousOldMan());
		return true;
	}

	private enum State {
		MAKE_FINISHED, MAKE_UNFINISHED, BANK, WAIT;
	}

	@Override
	public int loop() {
		switch (getState()) {
		case BANK:
			bank();
			break;
		case MAKE_FINISHED:
			makeFinished();
			break;
		case MAKE_UNFINISHED:
			makeUnfinished();
			break;
		case WAIT:
			sleep(250);
			break;
		default:
			break;

		}
		return 250;
	}

	/**
	 * Returns script state based on conditions
	 * 
	 * @return Script state
	 */
	private State getState() {
		if (tasks.get(0).activate() || tasks.get(1).activate()) {
			Status = "Random event active..";
			return State.WAIT;
		} else if (hasNeededItems()) {
			if (Bank.isOpen()) {
				Bank.close();
				return State.WAIT;
			}
			if (fullProcess) {
				if (ItemManager.hasAllItems(new int[] { VIAL_OF_WATER,
						potion.getHerbId() }))
					return State.MAKE_UNFINISHED;
				else
					return State.MAKE_FINISHED;
			} else if (makeUnfinished) {
				return State.MAKE_UNFINISHED;
			} else if (makeFinished) {
				return State.MAKE_FINISHED;
			}
		}
		return State.BANK;
	}

	/**
	 * Checks to see if user has required items.
	 * 
	 * @return true if has required items for task.
	 */
	private boolean hasNeededItems() {
		return fullProcess ? (ItemManager.hasAllItems(new int[] {
				VIAL_OF_WATER, potion.getHerbId() }) || ItemManager
				.hasAllItems(new int[] { potion.getUnfinishedId(),
						potion.getSecondaryId() }))
				: makeUnfinished ? (ItemManager.hasAllItems(new int[] {
						VIAL_OF_WATER, potion.getHerbId() }))
						: makeFinished ? (ItemManager.hasAllItems(new int[] {
								potion.getUnfinishedId(),
								potion.getSecondaryId() })) : false;
	}

	/**
	 * Makes unfinished potions
	 */
	private void makeUnfinished() {
		final Item vial = ItemManager.getItem(VIAL_OF_WATER);
		final Item herb = ItemManager.getItem(potion.getHerbId());
		Status = "Attempting to make unfinished pots..";
		if (vial != null && herb != null) {
			if (Teleports.isCombatTeleportsOpen()) {
				if (Teleports.CombatTeleport.NEVER_MIND.teleport()) {
					waitTask(new int[] { VIAL_OF_WATER, potion.getHerbId() });
				}
			} else {
				final int random = Random.between(0, 1);
				if (random == 0) {
					vial.interact("Use");
					sleep(Random.between(300, 650));
					herb.interact("Use");
				} else {
					vial.interact("Use");
					sleep(Random.between(300, 650));
					herb.interact("Use");
				}
				waitForMenu();
			}
		}
	}

	/**
	 * Makes finished potions
	 */
	private void makeFinished() {
		final Item unfinished = ItemManager.getItem(potion.getUnfinishedId());
		final Item secondary = ItemManager.getItem(potion.getSecondaryId());
		Status = "Attempting to make finished pots..";
		if (unfinished != null && secondary != null) {
			if (Teleports.isCombatTeleportsOpen()) {
				if (Teleports.CombatTeleport.NEVER_MIND.teleport()) {
					waitTask(new int[] { potion.getUnfinishedId(),
							potion.getSecondaryId() });
				}
			} else {
				final int random = Random.between(0, 1);
				if (random == 0) {
					unfinished.interact("Use");
					sleep(Random.between(300, 650));
					secondary.interact("Use");
				} else {
					secondary.interact("Use");
					sleep(Random.between(300, 650));
					unfinished.interact("Use");
				}
				waitForMenu();
			}
		}
	}

	/**
	 * Attempts to bank
	 */
	private void bank() {
		int count = 0;
		if (BankManager.isBankOpen()) {
			Status = "Attempting to bank..";
			// Full process
			if (fullProcess) {
				if (Inventory.getCount(true, potion.getUnfinishedId()) == 14) {
					count = Inventory.getCount(true, potion.getSecondaryId());
					if (BankManager.withdraw(potion.getSecondaryId(), 0))
						waitForItem(potion.getSecondaryId(), count);
				} else if (Inventory.getCount(true, potion.getFinishedId()) == 14) {
					count = Inventory.getCount(true, VIAL_OF_WATER);
					if (BankManager.withdraw(VIAL_OF_WATER, 0))
						waitForItem(VIAL_OF_WATER, count);
					if (Inventory.getCount(true, VIAL_OF_WATER) == 14) {
						BankManager.depositAll(ItemManager.getItem(potion.getFinishedId()));
						count = Inventory.getCount(true, potion.getHerbId());
						if (BankManager.withdraw(potion.getHerbId(), 0))
							waitForItem(potion.getHerbId(), count);
					}
				} else {
					Bank.depositAll();
					count = Inventory.getCount(true, VIAL_OF_WATER);
					if (BankManager.withdraw(VIAL_OF_WATER, 14)) {
						waitForItem(VIAL_OF_WATER, count);
						count = Inventory.getCount(true, potion.getHerbId());
						if (Inventory.getCount(true, VIAL_OF_WATER) == 14)
							if (BankManager.withdraw(potion.getHerbId(), 0))
								waitForItem(potion.getHerbId(), count);
					}
				}
				// Finished
			} else if (makeFinished) {
				if (Inventory.getCount(true, potion.getUnfinishedId()) == 14) {
					count = Inventory.getCount(true, potion.getSecondaryId());
					if (BankManager.withdraw(potion.getSecondaryId(), 0))
						waitForItem(potion.getSecondaryId(), count);
				} else {
					Bank.depositAll();
					count = Inventory.getCount(true, potion.getUnfinishedId());
					if (BankManager.withdraw(potion.getUnfinishedId(), 14)) {
						waitForItem(potion.getUnfinishedId(), count);
						count = Inventory.getCount(true,
								potion.getSecondaryId());
						if (Inventory.getCount(true, potion.getUnfinishedId()) == 14)
							if (BankManager
									.withdraw(potion.getSecondaryId(), 0))
								waitForItem(potion.getSecondaryId(), count);
					}
				}
				// Unfinished
			} else if (makeUnfinished) {
				if (Inventory.getCount(true, VIAL_OF_WATER) == 14) {
					count = Inventory.getCount(true, potion.getHerbId());
					if (BankManager.withdraw(potion.getHerbId(), 0))
						waitForItem(potion.getHerbId(), count);
				} else {
					Bank.depositAll();
					count = Inventory.getCount(true, VIAL_OF_WATER);
					if (BankManager.withdraw(VIAL_OF_WATER, 14)) {
						waitForItem(VIAL_OF_WATER, count);
						count = Inventory.getCount(true, potion.getHerbId());
						if (Inventory.getCount(true, VIAL_OF_WATER) == 14)
							if (BankManager.withdraw(potion.getHerbId(), 0))
								waitForItem(potion.getHerbId(), count);
					}
				}
			}
			// Open bank
		} else if (BankManager.openBank(true)) {
			sleep(new SleepCondition() {

				@Override
				public boolean isValid() {
					return BankManager.isBankOpen();
				}
			}, Random.between(2000, 3000));
		}
	}

	/**
	 * Waits if/while on task
	 * 
	 * @param ids
	 */
	private void waitTask(final int... ids) {
		for (int i = 0; i < 20 && ItemManager.hasAllItems(ids); i++) {
			Status = "Waiting while on task..";
			final int count = Inventory.getCount(ids[0]);
			sleep(Random.between(2000, 3000));
			if (Inventory.getCount(ids[0]) == count)
				break;
		}
	}

	/**
	 * Waits for make menu.
	 */
	private void waitForMenu() {
		sleep(new SleepCondition() {

			@Override
			public boolean isValid() {
				return Teleports.isCombatTeleportsOpen();
			}
		}, Random.between(2000, 2300));
	}

	/**
	 * Waits for inventory to contain an item w/ timeout
	 * 
	 * @param id
	 * @param count
	 */
	private void waitForItem(final int id, final int count) {
		sleep(new SleepCondition() {

			@Override
			public boolean isValid() {
				return Inventory.getCount(true, id) > count;
			}
		}, Random.between(2500, 3000));
		Time.sleep(560, 850);
	}

	// Paint vars

	private final Color COLOR_1 = new Color(11, 1, 1, 225);
	private final Color COLOR_2 = new Color(0, 0, 0, 114);
	private final Color COLOR_3 = new Color(249, 245, 245);
	private final Color COLOR_4 = new Color(255, 241, 241);
	private final Color COLOR_5 = new Color(13, 231, 30);
	private final Color COLOR_6 = new Color(255, 255, 255);

	private final BasicStroke STROKE_1 = new BasicStroke(1);

	private final Font FONT_1 = new Font("Arial", 1, 12);
	private final Font FONT_2 = new Font("Arial", 1, 10);

	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Methods.plusMouse(g, Color.RED, new Point(BotMouse.getMouseX(),
				BotMouse.getMouseY()));
		g.setColor(COLOR_1);
		g.fillRect(548, 337, 188, 124);
		g.setColor(COLOR_2);
		g.setStroke(STROKE_1);
		g.drawRect(548, 337, 188, 124);
		g.setColor(COLOR_3);
		g.setColor(COLOR_4);
		g.drawLine(548, 354, 737, 354);
		g.setFont(FONT_1);
		g.setColor(COLOR_5);
		g.drawString("BrHerblorePro v" + VERSION + " by Bradsta", 551, 350);
		g.setFont(FONT_2);
		g.setColor(COLOR_6);
		g.drawString("Status: " + Status, 551, 367);
		g.drawString("Runtime: " + Methods.getTimeRunning(startTime), 551, 455);
		g.drawString(
				"Herblore Lvl: " + startLvl + " (+"
						+ (skill.getRealLevel() - startLvl) + ")", 551, 394);
		final int gained = (skill.getExperience() - startXp);
		g.drawString("Herblore Xp/Hr: " + Methods.formatNumber(gained) + " / "
				+ Methods.perHour(gained, startTime), 551, 406);
		g.drawString("Potion Type: "
				+ (potion != null ? potion.getName(false) : ""), 551, 419);
		final int used = gained != 0 && potion != null ? gained
				/ potion.getXp() : 0;
		g.drawString("Potions Made/Hr: " + Methods.formatNumber(used) + " / "
				+ Methods.perHour(used, startTime), 551, 431);
	}

}
