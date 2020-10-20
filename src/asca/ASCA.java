package asca;

import java.awt.Color;

import javax.swing.UIManager;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import asca.pf.PFmain;

/**
 * mainメソッドを持つASCAの実行ファイル<br>
 * オプションを読み込み処理を行う
 */
@SuppressWarnings("deprecation")
public class ASCA {
	/**
	 * mainメソッド<br>
	 * オプションを読み込み処理を行う
	 * @param args オプション
	 */
	public static void main(String[] args) {
		// クラスロードによりASCAで使用するディレクトリを作成する
		Setting.getInstance();

		// 見た目の設定(鈴木の好み)
    	UIManager.put("MenuItem.selectionBackground", new Color(209, 226, 242));
    	UIManager.put("MenuItem.background", new Color(240, 240, 240));
    	UIManager.put("ComboBox.selectionBackground", new Color(209, 226, 242));
    	UIManager.put("ComboBox.background", new Color(240, 240, 240));
    	UIManager.put("ScrollBarUI", "com.sun.java.swing.plaf.windows.WindowsScrollBarUI");

		// オプションの設定
		Options options = new Options();
		options.addOption("a", false, ""); // -a の後に何もない
		options.addOption("csv", true, ""); // -csv の後に何かつく
		options.addOption("pf", false, ""); // -pf の後に何もない

		// オプションの読み込み
		CommandLineParser parser = new BasicParser();
		CommandLine commandLine;

		try {
			// オプションを読み込む
			commandLine = parser.parse(options, args);
		}
		catch (ParseException e) {
			e.printStackTrace();
			return;
		}

		// オプションの数をカウント
		int optionCount = 0;

		// オプションの処理  ex)java ASCA -a -csv asca.csv -pf
		if(commandLine.hasOption("a")) {
			optionCount++;
			System.out.println("aオプションの処理");
		}

		if(commandLine.hasOption("csv")) {
			optionCount++;
			System.out.println(commandLine.getOptionValue("csv"));
		}

		if(commandLine.hasOption("pf")) {
			optionCount++;
			new PFmain();
		}

		if(optionCount == 0) {
			new PFmain();
		}
	}
}
