package select;

import asca.pf.Particle;
import asca.pf.Robot;

/**
 * 重心から最も離れた点を選択する
 *
 */
public class Distcent {
	/**
	 * 重心位置から最も離れた座標を返す
	 * @param particle
	 * @param robot
	 * @param prerobot
	 */
	public static void solution(Particle[] particle, Robot robot, Robot prerobot){
		//パーティクルの配列の長さを取得
		int particle_len = particle.length;

		int data_num = 0;
		double data = 0;

		for(int i = 0; i < particle_len; i++) {
			double d = SelectSystem.distance(robot, particle[i]);

			data = Math.max(d, data);
			//更新を確認する
			if(data == d) {
				data_num = i;
			}
		}

		robot.setAll(particle[data_num].getX(), particle[data_num].getY(), SelectSystem.robAngle(prerobot.getX(), particle[data_num].getX(), prerobot.getY(), particle[data_num].getY()));
	}
}
