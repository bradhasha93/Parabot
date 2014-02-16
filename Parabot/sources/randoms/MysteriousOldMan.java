package randoms;

import org.parabot.core.ui.components.LogArea;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.api.methods.Game;
import org.rev317.api.methods.Npcs;
import org.rev317.api.wrappers.interactive.Npc;

public class MysteriousOldMan implements Strategy {

	@Override
	public boolean activate() {
		final Npc[] man = Npcs.getNearest("Mysterious Old Man");
		return man.length > 0;
	}

	@Override
	public void execute() {
		final Npc[] man = Npcs.getNearest("Mysterious Old Man");
		if (man.length > 0) {
			final String text = man[0].getDisplayedText();
			if (text != null) {
				if (text.contains(Game.getUsername())) {
					if (man[0].interact("Talk-to")) {
						LogArea.log("Solved Mysterious Old Man random!");
						Time.sleep(2000, 3000);
					}
				}
			}
		}
	}

}
