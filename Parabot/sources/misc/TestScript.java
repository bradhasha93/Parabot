package misc;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.parabot.core.ui.components.LogArea;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Random;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.api.methods.Interfaces;
import org.rev317.api.wrappers.hud.Interface;

import utils.Teleports;

@ScriptManifest(author = "Bradsta", category = Category.MAGIC, description = "Alches items", name = "SimpleAlcher", servers = { "PkHonor" }, version = 1.000)
public class TestScript extends Script implements Paintable {

	private final ArrayList<Strategy> tasks = new ArrayList<>();
	Interface open1 = null;
	boolean found = false;

	@Override
	public boolean onExecute() {
		// Add new main
		tasks.add(new Main());
		// Submit tasks for execution
		provide(tasks);
		return true;
	}

	@Override
	public void onFinish() {
		LogArea.log("Thank you for Simple Alcher by Bradsta!");
	}

	private class Main implements Strategy {

		@Override
		public boolean activate() {
			if (Interfaces.get(3209).getChild(0).isVisible()) {
				LogArea.log("Is visible");
			}
			sleep(500);
			return false;
		}

		@Override
		public void execute() {	
		/*	try {
				LogArea.log("Total length: "+Interfaces.getParentInterfaces().length);
				for (int i = 12449; i < 18000; i++) {
					if (found)
						break;
					open1 = Interfaces.get(i);
					if (open1 != null) {
						LogArea.log("" + open1.getId());
					}
					sleep(10);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		}
	}

	@Override
	public void paint(Graphics g) {
		final Rectangle tele = Teleports.CombatTeleport.RELLEKA.getArea();
		try {
			g.drawRect(tele.x, tele.y, tele.width, tele.height);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
