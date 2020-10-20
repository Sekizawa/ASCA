package asca.random;

/**
 * 乱数生成のテスト<br/>
 * ほぼ同時に精製した場合と、時間を空けた場合の乱数を比較する
 */
public class TestRandom {

	public static void main(String[] args) {

		// ほぼ同時にnew
		MersenneTwister rand1 = new MersenneTwister();
		MersenneTwister rand2 = new MersenneTwister();

		// 1秒待ってnew
		try {
			Thread.sleep(1000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		MersenneTwister rand3 = new MersenneTwister();

		// newして数値設定
		MersenneTwister rand4 = new MersenneTwister();
		rand4.setSeed(100);

		// 1秒待ってnewして数値設定
		try {
			Thread.sleep(1000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		MersenneTwister rand5 = new MersenneTwister();
		rand5.setSeed(100);


		for(int i = 0; i < 10; i++) {
			System.out.println(rand1.nextDouble());
		}

		System.out.println("------------------------");

		for(int i = 0; i < 10; i++) {
			System.out.println(rand2.nextDouble());
		}

		System.out.println("------------------------");

		for(int i = 0; i < 10; i++) {
			System.out.println(rand3.nextDouble());
		}

		System.out.println("------------------------");


		for(int i = 0; i < 10; i++) {
			System.out.println(rand4.nextDouble());
		}

		System.out.println("------------------------");

		for(int i = 0; i < 10; i++) {
			System.out.println(rand5.nextDouble());
		}
	}
}
