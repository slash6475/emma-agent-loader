package emma.loader;
import java.io.*;
import java.util.Arrays;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Agent extends CoapResource {
	
	// Constants
	private static final String ROOT = "A";
	
	// Attributes
	private String pre;
	private JSONArray post;
	private JSONArray target;
	//private String post;
	//private String target;
	PrintStream out;
	
	// Constructors
	public Agent () {
		super();
		this.pre = "";
		//this.post = "";
		//this.target = "";
		this.out = null;
	}
	
	public Agent (InputStream ips) {
		super(ips);
		this.out = null;
	}
	
	public Agent (String str) {
		super(str);
		this.out = null;
	}
	
	// Getters
	@Override
	public byte[] getRaw () {
		// Variables
		byte[] raw = new byte[0];
		
		// Convert back to Json string
		try {
			JSONObject jAgent = new JSONObject();
			jAgent.put("NAME", this.name);
			jAgent.put("PRE", this.pre);
			jAgent.put("POST", this.post);
			jAgent.put("TARGET", this.target);
			raw = jAgent.toString().getBytes();
		} catch (JSONException e) {
			if (this.out != null)
			{
				this.out.println("[AGENT GETRAW] [ERROR]:");
				this.out.println(e.toString());
			}
		} catch (Exception e) {
			if (this.out != null)
			{
				this.out.println("[AGENT GETRAW] [ERROR] Un-handled error:");
				this.out.println(e.toString());
			}
		}
		
		return raw;
	}
	
	public String getPre () {
		return this.pre;
	}
	
	public String getPost () {
		return this.post.toString();
	}
	
	public String getTarget () {
		return this.target.toString();
	}
	
	// Setters
	public void setOutput(PrintStream out) {
		this.out = out;
	}
	
	@Override
	protected boolean parse(String str) {
		// Parse str using JSON
		// Variables
		boolean valid = false;
		
		// Parse if valid
		try {
			JSONObject jAgent = new JSONObject(str);
			this.name = jAgent.getString("NAME");
			this.pre = jAgent.getString("PRE");
			this.post = jAgent.getJSONArray("POST");
			this.target = jAgent.getJSONArray("TARGET");
			valid = true;
		} catch (JSONException e) {
			if (this.out != null)
			{
				this.out.println("[AGENT PARSE] [ERROR] Unable to parse agent:");
				this.out.println(e.toString());
			}
		} catch (Exception e) {
			if (this.out != null)
			{
				this.out.println("[AGENT PARSE] [ERROR] Un-handled error:");
				this.out.println(e.toString());
			}
		}
		
		return valid;
	}
	
	public boolean send (String uriString) {
		return this.send(uriString, this.ROOT);
	}
	
	// toString override
	@Override
	public String toString() {
		String ret = "";
		ret += "AGENT: '" + this.name + "'" + "  (" + (this.valid ? "valid" : "invalid") + ")\n";
		ret += "    PRE    = '" + this.pre + "'\n";
		ret += "    POST   = '" + this.post.toString() + "'\n";
		ret += "    TARGET = '" + this.target.toString() + "'\n";
		return ret;
	}
}
