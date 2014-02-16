package BrJailMiner;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Random;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.LoopTask;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.rev317.api.methods.Calculations;
import org.rev317.api.methods.Camera;
import org.rev317.api.methods.Inventory;
import org.rev317.api.methods.Npcs;
import org.rev317.api.methods.Players;
import org.rev317.api.methods.SceneObjects;
import org.rev317.api.wrappers.interactive.Npc;
import org.rev317.api.wrappers.scene.SceneObject;

import utils.Methods;
import BrAutoFighter.Vars;

@ScriptManifest(author = "Bradsta", category = Category.MINING, description = "Mines rocks to get out of jail!", name = "BrJailMiner", servers = { "PkHonor" }, version = 1.000)
public class BrJailMiner extends Script implements LoopTask, Paintable {

	private final int[] ROCK_IDS = { 2093, 2092 };

	// Script states
	private enum State {
		MINE, DEPOSIT, WAIT;
	}

	/**
	 * Return state of script based on conditions
	 * 
	 * @return State of script
	 */
	private State getState() {

		if (Inventory.isFull()) {
			return State.DEPOSIT;
		} else if (Players.getLocal().getAnimation() != 625) {
			return State.MINE;
		}
		return State.WAIT;
	}

	@Override
	public int loop() {
		switch (getState()) {
		case DEPOSIT:
			deposit();
			break;
		case MINE:
			mine();
			break;
		case WAIT:
			break;
		default:
			break;
		}
		return 250;
	}

	/**
	 * Deposits ores to banker
	 */
	private void deposit() {
		final Npc[] jailer = Npcs.getNearest(201);
		if (jailer.length > 0) {
			if (Calculations.distanceTo(jailer[0].getLocation()) > 4) {
				final Point point = Calculations.tileToMinimap(
						jailer[0].getLocation(), true);
				if (point != null)
					Mouse.getInstance().click(point, true);
			}
			if (!jailer[0].isOnScreen())
				Camera.turnTo(jailer[0]);
			if (jailer[0].interact("Talk-to")) {
				sleep(new SleepCondition() {
					@Override
					public boolean isValid() {
						return !Inventory.isFull();
					}
				}, Random.between(2500, 3000));
			}
		}
	}

	/**
	 * Mines rocks.
	 */
	private void mine() {
		final SceneObject[] rock = SceneObjects.getNearest(ROCK_IDS);
		if (rock.length > 0) {
			if (Calculations.distanceTo(rock[0].getLocation()) > 5) {
				final Point point = Calculations.tileToMinimap(
						rock[0].getLocation(), true);
				if (point != null)
					Mouse.getInstance().click(point, true);
			}
			if (!rock[0].isOnScreen(true))
				Camera.turnTo(rock[0]);
			if (rock[0].interact("Mine")) {
				sleep(new SleepCondition() {
					@Override
					public boolean isValid() {
						return Players.getLocal().getAnimation() == 625;
					}
				}, Random.between(2500, 3000));
			}
		}
	}
	
	// Paint vars
	private final int CIRCLE_RADIUS = 15;
	private final int CIRCLE_DIAMETER = CIRCLE_RADIUS * 2;
	private final Color COLOR = Color.RED;

	@Override
	public void paint(Graphics g1) {
		final Point m = Mouse.getInstance().getPoint();
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(COLOR);
		g.drawOval(m.x - CIRCLE_RADIUS / 3, m.y - CIRCLE_RADIUS / 3,
				CIRCLE_DIAMETER / 3, CIRCLE_DIAMETER / 3);
		g.drawLine(m.x - CIRCLE_RADIUS, m.y + CIRCLE_RADIUS, m.x + CIRCLE_RADIUS,
				m.y - CIRCLE_RADIUS);
		g.drawLine(m.x - CIRCLE_RADIUS, m.y - CIRCLE_RADIUS, m.x + CIRCLE_RADIUS,
				m.y + CIRCLE_RADIUS);
	}

}
