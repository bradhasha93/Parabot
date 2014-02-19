package org.brad.enums;

public enum Potions {

	ATTACK(1, 249, 221, 91, 2428, 400),
	ANTI_POISON(5, 251, 235, 93, 2446, 650),
	STRENGTH(12, 253, 225, 95, 113, 800),
	RESTORE(22, 255, 223, 97, 2430, 1000),
	DEFENSE(30, 257, 239, 99, 2432, 1300),
	AGILITY(34, 2998, 2152, 3002, 3032, 1400),
	PRAYER(38, 257, 231, 99, 2434, 1500),
	SUPER_ATTACK(45, 259, 221, 101, 2436, 1750),
	SUPER_ANTI_POISON(48, 259, 235, 101, 2448, 2200),
	SUPER_ENERGY(52, 261, 2970, 103, 3016, 5500),
	SUPER_STRENGTH(55, 263, 225, 105, 2440, 7200),
	SUPER_RESTORE(63, 3000, 223, 3004, 3024, 10000),
	SUPER_DEFENSE(66, 265, 239, 107, 2442, 11500),
	ANTI_FIRE(69, 2481, 241, 2483, 2452, 13500),
	RANGING(72, 267, 245, 109, 2444, 15000),
	MAGIC(76, 2481, 3138, 2483, 3040, 16000),
	SARADOMIN_BREW(81, 2998, 5075, 3002, 6685, 18000),
	EXTREME_ATTACK(91, 0, 261, 2436, 15308, 19000),
	EXTREME_STRENGTH(92, 0, 267, 2440, 15312, 20000),
	EXTREME_DEFENSE(93, 0, 2481, 2442, 15316, 21000),
	EXTREME_MAGIC(94, 0, 9594, 3040, 15320, 22000),
	EXTREME_RANGING(95, 0, 8302, 2444, 15324, 23000),
	SUPER_PRAYER(97, 0, 6810, 2434, 15328, 25000);

	private final int reqLvl;
	private final int herbId;
	private final int secondaryId;
	private final int unfinishedId;
	private final int finishedId;
	private final int xp;

	/**
	 * Enum constructor
	 * 
	 * @param reqLvl
	 * @param herbId
	 * @param secondaryId
	 * @param unfinishedId
	 * @param finishedId
	 * @param xp
	 */
	private Potions(final int reqLvl, final int herbId, final int secondaryId,
			final int unfinishedId, final int finishedId, final int xp) {
		this.reqLvl = reqLvl;
		this.herbId = herbId;
		this.secondaryId = secondaryId;
		this.unfinishedId = unfinishedId;
		this.finishedId = finishedId;
		this.xp = xp;
	}

	/**
	 * Returns formatted name for potion + lvl req
	 * 
	 * @return Formatted name
	 */
	public final String getName(boolean includeLvl) {
		final String name = name();
		return includeLvl ? "Lvl "
				+ this.reqLvl
				+ ": "
				+ name.substring(0, 1)
				+ ""
				+ name.substring(1, name.length()).toLowerCase()
						.replaceAll("_", " ") : name.substring(0, 1)
				+ ""
				+ name.substring(1, name.length()).toLowerCase()
						.replaceAll("_", " ");
	}

	/**
	 * Returns required lvl to make potion
	 * 
	 * @return Required lvl
	 */
	public final int getReqLvl() {
		return this.reqLvl;
	}

	/**
	 * Returns herb id
	 * 
	 * @return Herb id
	 */
	public final int getHerbId() {
		return this.herbId;
	}

	/**
	 * Returns secondary id
	 * 
	 * @return secondary id
	 */
	public final int getSecondaryId() {
		return this.secondaryId;
	}

	/**
	 * Returns unfinished product id.
	 * 
	 * @return Unfinished product id.
	 */
	public final int getUnfinishedId() {
		return this.unfinishedId;
	}

	/**
	 * Returns finished product id.
	 * 
	 * @return Finished product id
	 */
	public final int getFinishedId() {
		return this.finishedId;
	}

	/**
	 * Returns completed potion xp
	 * 
	 * @return Xp of completed potion
	 */
	public final int getXp() {
		return this.xp;
	}

}
