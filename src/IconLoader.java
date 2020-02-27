import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


//REMEMBER TO KEEP ORDER IN getIcons() IN for LOOP, OTHERWISE ICONS WILL BE ASSIGNED TO WRONG VARIABLES

public class IconLoader {

	static ImageIcon background, temp0, temp1, temp2, temp3, sun, moon, cloud, sun_cloud, moon_cloud,
			sun_cloud_rain, moon_cloud_rain, cloud_snow, cloud_rain, sun_cloud_snow, moon_cloud_snow, wind, wind_N;

	private static ImageIcon[] resizedIcon = { background, temp0, temp1, temp2, temp3, sun, moon, cloud, sun_cloud, moon_cloud,
			sun_cloud_rain, moon_cloud_rain, cloud_snow, cloud_rain, sun_cloud_snow, moon_cloud_snow, wind, wind_N };


	public static void getIcons() {
		
		background = new ImageIcon("Icons/background.png");
		
		try {
			BufferedImage[] unresized = {ImageIO.read(new File("Icons/temp0.png")), ImageIO.read(new File("Icons/temp1.png")),
					ImageIO.read(new File("Icons/temp2.png")), ImageIO.read(new File("Icons/temp3.png")),

					ImageIO.read(new File("Icons/sun.png")), ImageIO.read(new File("Icons/moon.png")),
					ImageIO.read(new File("Icons/cloud.png")), ImageIO.read(new File("Icons/sun_cloud.png")),
					ImageIO.read(new File("Icons/moon_cloud.png")), ImageIO.read(new File("Icons/sun_cloud_rain.png")),
					ImageIO.read(new File("Icons/moon_cloud_rain.png")), ImageIO.read(new File("Icons/cloud_snow.png")),
					ImageIO.read(new File("Icons/cloud_rain.png")), ImageIO.read(new File("Icons/sun_cloud_snow.png")),
					ImageIO.read(new File("Icons/moon_cloud_snow.png")), ImageIO.read(new File("Icons/wind.png")),
					ImageIO.read(new File("Icons/wind_N2.png")) };

			for (int i = 0; i < unresized.length; i++) {
				Image resizedImage = unresized[i].getScaledInstance(96, 96, Image.SCALE_SMOOTH);
				resizedIcon[i] = new ImageIcon(resizedImage);
			}
			
			temp0 = resizedIcon[0];
			temp1 = resizedIcon[1];
			temp2 = resizedIcon[2];
			temp3 = resizedIcon[3];
			sun = resizedIcon[4];
			moon = resizedIcon[5];
			cloud = resizedIcon[6];
			sun_cloud = resizedIcon[7];
			moon_cloud = resizedIcon[8];
			sun_cloud_rain = resizedIcon[9];
			moon_cloud_rain = resizedIcon[01];
			cloud_snow = resizedIcon[11];
			cloud_rain = resizedIcon[12];
			sun_cloud_snow = resizedIcon[13];
			moon_cloud_snow = resizedIcon[14];
			wind = resizedIcon[15];
			wind_N = resizedIcon[16];
				

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}