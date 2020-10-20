package asca.pf;

import org.javafmi.wrapper.Simulation;

import asca.Setting;
import asca.pf.csv.CSVinput;
import asca.pf.csv.CSVparticle;
import asca.pf.csv.CSVrobot;
import asca.pf.gui.ControlController;
import asca.pf.gui.InputController;
import asca.pf.gui.interfaces.ControllerBase;
import asca.pf.map.ImageMap;
import asca.pf.map.WorldMap;
import select.Solution;

/**
 * パーティクルフィルタのメインクラス
 */
public class PFmain {
	private Setting setting = Setting.getInstance();

	private OutputRobotPlace orp = new OutputRobotPlace();

	/**
	 * FMUを用いた協調解析を行うか
	 */
	private boolean fmuSimulate = false;

	/**
	 * 協調解析の最大ステップ数
	 */
	private int stepMax = 10;

	/**
	 * 協調解析のステップ数
	 */
	private int stepCount = 0;

	private Solution solution = new Solution();

	/**
	 * コンストラクタ
	 */
	public PFmain() {
		PF();
	}

	/**
	 * パーティクルフィルタの実装<br>
	 * 外部からnewしてもPFmainを実行しても同じ動作をさせるためにメソッド化
	 */
	private void PF() {
		InputController incont = new InputController();

		// 初期データが入力されるまで待つ
		waiting(incont);

		// フレームを非表示にする
		incont.end();

		Singleton s = Singleton.getInstance();

		InputFile file = incont.getInput();

		// 粒子の数
		int n = file.getN();

		WorldMap map = new ImageMap(file.getMapname());

		s.setMap(map);
		s.setLandlen(file.getLandp() + file.getLandm());
		s.setLandp(file.getLandp());
		s.setLandm(file.getLandm());
		s.setLenmax(file.getLenmax());
		s.setPlus(file.getPlus());
		s.setMinus(file.getMinus());

		int landlen = s.getLandlen();

		Robot myrobot = new Robot(file.getX(), file.getY(), file.getAngle(), map);
		Particle[] result = new Particle[n];
		PFilter pf = new PFilter(n, map);

		if(file.getKnow()) {
			pf.newParticle(file.getX(), file.getY(), file.getAngle());
		}
		else {
			pf.newParticle();
		}
		LenAngle[] L = new LenAngle[landlen];

		Eval eval = new Eval(map.getworldsizex(), map.getworldsizey(), n);

		// 出力ファイルの初期化
		CSVparticle csvp = new CSVparticle(setting.getOutputPath() + "particle.csv");
		CSVinput csvi = new CSVinput(setting.getOutputPath() + "inputlog.csv");
		CSVrobot csvr = new CSVrobot(setting.getOutputPath() + "robot.csv");

		// 初期状態出力
		orp.outputPlace(myrobot.getX(), myrobot.getY());

		// ステップ0（初期設定）
		result = pf.getData();

		csvr.settingOut(myrobot, landlen);

		// 粒子の散り具合
		eval.evaluate(result, myrobot);

		// step0のパーティクルを出力
		csvp.particleOut(myrobot, result, n);

		ControlController cocont = new ControlController();

		// シミュレータ上に出力
		cocont.draw(result, myrobot, eval.getEst(), map);

		// フレームの表示
		cocont.start();

		// 操作を入力されるまで待機
		waiting(cocont);

		if(fmuSimulate) {
			cocont.startFMI();
		}

		// このループ1つが1ステップ
		while(true) {
			csvr.stepOut();
			csvr.positionOut(myrobot, true);

			InstructionManager.Instruction ins = cocont.getInstruction();
			double forward = ins.getForward();
			double angle = ins.getAngle();

			csvi.dataOut(forward, angle);

			//測定データを計算で求める
			pf.move(myrobot, forward, angle, cocont.getcomboW());

			//ロボットと粒子の初期位置を出力
			csvp.particleOut(myrobot, result, n);

			L = pf.calcLenAngle(myrobot);

			csvr.markOut(myrobot, L);

			//予測、尤度計算、復元抽出
			result = pf.doing(L, forward, angle);

			// 解の選択
			solution.simulation(result, myrobot, cocont.getSelect());

			orp.outputPlace(myrobot.getX(), myrobot.getY());

			//出力
			csvp.resultOut(myrobot, result, n);
			csvp.newLine();

			//粒子の散り具合
			eval.evaluate(result, myrobot);

			//シミュレータ上に出力
			cocont.draw(result, myrobot, pf.getLandmark(myrobot), eval.getEst(), map);

			csvr.positionOut(myrobot, false);

			if(fmuSimulate) {
				int startTime = 0;
				int stopTime = 1;
				double  stepSize = 1;
				Simulation test = new Simulation(setting.getFmuPath() + "ASCA.fmu");

				test.write("inputX").with(myrobot.getX());
				test.write("inputY").with(myrobot.getY());
				test.write("inputAngle").with(myrobot.getAngle());
				test.write("inputDist").with(ins.getForward());

				test.init(startTime, stopTime);

				for(int i = 0; i < (stopTime / stepSize); i++) {
					test.doStep(stepSize);
				}
				test.terminate();

				double tmpX = test.read("outputX").asDouble();
				double tmpY = test.read("outputY").asDouble();
				double tmpForward = Math.sqrt(Math.pow(tmpX - myrobot.getX(), 2) + Math.pow(tmpY - myrobot.getY(), 2));
				double tmpAngle = test.read("outputAngle").asDouble() - myrobot.getAngle();

				cocont.control(tmpForward, tmpAngle);

				if(++stepCount == stepMax) {
					break;
				}
			}
			else {
				// 操作が入力されるまで待機
				waiting(cocont);
			}
		}
	}

	/**
	 * waitingをひとまとめにする用のメソッド<br>
	 * 入力受付中は待機する
	 * @param c 対応するコントローラー
	 */
	private void waiting(ControllerBase c) {
		while(c.isInputting()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
