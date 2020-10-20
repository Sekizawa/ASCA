package select;

import asca.pf.Particle;
import asca.pf.Robot;

/**
 *
 */
public class Unidirectional {
	/**
	 * 現在地から角度を指定して候補を探す配列
	 * @param particle
	 * @param robot
	 */
	public static int[] solution(Particle[] particle, Robot robot, Robot prerobot) {
		robot.setAll(robot.getX(), robot.getY(), Math.toRadians(0));

		return Direction.solution(particle, robot, prerobot);
	}
}
