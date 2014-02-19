package BrAltarPro;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;

import org.brad.enums.Bones;
import org.brad.randoms.MysteriousOldMan;
import org.brad.randoms.SandwichLady;
import org.brad.utils.BankManager;
import org.brad.utils.ItemManager;
import org.brad.utils.Methods;
import org.brad.utils.Teleports;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Random;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.LoopTask;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.api.methods.Bank;
import org.rev317.api.methods.BotMouse;
import org.rev317.api.methods.Calculations;
import org.rev317.api.methods.Camera;
import org.rev317.api.methods.Inventory;
import org.rev317.api.methods.Skill;
import org.rev317.api.wrappers.scene.SceneObject;
import org.rev317.api.wrappers.scene.Tile;

@ScriptManifest(author = "Bradsta", category = Category.PRAYER, description = "Uses bones on altars in Edgeville/Premium Skilling zone!", name = "BrAltarPro", servers = { "PkHonor" }, version = 1.000)
public class BrAltarPro extends Script implements LoopTask, Paintable {

	private final ArrayList<Strategy> tasks = new ArrayList<>();

	// Bank tiles
	private final Tile EDGE_BANK_TILE = new Tile(3093, 3491);

	// UI set vars
	public static Bones bones = null;
	public static boolean useEdge = false;
	public static boolean startScript = false;

	// Starting stats & misc
	private String Status = "Waiting for setup..";
	private int startLvl = 0;
	private int startXp = 0;
	private long startTime = 0;

	private final Skill skill = Skill.PRAYER;

	@Override
	public boolean onExecute() {

		UI.main(null);

		for (int i = 0; i < 31 && !startScript; i++)
			sleep(500);
		// Start statistics
		startTime = System.currentTimeMillis();
		startLvl = skill.getRealLevel();
		startXp = skill.getExperience();

		// Add random handlers
		tasks.add(new SandwichLady());
		tasks.add(new MysteriousOldMan());
		provide(tasks);
		return true;
	}

	// Script states
	private enum State {
		BANK, USE_ALTAR;
	}

	/**
	 * Returns script state based on conditions
	 * 
	 * @return Script state
	 */
	private State getState() {

		if (ItemManager.hasItem(bones.getId())) {

			if (BankManager.isBankOpen()) {
				Bank.close();
			}
			return State.USE_ALTAR;

		} else {
			return State.BANK;
		}
	}

	/**
	 * Attempts to use bones on the altar
	 */
	private void useAltar() {
		final SceneObject altar = Methods.getObject(useEdge ? 10638 : 409);
		if (altar != null) {

			if (Calculations.distanceTo(altar.getLocation()) > 5) {
				Status = "Walking to altar..";
				Mouse.getInstance().click(
						Calculations.tileToMinimap(altar.getLocation(), true));
			}

			if (!altar.isOnScreen())
				Camera.turnTo(altar);

			if (!Teleports.isCombatTeleportsOpen()) {
				ItemManager.interactWithItem(
						ItemManager.getItem(bones.getId()), "Use");
				Status = "Attempting to use altar..";
				if (altar.interact("Use " + bones.getName() + " -> Altar")) {
					sleep(new SleepCondition() {

						@Override
						public boolean isValid() {
							return Teleports.isCombatTeleportsOpen();
						}
					}, Random.between(2500, 3000));
				}

			} else if (Teleports.isCombatTeleportsOpen()) {
				Status = "Attempting to click Offer All..";
				if (Teleports.CombatTeleport.NEVER_MIND.teleport())
					for (int i = 0; i < 20
							&& ItemManager.hasItem(bones.getId()); i++) {
						Status = "Waiting while on task..";
						final int count = Inventory.getCount(bones.getId());
						sleep(Random.between(2000, 3000));
						if (Inventory.getCount(bones.getId()) == count)
							break;
					}
			}
		}
	}

	/**
	 * Attempts to bank
	 */
	private void bank() {
		if (BankManager.isBankOpen()) {
			Status = "Banking..";
			if (!ItemManager.hasItem(bones.getId())) {
				final int count = Inventory.getCount(true, bones.getId());
				if (BankManager.withdraw(bones.getId(), 0))
					sleep(new SleepCondition() {

						@Override
						public boolean isValid() {
							return Inventory.getCount(true, bones.getId()) > count;
						}
					}, Random.between(2500, 3000));
			}

		} else if (Calculations.distanceTo(EDGE_BANK_TILE) > 5 && useEdge) {
			Status = "Walking to bank..";
			Mouse.getInstance().click(
					Calculations.tileToMinimap(EDGE_BANK_TILE, true), true);

		} else if (BankManager.openBank(false)) {
			Status = "Attempting to open bank..";
			sleep(new SleepCondition() {

				@Override
				public boolean isValid() {
					return BankManager.isBankOpen();
				}
			}, Random.between(2500, 3000));
		}
	}

	@Override
	public int loop() {
		if (tasks.get(0).activate() || tasks.get(1).activate()) {
			Status = "Random active.. Waiting..";
			sleep(Random.between(250, 300));
		} else {
			switch (getState()) {
			case BANK:
				bank();
				break;
			case USE_ALTAR:
				useAltar();
				break;
			default:
				break;

			}
		}
		return 250;
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
		g.drawString("BrAltarPro v1.000 by Bradsta", 551, 350);
		g.setFont(FONT_2);
		g.setColor(COLOR_6);
		g.drawString("Status: " + Status, 551, 367);
		g.drawString("Runtime: " + Methods.getTimeRunning(startTime), 551, 455);
		g.drawString("Prayer Lvl: " + startLvl + " (+"
				+ (skill.getRealLevel() - startLvl) + ")", 551, 394);
		final int gained = (skill.getExperience() - startXp);
		g.drawString("Prayer Xp/Hr: " + Methods.formatNumber(gained) + " / "
				+ Methods.perHour(gained, startTime), 551, 406);
		g.drawString("Bone Type: " + (bones != null ? bones.getName() : ""),
				551, 419);
		final int used = gained != 0 && bones != null ? gained / bones.getXp()
				: 0;
		g.drawString("Bones Used/Hr: " + Methods.formatNumber(used) + " / "
				+ Methods.perHour(used, startTime), 551, 431);
	}

}
