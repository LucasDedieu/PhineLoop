package fr.dauphine.javaavance.phineloops; 

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Option.Builder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
    private static String inputFile = null;  
    private static String outputFile = null;
    private static Integer width = -1;
    private static Integer height = -1;
    private static Integer maxcc = -1; 
    

    private void generate(int width, int height, String outputFile){
	// generate grid and store it to outputFile...
	//...            
    }

    private boolean solve(String inputFile, String outputFile){
	// load grid from inputFile, solve it and store result to outputFile...
	// ...

	return false; 
    }

    private boolean check(String inputFile){
	// load grid from inputFile and check if it is solved... 
	// ...

	return false; 
    }
    
    public static void main(String[] args) {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        
        options.addOption("g", "generate ", true, "Generate a grid of size height x width.");
        options.addOption("c", "check", true, "Check whether the grid in <arg> is solved.");
        
        options.addOption("s", "solve", true, "Solve the grid stored in <arg>.");   
        options.addOption("o", "output", true, "Store the generated or solved grid in <arg>. (Use only with --generate and --solve.)");
        options.addOption("t", "threads", true, "Maximum number of solver threads. (Use only with --solve.)");
        options.addOption("x", "nbcc", true, "Maximum number of connected components. (Use only with --generate.)");
	options.addOption("G", "gui", true, "Run with the graphic user interface.");
        options.addOption("h", "help", false, "Display this help");
        
        try {
            cmd = parser.parse( options, args);         
        } catch (ParseException e) {
            System.err.println("Error: invalid command line format.");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "phineloops", options );
            System.exit(1);
        }       
                
	try{    
	    if( cmd.hasOption( "g" ) ) {
		System.out.println("Running phineloops generator.");
		String[] gridformat = cmd.getOptionValue( "g" ).split("x");
		width = Integer.parseInt(gridformat[0]);
		height = Integer.parseInt(gridformat[1]); 
		if(! cmd.hasOption("o")) throw new ParseException("Missing mandatory --output argument.");
		outputFile = cmd.getOptionValue( "o" );

		generate(width, height, outputFile); 
	    }
	    else if( cmd.hasOption( "s" ) ) {
		System.out.println("Running phineloops solver.");
		inputFile = cmd.getOptionValue( "s" );
		if(! cmd.hasOption("o")) throw new ParseException("Missing mandatory --output argument.");      
		outputFile = cmd.getOptionValue( "o" );

		boolean solved = solve(intputFile, outputFile); 

		System.out.println("SOLVED: " + solved);            
	    }
        
	    else if( cmd.hasOption( "c" )) {
		System.out.println("Running phineloops checker.");
		inputFile = cmd.getOptionValue( "c" );
            		
		boolean solved = check(inputFile); 

		System.out.println("SOLVED: " + solved);           
	    }
	    else {
		throw new ParseException("You must specify at least one of the following options: -generate -check -solve ");           
	    }
	} catch (ParseException e) {
            System.err.println("Error parsing commandline : " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "phineloops", options );         
            System.exit(1); // exit with error      
	}

        System.exit(0); // exit with success                            
    }
}
