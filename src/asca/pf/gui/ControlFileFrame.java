package asca.pf.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import asca.pf.InstructionFileReader;
import asca.pf.gui.interfaces.FileFrameBase;

/**
 *
 */
public class ControlFileFrame extends ASCAFrame implements ActionListener, FileFrameBase {
	private JTextField pathtf;
	private DefaultTableModel dtm;
	private ControlController cont;
	private JButton startButton;
	private JButton browseButton;
	private JButton readButton;

	/**
	 * コンストラクタ<br>
	 * フレームを作成する
	 * @param cont ControlControllerクラスのインスタンス
	 */
	public ControlFileFrame(ControlController cont) {
		this.cont = cont;
		createFrame();
	}

	/**
	 * ファイル入力のフレームを作成
	 */
	private void createFrame() {
		this.setTitle("ファイルからロボットを操作する");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 上側
		// ファイルパス入力、ファイル選択、ファイル読み込みの３要素
		JPanel topPanel = new JPanel();
		// ファイルパス入力用テキストフィールド
		pathtf = new JTextField(setting.getSimulatePath() + "p1.csv", 16);
		browseButton = new JButton("参照");
		browseButton.addActionListener(this);
		// ファイル読み込みボタン テキストフィールドに入力された文字列のファイルを読み込みたい
		readButton = new JButton("読み込む");
		readButton.addActionListener(this);
		topPanel.add(pathtf);
		topPanel.add(browseButton);
		topPanel.add(readButton);

		// 下側
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		startButton = new JButton("start");
		startButton.addActionListener(this);
		bottomPanel.add(startButton);

		// 中央 表を表示したい
		JPanel centerPanel = new JPanel();

		// 表の項目名
		String[] names = {"step", "forward", "angle(rad)"};

		// 表
		dtm = new DefaultTableModel(names, 0);
		JTable table = new JTable(dtm);

		// 表が長い場合はスクロールできるようにしたい
		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(300, 100));
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		centerPanel.add(scroll);

		this.add(topPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);

		//フレームを常に前面に
		//this.setAlwaysOnTop(true);

		// ControlControllerのstartメソッドで表示されるため不要
		//this.setVisible(true);
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
		return pathtf.getText();
	}

	/**
	 *
	 * @param list
	 */
	public void updateTable(List<InstructionFileReader.Set> list) {
		while(dtm.getRowCount() > 0) {
			dtm.removeRow(0);
		}
		if(list == null) {
			return;
		}
		for(int i = 0; i < list.size(); i++) {
			String[] values = new String[3];
			values[0] = "" + list.get(i).getStep();
			values[1] = "" + list.get(i).getForward();
			values[2] = "" + list.get(i).getRadian();
			dtm.addRow(values);
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == startButton) {
			cont.controlFile();
		}

		else if(evt.getSource() == browseButton) {
			// ファイル選択画面作成
			JFileChooser chooser = new JFileChooser(setting.getSimulatePath());
			// csvだけが対象になるようなフィルタを作成
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV (*.csv)", "csv");
			chooser.setFileFilter(filter);
			// ファイル選択画面表示
			int val = chooser.showOpenDialog(this);
			if(val == JFileChooser.APPROVE_OPTION) {
				pathtf.setText(chooser.getSelectedFile().getPath());
			}
		}

		else if(evt.getSource() == readButton) {
			try {
				cont.readFile();
			}
			catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
		}
	}
}
