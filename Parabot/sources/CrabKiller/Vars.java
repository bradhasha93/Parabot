package CrabKiller;

import org.rev317.api.methods.Skill;

public class Vars {

	// Paint
	public static final String VERSION = "1.000";
	public static long startTime = 0;

	// State
	public static String Status = "Waiting..";

	// Skills
	public static int[] combatStartXp = new int[6];
	public static int[] combatStartLvls = new int[6];
	private static final Skill[] SKILLS = { Skill.ATTACK, Skill.STRENGTH,
			Skill.RANGE, Skill.HITPOINTS, Skill.MAGIC, Skill.DEFENSE };
	public static int xpGained = 0;

	/**
	 * Sets up starting combat lvls & experience.
	 */
	public static void setupCombatStart() {
		for (int i = 0; i < SKILLS.length; i++) {
			combatStartXp[i] = SKILLS[i].getExperience();
			combatStartLvls[i] = SKILLS[i].getRealLevel();
		}
	}

	/**
	 * Returns experience gained
	 * @return Experience gained for all combat skills
	 */
	public static int getXpGained() {
		int total = 0;
		for (int i = 0; i < SKILLS.length; i++) {
			total += SKILLS[i].getExperience() - combatStartXp[i];
		}
		return total;
	}

}
