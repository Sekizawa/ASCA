package asca.pf;

import asca.pf.map.WorldMap;

/**
 *
 */
public class Singleton {
	private static Singleton instance = new Singleton();

	/**
	 * コンストラクタ
	 */
	private Singleton() {

	}

	/**
	 * インスタンスを返す
	 * @return Singletonクラスのインスタンス
	 */
	public static Singleton getInstance() {
		return instance;
	}

	/**
	 *
	 */
	private int landlen, landp, landm;

	/**
	 *
	 */
	private double lenmax, plus, minus;

	private WorldMap map;

	/**
	 *
	 * @return
	 */
	public WorldMap getMap() {
		return map;
	}

	/**
	 *
	 * @return
	 */
	public int getLandlen() {
		return landlen;
	}

	/**
	 *
	 * @return
	 */
	public int getLandp() {
		return landp;
	}

	/**
	 *
	 * @return
	 */
	public int getLandm() {
		return landm;
	}

	/**
	 *
	 * @return
	 */
	public double getLenmax() {
		return lenmax;
	}

	/**
	 *
	 * @return
	 */
	public double getPlus() {
		return plus;
	}

	/**
	 *
	 * @return
	 */
	public double getMinus() {
		return minus;
	}

	/**
	 *
	 * @param map
	 */
	public void setMap(WorldMap map) {
		this.map = map;
	}

	/**
	 *
	 * @param landlen
	 */
	public void setLandlen(int landlen) {
		this.landlen = landlen;
	}

	/**
	 *
	 * @param landp
	 */
	public void setLandp(int landp) {
		this.landp = landp;
	}

	/**
	 *
	 * @param landm
	 */
	public void setLandm(int landm) {
		this.landm = landm;
	}

	/**
	 *
	 * @param lenmax
	 */
	public void setLenmax(double lenmax) {
		this.lenmax = lenmax;
	}

	/**
	 *
	 * @param plus
	 */
	public void setPlus(double plus) {
		this.plus = plus;
	}

	/**
	 *
	 * @param minus
	 */
	public void setMinus(double minus) {
		this.minus = minus;
	}
}
