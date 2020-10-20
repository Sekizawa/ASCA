package asca;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import asca.pf.gui.ASCAFrame;

/**
 * コンソールフレーム
 */
public class ConsoleFrame extends ASCAFrame implements MouseListener, ActionListener{
	private static ConsoleFrame instance = new ConsoleFrame();

	/**
	 * コンソール
	 */
	private JTextArea consoleText;

	/**
	 * スクロールパネル
	 */
	private JScrollPane scrollpane;

	/**
	 * ポップアップメニュー
	 */
	private JPopupMenu popup;

	/**
	 * ポップアップメニューのコピー
	 */
	private JMenuItem copyMenuItem;

	/**
	 * ポップアップメニューの全て選択
	 */
	private JMenuItem selectAllMenuItem;

	/**
	 * コンストラクタ<br>
	 * フレームを作成する
	 */
	private ConsoleFrame() {
		initConsoleFrame();
	}

	/**
	 * インスタンスを返す
	 * @return ConsoleFrameクラスのインスタンスを返す
	 */
	public static ConsoleFrame getInstance() {
		return instance;
	}

	/**
	 * フレームを作成する
	 */
	private void initConsoleFrame() {
		this.setTitle("コンソール");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		consoleText = new JTextArea();
		consoleText.setLineWrap(true); // 文字列の折り返し
		consoleText.setWrapStyleWord(false); // 単語ごとに折り返し
		consoleText.addMouseListener(this);
		consoleText.setEditable(false); // 編集不可

		scrollpane = new JScrollPane(consoleText);
		scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		this.add(scrollpane);

		popup = new JPopupMenu();
		copyMenuItem = new JMenuItem("コピー");
		copyMenuItem.addActionListener(this);
		popup.add(copyMenuItem);
		selectAllMenuItem = new JMenuItem("すべて選択");
		selectAllMenuItem.addActionListener(this);
		popup.add(selectAllMenuItem);

		//フレームを常に前面に
		//this.setAlwaysOnTop(true);

		//this.setVisible(true);
	}

	/**
	 * コンソールに表示する
	 * @param text 表示するテキスト
	 */
	public void println(Object o) {
		String text = o.toString();
		consoleText.append(text + "\n");
		System.out.println(text);
		scrollpane.getVerticalScrollBar().setValue(scrollpane.getVerticalScrollBar().getMaximum());
	}

	/**
	 * フレームを表示するか設定する
	 * @param b 表示する場合true,そうでない場合false
	 */
	public void setVisible(boolean b) {
		super.setVisible(b);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)) {
			if(e.getSource() == consoleText) {
				// コピーできるか
				if(consoleText.getSelectedText() != null) {
					copyMenuItem.setEnabled(true);
				}
				else {
					copyMenuItem.setEnabled(false);
				}

				// 全て選択できるか
				if(consoleText.getText().replaceAll("　", "").replaceAll(" ", "").replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\t", "").trim().length() != 0) {
					selectAllMenuItem.setEnabled(true);
				}
				else {
					selectAllMenuItem.setEnabled(false);
				}

				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// コピー
		if(e.getSource() == copyMenuItem){
			consoleText.copy();
		}

		// 全選択
		else if(e.getSource() == selectAllMenuItem) {
			consoleText.selectAll();
		}
	}
}
