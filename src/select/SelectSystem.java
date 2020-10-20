package select;

import java.util.ArrayList;

import asca.pf.Particle;
import asca.pf.Robot;

/**
 * 解選択の複数個所で使用する可能性のある処理を記述する
 */
public class SelectSystem {
	/**
	 * パーティクルから指定されたインデックスのパーティクルを配列として返す
	 * @param particle 全体のパーティクル
	 * @param index 抜き出すパーティクルのインデックス
	 * @return 指定されたインデックスのパーティクル
	 */
	public static Particle[] samplingParticle(Particle[] particle, int[] index) {
		Particle[] result = new Particle[index.length];

		for(int i = 0; i < index.length; i++) {
			result[i] = particle[index[i]];
		}

		return result;
	}

	/**
	 * インデックスリストにないパーティクルを返す
	 * @param particle 全体のパーティクル
	 * @param list 抜き出さないインデックスリスト
	 * @return 抜き出されたパーティクル配列
	 */
	public static Particle[] samplingExclusiveParticle(Particle[] particle, int[] index) {
		boolean[] exBoolean = new boolean[particle.length];

		for(int i = 0; i < index.length; i++) {
			exBoolean[index[i]] = true;
		}

		ArrayList<Integer> list = new ArrayList<Integer>();

		for(int i = 0; i < exBoolean.length; i++) {
			if(!exBoolean[i]) {
				list.add(i);
			}
		}

		int[] exIndex = new int[list.size()];

		for(int i = 0; i < list.size(); i++) {
			exIndex[i] = list.get(i);
		}

		return samplingParticle(particle, exIndex);
	}

	/**
	 * (x1, y1)から(x2, y2)までの角度を求める
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @return
	 */
	public static double robAngle(double x1, double x2, double y1, double y2){
		double x = distance(x1, x2);
		double y = distance(y1, y2);

		double angle = Math.atan2(y, x);

		angle = angle * Math.PI / 180;
		return angle;
	}

	/**
	 * 座標r1から座標r2までの角度を求める
	 * @param r1
	 * @param r2
	 * @return
	 */
	public static double robAngle(Robot r1, Robot r2) {
		return robAngle(r1.getX(), r2.getX(), r1.getY(), r2.getY());
	}

	/**
	 * 直線上の2点の距離を求める
	 * @param x1
	 * @param x2
	 * @return
	 */
	public static double distance(double x1, double x2) {
		return Math.sqrt(Math.pow(x1 - x2, 2));
	}

	/**
	 * 2点間距離を求める
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @return
	 */
	public static double distance(double x1, double x2, double y1, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	/**
	 * 2点間距離を求める
	 * @param r1
	 * @param r2
	 * @return
	 */
	public static double distance(Robot r1, Robot r2) {
		return distance(r1.getX(), r2.getX(), r1.getY(), r2.getY());
	}
}
