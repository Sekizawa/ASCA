package asca.pf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import asca.ConsoleFrame;
import asca.Setting;

/**
 * ファイルから設定を読み込む
 */
public class InputFile {

	private String mapname;
	private int n, landp, landm;
	private double x, y, angle, lenmax, plus, minus;
	private boolean alk;

	private Setting setting = Setting.getInstance();

	/**
	 * コンストラクタ
	 * @param mapname
	 * @param n
	 * @param x
	 * @param y
	 * @param angle
	 * @param lenmax
	 * @param plus
	 * @param minus
	 * @param estp
	 * @param estm
	 * @param a
	 * @throws NumberFormatException
	 * @throws FileNotFoundException
	 */
	public void readFile(String mapname, int n, double x, double y, double angle, double lenmax, double plus, double minus, int estp, int estm, boolean a) throws NumberFormatException, FileNotFoundException {

		if(!this.getSuffixImage(mapname)) {
			throw new FileNotFoundException();
		}

		if (!(new File(mapname).exists())) {
			//拡張子は正しいが、ファイルが存在しない
			throw new FileNotFoundException();
		}

		this.mapname = mapname;

		this.n = n;

		this.landp = estp;
		this.landm = estm;

		this.x = x;
		this.y = y;
		this.angle = angle;
		this.lenmax = lenmax;

		this.plus = plus;
		this.minus = minus;

		this.alk = a;

		this.check(n, estp, estm, x, y, lenmax, plus, minus);
	}

	/**
	 * ファイルパスからデータを読み込む
	 * @param filepath csvファイルのファイルパス
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void readFile(String filepath) throws FileNotFoundException, IOException {

		if(!this.getSuffixCSV(filepath)) {
			throw new IOException();
		}

		FileReader f = new FileReader(filepath);
		BufferedReader b = new BufferedReader(f);

		mapname = setting.getImagePath() + b.readLine();



		n = this.readInt(b.readLine());

		StringTokenizer token3 = new StringTokenizer(b.readLine(), ",");
		landp = this.readInt(token3.nextToken());
		landm = this.readInt(token3.nextToken());

		StringTokenizer token1 = new StringTokenizer(b.readLine(), ",");
		x = this.readDouble(token1.nextToken());
		y = this.readDouble(token1.nextToken());
		angle = this.readDouble(token1.nextToken());

		lenmax = this.readDouble(b.readLine());

		StringTokenizer token2 = new StringTokenizer(b.readLine(), ",");
		plus = this.readDouble(token2.nextToken());
		minus = this.readDouble(token2.nextToken());


		//未知なら0 既知なら1
		alk = readKnow(b.readLine());

		this.check(n, landp, landm, x, y, lenmax, plus, minus);

		b.close();

		if (!(new File(mapname).exists())) {
			ConsoleFrame.getInstance().println(mapname);
			//ConsoleFrame.getInstance().println(new File(".").getAbsoluteFile().getParent());
			//拡張子は正しいが、ファイルが存在しない
			throw new FileNotFoundException();
		}
	}

	/**
	 * 初期位置が既知かどうかを返す
	 * @param readLine ファイルから読み込んだ値
	 * @return 既知ならtrue,そうでなければfalse
	 */
	private boolean readKnow(String readLine) {
		if(readLine.equals("0")) {
			return false;
		}

		if(readLine.equals("1")) {
			return true;
		}
		return false;
	}

	/**
	 * 拡張子が「.csv」であるか判断
	 * @param fileName ファイル名
	 * @return csvならtrue,そうでなければfalse
	 */
	public boolean getSuffixCSV(String fileName) {
		String st;
		if (fileName == null) {
			return false;
		}

		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			//拡張子の取得
			st= fileName.substring(point + 1);
		}
		else return false;

		if(!st.equals("csv")) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * 拡張子が「.png」であるか判断
	 * @param fileName ファイル名
	 * @return pngならtrue,そうでなければfalse
	 */
	public boolean getSuffixImage(String fileName) {
		String st;
		if (fileName == null) {
			return false;
		}
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			//拡張子の取得
			st = fileName.substring(point + 1);
		}
		else {
			return false;
		}

		if(!st.equals("png")) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * Stringをintに変換して返す
	 * @param n intに変換したいString
	 * @return 変換したint
	 * @throws NumberFormatException
	 */
	private int readInt(String n) throws NumberFormatException {
		int number;

		number = Integer.parseInt(n);

		return number;
	}

	/**
	 * Stringをdoubleに変換して返す
	 * @param n doubleに変換したいString
	 * @return 変換したdouble
	 */
	private double readDouble(String n) {
		double number;

		try {
			number = Double.valueOf(n);
		} catch(NumberFormatException e) {
			number = -1;
		}

		return number;
	}

	/**
	 * 地図のパスを返す
	 * @return 地図のパス
	 */
	public String getMapname() {
		return mapname;
	}

	/**
	 * パーティクルの数を返す
	 * @return パーティクルの数
	 */
	public int getN() {
		return n;
	}

	/**
	 * 左方向の目印の個数を返す
	 * @return 左方向の目印の個数
	 */
	public int getLandp() {
		return landp;
	}

	/**
	 * 右方向の目印の個数を返す
	 * @return 右方向の目印
	 */
	public int getLandm() {
		return landm;
	}

	/**
	 * ロボットのx座標を返す
	 * @return ロボットのx座標
	 */
	public double getX() {
		return x;
	}

	/**
	 * ロボットのy座標を返す
	 * @return ロボットのy座標
	 */
	public double getY() {
		return y;
	}

	/**
	 * ロボットの角度を返す
	 * @return ロボットの角度
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * センサーの可視距離を返す
	 * @return センサーの可視距離
	 */
	public double getLenmax() {
		return lenmax;
	}

	/**
	 * 左最大旋回角度を返す
	 * @return 左最大旋回角度
	 */
	public double getPlus() {
		return plus;
	}

	/**
	 * 右最大旋回角度を返す
	 * @return 右最大旋回角度
	 */
	public double getMinus() {
		return minus;
	}

	/**
	 * 初期位置が既知かどうかを返す
	 * @return 既知ならtrue,そうでないならfalse
	 */
	public boolean getKnow() {
		return alk;
	}

	/**
	 * 値が正常値か確認する
	 * @param n
	 * @param estp
	 * @param estm
	 * @param x
	 * @param y
	 * @param lenmax
	 * @param plus
	 * @param minus
	 */
	public void check(int n, int estp, int estm, double x, double y, double lenmax, double plus, double minus) {

		if(n<=0) {
			throw new NumberFormatException();
		}

		if(estp<0) {
			throw new NumberFormatException();
		}

		if(estm<0) {
			throw new NumberFormatException();
		}

		if((estp+estm)<=0) {
			throw new NumberFormatException();
		}

		if(x<0) {
			throw new NumberFormatException();
		}

		if(y<0) {
			throw new NumberFormatException();
		}

		if(lenmax<=0) {
			throw new NumberFormatException();
		}

		if(plus<0) {
			throw new NumberFormatException();
		}

		if(minus<0) {
			throw new NumberFormatException();
		}
	}
}
