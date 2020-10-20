package asca.pf.gui;

import javax.swing.JFrame;

import asca.Setting;

/**
 * ASCAのフレームは全てこれを継承する
 *
 */
public class ASCAFrame extends JFrame {
	protected Setting setting = Setting.getInstance();

	/**
	 *
	 */
	public ASCAFrame() {
		this.setIconImage(setting.getIcon());
	}
}
