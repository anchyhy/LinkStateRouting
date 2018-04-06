package com.project4;

public class RoutingTable {
	private int networkId;
	private String networkName;
	private int cost;
	private String outgoingLink;
	
	public RoutingTable(int networkId, String networkName, int cost,
			String outgoingLink) {
		super();
		this.networkId = networkId;
		this.networkName = networkName;
		this.cost = cost;
		this.outgoingLink = outgoingLink;
	}
	public int getNetworkId() {
		return networkId;
	}
	public void setNetworkId(int networkId) {
		this.networkId = networkId;
	}
	public String getNetworkName() {
		return networkName;
	}
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getOutgoingLink() {
		return outgoingLink;
	}
	public void setOutgoingLink(String outgoingLink) {
		this.outgoingLink = outgoingLink;
	}
	
	
}
