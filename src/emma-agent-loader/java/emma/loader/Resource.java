package emma.loader;

import java.io.*;
import java.util.Arrays;

import org.json.JSONObject;
import org.json.JSONException;

public class Resource extends CoapResource {
	
	// Constants
	private static final String ROOT = "R";
	
	// Attributes
	private String expression;
	PrintStream out;
	
	// Constructors
	public Resource () {
		super();
		this.expression = "";
		this.out = null;
	}
	
	public Resource (InputStream ips) {
		super(ips);
		this.out = null;
	}
	
	public Resource (String str) {
		super(str);
		this.out = null;
	}
	
	// Getters
	@Override
	public byte[] getRaw () {
		return this.expression.getBytes();
	}
	
	public String getExpression () {
		return this.expression;
	}
	
	// Setters
	public void setOutput(PrintStream out) {
		this.out = out;
	}
	
	@Override
	protected boolean parse(String str) {
		// Variables
		boolean valid = false;
		
		// Parse if valid
		try {
			JSONObject jResource = new JSONObject(str);
			this.name = jResource.getString("NAME");
			this.expression = jResource.getString("EXPRESSION");
			valid = true;
		} catch (JSONException e) {
			if (this.out != null)
			{
				this.out.println("[RESOURCE PARSE] [ERROR] Unable to parse resource:");
				this.out.println(e.toString());
			}
		} catch (Exception e) {
			if (this.out != null)
			{
				this.out.println("[RESOURCE PARSE] [ERROR] Un-handled error:");
				this.out.println(e.toString());
			}
		}
		
		// TODO: Execute a real check on given expression
		// returning false if not syntaxically valid
		
		return valid;
	}
	
	public boolean send (String uriString) {
		return this.send(uriString, this.ROOT);
	}
	
	// toString override
	@Override
	public String toString() {
		String ret = "";
		ret += "RESOURCE: '" + this.name + "'" + "  (" + (this.valid ? "valid" : "invalid") + ")\n";
		ret += "    EXPRESSION = '" + this.expression + "'\n";
		return ret;
	}
}
