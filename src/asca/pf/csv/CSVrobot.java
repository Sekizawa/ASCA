package asca.pf.csv;
import asca.pf.LenAngle;
import asca.pf.Robot;
import asca.pf.map.Map;

/**
 *
 */
public class CSVrobot extends CSV {
	private int step = 0;

	/**
	 * コンストラクタ<br>
	 * ファイルパスを読み込みcsvファイルを作成する
	 * @param filename ファイルパス
	 */
	public CSVrobot(String filename) {
		super(filename);
	}

	/**
	 *
	 * @param myrobot
	 * @param landlen
	 */
	public void settingOut(Robot myrobot, int landlen) {
		post();

		pw.println("目印の個数" + "," + landlen);

		pw.println("ロボットの初期位置");

		pw.println("" + "," + "x" + "," + "y" + "," + "angle(Degrees)" + "," + "angle(Radian)");
		pw.println("" + "," + myrobot.getX() + "," + myrobot.getY() + "," + Math.toDegrees(myrobot.getAngle()) + "," + myrobot.getAngle());

		pw.println("センサーの設定");
		pw.println("可視距離" + "," + myrobot.getMaxlen());
		pw.println("左最大旋回角度" + "," + myrobot.getPlus());
		pw.println("右最大旋回角度" + "," + myrobot.getMinus());

		close();
	}

	/**
	 *
	 * @param myrobot
	 * @param land
	 */
	public void markOut(Robot myrobot, LenAngle[] land) {
		post();
		pw.println("ロボットが取得した目印のデータ");

		Map[] landmark = myrobot.getLandmark();
		pw.print("");

		for(int i = 0; i < landmark.length; i++) {
			pw.print("," + i + "番目");
		}
		pw.println();

		pw.print("x座標");
		for(int i = 0; i < landmark.length; i++) {
			pw.print("," + landmark[i].getx());
		}
		pw.println();

		pw.print("y座標");
		for(int i = 0; i < landmark.length; i++) {
			pw.print("," + landmark[i].gety());
		}
		pw.println();

		pw.print("カベの有無");
		for(int i = 0; i < landmark.length; i++) {
			pw.print("," + landmark[i].getwall());
		}
		pw.println();

		pw.print("目印との距離");
		for(int i = 0; i < land.length; i++) {
			pw.print("," + land[i].getLength());
		}

		pw.println();

		pw.print("目印とロボットの向きとの角度");
		for(int i = 0; i < land.length; i++) {
			pw.print("," + land[i].getAngle());
		}
		pw.println();
		pw.println();
		close();
	}

	/**
	 *
	 * @param myrobot
	 * @param a
	 */
	public void positionOut(Robot myrobot, boolean a) {
		post();

		pw.println("ロボットの位置");
		pw.println("" + "," + "x" + "," + "y" + "," + "angle(Degrees)" + "," + "angle(Radian)");
		if(a) {
			pw.print("更新前" + ",");
		}
		else {
			pw.print("更新後" + ",");
		}
		pw.println(myrobot.getX() + "," + myrobot.getY() + "," + Math.toDegrees(myrobot.getAngle()) + "," + myrobot.getAngle());
		pw.println();
		close();

	}

	/**
	 *
	 * @param step
	 */
	public void stepOut() {
		post();
		pw.println();
		pw.println();
		pw.println("ステップ" + step + ",");
		close();
		step++;
	}
}
