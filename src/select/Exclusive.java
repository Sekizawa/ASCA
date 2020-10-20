package select;

import java.util.ArrayList;

import asca.pf.Particle;

/**
 * 排他的論理和
 */
public class Exclusive {
	/**
	 * 排他的論理和
	 * @param particle 現在の配列
	 * @param preparticle 前回の配列 なくてもnullをいれて使用可
	 * @return 前回の配列と今回の配列の排他的論理和を返す
	 */
	public static int[] solution(Particle[] particle, Particle[] preparticle) {
		ArrayList<Integer> particleList = new ArrayList<Integer>();
		int[] par = null;

		if(preparticle != null) {
			for(int i = 0; i < particle.length; i++) {
				for(int j = 0; j < preparticle.length; j++)
					if(particle[i].getX() == preparticle[j].getX()) {
						if(particle[i].getY() == preparticle[j].getY()) {
							particleList.add(i);
						}
					}
			}

			/*int index = WrapMersenneTwister.getInstance().nextInt() % particleList.size();
			Particle par = particleList.get(index);*/

			par = new int[particleList.size()];
			for(int i = 0; i < particleList.size(); i++) {
				par[i] = particleList.get(i);
			}
		}
		else {
			par = new int[particle.length];
			for(int i = 0; i < particle.length; i++) {
				par[i] = i;
			}
		}

		return par;
	}
}