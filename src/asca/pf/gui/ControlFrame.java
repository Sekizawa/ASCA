package asca.pf.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 */
public class ControlFrame extends ASCAFrame implements ActionListener, KeyListener {
	private JTextField forward, angle;
	private JComboBox<String> comboW;
	private JComboBox<String> comboA;
	private JComboBox<String> selectCombo;
	private ControlController cont;
	private JButton startButton;

	/**
	 * コンストラクタ<br>
	 * フレームを作成する
	 * @param cont ControlControllerクラスのインスタンス
	 */
	public ControlFrame(ControlController cont) {
		this.cont = cont;
		initControlFrame();
	}

	/**
	 * フレームを作成する
	 */
	private void initControlFrame() {
		this.setTitle("ロボットを操作する");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		forward = new JTextField("0");
		forward.addActionListener(this);
		String[] wallcombo = {"壁を考慮する", "壁を無視する"};
		comboW = new JComboBox<String>(wallcombo);

		JPanel movePanel = new JPanel();
		movePanel.setLayout(new GridLayout(1, 3));
		movePanel.add(forward);
		movePanel.add(new JLabel("進む"));
		movePanel.add(comboW);

		angle = new JTextField("0");
		angle.addActionListener(this);
		// 弧度法かラジアンかを選択できるコンボボックス
		String[] anglecombo = {"Degree", "Radian"};
		comboA = new JComboBox<String>(anglecombo);

		JPanel anglePanel = new JPanel();
		anglePanel.setLayout(new GridLayout(1, 3));
		anglePanel.add(angle);
		anglePanel.add(new JLabel("回る"));
		anglePanel.add(comboA);

		String[] selectTmp = {
				"理想位置",
				"重心",
				"重心から遠い点",
				"境界線上の点",
				"移動方向の点",
				"排他的論理和",
				"単方向の点",
				"象限方向を選択",
				"解空間を縮小した点"
		};
		selectCombo = new JComboBox<String>(selectTmp);

		JPanel selectPanel = new JPanel();
		selectPanel.setLayout(new GridLayout(1, 2));
		selectPanel.add(new JLabel("選択手法：", JLabel.RIGHT));
		selectPanel.add(selectCombo);

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(0, 1));
		controlPanel.add(movePanel);
		controlPanel.add(anglePanel);
		controlPanel.add(selectPanel);

		startButton = new JButton("start");
		startButton.addActionListener(this);
		startButton.addKeyListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(startButton);

		this.setLayout(new BorderLayout());
		this.add(controlPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);

		//フレームを常に前面に
		//this.setAlwaysOnTop(true);

		// ControlControllerのstartメソッドで表示されるため不要
		//this.setVisible(true);
	}

	/**
	 *
	 * @return
	 */
	public int getComboW() {
		return comboW.getSelectedIndex();
	}

	/**
	 *
	 * @return
	 */
	public int getComboA() {
		return comboA.getSelectedIndex();
	}

	/**
	 * 解の選択手法を返す
	 * @return
	 */
	public int getSelect() {
		return selectCombo.getSelectedIndex();
	}

	/**
	 * フレームを表示するか設定する
	 * @param b 表示したい場合true,そうでない場合false
	 */
	public void setVisible(boolean b) {
		super.setVisible(b);
	}

	/**
	 *
	 * @return
	 */
	public String getForward() {
		return forward.getText();
	}

	/**
	 *
	 * @return
	 */
	public String getAngle() {
		return angle.getText();
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		cont.control();
		/*if(evt.getSource() == startButton) {
			cont.control();
		}*/
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			cont.control();
		}
	}
}
