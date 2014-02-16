package org.brad.enums;

public enum Bones {

	BONES(526, 100),
	BIG_BONES(532, 600),
	DRAGON_BONES(536, 4000),
	FROST_DRAGON_BONES(16570, 20000);

	private final int id;
	private final int xp;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param xp
	 */
	private Bones(final int id, final int xp) {
		this.id = id;
		this.xp = xp;
	}

	/**
	 * Returns formatted enum name.
	 * @return Formatted name
	 */
	public final String getName() {
		final String name = this.name();
		return name.substring(0, 1)
				+ ""
				+ name.substring(1, name.length()).toLowerCase()
						.replaceAll("_", " ");
	}

	/**
	 * Returns id of bone
	 * 
	 * @return ID of bone
	 */
	public final int getId() {
		return this.id;
	}

	/**
	 * Returns xp when used on altar
	 * 
	 * @return Xp gained from offering
	 */
	public final int getXp() {
		return this.xp;
	}

}
