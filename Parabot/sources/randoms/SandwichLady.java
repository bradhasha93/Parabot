package randoms;

import org.parabot.core.ui.components.LogArea;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.api.methods.Game;
import org.rev317.api.methods.Npcs;
import org.rev317.api.wrappers.interactive.Npc;

public class SandwichLady implements Strategy {

	@Override
	public boolean activate() {
		final Npc[] SANDWICH_LADY = Npcs.getNearest("Sandwich Lady");
		return SANDWICH_LADY.length > 0;
	}

	@Override
	public void execute() {
		final Npc[] SANDWICH_LADY = Npcs.getNearest("Sandwich Lady");
		if (SANDWICH_LADY.length > 0) {
			final String text = SANDWICH_LADY[0].getDisplayedText();
			if (text != null) {
				if (text.contains(Game.getUsername())) {
					if (SANDWICH_LADY[0].interact("Talk-to")) {
						LogArea.log("Solved Sandwich Lady random!");
						Time.sleep(2500,3000);
					}
				}
			}
		}
	}

}
