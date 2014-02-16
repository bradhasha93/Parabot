package BrAlchemy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;

import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Random;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.LoopTask;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.api.methods.BotMouse;
import org.rev317.api.methods.Magic;
import org.rev317.api.methods.Players;
import org.rev317.api.methods.Magic.StandardMagic377;
import org.rev317.api.wrappers.hud.Item;

import randoms.MysteriousOldMan;
import randoms.SandwichLady;
import utils.Methods;
import utils.Teleports;

@ScriptManifest(author = "Bradsta", category = Category.MAGIC, description = "Alches items!", name = "BrAlchemy", servers = { "PkHonor" }, version = 1.000)
public class BrAlchemy extends Script implements LoopTask, Paintable {

	private final ArrayList<Strategy> tasks = new ArrayList<>();

	@Override
	public boolean onExecute() {
		tasks.add(new SandwichLady());
		tasks.add(new MysteriousOldMan());
		provide(tasks);
		return true;
	}

	@Override
	public int loop() {
		if (tasks.get(0).activate() || tasks.get(1).activate()) {
			sleep(Random.between(1000, 1500));
		} else if (Teleports.openMagicTab()) {
			final Item item = Methods.getItem(557);
			if (item != null) {
				Magic.castSpell(StandardMagic377.CHARGE_WATER_ORB, item);
				if (Players.getLocal().getAnimation() != -1)
					sleep(Random.between(700, 850));
			}
		}
		return 200;
	}

	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Methods.plusMouse(g, Color.RED, new Point(BotMouse.getMouseX(),
				BotMouse.getMouseY()));
	}

}
