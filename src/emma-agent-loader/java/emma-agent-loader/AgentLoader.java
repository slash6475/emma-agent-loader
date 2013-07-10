import java.io.*;
import java.util.Arrays;

import java.net.URI;
import java.net.URISyntaxException;

import ch.ethz.inf.vs.californium.coap.GETRequest;
import ch.ethz.inf.vs.californium.coap.POSTRequest;
import ch.ethz.inf.vs.californium.coap.PUTRequest;
import ch.ethz.inf.vs.californium.coap.Request;
import ch.ethz.inf.vs.californium.coap.Response;
import ch.ethz.inf.vs.californium.coap.Option;
import ch.ethz.inf.vs.californium.coap.BlockOption;
import ch.ethz.inf.vs.californium.coap.registries.OptionNumberRegistry;


public class AgentLoader {

	/*
	 * Application entry point.
	 * 
	 */	
	 
	private static final int MAX_BUFFER = 5;
	 
	 
	// Program entry point
	public static void main(String args[]) {
		
		System.out.println("|====================================|");
		System.out.println("|Resource Loader for EMMA application|");
		System.out.println("|====================================|");

		// Variables
		URI uri = null; // URI parameter of the request
		String uriString="";
		String file="./LOAD.txt";
		char[] buffer = new char[MAX_BUFFER];
		String agent = "";
		String temp = "";
		Request request;
		Response response;
		//String payload = "";
		
		// Analyze input parameters
		if (args.length > 0) {
			// input URI from command line arguments
			uriString = args[0];
			try {
				uri = new URI(uriString);
			} catch (URISyntaxException e) {
				System.err.println("Invalid URI: " + e.getMessage());
				System.exit(-1);
			}
			
			// input file from command line arguments
			if (args.length > 1) {
				file = args[1];
			}
		
			// Read file and load agent
			/*try {
				System.out.println("Loading agent '" + file + "'");
				// Open file for read operation
				InputStream ips = new FileInputStream(file);
				InputStreamReader ipsr = new InputStreamReader(ips);
				BufferedReader br = new BufferedReader(ipsr);
				int nbCharRead = 0;
			
				// Save agent in RAM
				do {
					nbCharRead = br.read(buffer, 0, MAX_BUFFER);
					if (nbCharRead > 0)
					{
						temp = new String(buffer, 0, nbCharRead);
						agent += temp;
					}
				} while (nbCharRead == MAX_BUFFER);
				agent = agent.substring(0, agent.length()-1);
			
				System.out.println("Retrieved agent (" + agent.length() + " bytes): \"" + agent + "\"");
			
			} catch (Exception e) {
				System.out.println("Error while loading agent:");
				System.out.println(e.toString());
			}*/
			System.out.println("Loading file '" + file + "'");
			Agent eAgent = new Agent();
			Resource eResource = new Resource();
			eAgent.setOutput(System.out);
			eResource.setOutput (System.out);
			try {
				InputStream ips = new FileInputStream(file);
				eAgent.set(ips);
				ips.close();
				
				ips = new FileInputStream(file);
				eResource.set(ips);
				ips.close();
			} catch (Exception e) {
				System.out.println("Error while loading agent from '"+ file +"' :");
				System.out.println(e.toString());
				System.exit(-1);
			}
			
			if (eAgent.isValid())
			{
				System.out.println("Agent loaded:\n" + eAgent.toString());
				eAgent.send(uriString);
			}
			else if (eResource.isValid())
			{
				System.out.println("Resource loaded:\n" + eResource.toString());
				eResource.send(uriString);
			}
			else System.out.println("[COAP LOADER] [ERROR] Invalid input file '"+ file + "'.");
			
			//System.out.println("Convert back to JSON: " + new String(eAgent.getRaw()));
			
			// Create agent on node
			/*request = new POSTRequest();
			try {
				request.setURI(new URI(uri + "/A?name=" + eAgent.getNAME()));
				response = AgentLoader.send(request);
				if (response != null) response.prettyPrint();
			} catch (Exception e) {
				System.out.println("[AGENT LOADER] Invalid URI.");
				System.exit(-1);
			}
			
			// Send agent in a block-wise style
			try {
				AgentLoader.sendBlock(new URI(uri + "/A/" + eAgent.getNAME()), eAgent.getRaw());
				//request.setURI(new URI(uri + "/A?name=" + eAgent.getNAME()));
				//response = AgentLoader.send(request);
				//if (response != null) response.prettyPrint();
			} catch (Exception e) {
				System.out.println("[AGENT LOADER] Invalid URI.");
				System.exit(-1);
			}*/
			
			
		
			// Check agent string
			//System.out.print("\n[AGENT LOADER] [WARNING] Agent check method not implemented.\n\n");
		
			// Check if agent exists on node, create if not
			//request = new POSTRequest();
			//request.setURI(uri);
			//payload += ("?name=" + );
			//resquest.setPayload();
		
			// Send agent to URI in a block-wise style
			/*int blockNumber = 0;
			boolean blockMore = true;
			int blockSize = 16;
			int payloadSize = agent.length();
			char[] payload = new char[payloadSize];
		
			agent.getChars(0, payloadSize, payload, 0);
			BlockOption bo = new BlockOption(OptionNumberRegistry.BLOCK1, blockNumber, blockSize, blockMore);
		
			// Send chunks, refresh (blockNumber, blockMore, block1 in request)
			// First try to send two chunks, verify that there are no errors on node (not enough memory, etc)
			// Display node errors if any
		
			String payload1 = "Part 1";
			String payload2 = "Part 2";
			Response response;
			Option bopt;
			Request request = new PUTRequest();
			request.setURI(uri);
			bopt = new BlockOption(OptionNumberRegistry.BLOCK1, 0, 16, true);
			System.out.println("Part 1 block options: " + bopt.toString());
			request.setOption(bopt);
			request.setPayload(payload1.getBytes());
			request.enableResponseQueue(true);
		
			//request.prettyPrint();
			// Send request
			try {
				request.execute();
			} catch (IOException e) {
				System.err.println("Failed to execute request: " + e.getMessage());
				System.exit(-1);
			}
		
			// Receive response
			try {
			response = request.receiveResponse();
		
				if (response != null) {
					// response received, output a pretty-print
					response.prettyPrint();
				} else {
					System.out.println("No response received.");
				}
		
			} catch (InterruptedException e) {
				System.err.println("Receiving of response interrupted: " + e.getMessage());
				System.exit(-1);
			}
		
			// Send the second (and last) block
			request = new PUTRequest();
			request.setURI(uri);
			bopt = new BlockOption(OptionNumberRegistry.BLOCK1, 1, 16, false);
			System.out.println("Part 2 block options: " + bopt.toString());
			request.setOption(bopt);
			request.setPayload(payload2.getBytes());
			request.enableResponseQueue(true);
		
			try {
				request.execute();
			} catch (IOException e) {
				System.err.println("Failed to execute request: " + e.getMessage());
				System.exit(-1);
			}
		
			// Receive response
			try {
			response = request.receiveResponse();
		
				if (response != null) {
					// response received, output a pretty-print
					response.prettyPrint();
				} else {
					System.out.println("No response received.");
				}
		
			} catch (InterruptedException e) {
				System.err.println("Receiving of response interrupted: " + e.getMessage());
				System.exit(-1);
			}*/
		
			// End of loader
			
		}
		else {
			// display help
			System.out.println("EMMA Californium (MACF) Agent Loader");
			//System.out.println("(c) 2012, Institute for Pervasive Computing, ETH Zurich");
			System.out.println();
			System.out.println("Usage: <URI> <File>");
			System.out.println("  URI: The CoAP URI of the remote resource to load agent on");
			System.out.println("  File: The file where an agent is described");
		}
	}
	
	private static Response send (Request request) {
		Response response;
		request.enableResponseQueue(true);
		
		// Send request
		try {
			request.execute();
		} catch (IOException e) {
			System.err.println("Failed to execute request: " + e.getMessage());
			return null;
		}
	
		// Receive response
		try {
			response = request.receiveResponse();
			return response;
		} catch (InterruptedException e) {
			System.err.println("Receiving of response interrupted: " + e.getMessage());
			return null;
		}
		
		//return null;
	}
	
	private static boolean sendBlock(URI uri, byte[] block) {
		return false;
	}
}
