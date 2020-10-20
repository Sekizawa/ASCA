package select;

import asca.Setting;
import asca.pf.Particle;
import asca.pf.Robot;
import asca.pf.csv.CSVparticle2;

/**
 * 解空間を選択するための基礎となるクラス
 */
public class Solution {
	private Particle[] preparticle = null;
	private Robot prerobot = null;

	//CSV追記用
	private static Setting setting = Setting.getInstance();
	//CSV追記〇１
	private static CSVparticle2 csv = new CSVparticle2(setting.getOutputPath() + "particle2.csv");

	/**
	 * ユーザーの指定する解選択を行う
	 * @param particle
	 * @param robot
	 * @param select
	 * @return
	 */
	public int[] simulation(Particle[] particle, Robot robot, int select) {
		int[] result = null;

		switch(select) {

		case 0://理想
			break;

		case 1://重心
			Centroid.solution(particle, robot);
			break;

		case 2://重心から遠い点
			prerobot = robot;
			Centroid.solution(particle, robot);
			Distcent.solution(particle, robot, prerobot);
			break;


		case 3://境界線上の点
			result = Boundary.solution(particle, robot, prerobot);
			break;

		case 4://移動方向の点
			result = Direction.solution(particle, robot, prerobot);
			break;

		case 5://排他的論理和となる点
			result = Exclusive.solution(particle, preparticle);
			Centroid.solution(SelectSystem.samplingParticle(particle, result), robot);
			break;

		case 6://単方向に選択
			result = Unidirectional.solution(particle, robot, prerobot);
			break;

		case 7://象限方向を選択
			Centroid.solution(particle, robot);
			result = Quadrant.solution(particle, robot, prerobot);
			break;

		case 8://解空間を縮小した点
			result = Narrow.solution(particle, robot, prerobot);
			break;
		}

		this.preparticle = particle; //最後
		this.prerobot = robot;
		//return new Robot(0, 0, 0, null);

		csv.particleOut(robot, particle, result);

		return result;
	}
}
