import java.io.*;
import java.util.Arrays;
import java.lang.Math;

import java.net.URI;
import java.net.URISyntaxException;

//import ch.ethz.inf.vs.californium.coap.GETRequest;
import ch.ethz.inf.vs.californium.coap.POSTRequest;
import ch.ethz.inf.vs.californium.coap.PUTRequest;
import ch.ethz.inf.vs.californium.coap.Request;
import ch.ethz.inf.vs.californium.coap.Response;
import ch.ethz.inf.vs.californium.coap.Option;
import ch.ethz.inf.vs.californium.coap.BlockOption;
import ch.ethz.inf.vs.californium.coap.registries.OptionNumberRegistry;

public abstract class CoapResource {
	
	// Constants
	private static final int MAX_BUFFER = 100;
	private static final int BLOCK_SIZE = 32;
	
	// Attributes
	protected String name;
	protected boolean valid;
	
	// Constructors
	public CoapResource () {
		this.name = "";
		this.valid = false;
	}
	
	public CoapResource (InputStream ips) {
		this.name = "";
		this.valid = this.set(ips);
	}
	
	public CoapResource (String str) {
		this.name = "";
		this.valid = this.parse(str);
	}
	
	// Abstract methods
	abstract public byte[] getRaw ();
	
	// Must set name and other attributes that are derived
	// No need to set validity (already handled)
	abstract protected boolean parse(String str);
	
	// Getters
	public String getName () {
		return this.name;
	}
	
	// Setters
	public boolean set (String str) {
		this.valid = this.parse(str);
		return this.valid;
	}
	
	public boolean set (InputStream ips) {
		// Variables
		InputStreamReader ipsr;
		BufferedReader br;
		int nbCharRead = 0;
		char[] buffer = new char[MAX_BUFFER];
		String temp = "";
		
		// Read agent from file
		try {
			// Open file for read operation
			ipsr = new InputStreamReader(ips);
			br = new BufferedReader(ipsr);
	
			// Save agent in RAM
			do {
				nbCharRead = br.read(buffer, 0, MAX_BUFFER);
				if (nbCharRead > 0)
				{
					temp += new String(buffer, 0, nbCharRead);
					//agent += temp;
				}
			} while (nbCharRead == MAX_BUFFER);
			temp = temp.substring(0, temp.length()-1);
	
		} catch (Exception e) {
			System.out.println("[COAP RESOURCE SET] Error while loading resource:");
			System.out.println(e.toString());
		}
		
		// Parse agent
		this.valid = this.parse(temp);
		
		return this.valid;
	}
	
	// Methods
	public boolean isValid() {
		return this.valid;
	}
	
	protected boolean send (String uriString, String rootResource) {
		// Variables
		Request request = new PUTRequest();
		URI uri;
		Response response;
		BlockOption bopt;
		int blockCounter = 0;
		int blockSize = BLOCK_SIZE;
		byte[] block = this.getRaw();
		byte[] payload;
		boolean prediction = false;
		boolean success = false;
		
		/* Create agent on node
		try {
			request = new POSTRequest();
			//request.setURI(new URI(uriString + "/" + rootResource +"?name=" + this.name));
			request.setURI(new URI(uriString + "/" + rootResource + "/" + this.name));
			response = this.sendRequest(request);
			if (response != null) response.prettyPrint();
			success = true;
		} catch (Exception e) {
			System.out.println("[COAP RESOURCE SEND] Invalid URI.");
			success = false;
		}//*/
		success = true;
		
		// Send agent in a block-wise style
		if ((this.valid == true) && (success = true))
		{
			try {
				uri = new URI(uriString + "/" + rootResource + "/" + this.name);
				/*System.out.println("Block length: " + block.length);
				System.out.println("Payload: '" + block + "'");*/
		
				//*
				while (blockCounter*blockSize < block.length)
				{
					request = new PUTRequest();
					request.setURI(uri);
					//System.out.println("\n\nBlock counter: " + blockCounter);
					prediction = ((blockCounter+1)*blockSize < block.length);
					bopt = new BlockOption(OptionNumberRegistry.BLOCK1, blockCounter, (int)((Math.log(blockSize)/Math.log(2))-4), prediction ?true:false);
					request.setOption(bopt);
					if (prediction == true)
					{
						payload = Arrays.copyOfRange(block, blockCounter*blockSize, ((blockCounter + 1)*blockSize));
					}
					else
					{
						// This is the last packet we will send
						payload = Arrays.copyOfRange(block, blockCounter*blockSize, block.length);
					}
					request.setPayload(payload);
					response = this.sendRequest(request);
		
					if (response != null) response.prettyPrint();
		
					// TODO: Get response codes, act differentlty according to errors
					success = true;
		
					blockCounter++;
				}//*/
			} catch (Exception e) {
				System.out.println("[COAP RESOURCE SEND] Invalid URI.");
				success = false;
			}
		}
		else
		{
			System.out.println("[COAP RESOURCE SEND] Resource is not valid.");
			success = false;
		}
		
		return success;
	}
	
	private Response sendRequest(Request request)
	{
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
	}
	
	// toString override
	@Override
	public String toString() {
		String ret = "";
		ret += "CoAP Resource: '" + this.name + "' (" + (this.valid ? "valid" : "invalid") + ")\n";
		return ret;
	}
}
