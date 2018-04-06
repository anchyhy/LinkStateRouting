package com.project4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Router {
	private int id;
	private String name;
	private List<Router> connectedRouters;
	private List<Integer> connectedRoutersCost;
	private Packet lastLSP; 
	private int countLSPOriginated;
	private List<RoutingTable> routingTables;
	private List<RoutingTable> directelyLinkedRoutingTables;
	private boolean isActive;
	
	public Router(int id, String name) {
		super();
		this.id = id;
		this.name = name;
		connectedRouters = new ArrayList<>();
		connectedRoutersCost = new ArrayList<>();
		routingTables = new ArrayList<>();
		directelyLinkedRoutingTables = new ArrayList<>();
		countLSPOriginated = 0;
		isActive = true;
	}

	public void receivePacket(Packet packet) {
		if(!isActive)
			return;
		// 1. Decrement TTL
		int ttl = packet.getTTL();
		packet.setTTL(ttl-1);
		// 2. Judgement(1)
		if(packet.getTTL() == 0)
			return;
		// 2. Judgement(2)
		if(lastLSP != null && packet.getId() == lastLSP.getId() && packet.getSequenceNumber() >= lastLSP.getSequenceNumber())
			return;
		// 3. Send out
		send(packet);
	}
	
	private void send(Packet packet) {
		Map<String, Integer> costMap = packet.getCostMap();
		boolean haveSent = false;
		for(int i = 0; i < connectedRouters.size(); i++) {
			Router router = connectedRouters.get(i);
			if(!router.isActive)
				continue;
			String key = id + "->" + router.id;
			if(costMap.containsKey(key)) {
				RoutingTable routingTable = new RoutingTable(router.id, router.getName(), connectedRoutersCost.get(i), "->" + router.id);
				if (lastLSP == null)
					directelyLinkedRoutingTables.add(routingTable);
				router.receivePacket(packet);
				haveSent = true;
			}
		}
		if(haveSent)
			lastLSP = packet;
	}
	
	public void originatePacket() {
		if(!isActive)
			return;
		countLSPOriginated++;
		Packet packet = new Packet(id, countLSPOriginated);
		// A list that indicates each directly connected router, 
		// the network behind each one, and the cost to get to that router
		Map<String, Integer> costMap = new HashMap<>();
		for(int i = 0; i < connectedRouters.size(); i++) {
			costMap.put(id+"->"+connectedRouters.get(i).getId(), connectedRoutersCost.get(i));
			List<Router> connectedRouters2 = connectedRouters.get(i).getConnectedRouters();
			List<Integer> connectedRoutersCost2 = connectedRouters.get(i).getConnectedRoutersCost();
			for(int j = 0; j < connectedRouters2.size(); j++) {
				if(connectedRouters2.get(j).getId() == id)
					continue;
				costMap.put(connectedRouters.get(i).getId() + "->" + connectedRouters2.get(j).getId(), connectedRoutersCost2.get(j));
			}
		}
		packet.setCostMap(costMap);
		send(packet);
	}
	
	public void initialize() {
		lastLSP = null;
		routingTables = new ArrayList<>();
		directelyLinkedRoutingTables = new ArrayList<>();
		countLSPOriginated = 0;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Router> getConnectedRouters() {
		return connectedRouters;
	}
	public void setConnectedRouters(List<Router> connectedRouters) {
		this.connectedRouters = connectedRouters;
		// Set default values for link cost
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < connectedRouters.size(); i++) {
			list.add(1);
		}
		this.connectedRoutersCost = list;
	}
	public List<Integer> getConnectedRoutersCost() {
		return connectedRoutersCost;
	}

	public void setConnectedRoutersCost(List<Integer> connectedRoutersCost) {
		this.connectedRoutersCost = connectedRoutersCost;
	}

	public List<RoutingTable> getRoutingTables() {
		return routingTables;
	}

	public void setRoutingTables(List<RoutingTable> routingTables) {
		this.routingTables = routingTables;
	}

	public List<RoutingTable> getDirectelyLinkedRoutingTables() {
		return directelyLinkedRoutingTables;
	}

	public void setDirectelyLinkedRoutingTables(
			List<RoutingTable> directelyLinkedRoutingTables) {
		this.directelyLinkedRoutingTables = directelyLinkedRoutingTables;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
