package br.com.qualiti.bolinha;


public class CustomViewEvento extends java.util.EventObject {
	private Object data;
	
	public CustomViewEvento(CustomView source, Object data) {
		super(source);
		this.data = data;
	}
	
	
	public Object getData(){
		return data;
	}
}