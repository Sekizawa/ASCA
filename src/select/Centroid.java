package select;
import asca.pf.Particle;
import asca.pf.Robot;

/**
 * 重心を選択する
 */
public class Centroid {
	/**
	 * 重心を選択する
	 * @param particle
	 * @param robot
	 */
	public static void solution(Particle[] particle, Robot robot){
		//パーティクルの配列の長さを取得
		int particle_len = particle.length;

		//パーティクルx,yの宣言と初期化
		double cent_x = 0;
		double cent_y = 0;

		//送られたデータ数繰り返し
		for(int i=0 ; i < particle_len; i++) {
			cent_x += particle[i].getX() / particle_len;
			cent_y += particle[i].getY() / particle_len;
		}

		robot.setAll(cent_x, cent_y, SelectSystem.robAngle(robot.getX(), cent_x, robot.getY(), cent_y));
	}
}
