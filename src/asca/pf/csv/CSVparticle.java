package asca.pf.csv;

import asca.pf.Particle;
import asca.pf.Robot;

/**
 *
 */
public class CSVparticle extends CSV {
	private int step = 0;

	/**
	 * コンストラクタ<br>
	 * ファイルパスを読み込みcsvファイルを作成する
	 * @param filename ファイルパス
	 */
	public CSVparticle(String filename) {
		super(filename);
	}

	/**
	 * 計算前のパーティクルの値を出力
	 * @param myrobot ロボット
	 * @param result 粒子
	 * @param n 粒子の個数
	 */
	public void particleOut(Robot myrobot, Particle[] result, int n) {
		post();
		pw.println("ステップ" + step);

		pw.print("ロボットの位置（x）" + ",");
		pw.println(myrobot.getX());
		pw.print("ロボットの位置（y）" + ",");
		pw.println(myrobot.getY());

		pw.print("粒子の位置（x）");
		for(int i = 0; i < n; i++) {
			pw.print(",");
			pw.print(result[i].getX());
		}
		pw.println();

		pw.print("粒子の位置（y）");
		for(int i = 0; i < n; i++) {
			pw.print(",");
			pw.print(result[i].getY());
		}
		pw.println();

		pw.print("");
		for(int i = 0;i < n; i++) {
			pw.print(",");
			pw.print(result[i].getNweight());
		}

		pw.println();
		pw.println();
		close();
		step++;
	}

	/**
	 * 計算後のパーティクルの値を出力
	 * @param myrobot ロボット
	 * @param result 粒子
	 * @param n 粒子の個数
	 */
	public void resultOut(Robot myrobot, Particle[] result, int n) {
		post();
		pw.print("ロボットの位置（x）" + ",");
		pw.println(myrobot.getX());
		pw.print("ロボットの位置（y）" + ",");
		pw.println(myrobot.getY());
		pw.print("ロボットの角度" + ",");
		pw.println(myrobot.getAngle());

		pw.print("粒子の位置（x）");
		for(int i = 0; i < n; i++) {
			pw.print(",");
			pw.print(result[i].getX());
		}
		pw.println();

		pw.print("粒子の位置（y）");
		for(int i = 0; i < n; i++) {
			pw.print(",");
			pw.print(result[i].getY());
		}
		pw.println();

		pw.print("粒子の角度");
		for(int i = 0; i < n; i++) {
			pw.print(",");
			pw.print(result[i].getAngle());
		}
		pw.println();

		pw.print("重み");
		for(int i = 0;i < n; i++) {
			pw.print(",");
			pw.print(result[i].getWeight());
		}
		pw.println();

		pw.print("計算後の重み");
		for(int i = 0;i < n; i++) {
			pw.print(",");
			pw.print(result[i].getNweight());
		}
		pw.println();
		pw.println();
		close();
	}
}
