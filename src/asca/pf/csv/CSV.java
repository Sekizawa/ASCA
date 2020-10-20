package asca.pf.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * csvファイルを作成し接続するクラス
 */
public class CSV {
	protected File file;
	protected PrintWriter pw;

	/**
	 * コンストラクタ<br>
	 * 新しくファイルを作成する
	 * @param filename
	 */
	public CSV(String filename) {
		this.file = new File(filename);
		try {
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "Shift-JIS")));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		close();
	}

	/**
	 * ファイルに接続する
	 */
	protected void post() {
		try {
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true),"Shift-JIS")));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ファイルの接続を切断する
	 */
	protected void close() {
		pw.close();
	}

	/**
	 *
	 */
	protected void setStep() {

	}

	/**
	 * 改行する
	 */
	public void newLine() {
		post();

		pw.println();
		pw.println();

		close();
	}
}
