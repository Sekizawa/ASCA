package asca.pf.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 */
public class HistoryFrame extends ASCAFrame implements ActionListener {
	private JButton preButton;
	private JButton nextButton;
	private ControlController cont;

	/**
	 * コンストラクタ<br>
	 * フレームを作成する
	 * @param cont ControlControllerクラスのインスタンス
	 */
	public HistoryFrame(ControlController cont) {
		this.cont = cont;
		initHistoryFrame();
	}

	/**
	 * フレームを作成する
	 */
	public void initHistoryFrame() {
		this.setTitle("History");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setLayout(new GridLayout(2, 1));

		preButton = new JButton("前のステップに戻る");
		preButton.addActionListener(this);

		nextButton = new JButton("次のステップに進む");
		nextButton.addActionListener(this);

		this.add(preButton);
		this.add(nextButton);

		//フレームを常に前面に
		//this.setAlwaysOnTop(true);

		// ControlControllerのstartメソッドで表示されるため不要
		//this.setVisible(true);
	}

	/**
	 * フレームの表示可否を指定する
	 * @param b trueなら表示,falseなら非表示
	 */
	public void setVisible(boolean b) {
		super.setVisible(b);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == preButton) {
			cont.prev();
		}

		else if(evt.getSource() == nextButton) {
			cont.next();
		}
	}
}
