package asca.pf.gui;

import java.awt.CardLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import asca.pf.Particle;
import asca.pf.Robot;
import asca.pf.map.WorldMap;

/**
 * マップを表示するフレーム
 */
public class SimFrame extends ASCAFrame implements ActionListener{
	private CardLayout clayout;

	private JPanel simPanel;

	private JMenuItem saveImage;
	private int step = 0;

	/**
	 * コンストラクタ<br>
	 * フレームを作成する
	 */
	public SimFrame() {
		initSimFrame();
	}

	/**
	 * フレームを作成する
	 */
	private void initSimFrame() {
		this.setTitle("グラフ");

		// メニューバーの設定
		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("ファイル");
		saveImage = new JMenuItem("画像に出力");
		saveImage.addActionListener(this);
		menu1.add(saveImage);
		menuBar.add(menu1);
		this.setJMenuBar(menuBar);

		simPanel = new JPanel();

		clayout = new CardLayout();
		simPanel.setLayout(clayout);

		this.add(simPanel);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//フレームを常に前面に
		//this.setAlwaysOnTop(true);

		this.setVisible(false);
	}

	/**
	 * 描く
	 * @param data 粒子
	 * @param actual 実際のロボット
	 * @param landmark 目印
	 * @param est 推定位置
	 * @param map
	 */
	public void draw(Particle[] data, Robot actual, double[][] landmark, double[] est, WorldMap map) {
		commonDrawProcessing(new Drawing(data, step, actual, landmark, est, map));
	}

	/**
	 * 描く
	 * @param data
	 * @param step
	 * @param actual
	 * @param est
	 * @param map
	 */
	public void draw(Particle[] data, Robot actual, double[] est, WorldMap map) {
		commonDrawProcessing(new Drawing(data, step, actual, est, map));
	}

	/**
	 * drawメソッドの共通処理
	 * @param canvas 追加するグラフ
	 */
	private void commonDrawProcessing(Drawing canvas) {
		simPanel.add(canvas);
		canvas.setBounds(0, 0, setting.getSimFrameSize(), setting.getSimFrameSize());
		clayout.last(simPanel);
		step++;
	}

	/**
	 * フレームを表示するか設定
	 * @param b 表示したい場合true,そうでない場合false
	 */
	public void setVisible(boolean b) {
		super.setVisible(b);
	}

	/**
	 * 前のステップを表示する
	 */
	public void prev() {
		clayout.previous(simPanel);
	}

	/**
	 * 次のステップを表示する
	 */
	public void next() {
		clayout.next(simPanel);
	}

	/**
	 * 画像として保存する<br>
	 * ファイルが既に存在する場合は警告なしに上書きされる<br>
	 * 警告を出すことは技術的に可能なため、気になるようなら付け加えてください
	 */
	public void saveImage(String path) {
		try{
			OutputStream out = new FileOutputStream(path);

			// フレームサイズと同じサイズにする
			BufferedImage cachedImage = new BufferedImage(simPanel.getWidth(), simPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g1 = cachedImage.createGraphics();
			simPanel.paint(g1);
			g1.dispose();

			/*// サイズを正方形にする
			BufferedImage image = new BufferedImage(setting.getSimFrameSize(), setting.getSimFrameSize(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = image.createGraphics();
			g2.drawImage(cachedImage, 0, 0, setting.getSimFrameSize(), setting.getSimFrameSize(), null);
			g2.dispose();*/

			ImageIO.write(cachedImage, "png", out);
			out.close();
		}
		catch(Exception error){
			error.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == saveImage) {
			// 今日の日付の取得
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
			String date = sdf.format(calendar.getTime());

			// ファイル選択画面作成
			JFileChooser chooser = new JFileChooser();
			chooser.setSelectedFile(new File(setting.getOutputPath(), date + ".png"));

			// pngだけが対象になるようなフィルタを作成
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG (*.png)", "png");
			chooser.setFileFilter(filter);

			// ファイル選択画面表示
			int val = chooser.showSaveDialog(this);
			if(val == JFileChooser.APPROVE_OPTION) {
				String path = chooser.getSelectedFile().getPath();

				if(!path.substring(path.lastIndexOf(".") + 1).toLowerCase().equals("png")) {
					path += ".png";
				}

				saveImage(path);
			}
		}
	}
}
