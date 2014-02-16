package org.brad.utils;

import org.parabot.environment.api.utils.Filter;
import org.rev317.api.methods.Camera;
import org.rev317.api.methods.Npcs;
import org.rev317.api.wrappers.interactive.Npc;

public class NpcManager {

	public static Npc getNpc(final int... ids) {
		final Npc[] npcs = Npcs.getNearest(new Filter<Npc>() {

			@Override
			public boolean accept(Npc npc) {
				for (int id : ids) {
					if (npc.getDef().getId() == id && !npc.isInCombat())
						return true;
				}
				return false;
			}
		});
		return npcs.length > 0 ? npcs[0] : null;
	}

	public static boolean attackNpc(final Npc npc) {
		if (npc != null) {
			if (npc.isOnScreen()) {
				final org.rev317.api.wrappers.defs.NpcDef def = npc.getDef();
				final String name = def != null ? def.getName() : null;
				if (name != null) {
					if (npc.interact("Attack " + name))
						return true;
				} else if (npc.interact("Attack"))
					return true;
			} else {
				Camera.turnTo(npc);
			}
		}
		return false;
	}
}
