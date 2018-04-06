package com.project4;

import java.util.Map;

public class Packet {
	private int id;
	private int sequenceNumber;
	private int TTL;
	private Map<String, Integer> costMap;
	
	public Packet(int id, int sequenceNumber) {
		super();
		this.id = id;
		this.sequenceNumber = sequenceNumber;
		// Use 10 as initial value
		this.TTL = 10;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public int getTTL() {
		return TTL;
	}
	public void setTTL(int tTL) {
		TTL = tTL;
	}
	public Map<String, Integer> getCostMap() {
		return costMap;
	}
	public void setCostMap(Map<String, Integer> costMap) {
		this.costMap = costMap;
	}
}
