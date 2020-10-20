package asca.pf;

import asca.ConsoleFrame;

/**
 * ロボットの位置を出力する
 *
 */
public class OutputRobotPlace {
	private int step = 0;

	public OutputRobotPlace() {

	}

	/**
	 * ロボットの位置を出力する
	 * @param x ロボットのx座標
	 * @param y ロボットのy座標
	 */
	public void outputPlace(double x, double y) {
		ConsoleFrame.getInstance().println("**************************************************");
		ConsoleFrame.getInstance().println("ステップ" + step);
		ConsoleFrame.getInstance().println("ロボットの位置（x）：" + x);
		ConsoleFrame.getInstance().println("ロボットの位置（y）：" + y);
		step++;
	}
}
