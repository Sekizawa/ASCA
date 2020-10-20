package select;

import java.util.ArrayList;

import asca.pf.Particle;
import asca.pf.Robot;
import asca.random.WrapMersenneTwister;

/**
 *
 */
public class Quadrant {
	/**
	 *
	 * @param particle
	 * @param robot
	 * @param prerobot
	 */
	public static int[] solution(Particle[] particle, Robot robot, Robot prerobot) {
		ArrayList<Integer> particleList = new ArrayList<Integer>();

		// 本当はユーザーが選択するべきもの by 鈴木
		int select = 1;//象限の数字を入力

		for(int i = 0; i < particle.length; i++) {

			switch(select) {
			case 1:
				if(robot.getX() <= particle[i].getX()) {
					if(robot.getY() <= particle[i].getY()) {
						particleList.add(i);
					}
				}
				break;
			case 2:
				if(robot.getX() > particle[i].getX()) {
					if(robot.getY() <= particle[i].getY()) {
						particleList.add(i);
					}
				}
				break;
			case 3:
				if(robot.getX() > particle[i].getX() ) {
					if(robot.getY() > particle[i].getY()) {
						particleList.add(i);
					}
				}
				break;
			case 4:
				if(robot.getX() <= particle[i].getX() ) {
					if(robot.getY() > particle[i].getY()) {
						particleList.add(i);
					}
				}
				break;
			}
		}

		int[] par = new int[particleList.size()];
		for(int i = 0; i < particleList.size(); i++) {
			par[i] = particleList.get(i);
		}

		int count = particleList.size();
		int index = Math.abs(WrapMersenneTwister.getInstance().nextInt()) % count;
		Particle result = particle[par[index]];

		robot.setAll(result.getX(), result.getY(), SelectSystem.robAngle(robot.getX(), result.getX(), robot.getY(), result.getY()));

		return par;
	}
}