package asca.pf.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import asca.pf.Robot;
import asca.pf.map.ImageMap;
import asca.pf.map.WorldMap;

/**
 *
 */
public class InputFrame extends ASCAFrame implements ActionListener {
	private JFrame previewFrame;
	private JTextField maptf, parttf, landtfp, landtfm, xtf, ytf, atf, slentf, saltf, sartf;
	private JCheckBox check;
	private InputController cont;
	private JButton okButton;
	private JButton browseButton;
	private JButton previewButton;

	/**
	 * コンストラクタ<br>
	 * フレームを作成する
	 * @param cont ControlControllerクラスのインスタンス
	 */
	public InputFrame(InputController cont) {
		this.cont = cont;
		this.setTitle("直接設定を入力する");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// パネルを貼り付ける
		this.add(getCenter(), BorderLayout.CENTER);
		this.add(getBottom(), BorderLayout.SOUTH);

		//フレームを常に前面に
		//this.setAlwaysOnTop(true);

		this.setVisible(true);
	}

	/**
	 * 中央の設定入力パネルを作成する
	 * @return 中央の設定入力パネル
	 */
	private JPanel getCenter() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(getMapPanel());
		panel.add(getParticlePanel());
		panel.add(getRobotPanel());
		panel.add(getSensorPanel());

