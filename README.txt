***************************************
********** EMMA Agent Loader **********
***************************************

/dep: Libraries sources
/doc: Javadoc folder
/patch: Patches folder
/src: EMMA Agent Loader sources

/install.sh: EMMA Agent Loader installer

***************************************

This program allows to send agents or resources in JSON format through the network.
It uses CoAP-08 protocol (see IETF Draft for more informations)

For the time being, the destination IPv6 address is hardcoded (set to aaaa::200:2:2:202).
The loader is able to send only one agent (or resource) at a time. The agent (or resource) MUST
be written in a file named 'LOAD.txt'. This file is located in 'MACF/run_env'.

The JSON format used is described next:
{
	"NAME":"<The name of the agent>",
	"PRE":"<The arithmetico-logical pre-condition>",
	"POST":[<A coma-separated list of posts>],
	"TARGET":[<A coma-separated list of targets>]
}

Please see JSON official page for more details about types (STRING, OBJECT, ...):
http://www.json.org/

PRE:
	A PRE-element is a STRING that represents an arithmetico-logical expression.
	References to other resources are made using the URI of the referenced resource parsed
	with '#' instead of '/'.
	
	Valid operators are:
		- "||": LOGICAL OR
		- "&&": LOGICAL AND
		- "==": EQUAL
		- ">": HIGHER THAN
		- "<": LOWER THAN
		- "+": PLUS
		- "-": MINUS
		- "/": DIVIDE
		- "*": MULTIPLY
		- "!": NEGATE
	
	They have the same precedence as in the C-language. Parenthesis "(" and ")" can
	be used to force native precedence.
	
	Example: "1+R#brightness<5" equals 1 if "one plus value of resource R/brignthess is lower than five"

POST:
	The POST value associated with the "POST" key MUST be a table. Even if it contains only one element.

POST-element:
	A POST-element can be either a STRING or an OBJECT. If it is a STRING, it will be interpreted as
	an arithmetico-logical expression (see PRE-condition). If it is an OBJECT, it will simply be send
	as is but the trimming and trailing brackets.

TARGET:
	The TARGET value associated with the "TARGET" key MUST be a table. Even if it contains only one element.

TARGET-element:
	A TARGET-element is a STRING that represents an IPv6 address. The address MUST contain a destination
	port and an URI.
	
	Example: "[aaaa::200:2:2:202]:5683/R/test" refers to the resource 'R/test' located on node [aaaa::200:2:2:202]


***************************************

The loader uses two libraries:
	- JSON
	- Californium

The sources of the JSON library can be found at www.json.org.
The sources of the Californium library can be found at http://people.inf.ethz.ch/mkovatsc/californium.php

However, the Californium library does not support CoAP-08 draft as announced on the web-page. Therefore a patch
is provided in 'EMMA-agent-loader' package to use it with CoAP-08. Actually, CoAP-08 is the latest supported version
of CoAP in Contiki OS.

The Californium library also needs some dependencies to be compiled with ant, these libraries are located in
dep/californium/extern

***************************************
