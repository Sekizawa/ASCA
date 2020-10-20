package asca.pf.gui;

import java.io.FileNotFoundException;
import java.io.IOException;

import asca.Setting;
import asca.pf.InputFile;
import asca.pf.gui.interfaces.ControllerBase;

/**
 *
 */
public class InputController implements ControllerBase {
	private InputFrame inputFrame;
	private InputFileFrame inputFileFrame;
	private InputFile file;

	private Setting setting = Setting.getInstance();

	/**
	 * コンストラクタ<br>
	 * 設定フレームを表示する
	 */
	public InputController() {
		inputFileFrame = new InputFileFrame(this);
		inputFileFrame.pack();
		inputFileFrame.setBounds(setting.getSimFrameSize(), 0, inputFileFrame.getWidth(), inputFileFrame.getHeight());
		inputFileFrame.setResizable(false);

		inputFrame = new InputFrame(this);
		inputFrame.pack();
		inputFrame.setBounds(setting.getSimFrameSize(), inputFileFrame.getY() + inputFileFrame.getHeight(), inputFrame.getWidth(), inputFrame.getHeight());
		inputFrame.setResizable(false);

		file = null;
	}

	/**
	 * InputFrameからデータを読み込む
	 * @throws FileNotFoundException
	 */
	public void inputData() throws FileNotFoundException {
		String mappath = inputFrame.getPath();
		try {
			int particle = Integer.parseInt(inputFrame.getParticle());
			double x = Double.valueOf(inputFrame.getRobotx());
			double y = Double.valueOf(inputFrame.getRoboty());
			double r = Double.valueOf(inputFrame.getRobotr());
			int landplus = Integer.parseInt(inputFrame.getLandplus());
			int landminus = Integer.parseInt(inputFrame.getLandminus());
			double senslen = Double.valueOf(inputFrame.getSenslen());
			double sensplus = Double.valueOf(inputFrame.getSensdegplus());
			double sensminus = Double.valueOf(inputFrame.getSensdegminus());
			boolean a = inputFrame.isKnownRobotCoordinate();
			InputFile infile = new InputFile();
			infile.readFile(mappath, particle, x, y, r, senslen, sensplus, sensminus, landplus, landminus, a);
			setFile(infile);
		}
		catch (NumberFormatException e) {
			throw e;
		}
		catch (FileNotFoundException e) {
			throw e;
		}
	}

	/**
	 * ファイルからデータを読み込む<br>
	 * ファイルはInputFileで読み込む
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void inputFile() throws FileNotFoundException, IOException {
		String path = inputFileFrame.getPath();
		InputFile infile = new InputFile();
		try {
			infile.readFile(path);
			setFile(infile);
		}
		catch (FileNotFoundException e) {
			throw e;
		}
		catch (IOException e) {
			throw e;
		}
	}

	/**
	 *
	 * @param infile
	 */
	synchronized private void setFile(InputFile infile) {
		if(file == null) {
			file = infile;
		}
	}

	/**
	 *
	 */
	public boolean isInputting() {
		return (file == null) ? true : false;
	}

	/**
	 *
	 * @return
	 */
	public InputFile getInput() {
		return file;
	}

	/**
	 * フレームを非表示にする
	 */
	public void end() {
		inputFrame.setVisible(false);
		inputFileFrame.setVisible(false);
	}

	/**
	 *
	 * @return
	 */
	public boolean isKnownRobotCoordinate() {
		return inputFrame.isKnownRobotCoordinate();
	}
}
