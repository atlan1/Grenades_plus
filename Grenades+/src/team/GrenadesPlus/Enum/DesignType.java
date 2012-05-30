package team.GrenadesPlus.Enum;

public enum DesignType {

	CUBE(6), PYRAMID(5), STEP(6);
	
	boolean attach = false;
	int faces = 0;
	
	private DesignType(int faces){
		this.faces = faces;
	}
	
	public int getFaces(){
		return faces;
	}
	
	public boolean isAttaching(){
		return attach;
	}
	
	public void setAttaching(boolean a){
		this.attach = a;
	}
}
