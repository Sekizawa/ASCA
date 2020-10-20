package asca.pf.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import asca.Setting;
import asca.pf.Robot;
import asca.pf.map.WorldMap;

/**
 * グラフを描画するクラス
 */
public class Drawing extends JPanel {
	private Setting setting = Setting.getInstance();

	@SuppressWarnings("unused")
	private Robot[] data;

	/**
	 * dataの個数
	 */
	private int data_n;

	private int step;

	/**
	 * グラフの左上の座標
	 */
	private final int X0 = 45, Y0 = 35;

	/**
	 * グラフの最大辺<br>
	 * 100単位で調整しないとうまく描画されない
	 */
	private final int maxSide = setting.getSimFrameSize() - 100;

	/**
	 * グラフの1メモリの1辺
	 */
	private final double side = maxSide / 100.0;

	private int height, width;
	private double[] dataX;
	private double[] dataY;
	private double[][] landmark;
	private boolean mode = false;
	public WorldMap map;
	private double actX, actY;
	private double mag;

	/**
	 * コンストラクタ
	 * @param data
	 * @param step
	 * @param actual
	 * @param landmark
	 * @param est
	 * @param map
	 */
	public Drawing(Robot[] data, int step, Robot actual, double[][] landmark, double[] est, WorldMap map) {
		this.landmark = landmark;
		this.mode = true;

		init(data, step, actual, est, map);
	}

	/**
	 *
	 * @param data
	 * @param step
	 * @param actual
	 * @param est
	 * @param map
	 */
	public Drawing(Robot[] data, int step, Robot actual, double[] est, WorldMap map) {
		init(data, step, actual, est, map);
	}

	/**
	 * 初期化<br>
	 * 各コンストラクタで共通する処理を記述する
	 * @param data
	 * @param step
	 * @param actual
	 * @param est
	 * @param map
	 */
	private void init(Robot[] data, int step, Robot actual, double[] est, WorldMap map) {
		this.data = data;
		this.data_n = data.length;

		// グラフを写像する際に用いる基準値
		mag = Math.min(maxSide / map.getworldsizex(), maxSide / map.getworldsizey());

		dataX = new double[data_n];
		dataY = new double[data_n];

		// mapの座標から表示するグラフの座標に写像する
		for(int i = 0; i < data_n; i++) {
			dataX[i] = data[i].getX() * mag - 0.5;
			dataY[i] = data[i].getY() * mag + 1;
		}

		this.step = step;
		this.map = map;

		this.actX = actual.getX();
		this.actY = actual.getY();

		// グラフの最大辺をmaxSizeにしてもう片方の辺はmapの比率と同じになるように決める
		int a = maxSide / 10;
		if(map.getworldsizex() <= map.getworldsizey()) {
			height = maxSide;
			width = (int)((height / map.getworldsizey()) * map.getworldsizex());
			if(width % a != 0) {
				// width以上のaの倍数にする
				width += (a - width % a);
			}
			//ConsoleFrame.getInstance().println(width + "," + height);
		}
		else {
			width = maxSide;
			height = (int)((width / map.getworldsizex()) * map.getworldsizey());
			if(height % a != 0) {
				// height以上のaの倍数にする
				height += (a - height % a);
			}
			//ConsoleFrame.getInstance().println(width + "," + height);
		}

		this.setOpaque(false);
	}

