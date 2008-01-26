/**
 * @author wItspirit
 * 12-nov-2005
 * PatternTester.java
 */

package be.vanvlerken.bert.flickrstore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTester {

    /**
     * @param args
     */
    public static void main(String[] args) {
	String scrambledName = "This ? is : an<* invalid >|\\/\" filename";
	System.out.println("Initial Name = " + scrambledName);
	// Forbidden chars are: \ / : * ? " < > |
	Pattern scramblePattern = Pattern.compile("[\\\\/:\\*\\?\"<>|]");
	Matcher scrambler = scramblePattern.matcher(scrambledName);
	scrambledName = scrambler.replaceAll("_");
	System.out.println("Scrambled Name = " + scrambledName);
    }

}
