package select;

import java.util.ArrayList;

import asca.pf.Particle;
import asca.pf.Robot;
import asca.pf.Singleton;
import asca.pf.map.WorldMap;
import asca.random.WrapMersenneTwister;

/**
 * 移動方向の点を選択
 */
public class Direction {
	/**
	 * 移動方向の点を選択
	 * @param particle
	 * @param robot
	 * @param prerobot
	 * @return
	 */
	public static int[] solution(Particle[] particle, Robot robot, Robot prerobot) {

		ArrayList<Integer> particleList = new ArrayList<Integer>();
		int leng = particle.length;

		double dir_angle = Math.toDegrees(robot.getAngle());

		double size = 1;
		double first_x = Math.cos(dir_angle) + size + robot.getX() - 10 - (size / 2);
		double first_y = Math.sin(dir_angle) + size + robot.getY() - (size / 2);
		double x = robot.getX() - 10 - (size / 2);
		double y = robot.getY() - (size / 2);

		WorldMap map = Singleton.getInstance().getMap();

		/*
		// 前進可能距離の大きさが、前進限界距離の大きさを超えた場合、ループ終了
		while(Math.abs(len) < Math.abs(forward)) {
			double dx = len * cos;
			dx = (act.getX() + dx + map.getworldsizex()) % map.getworldsizex();
			double dy = len * sin;
			dy = (act.getY() + dy + map.getworldsizey()) % map.getworldsizey();
			// 後退してたらsin,cosが逆になるように調整して調べる
			if(this.nearWall(cos * Math.signum(forward), sin * Math.signum(forward), dx, dy)) {
				// 衝突するなら衝突点一歩手前までの前進可能距離を戻す
				return (len == 0.0) ? 0.0 : len - inc;
			}
			// 前進可能距離を更新する
			len += inc;
		}
		return forward;
	}
		 */
		while(x <= map.getworldsizex() && y <= map.getworldsizey() && 0 <= x && 0 <= y) {
			for(int i = 0; i < leng; i++) {
				if(x <= particle[i].getX() && particle[i].getX() <= first_x) {
					if(y <= particle[i].getY() && particle[i].getY() <= first_y) {
						particleList.add(i);
					}
				}
			}
			x += Math.cos(dir_angle);
			y += Math.sin(dir_angle);
			first_x += Math.cos(dir_angle);
			first_y += Math.sin(dir_angle);

			//System.out.println(map.getworldsizex() + ","  + map.getworldsizey() + "," + x + "," + y);
		}

		int[] result = new int[particleList.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = particleList.get(i);
		}

		//System.out.println("pass");
		if(particleList.size() != 0) {
			int index = Math.abs(WrapMersenneTwister.getInstance().nextInt()) % particleList.size();
			Particle par = particle[result[index]];

			// ここの(- 10)はマジックナンバーでは？ by 鈴木
			robot.setAll(par.getX(), par.getY(), SelectSystem.robAngle(par.getX(), robot.getX() - 10, par.getY(), robot.getY()));
		}
		else {
			//該当なし
			System.out.println("none");
		}

		return result;
	}
}
