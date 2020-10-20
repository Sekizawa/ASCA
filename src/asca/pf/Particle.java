package asca.pf;

import asca.pf.map.WorldMap;

/**
 *
 */
public class Particle extends Robot {
	/**
	 * 重み
	 */
	private double weight;

	/**
	 * 正規化後の重み
	 */
	private double Nweight;

	private double datax;
	private double datay;

	/**
	 * コンストラクタ
	 * @param x
	 * @param y
	 * @param angle
	 * @param map
	 */
	public Particle(double x, double y, double angle, WorldMap map) {
		super(x, y, angle, map);

		this.setdatax(x);
		this.setdatay(y);

		this.setWeight(1.0);
		this.setNweight(0.0);
	}

	/**
	 *
	 * @param w
	 */
	public void setWeight(double w) {
		this.weight = w;
	}

	/**
	 *
	 * @param nw
	 */
	public void setNweight(double nw) {
		this.Nweight = nw;
	}

	/**
	 *
	 * @param x
	 */
	public void setdatax(double x) {
		this.datax = x;
	}

	/**
	 *
	 * @param y
	 */
	public void setdatay(double y) {
		this.datay = y;
	}

	/**
	 *
	 * @return
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 *
	 * @return
	 */
	public double getNweight() {
		return Nweight;
	}

	/**
	 *
	 * @return
	 */
	public double getDatax() {
		return this.datax;
	}

	/**
	 *
	 * @return
	 */
	public double getDatay() {
		return this.datay;
	}
}
