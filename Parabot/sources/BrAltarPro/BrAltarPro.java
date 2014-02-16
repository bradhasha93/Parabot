package BrAltarPro;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;

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

import randoms.MysteriousOldMan;
import randoms.SandwichLady;
import utils.BankManager;
import utils.Methods;
import utils.Teleports;

@ScriptManifest(author = "Bradsta", category = Category.PRAYER, description = "Uses bones on altar!", name = "BrAltarPro", servers = { "PkHonor" }, version = 1.000)
public class BrAltarPro extends Script implements LoopTask, Paintable {

	private final ArrayList<Strategy> tasks = new ArrayList<>();

	private final Tile BANK_TILE = new Tile(3093,3491);
	private final int DRAGON_BONES = 536;

	private int startLvl = 0;
	private int startXp = 0;
	private long startTime = 0;

	private final Skill skill = Skill.PRAYER;

	@Override
	public boolean onExecute() {
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

	private enum State {
		BANK, USE_ALTAR;
	}

	private State getState() {

		if (Methods.hasItem(DRAGON_BONES)) {
			if (BankManager.isBankOpen()) {
				Bank.close();
			}
			return State.USE_ALTAR;
		} else {
			return State.BANK;
		}
	}

	private void useAltar() {
		final SceneObject altar = Methods.getObject(10638);
		if (altar != null) {
			if (Calculations.distanceTo(altar.getLocation()) > 5) {
				Mouse.getInstance().click(
						Calculations.tileToMinimap(altar.getLocation(), true));
			}
			if (!altar.isOnScreen())
				Camera.turnTo(altar);
			if (!Teleports.isCombatTeleportsOpen()) {
				Methods.interactWithItem(Methods.getItem(DRAGON_BONES), "Use");
				if (altar.interact("Use Dragon Bones -> Altar")) {
					sleep(new SleepCondition() {

						@Override
						public boolean isValid() {
							return Teleports.isCombatTeleportsOpen();
						}
					}, Random.between(2500, 3000));
				}
			} else {
				if (Teleports.isCombatTeleportsOpen()) {
					if (Teleports.CombatTeleport.NEVER_MIND.teleport())
						for (int i = 0; i < 20 && Methods.hasItem(DRAGON_BONES); i++) {
							final int count = Inventory.getCount(DRAGON_BONES);
							sleep(Random.between(2000, 3000));
							if (Inventory.getCount(DRAGON_BONES) == count)
								break;
						}

				}
			}
		}
	}

	private void bank() {
		if (BankManager.isBankOpen()) {
			Bank.withdraw(DRAGON_BONES, 29);
		} else if (Calculations.distanceTo(BANK_TILE) > 5) {
			Mouse.getInstance().click(Calculations.tileToMinimap(BANK_TILE, true), true);
		} else if (BankManager.openBank(false)) {
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

	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Methods.plusMouse(g, Color.RED, new Point(BotMouse.getMouseX(),
				BotMouse.getMouseY()));
		g.drawString("Prayer Lvl: " + startLvl + " (+"
				+ (skill.getRealLevel() - startLvl) + ")", 276, 356);
		g.drawString(
				"Prayer Xp/Hr: "
						+ (skill.getExperience() - startXp)
						+ " / "
						+ Methods.perHour((skill.getExperience() - startXp),
								startTime), 276, 370);
		g.drawString("Runtime: "+Methods.getTimeRunning(startTime), 276, 384);
	}

}
