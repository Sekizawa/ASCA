package select;

import asca.pf.Particle;
import asca.pf.Robot;
import asca.pf.Singleton;
import asca.pf.map.WorldMap;
import asca.random.WrapMersenneTwister;

/**
 * 境界線を選択する
 */
public class Boundary {
	/**
	 * 境界線を選択する
	 * @param particle
	 * @param robot
	 * @param prerobot
	 */
	public static int[] solution(Particle[] particle, Robot robot, Robot prerobot) {
		WorldMap map = Singleton.getInstance().getMap();

		double size = 1;

		// 各範囲の最大最小値のインデックス
		int[][] resultX = new int[(int)(map.getworldsizex() / size)][2];

		// 配列を-1で初期化
		for(int i = 0; i < resultX.length; i++) {
			resultX[i][0] = -1;
			resultX[i][1] = -1;
		}

		//System.out.println("start");

		for(int i = 0; i < particle.length; i++) {
			int range = (int)(particle[i].getX() / size);

			if(resultX[range][0] == -1 || resultX[range][1] == -1) {
				resultX[range][0] = i;
				resultX[range][1] = i;
			}
			else {
				//yの最大値
				if(particle[i].getY() > particle[resultX[range][0]].getY()) {
					resultX[range][0] = i;
				}
				//yの最小値
				if(particle[i].getY() < particle[resultX[range][1]].getY()) {
					resultX[range][1] = i;
				}
			}
		}

		int count = 0;

		for(int i = 0; i < resultX.length; i++) {
			for(int j = 0; j < 2; j++) {
				if(resultX[i][j] != -1) {
					count++;
				}
			}
		}

		int[] par = new int[count];


		int tmp = 0;
		for(int i = 0; i < resultX.length; i++) {
			for(int j = 0; j < 2; j++) {
				if(resultX[i][j] != -1) {
					par[tmp++] = resultX[i][j];
				}
			}
		}
		int index = Math.abs(WrapMersenneTwister.getInstance().nextInt()) % count;
		Particle result = particle[par[index]];

		/*ArrayList[]<Particle> particleList = new ArrayList[]<Particle>;

		for(int i=0;i < map.getworldsizex();i++) {
			for(int j=0;j < map.getworldsizey();j++) {
				double max;
				double min;

			}
		}

		Particle[] par = new Particle[particleList.size()];
		for(int i = 0; i < particleList.size(); i++) {
			par[i] = particleList.get(i);
		}*/

		// ここの(- 10)はマジックナンバーでは？ by 鈴木
		robot.setAll(result.getX(), result.getY(), SelectSystem.robAngle(robot.getX() - 10, result.getX(), robot.getY(), result.getY()));

		return par;
	}
}
