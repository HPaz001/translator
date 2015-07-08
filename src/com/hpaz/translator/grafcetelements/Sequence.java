package com.hpaz.translator.grafcetelements;

import java.util.Collection;
import java.util.LinkedList;

public class Sequence {
	private int idSeq;
	private LinkedList<Object> list;// puede tener tanto transiciones como pasos
	private LinkedList<String> signals; 
	
	public Sequence() {
		list = new LinkedList<Object>();
		signals = new LinkedList<String>();
	}
	public int getIdSeq() {
		return idSeq;
	}
	public void setIdSeq(int id) {
		this.idSeq = id;
	}
	public LinkedList<Object> getList() {
		return list;
	}
	public LinkedList<String> getSignals() {
		return signals;
	}
	/**puede ser una transition o un step*/
	public void addTorS(Object pTorS) {
		this.list.add(pTorS);
	}
	
	public LinkedList<String> printSeqVG(){
		LinkedList<String> textAux= new LinkedList<String>();
		for (Object st : list) {/*puede ser una transition o un step*/
			if (st instanceof Transition){
				signals.addAll(((Transition) st).printTransVG());
			}else if(st instanceof Step){
				textAux.add(((Step) st).printStepVG());
				String action = (((Step) st).getAction());
				if(!action.equals("")){
					signals.add("\t"+((Step) st).getAction()+"\t: BOOL;\n");
				}
				
			}
		}
		return textAux;
		
	}
	/**Devuelve un listado con los set y reset correspondientes a cada objeto de la secuencia*/
	public LinkedList<String> getSetReset() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