		return panel;
	}

	/**
	 * 地図設定入力パネルを作成する
	 * @return 地図設定入力パネル
	 */
	private JPanel getMapPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		JLabel label = new JLabel("地図：");
		maptf = new JTextField(setting.getImagePath() + "test.png", 16);
		browseButton = new JButton("参照");
		browseButton.addActionListener(this);
		panel.add(label);
		panel.add(maptf);
		panel.add(browseButton);
		return panel;
	}

	/**
	 * パーティクル設定入力パネルを作成する
	 * @return パーティクル設定入力パネル
	 */
	private JPanel getParticlePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel titleLabel = new JLabel("パーティクルの設定");
		JPanel centerPanel = new JPanel(new GridLayout(2, 1));
		JPanel numsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel("パーティクルの個数：");
		parttf = new JTextField("10000", 5);
		numsPanel.add(label);
		numsPanel.add(parttf);

		JPanel pointsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		check = new JCheckBox("初期位置を既知にする");
		pointsPanel.add(check);

		centerPanel.add(numsPanel);
		centerPanel.add(pointsPanel);

		panel.add(titleLabel, BorderLayout.NORTH);
		panel.add(centerPanel, BorderLayout.CENTER);
		return panel;
	}

	/**
	 * ロボット設定入力パネルを作成する
	 * @return ロボット設定入力パネル
	 */
	private JPanel getRobotPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel titleLabel = new JLabel("ロボットの設定");
		JPanel centerPanel = new JPanel(new GridLayout(4, 1));
		JPanel pointPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel pointLabel = new JLabel("座標");
		JLabel xLabel = new JLabel("x：");
		xtf = new JTextField("50.0", 4);
		xtf.addActionListener(this);
		JLabel yLabel = new JLabel("y：");
		ytf = new JTextField("50.0", 4);
		ytf.addActionListener(this);
		previewButton = new JButton("プレビュー");
		previewButton.addActionListener(this);
		pointPanel.add(pointLabel);
		pointPanel.add(xLabel);
		pointPanel.add(xtf);
		pointPanel.add(yLabel);
		pointPanel.add(ytf);
		pointPanel.add(previewButton);

		JPanel anglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel angleLabel = new JLabel("角度：");
		atf = new JTextField("0.0", 4);
		atf.addActionListener(this);
		JLabel angleUnit = new JLabel("ラジアン");
		anglePanel.add(angleLabel);
		anglePanel.add(atf);
		anglePanel.add(angleUnit);

		JPanel landmarkp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lLabelp = new JLabel("目印の個数（左方向）：");
		landtfp = new JTextField("5", 3);
		landtfp.addActionListener(this);
		landmarkp.add(lLabelp);
		landmarkp.add(landtfp);

		JPanel landmarkm = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lLabelm = new JLabel("目印の個数（右方向）：");
		landtfm = new JTextField("5", 3);
		landtfm.addActionListener(this);
		landmarkm.add(lLabelm); landmarkm.add(landtfm);

		centerPanel.add(pointPanel);
		centerPanel.add(anglePanel);
		centerPanel.add(landmarkp);
		centerPanel.add(landmarkm);

		panel.add(titleLabel, BorderLayout.NORTH);
		panel.add(centerPanel, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * センサー設定入力パネルを作成する
	 * @return センサー設定入力パネル
	 */
	private JPanel getSensorPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel titleLabel = new JLabel("センサーの設定");
		JPanel centerPanel = new JPanel(new GridLayout(3, 1));

		JPanel lengthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lengthLabel = new JLabel("可視距離：");
		slentf = new JTextField("50.0", 4);
		slentf.addActionListener(this);
		lengthPanel.add(lengthLabel);
		lengthPanel.add(slentf);

		JPanel rightAnglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel rightAngleLabel = new JLabel("右最大旋回角度：");
		sartf = new JTextField("90.0", 4);
		sartf.addActionListener(this);
		JLabel rightUnit = new JLabel("度");
		rightAnglePanel.add(rightAngleLabel);
		rightAnglePanel.add(sartf);
		rightAnglePanel.add(rightUnit);

		JPanel leftAnglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel leftAngleLabel = new JLabel("左最大旋回角度：");
		saltf = new JTextField("90.0", 4);
		saltf.addActionListener(this);
		JLabel leftUnit = new JLabel("度");
		leftAnglePanel.add(leftAngleLabel);
		leftAnglePanel.add(saltf);
		leftAnglePanel.add(leftUnit);

		centerPanel.add(lengthPanel);
		centerPanel.add(leftAnglePanel);
		centerPanel.add(rightAnglePanel);
		panel.add(titleLabel, BorderLayout.NORTH);
		panel.add(centerPanel, BorderLayout.CENTER);
		return panel;
	}

	/**
	 * 下部のボタンパネルを作成する
	 * @return 下部のボタンパネル
	 */
	private JPanel getBottom() {
		JPanel panel = new JPanel(new FlowLayout());
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		panel.add(okButton);
		return panel;
	}

	/**
	 * プレビュー画面を表示するメソッド。ロボットの座標を確認するため。
	 * @param path 地図画像のアドレス
	 * @param x ロボットのx座標
	 * @param y ロボットのy座標
	 */
	private void preview(String path, double x, double y) {
		JFrame prev = new JFrame();
		prev.setTitle("プレビュー");
		prev.setBounds(0, 0, setting.getSimFrameSize(), setting.getSimFrameSize() + 30);
		prev.setResizable(false);
		WorldMap map = new ImageMap(path);
		Robot rob = new Robot(x, y, 0, map);
		Robot[] robs = new Robot[1];
		robs[0] = new Robot(0, 0, 0, map);
		double[] ests = {0, 0};
		Drawing d = new Drawing(robs, 0, rob, ests, map);
		d.setBounds(0, 0, setting.getSimFrameSize(), setting.getSimFrameSize());
		prev.add(d);
		if(previewFrame != null) {
			previewFrame.dispose();
		}
		previewFrame = prev;
		previewFrame.setVisible(true);
	}

	/**
	 * フレームを表示するか設定
	 * @param b 表示したい場合true,そうでない場合false
	 */
	public void setVisible(boolean b) {
		if(previewFrame != null) {
			previewFrame.setVisible(b);
		}
		super.setVisible(b);
	}

	/**
	 * テキストフィールドからパスを取得
	 * @return
	 */
	public String getPath() {
		return maptf.getText();
	}

	/**
	 * パーティクルの数を返す
	 * @return パーティクルの数
	 */
	public String getParticle() {
		return parttf.getText();
	}

	/**
	 * ロボットのx座標を返す
	 * @return ロボットのx座標
	 */
	public String getRobotx() {
		return xtf.getText();
	}

	/**
	 * ロボットのy座標を返す
	 * @return ロボットのy座標
	 */
	public String getRoboty() {
		return ytf.getText();
	}

	/**
	 * ロボットの角度を返す
	 * @return ロボットの角度
	 */
	public String getRobotr() {
		return atf.getText();
	}

	/**
	 * 左方向の目印の個数を返す
	 * @return 左方向の目印の個数
	 */
	public String getLandplus() {
		return landtfp.getText();
	}

	/**
	 * 右方向の目印の個数を返す
	 * @return 右方向の目印
	 */
	public String getLandminus() {
		return landtfm.getText();
	}

	/**
	 * センサーの可視距離を返す
	 * @return センサーの可視距離
	 */
	public String getSenslen() {
		return slentf.getText();
	}

	/**
	 * 左最大旋回角度を返す
	 * @return 左最大旋回角度
	 */
	public String getSensdegplus() {
		return saltf.getText();
	}

	/**
	 * 右最大旋回角度を返す
	 * @return 右最大旋回角度
	 */
	public String getSensdegminus() {
		return sartf.getText();
	}

	/**
	 * 初期位置が既知かどうかを返す
	 * @return 既知ならtrue,そうでないならfalse
	 */
	public boolean isKnownRobotCoordinate() {
		return check.isSelected();
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == browseButton) {
			// ファイル選択画面作成
			JFileChooser chooser = new JFileChooser(setting.getImagePath());
			// csvだけが対象になるようなフィルタを作成
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG (*.png)", "png"/*, "txt", "bin"...で追加可能*/);
			chooser.setFileFilter(filter);
			// ファイル選択画面表示
			int val = chooser.showOpenDialog(this);
			// yes,okが押されたとき ファイルが選択されたとき
			if(val == JFileChooser.APPROVE_OPTION) {
				maptf.setText(chooser.getSelectedFile().getPath());
			}
		}

		else if(evt.getSource() == previewButton) {
			double x = Double.valueOf(xtf.getText());
			double y = Double.valueOf(ytf.getText());
			preview(maptf.getText(), x, y);
		}

		// それ以外はOKボタンと同じ動作
		else {
			try {
				cont.inputData();
			}
			catch (NumberFormatException error) {
				JOptionPane.showMessageDialog(this, "数値が不正です");
			}
			catch (FileNotFoundException error) {
				JOptionPane.showMessageDialog(this, "存在しないマップです");
			}
		}
	}
}
