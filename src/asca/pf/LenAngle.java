package asca.pf;

/**
 *
 */
public class LenAngle {

	private double length;
	private double angle;
	private boolean wall = false;

	/**
	 * コンストラクタ
	 * @param length
	 * @param angle
	 * @param wall
	 */
	public LenAngle(double length, double angle, boolean wall) {
		this.length = length;
		this.angle = angle;
		this.wall = wall;
	}

	/**
	 *
	 * @return
	 */
	public double getLength() {
		return length;
	}

	/**
	 *
	 * @return
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 *
	 * @return
	 */
	public boolean isWall() {
		return wall;
	}

	/**
	 *
	 * @param length
	 */
	public void setLength(double length) {
		this.length = length;
	}

	/**
	 *
	 * @param angle
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}

	/**
	 *
	 * @param wall
	 */
	public void setWall(boolean wall) {
		this.wall = wall;
	}
}
