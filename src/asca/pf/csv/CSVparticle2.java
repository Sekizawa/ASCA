package asca.pf.csv;

import asca.pf.Particle;
import asca.pf.Robot;

/**
 *
 */
public class CSVparticle2 extends CSV {
	private int step = 0;

	/**
	 * コンストラクタ<br>
	 * ファイルパスを読み込みcsvファイルを作成する
	 * @param filename ファイルパス
	 */
	public CSVparticle2(String filename) {
		super(filename);
	}

	/**
	 *
	 * @param myrobot ロボットの状態
	 * @param result1 全てのパーティクル
	 * @param result2 解の候補
	 */
	public void particleOut(Robot myrobot, Particle[] result1, int[] result2) {
		post();

		pw.println("ステップ" + step++);
		if(myrobot != null) {
			pw.println("ロボットの位置（x）");
			//pw.print(",");
			pw.println(myrobot.getX());
			pw.println("ロボットの位置（y）");
			//pw.print(",");
			pw.println(myrobot.getY());
			pw.println("ロボットの角度");
			//pw.print(",");
			pw.println(myrobot.getAngle());

		}

		pw.println("すべてのパーティクル");

		pw.println("粒子の位置（x）");
		for(int i = 0; i < result1.length; i++) {
			pw.print(",");
			pw.print(result1[i].getX());
		}
		pw.println();

		pw.println("粒子の位置（y）");
		for(int i = 0; i < result1.length; i++) {
			pw.print(",");
			pw.print(result1[i].getY());
		}
		pw.println();
		pw.println();

		pw.println("パーティクルの候補");
		if(result2 != null) {
			pw.println("粒子の位置（x）");
			for(int i = 0; i < result2.length; i++) {
				pw.print(",");
				pw.print(result1[result2[i]].getX());
			}
			pw.println();

			pw.println("粒子の位置（y）");
			for(int i = 0; i < result2.length; i++) {
				pw.print(",");
				pw.print(result1[result2[i]].getY());
			}
			pw.println();
		}
		else {
			pw.println("候補なし");
		}

		pw.println();

		close();
	}
}
