package asca.pf.csv;

/**
 * 入力ログをCSV形式で出力する
 */
public class CSVinput extends CSV {
	private int step = 1;

	/**
	 * コンストラクタ<br>
	 * ファイルパスを読み込みcsvファイルを作成する
	 * @param filename ファイルパス
	 */
	public CSVinput(String filename) {
		super(filename);
		this.headerOut();
	}

	/**
	 * ヘッダを書き込む
	 */
	public void headerOut() {
		post();
		pw.println("入力ログ");
		pw.println("" + "," + "forward" + "," + "angle(Degrees)" + "," + "angle(Radian)");
		close();
	}

	/**
	 *
	 * @param forward
	 * @param angle
	 */
	public void dataOut(double forward, double angle) {
		post();
		pw.println("ステップ" + step + "," + forward + "," + Math.toDegrees(angle) + "," + angle);
		close();
		step++;
	}
}
