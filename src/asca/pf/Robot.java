package asca.pf;

import asca.pf.map.Map;
import asca.pf.map.WorldMap;

/**
 * ロボットクラス<br>
 * ロボットは地図情報を持っているものとする
 * @author asuka
 */
public class Robot {
	/**
	 * 初期位置のx座標
	 */
	private double x;

	/**
	 * 初期位置のy座標
	 */
	private double y;

	/**
	 * ロボットが進む角度
	 */
	private double angle=0;


	private WorldMap map;

	/**
	 * 目印
	 */
	private Map[] landmark;

	/**
	 * 赤外線センサーが届く最大距離
	 */
	private double maxlen;

	/**
	 * 赤外線センサーを動かす角度（要するに首が回れる角度）
	 */
	private double plus,minus;

	private Singleton s;

	/**
	 * 追加した属性
	 */
	private int landp, landm;

	/**
	 * コンストラクタ
	 * @param x
	 * @param y
	 * @param angle
	 * @param map
	 */
	public Robot(double x, double y, double angle, WorldMap map/*, int landlen, double maxlen, double plus, double minus, int estp, int estm*/) {
		this.setAll(x, y, angle);
		this.map = map;

		s = Singleton.getInstance();

		landmark = new Map[s.getLandlen()];
		for(int i = 0; i < landmark.length; i++) {
			landmark[i] = new Map(0, 0, false);
		}

		this.maxlen = s.getLenmax();
		this.plus = s.getPlus();
		this.minus = s.getMinus();
		this.landp = s.getLandp();
		this.landm = s.getLandm();
	}

	/**
	 * 超音波センサーの動き
	 * @return
	 */
	public boolean useSensor() {

		for(int i = 0; i < landmark.length; i++)
			landmark[i] = new Map(0, 0, false);

		//超音波センサーが届く最大の距離を指定する
		double x,y;
		//int count = 0;
		int cp = 0, cm = 0;


		/*
		 * 0～plus度,360～360-minus度間でそれぞれ
		 * estp,estm個の目印を取得できる
		 */


		for(double a = 0; a <= plus;a = a + 1) {
			if(cp > landp - 1/*(landmark.length) / 2*/) {
				break;
			}

			for(double len = 0.1; len < maxlen; len = len + 1.0) {

				x = this.getX() + len * (Math.cos(Math.toRadians(a) + this.getAngle()));
				y = this.getY() + len * (Math.sin(Math.toRadians(a) + this.getAngle()));

				double tmpx = x;
				double tmpy = y;

				x = tmpx % map.getworldsizex();
				y = tmpy % map.getworldsizey();

				//x %= map.getworldsizex();
				//y %= map.getworldsizey();
				if(x < 0) {
					x = map.getworldsizex() + x;
				}
				if(y < 0) {
					y = map.getworldsizey() + y;
				}

				if(map.getwall((int)(x % map.getworldsizex()), (int)(y % map.getworldsizey()))) {
					//目印が見つかった

					boolean b = true;
					//目印が重複しているか確認する
					for(int i = 0; i < cp; i++){
						if(((int)x == landmark[i].getx()) && ((int)y == landmark[i].gety())) {
							b = false;
						}
					}

					//目印が重複していない場合
					if(b) {
						landmark[cp].setx(tmpx/*map.getx((int)x, (int)y)*/);
						landmark[cp].sety(tmpy/*map.gety((int)x, (int)y)*/);
						landmark[cp].setwall(true);

						cp++;
					}
					break;//同角度の別座標の目印の取得を防ぐため
				}
			}
		}

		for(double a = 360; a >= (360 - minus); a = a - 1) {
			if(cm > landm - 1/* > landmark.length - 1*/) {
				break;
			}
			for(double len = 0.1; len < maxlen; len = len + 1.0) {

				x = this.getX() + (len * (Math.cos(Math.toRadians(a) + this.getAngle())));
				y = this.getY() + (len * (Math.sin(Math.toRadians(a) + this.getAngle())));

				double tmpx = x;
				double tmpy = y;

				x = tmpx % map.getworldsizex();
				y = tmpy % map.getworldsizey();

				/*x %= map.getworldsizex();
				y %= map.getworldsizey();*/

				if(x < 0) {
					x = map.getworldsizex() + x;
				}
				if(y < 0){
					y = map.getworldsizey() + y;
				}

				if(map.getwall((int)(x % map.getworldsizex()), (int)(y % map.getworldsizey()))) {
					//目印が見つかった

					boolean b = true;
					//目印が重複しているか確認する
					for(int i = 0;i < (cp + cm); i++) {
						if(((int)x == (landmark[i].getx())) && ((int)y == (landmark[i].gety()))) {
							b = false;
						}
					}

					//目印が重複していない場合
					if(b) {
						landmark[cp + cm].setx(tmpx/*map.getx((int)x, (int)y)*/);
						landmark[cp + cm].sety(tmpy/*map.gety((int)x, (int)y)*/);
						landmark[cp + cm].setwall(true);

						cm++;
					}
					break;//同角度の別座標の目印の取得を防ぐため
				}
			}
		}

		if((cp + cm) > (landmark.length - 1)) {
			//目印が十分集まった
			return true;
		}
		return false;
	}

	/**
	 * ロボットが取得した目印を取得
	 * @return ロボットが取得した目印
	 */
	public Map[] getLandmark() {
		return landmark;
	}

	/**
	 * ロボットの位置（x座標）を取得
	 * @return ロボットの位置（x座標）
	 */
	public double getX() {
		return x;
	}

	/**
	 * ロボットの位置（y座標）を取得
	 * @return ロボットの位置（y座標）
	 */
	public double getY() {
		return y;
	}

	/**
	 * ロボットの向きを取得
	 * @return ロボットの向き
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * 左最大旋回角度を返す
	 * @return 左最大旋回角度
	 * @return
	 */
	public double getPlus() {
		return plus;
	}

	/**
	 * 右最大旋回角度を返す
	 * @return 右最大旋回角度
	 */
	public double getMinus() {
		return minus;
	}

	/**
	 * センサーの可視距離を返す
	 * @return センサーの可視距離
	 */
	public double getMaxlen() {
		return maxlen;
	}

	/**
	 * ロボットの位置を更新する
	 * @param x 更新するx座標
	 * @param y 更新するy座標
	 * @param angle 更新する角度
	 */
	public void setAll(double x, double y, double angle) {
		setX(x);
		setY(y);
		setAngle(angle);
	}

	/**
	 * ロボットの位置（x座標）を更新する
	 * @param x 更新するx座標
	 */
	private void setX(double x) {
		this.x = x;
	}

	/**
	 * ロボットの位置（y座標）を更新する
	 * @param x 更新するx座標
	 */
	private void setY(double y) {
		this.y = y;
	}

	/**
	 * ロボットの位置（角度）を更新する
	 * @param x 更新するx座標
	 */
	private void setAngle(double angle) {
		this.angle = angle;
	}
}