	/**
	 *
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		// 軸のラベル描画準備
		// 10目盛りの数値は大きいほうに合わせる
		int a = (int)Math.max(map.getworldsizex(), map.getworldsizey()) / 10;
		g2.setColor(Color.BLACK);

		// 縦軸のラベルの描画
		// パネルの上から描画を始めるため、数値はy軸の最大値から始める
		int county = (int)map.getworldsizey();

		// 端数がある可能性がある場合は1つ上のaの倍数にする
		if(county % a != 0) {
			// county以上のaの倍数にする
			county += (a - county % a);
		}
		for(int i = 0; i <= Math.ceil(height / (maxSide / 10.0)); i++) {
			g2.drawString(Integer.toString(county), X0 - 22, Y0 + (int)((maxSide / 10.0) * i));
			county -= a;
		}

		// 横軸のラベルの描画
		// パネルの左から描画を始めるため、数値は0から始める
		int countx = 0;
		for(int i = 0; i <= Math.ceil(width / (maxSide / 10.0)); i++) {
			g2.drawString(Integer.toString(countx), X0 + (int)((maxSide / 10.0) * i), (int)(Y0 + height + 20));
			countx += a;
		}

		// グラフを小分割する格子の描画
		g2.setColor(Color.lightGray);

		// 1メモリの大きさ
		// 100分割したときの1メモリ
		double div = maxSide / 100.0;

		//縦線
		for(int i = 0; i <= width / div; i++) {
			int x = (int)(div * i + 0.5);
			g2.drawLine(X0 + x, Y0, X0 + x, Y0 + height);
		}

		//横線
		for(int i = 0; i <= height / div; i++) {
			int y = (int)(div * i + 0.5);
			g2.drawLine(X0, Y0 + height - y, X0 + width, Y0 + height - y);
		}

		// グラフを中分割する格子の描画
		g2.setColor(Color.gray);

		// 10分割したときの1メモリ
		div = (maxSide / 10.0);

		//縦線
		for(int i = 0; i <= width / div; i++) {
			int x = (int)(div * i + 0.5);
			g2.drawLine(X0 + x, Y0, X0 + x, Y0 + height);
		}

		//横線
		for(int i = 0;i <= height / div; i++) {
			int y = (int)(div * i + 0.5);
			g2.drawLine(X0, Y0 + height - y, X0 + width, Y0 + height - y);
		}

		// ステップ数とマップサイズの描画
		g2.setColor(Color.black);
		g2.drawString("ステップ" + step + "    マップサイズ(W×H)：" + (int)map.getworldsizex() + "×" + (int)map.getworldsizey(), X0, Y0 - 20);

		drawLine(g2);
	}

	/**
	 *
	 * @param g
	 */
	private void drawLine(Graphics g) {

		Graphics2D g2 = (Graphics2D)g;

		// 壁の描画
		g.setColor(Color.black);
		for(int i = 0;i < map.getworldsizex(); i++) {
			for(int j = 0; j < map.getworldsizey(); j++) {
				if(map.getwall(i, j)) {
					g2.fill(new Rectangle2D.Double(X0 + map.getx(i, j) * mag, Y0 + height - mag - map.gety(i, j) * mag, mag, mag));
				}
			}
		}

		// パーティクルの描画
		g.setColor(Color.blue);
		for(int i = 0; i < data_n; i++) {
			g2.fill(new Rectangle2D.Double(X0 + dataX[i], Y0 + height - dataY[i], 2, 2));
		}

		// 目印の描画
		g.setColor(Color.ORANGE);
		if(this.mode) {
			for(int i = 0; i < landmark.length; i++) {
				double[] land = new double[2];
				land[0] = (int)this.landmark[i][0];
				land[1] = (int)this.landmark[i][1];

				if(this.landmark[i][0] < map.getworldsizex()) {
					land[0] = (int)this.landmark[i][0] + map.getworldsizex();
				}

				if(this.landmark[i][1] < map.getworldsizey()) {
					land[1] = (int)this.landmark[i][1] + map.getworldsizey();
				}

				g2.fill(new Rectangle2D.Double(X0 + land[0] % map.getworldsizex() * mag, Y0 + height - mag - land[1] % map.getworldsizey() * mag, mag, mag));
			}
		}

		// 現在地の描画
		g.setColor(Color.red);
		double rectX = X0 + (actX - 0.5) * mag;
		double rectY = Y0 + height - (actY + 0.5) * mag;
		g2.fill((new Rectangle2D.Double(rectX, rectY, side, side)));
	}
}
