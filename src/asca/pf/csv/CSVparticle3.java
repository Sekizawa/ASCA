package asca.pf.csv;

import asca.pf.Particle;

/**
 *
 */
public class CSVparticle3 extends CSV {
	private int step = 0;

	/**
	 * コンストラクタ<br>
	 * ファイルパスを読み込みcsvファイルを作成する
	 * @param filename ファイルパス
	 */
	public CSVparticle3(String filename) {
		super(filename);
	}

	/**
	 *
	 * @param myrobot ロボットの状態
	 * @param result1 全てのパーティクル
	 * @param result2 true stepがリセット
	 */
	public void particleOut(Particle[] result1) {
		post();

		pw.println("ステップ" + step++);


		pw.println("すべてのパーティクル");

		if(result1 != null) {

			pw.print("粒子の位置（x）,");
			for(int i = 0; i < result1.length; i++) {
				pw.print(",");
				pw.print(result1[i].getX());
			}
			pw.println();

			pw.print("粒子の位置（y）,");
			for(int i = 0; i < result1.length; i++) {
				pw.print(",");
				pw.print(result1[i].getY());
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
