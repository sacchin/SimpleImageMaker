package org.gradle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

public class Main {
	public static final int CLUSTER = 3;
	
	public static void main(String[] args) {
		File file = new File("C:/Users/sacchin/Downloads/q.jpg");
		BufferedImage read;

		try{
			read = ImageIO.read(file);
			final int y = read.getHeight();
			final int x = read.getWidth();

			Map<Integer, Integer> rgbMap = new TreeMap<>();
			for (int i = 0; i < y; i++) {
				for (int j = 0; j < x; j++) {
					int rgb = read.getRGB(j, i);
					Integer count = rgbMap.get(rgb);
					if(count == null){
						rgbMap.put(rgb, 1);
					}else{
						rgbMap.put(rgb, count + 1);
					}
				}
			}

			Map<Integer, Integer> reverseMap = new TreeMap<>();
			for (Integer rgbValue : rgbMap.keySet()) {
				if(9 < rgbMap.get(rgbValue) && rgbValue < -2){
					reverseMap.put(rgbMap.get(rgbValue), rgbValue);
				}
			}

			BufferedImage write =
					new BufferedImage(10, reverseMap.keySet().size(), BufferedImage.TYPE_INT_RGB);
			int k = 0;
			for (Integer count : reverseMap.keySet()) {
				for (int i = 0; i < 10; i++) {
					write.setRGB(i,k,reverseMap.get(count));
				}
				k++;
			}
			File f2 = new File("C:/Users/sacchin/Downloads/ret.png");
			ImageIO.write(write, "png", f2);


			List<Cluster> clusters = new ArrayList<Cluster>();
			int classValue = 0;
			for (Integer count : reverseMap.keySet()) {
				Cluster c = new Cluster(reverseMap.get(count), classValue, count);
				clusters.add(c);
				classValue = (classValue==CLUSTER - 1) ? 0 : (classValue + 1);
			}

			int cou = 0;
			boolean needToUpdate = true;
			while(needToUpdate){
				List<Double> centers = calcCenters(clusters);
				needToUpdate = updateTag(clusters, centers);
				
				final int aaa = cou++;
				clusters.stream()
				.forEach(c -> System.out.println(aaa + ":" + c));
			}
			
			List<Cluster> delegates = new ArrayList<Cluster>();
			delegates.add(new Cluster(-1, 5, 0));
			for(int i = 0 ; i < CLUSTER ; i++){
				final int tag = i;
				Optional<Cluster> max = clusters.stream()
						.filter(c -> c.classTag == tag)
						.max(new Comparator<Cluster>() {
							@Override
							public int compare(Cluster o1, Cluster o2) {
								return (o1.getCount() - o2.getCount());
							}
						});
				if(max.isPresent()){
					delegates.add(max.get());
				}
			}
			
			
			BufferedImage writ2e =
					new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < y; i++) {
				for (int j = 0; j < x; j++) {
					final int rgb = read.getRGB(j, i);
					
					int minIndex = 0;
					double min = Double.MAX_VALUE;
					for (int l = 0; l < delegates.size(); l++) {
						double distance = Math.abs(rgb - delegates.get(l).getRgb());
						if(distance < min){
							min = distance;
							minIndex = l;
						}
					}
					writ2e.setRGB(j,i,delegates.get(minIndex).getRgb());
				}
			}

			File f3 = new File("C:/Users/sacchin/Downloads/ret2.png");
			ImageIO.write(writ2e, "png", f3);

			
		}catch(IOException e){

		}
	}

	private static List<Double> calcCenters(List<Cluster> clusters) {
		List<Double> centers = new ArrayList<Double>();
		for(int i = 0 ; i < CLUSTER ; i++){
			final int tag = i;
			double a = clusters.stream()
					.filter(c -> c.classTag == tag)
					.collect(Collectors.averagingInt(Cluster::getRgb));
			centers.add(a);
		}
		return centers;
	}

	private static boolean updateTag(List<Cluster> clusters, List<Double> centers) {
		if(centers == null || centers.size() < CLUSTER){
			return false;
		}
		
//		centers.stream()
//		.forEach(c -> System.out.print(c + ","));
//		System.out.println("\n");

		boolean needToUpdate = false;
		for(Cluster cl : clusters){
			int minIndex = 0;
			double min = Double.MAX_VALUE;
			for (int i = 0; i < centers.size(); i++) {
				double distance = Math.abs(cl.getRgb() - centers.get(i));
				if(distance < min){
					min = distance;
					minIndex = i;
				}
			}
			if(cl.getClassTag() != minIndex){
				needToUpdate = true;
			}
			cl.setClassTag(minIndex);
//			System.out.println(needToUpdate + ", " + minIndex);
		}
		return needToUpdate;
	}

	public static int a(int c){
		return c>>>24;
	}
	public static int r(int c){
		return c>>16&0xff;
	}
	public static int g(int c){
		return c>>8&0xff;
	}
	public static int b(int c){
		return c&0xff;
	}
	public static int rgb
	(int r,int g,int b){
		return 0xff000000 | r <<16 | g <<8 | b;
	}
	public static int argb
	(int a,int r,int g,int b){
		return a<<24 | r <<16 | g <<8 | b;
	}
}