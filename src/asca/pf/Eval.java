package asca.pf;

import asca.ConsoleFrame;
import asca.Setting;
import asca.pf.csv.CSVEval;

/**
 *
 */
public class Eval {
	private Setting setting = Setting.getInstance();
	private double worldsizex, worldsizey;
	private int n;
	private double average;
	private double[] est = new double[2];
	private CSVEval csv = new CSVEval(setting.getOutputPath() + "eval.csv");

	/**
	 * コンストラクタ
	 * @param worldsizex
	 * @param worldsizey
	 * @param n
	 */
	public Eval(double worldsizex, double worldsizey, int n) {
		this.worldsizex = worldsizex;
		this.worldsizey = worldsizey;
		this.n = n;
	}

	/**
	 * 評価
	 * @param result 粒子
	 * @param myrobot 実際のロボット
	 */
	public void evaluate(Particle[] result, Robot myrobot) {
		double sum = 0;
		double dx, dy;

		//推定位置（0:x座標 1:y座標）
		double[] estsum=new double[2];

		est[0] = 0.0;
		est[1] = 0.0;


		for(int i = 0; i < n; i++) {
			// eval.csvの推定位置が変わらない原因
			estsum[0] += result[i].getDatax();
			estsum[1] += result[i].getDatay();

			dx = (result[i].getX() - myrobot.getX() + (worldsizex / 2)) % worldsizex - (worldsizex / 2);
			dy = (result[i].getY() - myrobot.getY() + (worldsizey / 2)) % worldsizey - (worldsizey / 2);
			double err = Math.sqrt(dx * dx + dy * dy);
			sum += err;
		}

		est[0] = estsum[0] / n;
		est[1] = estsum[1] / n;


		average = sum / n;

		csv.evalOut(myrobot, est);

		consoleOut();
	}

	/**
	 * 推定位置を返す
	 * @return 推定位置
	 */
	public double[] getEst() {
		return est;
	}

	/**
	 * コンソール上に値を表示する
	 */
	public void consoleOut() {
		ConsoleFrame.getInstance().println("粒子と実際の位置の距離の平均：" + average);
	}
}
