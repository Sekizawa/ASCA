package select;

import asca.Setting;
import asca.pf.Particle;
import asca.pf.Robot;
import asca.pf.csv.CSVparticle3;
import asca.random.WrapMersenneTwister;

/**
 * 解空間から境界線上の点を除去し続け、最後に残った点からランダムに選択
 * @author Suzuki
 */
public class Narrow {
	private static Setting setting = Setting.getInstance();
	private static CSVparticle3 csv = new CSVparticle3(setting.getOutputPath() + "particle3.csv");

	/**
	 * 解空間の境界線にあるパーティクルの除去を繰り返して解を決める<br/>
	 * 【アルゴリズム】<br/>
	 * ①Boundaryクラスを用いて解空間の境界線上の点を得る<br/>
	 * ②解空間の配列から境界線上の点を除去<br/>
	 * ③残った解空間の境界線上の点をBoundaryクラスから得る<br/>
	 * ①～③を繰り返す<br>
	 * <br/>
	 * 
	 * 配列が0以下になるまで繰り返す（Boundaryクラスの仕様上0にならない場合があるため以下とする）<br/>
	 * ④最後に残った点からランダムで選択する
	 * @param particle 解空間
	 * @param robot 現在のロボットの状態
	 * @param prerobot 前のロボットの状態
	 * @return
	 */
	public static int[] solution(Particle[] particle, Robot robot, Robot prerobot) {
		// 参照による呼び出し元の改変を防ぐ
		Particle[] tmpParticle = particle.clone();

		int[] tmpIndex = null;

		// 解の候補が0になるまで繰り返す
		while(true) {
			// 境界線上のパーティクルを計算
			tmpIndex = Boundary.solution(tmpParticle, robot, prerobot);

			// 解の候補が0以下の場合、除去を停止して解を決定する
			if(tmpParticle.length - tmpIndex.length <= 0) {

				//除外するパーティクルの表示=>particle3.csv
				csv.particleOut(tmpParticle);

				break;
			}

			// それ以外の場合、解空間(tmpParticle)を更新する
			else {
				//除外するパーティクルの表示=>particle3.csv
				//csv.particleOut(SelectSystem.samplingParticle(tmpParticle, tmpIndex), newstep);
				tmpParticle = SelectSystem.samplingExclusiveParticle(tmpParticle, tmpIndex);
			}
		}

		// 残った解の候補からランダムで1つに決定する
		int index = Math.abs(WrapMersenneTwister.getInstance().nextInt()) % tmpParticle.length;
		Particle result = tmpParticle[index];

		// ロボットの状態更新
		// ここの(- 10)はマジックナンバーでは？ by 鈴木
		robot.setAll(result.getX(), result.getY(), SelectSystem.robAngle(robot.getX() - 10, result.getX(), robot.getY(), result.getY()));

		return null;
	}
}
