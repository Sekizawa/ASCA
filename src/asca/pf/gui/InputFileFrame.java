package asca.pf.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import asca.pf.gui.interfaces.FileFrameBase;

/**
 *
 */
public class InputFileFrame extends ASCAFrame implements ActionListener, FileFrameBase {
	private JTextField tf;
	private InputController cont;
	private JButton browseButton;
	private JButton okButton;

	/**
	 * コンストラクタ<br>
	 * フレームを作成する
	 * @param cont ControlControllerクラスのインスタンス
	 */
	public InputFileFrame(InputController cont) {
		this.cont = cont;
		createFrame();
	}

	/**
	 * フレームを作成する
	 */
	private void createFrame() {
		this.setTitle("ファイルから設定を読み込む");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel("ファイル名：");
		tf = new JTextField(setting.getSettingPath() + "test.csv", 14);
		tf.addActionListener(this);
		browseButton = new JButton("参照");
		browseButton.addActionListener(this);
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		centerPanel.add(label);
		centerPanel.add(tf);
		centerPanel.add(browseButton);
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(okButton);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);

		//フレームを常に前面に
		//this.setAlwaysOnTop(true);

		this.setVisible(true);
	}

	/**
	 * フレームを表示するか設定する
	 * @param b 表示する場合true,そうでない場合false
	 */
	public void setVisible(boolean b) {
		super.setVisible(b);
	}

	/**
	 * テキストフィールドからパスを取得
	 */
	public String getPath() {
		return tf.getText();
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == browseButton) {
			// ファイル選択画面作成
			JFileChooser chooser = new JFileChooser(setting.getSettingPath());
			// csvだけが対象になるようなフィルタを作成
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV (*.csv)", "csv"/*, "txt", "bin"...で追加可能*/);
			chooser.setFileFilter(filter);
			// ファイル選択画面表示
			int val = chooser.showOpenDialog(this);
			// yes,okが押されたとき ファイルが選択されたとき？
			if(val == JFileChooser.APPROVE_OPTION) {
				tf.setText(chooser.getSelectedFile().getPath());
			}
		}

		else if(evt.getSource() == okButton || evt.getSource() == tf) {
			try {
				cont.inputFile();
			}
			catch(FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, "ファイルが存在しません");
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(this, "データが不正です");
			}
		}
	}
}
