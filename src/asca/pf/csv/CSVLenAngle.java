package asca.pf.csv;

import asca.pf.LenAngle;

/**
 * 2018/07/14 もしかしたらこのクラスは使用されていないかも by 鈴木
 */
public class CSVLenAngle extends CSV {

	private int step = 0;

	/**
	 * コンストラクタ<br>
	 * ファイルパスを読み込みcsvファイルを作成する
	 * @param filename ファイルパス
	 */
	public CSVLenAngle(String filename) {
		super(filename);
		this.headerOut();
	}

	/**
	 * ヘッダを書き込む
	 */
	public void headerOut() {
		post();

		pw.println("ステップ" + "," + "パーティクル番号" + "," + "距離" + "," + "角度");

		close();
	}

	/**
	 *
	 * @param la
	 * @param pnum
	 */
	public void laOut(LenAngle[] la, int pnum) {
		post();

		for(int i = 0; i < la.length; i++) {
			pw.print("ステップ" + step + ",");
			pw.print(pnum + ",");
			pw.println(la[i].getLength() + "," + la[i].getAngle());
		}

		close();
	}

	/**
	 *
	 */
	public void step() {
		step++;
	}
}
