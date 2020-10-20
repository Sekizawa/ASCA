package asca.pf.gui;

import java.io.FileNotFoundException;
import java.io.IOException;

import asca.ConsoleFrame;
import asca.Setting;
import asca.pf.InstructionFileReader;
import asca.pf.InstructionManager;
import asca.pf.Particle;
import asca.pf.Robot;
import asca.pf.gui.interfaces.ControllerBase;
import asca.pf.map.WorldMap;

/**
 *
 */
public class ControlController implements ControllerBase {
	private Setting setting = Setting.getInstance();
	private ControlFrame contFrame;
	private ControlFileFrame contFileFrame;
	private HistoryFrame historyFrame;
	private InstructionManager manager;
	private InstructionFileReader reader;
	private SimFrame simFrame;

	private ConsoleFrame consFrame;

	/**
	 * コンストラクタ<br>
	 * 実行中のフレームを表示する
	 */
	public ControlController() {
		contFrame = new ControlFrame(this);
		contFrame.pack();
		contFrame.setBounds(setting.getSimFrameSize(), 0, contFrame.getWidth(), contFrame.getHeight());
		contFrame.setResizable(false);

		contFileFrame = new ControlFileFrame(this);
		contFileFrame.pack();
		contFileFrame.setBounds(setting.getSimFrameSize(), contFrame.getY() + contFrame.getHeight(), contFileFrame.getWidth(), contFileFrame.getHeight());
		contFileFrame.setResizable(false);

		historyFrame = new HistoryFrame(this);
		historyFrame.pack();
		historyFrame.setBounds(setting.getSimFrameSize(), contFileFrame.getY() + contFileFrame.getHeight(), historyFrame.getWidth(), historyFrame.getHeight());
		historyFrame.setResizable(false);

		reader = new InstructionFileReader();
		manager = new InstructionManager();

		simFrame = new SimFrame();
		simFrame.setBounds(0, 0, setting.getSimFrameSize(), setting.getSimFrameSize() + 40);
		simFrame.setResizable(false);

		consFrame = ConsoleFrame.getInstance();
		consFrame.setBounds(0, simFrame.getHeight(), setting.getConsFrameSize().width, (int)setting.getConsFrameSize().height);
		//consFrame.setResizable(false);
	}

	/**
	 * 距離と角度を取得し、キューが空の場合に処理を追加する
	 */
	public void control() {
		double forward = Double.valueOf(contFrame.getForward());
		double radian;

		if(this.getcomboA() == 0){
			radian = Math.toRadians(Double.valueOf(contFrame.getAngle()));
		}
		else{
			radian = Double.valueOf(contFrame.getAngle());
		}

		control(forward, radian);
	}

	/**
	 * 20181206
	 * キューが空の場合に処理を追加する
	 * @param forward
	 * @param radian
	 */
	public void control(double forward, double radian) {
		if(!manager.hasInstruction()) {
			manager.addInstruction(forward, radian);
		}
	}

	/**
	 *
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void readFile() throws FileNotFoundException, IOException {
		String path = contFileFrame.getPath();
		try {
			reader.read(path);
			contFileFrame.updateTable(reader.getSetList());
		}
		catch (FileNotFoundException e) {
			throw e;
		}
		catch (IOException e) {
			throw e;
		}
	}

	/**
	 * キューが空の場合に処理を追加する
	 */
	public void controlFile() {
		if(!manager.hasInstruction()) {
			for(int i = 0; i < reader.getSize(); i++) {
				manager.addInstruction(reader.getForward(i), reader.getAngleRad(i));
			}
		}
	}

	/**
	 *
	 */
	public boolean isInputting() {
		return !manager.hasInstruction();
	}

	/**
	 *
	 * @return
	 */
	public InstructionManager.Instruction getInstruction() {
		return manager.getInstruction();
	}

	/**
	 * フレームを表示する
	 */
	public void start() {
		consFrame.setVisible(true);
		contFrame.setVisible(true);
		contFileFrame.setVisible(true);
		simFrame.setVisible(true);
		historyFrame.setVisible(true);
	}

	/**
	 * FMIシミュレーションを開始するにあたって必要のないフレームを使用不可にする
	 */
	public void startFMI() {
		contFrame.setEnabled(false);
		contFileFrame.setEnabled(false);
	}

	/**
	 * 壁を考慮するかのコンボボックスの値を返す
	 * @return コンボボックスの値
	 */
	public int getcomboW() {
		return contFrame.getComboW();
	}

	/**
	 * 角度かラジアンかのコンボボックスの値を返す
	 * @return コンボボックスの値
	 */
	public int getcomboA() {
		return contFrame.getComboA();
	}

	/**
	 * 解の選択手法を返す
	 * @return
	 */
	public int getSelect() {
		return contFrame.getSelect();
	}

	/**
	 *
	 * @param data
	 * @param actual
	 * @param landmark
	 * @param est
	 * @param map
	 */
	public void draw(Particle[] data, Robot actual, double[][] landmark, double[] est, WorldMap map) {
		simFrame.draw(data, actual, landmark, est, map);
	}

	/**
	 *
	 * @param data
	 * @param actual
	 * @param est
	 * @param map
	 */
	public void draw(Particle[] data, Robot actual, double[] est, WorldMap map) {
		simFrame.draw(data, actual, est, map);
	}

	/**
	 * SimFrameに前のステップを表示する
	 */
	public void prev() {
		simFrame.prev();
	}

	/**
	 * SimFrameに次のステップを表示する
	 */
	public void next() {
		simFrame.next();
	}
}
