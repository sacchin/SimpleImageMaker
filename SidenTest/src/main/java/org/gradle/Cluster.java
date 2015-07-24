package org.gradle;

public class Cluster {
	
	public int rgb = 0;
	public int classTag = 0;
	public int count = 0;
	
	public Cluster(int rgb, int classTag, int count){
		this.rgb = rgb;
		this.classTag =  classTag;
		this.count = count;
	}

	public int getRgb() {
		return rgb;
	}

	public void setRgb(int rgb) {
		this.rgb = rgb;
	}

	public int getClassTag() {
		return classTag;
	}

	public void setClassTag(int classTag) {
		this.classTag = classTag;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String toString(){
		return "{rgb:" + rgb + ",classTag:" + classTag + ",count:" + count + "}";
	}

}
