package asca.pf.map;

/**
 *
 */
public class Map {
	/**
	 * 目印があるかないか
	 */
	private boolean mark;

	/**
	 * x座標
	 */
	private double x;

	/**
	 * y座標
	 */
	private double y;

	/**
	 * コンストラクタ
	 * @param x
	 * @param y
	 * @param mark
	 */
	public Map(int x, int y, boolean mark) {
		this.mark = mark;
		this.x = x;
		this.y = y;
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param wall
	 */
	public void setter(int x, int y, boolean wall) {
		this.setx(x);
		this.sety(y);
		this.setwall(wall);
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param wall
	 */
	public void setter(double x, double y, boolean wall) {
		this.setx(x);
		this.sety(y);
		this.setwall(wall);
	}

	/**
	 *
	 * @return
	 */
	public double getx() {
		return this.x;
	}

	/**
	 *
	 * @return
	 */
	public double gety() {
		return this.y;
	}

	/**
	 *
	 * @return
	 */
	public boolean getwall() {
		return this.mark;
	}

	/**
	 *
	 * @param x
	 */
	public void setx(int x) {
		this.x = x;
	}

	/**
	 *
	 * @param y
	 */
	public void sety(int y) {
		this.y = y;
	}

	/**
	 *
	 * @param x
	 */
	public void setx(double x) {
		this.x = x;
	}

	/**
	 *
	 * @param y
	 */
	public void sety(double y) {
		this.y = y;
	}

	/**
	 *
	 * @param m
	 */
	public void setwall(boolean m) {
		this.mark = m;
	}
}
