package asca;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

import javax.imageio.ImageIO;

/**
 * ASCAの設定クラス<br>
 * パスの計算やディレクトリの作成を行う
 */
public class Setting {
	private static Setting instance = new Setting();

	/**
	 * システムのファイル区切り記号
	 */
	private String fileSeparator;

	/**
	 * 基盤となるディレクトリのパス
	 */
	private String basePath;

	/**
	 * ディスプレイサイズ
	 */
	private Dimension displaySize;

	/**
	 * ASCAで使用するディレクトリのパス情報<br><br>
	 * 0:imageディレクトリ<br>
	 * 1:inputディレクトリ<br>
	 * 2:input/settingディレクトリ<br>
	 * 3:input/simulateディレクトリ<br>
	 * 4:outputディレクトリ
	 */
	private String[] dirPath;

	/**
	 * ASCAのアイコン
	 */
	private BufferedImage icon;

	/**
	 * コンストラクタ
	 */
	private Setting() {
		// システム情報取得
		fileSeparator = System.getProperty("file.separator");

		// ディスプレイサイズの取得
		displaySize = Toolkit.getDefaultToolkit().getScreenSize();

		setBasePath();

		// 本ソフトウェアで使用するディレクトリのパス
		String[] tmpPath = {
				this.getImagePath(),
				this.getInputPath(),
				this.getFmuPath(),
				this.getSettingPath(),
				this.getSimulatePath(),
				this.getOutputPath()
		};
		dirPath = tmpPath;

		// 本ソフトウェアで使用するディレクトリの作成
		this.createDir();

		// ASCAアイコンの取得
		try {
			URL url = this.getClass().getResource("/ASCA.png");

			icon = ImageIO.read(url);
		}
		catch(Exception error) {
			icon = null;
		}
	}

	/**
	 * インスタンスを返す
	 * @return Settingクラスのインスタンス
	 */
	public static Setting getInstance() {
		return instance;
	}

	/**
	 * 基盤となるディレクトリパスの取得<br>
	 * 1. jarファイルを実行したときはjarファイルと同一のディレクトリのパス<br>
	 * 2. クラスファイルを実行したときはbinの親ディレクトリのパス<br>
	 * 3. Eclipseで実行したときはbinの親ディレクトリのパス
	 */
	private void setBasePath() {
		try {
			basePath = URLDecoder.decode(this.getClass().getResource("/").getPath(), "utf-8");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// ファイルの区切り記号を実行環境に合わせる
		if(fileSeparator.equals("\\")) {
			basePath = basePath.replaceAll("/", "\\\\");
		}
		else {
			basePath = basePath.replaceAll("\\\\", "/");
		}

		/*
		 * jarファイル以外の実行の場合、クラスファイルが格納されたディレクトリのパスになる
		 * 現在、クラスファイルを格納するディレクトリ名が「bin」であり、その親ディレクトリを指定する
		 */
		String classDirName = "bin" + fileSeparator;
		if(basePath.lastIndexOf(classDirName) == basePath.length() - classDirName.length()) {
			basePath = basePath.substring(0, basePath.length() - 4);
		}
	}

	/**
	 * ファイルパスの区切り記号を返す
	 * @return ファイルパスの区切り記号
	 */
	public String getFileSeparator() {
		return fileSeparator;
	}

	/**
	 * 基盤となるディレクトリのパスを返す
	 * @return 基盤となるディレクトリのパス
	 */
	public String getBasePath() {
		return basePath;
	}

	/**
	 * imageディレクトリのパスを返す
	 * @return imadeディレクトリのパス
	 */
	public String getImagePath() {
		return this.getBasePath() + "image" + this.getFileSeparator();
	}

	/**
	 * inputディレクトリのパスを返す
	 * @return inputディレクトリのパス
	 */
	public String getInputPath() {
		return this.getBasePath() + "input" + this.getFileSeparator();
	}

	/**
	 * fmuディレクトリのパスを返す
	 * @return input/fmuディレクトリのパス
	 */
	public String getFmuPath() {
		return this.getInputPath() + "fmu" + this.getFileSeparator();
	}

	/**
	 * settingディレクトリのパスを返す
	 * @return input/settingディレクトリのパス
	 */
	public String getSettingPath() {
		return this.getInputPath() + "setting" + this.getFileSeparator();
	}

	/**
	 * simulateディレクトリのパスを返す
	 * @return input/simulateディレクトリのパス
	 */
	public String getSimulatePath() {
		return this.getInputPath() + "simulate" + this.getFileSeparator();
	}

	/**
	 * outputディレクトリのパスを返す
	 * @return outputディレクトリのパス
	 */
	public String getOutputPath() {
		return this.getBasePath() + "output" + this.getFileSeparator();
	}

	/**
	 * SimFrameのサイズを返す<br/>
	 * ディスプレイの縦半分を100単位で切り捨て、200足した大きさ
	 * @return SimFrameのサイズ
	 */
	public int getSimFrameSize() {
		return (int)((displaySize.getHeight() / 2) - ((displaySize.getHeight() / 2) % 100) + 200);
	}

	/**
	 * ConsoleFrameのサイズを返す
	 * @return ConsoleFrameのサイズ
	 */
	public Dimension getConsFrameSize() {
		int tmp = getSimFrameSize() / 3;
		return new Dimension(getSimFrameSize(), tmp);
	}

	/**
	 * ASCAで使用するディレクトリの作成
	 */
	public void createDir() {
		// ASCA.jarと同一ディレクトリに作成する（Eclipseの場合はプロジェクト直下）
		for(int i = 0; i < dirPath.length; i++) {
			File file = new File(dirPath[i]);
			file.mkdirs();
		}
	}

	/**
	 * ディスプレイの横幅を返す
	 * @return ディスプレイの横幅
	 */
	public int getDisplayWidth() {
		return displaySize.width;
	}

	/**
	 * ディスプレイの縦幅を返す
	 * @return ディスプレイの縦幅
	 */
	public int getDisplayHeight() {
		return displaySize.height;
	}

	/**
	 * ASCAのアイコンを返す
	 */
	public BufferedImage getIcon() {
		return icon;
	}
}
