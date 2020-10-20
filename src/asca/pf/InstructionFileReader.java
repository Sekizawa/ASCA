package asca.pf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 *
 */
public class InstructionFileReader {
	private final String err = "error";
	private final LinkedList<Set> list;

	/**
	 * InstructionFileReaderのサブクラス
	 */
	public class Set {
		private int step;
		private double forward;
		private double angleRad;

		/**
		 * コンストラクタ
		 * @param step
		 * @param forward
		 * @param angleRad
		 */
		private Set(int step, double forward, double angleRad) {
			this.step = step;
			this.forward = forward;
			this.angleRad = angleRad;
		}

		/**
		 *
		 * @return
		 */
		public int getStep() {
			return step;
		}

		/**
		 *
		 * @return
		 */
		public double getForward() {
			return forward;
		}

		/**
		 *
		 * @return
		 */
		public double getRadian() {
			return angleRad;
		}
	}

	/**
	 * コンストラクタ
	 */
	public InstructionFileReader() {
		list = new LinkedList<Set>();
	}

	/**
	 * ファイルを読み込んで命令リストを作成する
	 * ファイルが存在しない、フォーマットにそぐわないなどの場合はエラー表示からリスト削除して読み込み終了
	 * @param filepath 読み込むファイルのパス
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void read(String filepath) throws IOException, FileNotFoundException {
		if(filepath == null) {
			return;
		}

		File file = new File(filepath);

		try {
			Scanner sc = new Scanner(file);
			this.remove();
			for(int i = 1; sc.hasNextLine(); i++) {
				String str = sc.nextLine();
				String[] split = str.split(",");
				if(split.length != 2) {
					System.err.println("file format error");
					this.remove();
					sc.close();
					throw new IOException("ファイルの書式間違い：" + i + "行目：" + str + "\n\t前進距離,角度 の形式で入力して下さい");
				}
				String fval = this.shapeForward(split[0]);
				String aval = this.shapeRadian(split[1]);
				if(fval.equals(err)) {
					System.err.println("file format error");
					this.remove();
					sc.close();
					throw new IOException("ファイルの書式間違い：" + i + "行目：" + split[0] + "\n\t前進距離は半角の浮動小数点型で入力して下さい");
				}
				if(aval.equals(err)) {
					System.err.println("file format error");
					this.remove();
					sc.close();
					throw new IOException("ファイルの書式間違い：" + i + "行目：" + split[1] + "\n\t角度は半角の浮動小数点型で入力して下さい" + "\n\tまた、ラジアンなら「r」、度なら「d」を先頭につけて下さい");
				}
				list.add(new Set(i, Double.valueOf(fval), Double.valueOf(aval)));
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.err.println("file not found.");
			throw new FileNotFoundException("ファイルが見つかりません");
		}
	}

	/**
	 * リスト消去
	 */
	private void remove() {
		while(!list.isEmpty()) {
			list.removeFirst();
		}
	}

	/**
	 * ステップ数を取得
	 * @param idx 命令リストのインデックス
	 * @return idx番のステップ数 idxがリストの範囲外の場合-1を返す
	 */
	public int getStep(int idx) {
		if(idx >= list.size() || idx < 0) {
			return -1;
		}
		return list.get(idx).step;
	}

	/**
	 * 前進距離を取得
	 * @param idx 命令リストのインデックス
	 * @return idx番の前進距離 idxがリストの範囲外の場合Double.NaNを返す
	 */
	public double getForward(int idx) {
		if(idx >= list.size() || idx < 0) {
			return Double.NaN;
		}
		return list.get(idx).forward;
	}

	/**
	 * ラジアンを取得
	 * @param idx 命令リストのインデックス
	 * @return idx番のラジアン idxがリストの範囲外の場合Double.NaNを返す
	 */
	public double getAngleRad(int idx) {
		if(idx >= list.size() || idx < 0) {
			return Double.NaN;
		}
		return list.get(idx).angleRad;
	}

	public List<Set> getSetList() {
		return list;
	}

	/**
	 * リストのサイズを取得
	 * @return 命令リストのサイズ
	 */
	public int getSize() {
		return list.size();
	}

	/**
	 * Double型で表され、前進距離として扱える文字列に変換する
	 * @param str
	 * @return
	 */
	private String shapeForward(String str) {
		String ret = err;
		if(str == null) {
			return ret;
		}
		if(this.isDouble(str)) {
			ret = "" + Double.valueOf(str);
		}
		return ret;
	}

	/**
	 * Double型で表され、ラジアンとして扱える文字列に変換する
	 * @param str
	 * @return
	 */
	private String shapeRadian(String str) {
		String ret = err;
		if(str == null) {
			return ret;
		}

		switch (str.charAt(0)) {
		case 'd':
			str = str.substring(1);
			if(this.isDouble(str)) {
				ret = "" + Math.toRadians(Double.valueOf(str));
			}
			break;
		case 'r':
			str = str.substring(1);
			if(this.isDouble(str)) {
				ret = "" + Double.valueOf(str);
			}
			break;
		default:
			break;
		}
		return ret;
	}

	/**
	 * strがdouble型で表せるか調べる
	 * @param str
	 * @return
	 */
	private boolean isDouble(String str) {
		return str.matches("[+-]?[0-9]+(.?[0-9]+)?");
	}
}
