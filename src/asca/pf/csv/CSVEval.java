package asca.pf.csv;

import asca.pf.Robot;

/**
 *
 */
public class CSVEval extends CSV {

	public int step = 0;

	/**
	 * コンストラクタ<br>
	 * ファイルパスを読み込みcsvファイルを作成する
	 * @param filename ファイルパス
	 */
	public CSVEval(String filename) {
		super(filename);
		this.header();
	}

	/**
	 * ファイルのヘッダを書き込む
	 */
	public void header() {
		post();
		pw.println("ロボットの位置とパーティクルによる推定位置");
		close();
	}

	/**
	 *
	 * @param robot
	 * @param est
	 */
	public void evalOut(Robot robot, double[] est) {
		post();

		pw.println("ステップ" + step + "," + "x" + "," + "y");

		pw.println("ロボットの座標" + "," + robot.getX() + "," + robot.getY());
		pw.println("推定位置" + "," + est[0] + "," + est[1]);

		double xx = robot.getX() - est[0];
		double yy = robot.getY() - est[1];

		pw.println("差" + "," + xx + "," + yy);

		double gx = xx / est[0];
		double gy = yy / est[1];

		pw.println("誤差" + "," + gx + "," + gy);
		pw.println();

		step++;

		close();
	}
}
