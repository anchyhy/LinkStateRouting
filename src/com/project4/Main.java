package com.project4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Map<Integer, Router> map = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("/Users/shaoanqi/Desktop/network.dat"));
			String line = "";
			List<Router> connectedRouters = null;
			List<Integer> connectedRoutersCost = null;
			Router router = null;
			br.mark(1000);
			while((line = br.readLine()) != null) {
				if(!line.startsWith(" ")) {
					String[] routerParams = line.trim().split(" ");
					router = new Router(Integer.parseInt(routerParams[0]), routerParams[1]);
					map.put(router.getId(), router);
				}
			}
			br.reset();
			while((line = br.readLine()) != null) {
				if(line.startsWith(" ")) {
					String[] routerParams = line.trim().split(" ");
					connectedRouters.add(map.get(Integer.parseInt(routerParams[0])));
					connectedRoutersCost.add(Integer.parseInt(routerParams[1]));
				} else {
					String[] routerParams = line.trim().split(" ");
					router = map.get(Integer.parseInt(routerParams[0]));
					connectedRouters = router.getConnectedRouters();
					connectedRoutersCost = router.getConnectedRoutersCost();
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		System.out.println("File loaded! Would you like to continue?");
		Scanner s = new Scanner(System.in);
		char flag = 'D';
		String input = "";
		while(true) {
			System.out.println("==============================================================");
			System.out.println("Continue: press \"C\"\nQuit: press \"Q\"\nPrint the routing table of a router: press \"P\" followed by the router's id number\nShut down a router : press \"S\" followed by the router's id number\nStart up a router : press \"T\" followed by the router's id number");
			System.out.println("==============================================================");
			if(flag != 'C') {
				input = s.next().toUpperCase();
				flag = input.charAt(0);
			}
			switch(flag) {
			case 'C':
				break;
			case 'Q':
				s.close();
				return;
			case 'P':
				int id = Integer.parseInt(input.substring(1, input.length()));
				Router router = map.get(id);
				// Initialize the data
				for (Map.Entry<Integer, Router> entry : map.entrySet()) {
				    entry.getValue().initialize();
				}
				// Originate packets
			    router.originatePacket();
				calculateCompletedRoutingTable(router, map);
				List<RoutingTable> routingTables = router.getRoutingTables();
				for(RoutingTable routingTable : routingTables) {
					System.out.println(routingTable.getNetworkName() + "          " + routingTable.getCost() + "          " + router.getId() + routingTable.getOutgoingLink());
				}
				break;
			case 'S':
				id = Integer.parseInt(input.substring(1, input.length()));
				router = map.get(id);
				router.setActive(false);
				flag = 'C';
				continue;
			case 'T':
				id = Integer.parseInt(input.substring(1, input.length()));
				router = map.get(id);
				router.setActive(true);
				flag = 'C';
				continue;
			default:
				System.out.println("Invalid letter. Please try again!");
			}
			flag = 'D';
		}
	}

	private static void calculateCompletedRoutingTable(Router router, Map<Integer, Router> map) {
		List<RoutingTable> routingTables = router.getDirectelyLinkedRoutingTables();
		List<RoutingTable> newRoutingTables = new ArrayList<>();
		newRoutingTables.addAll(routingTables);
		for(RoutingTable routingTable : routingTables) {
			Router router2 = map.get(routingTable.getNetworkId());
			for(RoutingTable routingTable2 : router2.getDirectelyLinkedRoutingTables()) {
				RoutingTable newRoutingTable = new RoutingTable(routingTable2.getNetworkId(), routingTable2.getNetworkName(), routingTable.getCost() + routingTable2.getCost(), routingTable.getOutgoingLink() + routingTable2.getOutgoingLink());
				newRoutingTables.add(newRoutingTable);
			}
		}
		router.setRoutingTables(newRoutingTables);
	}
}
