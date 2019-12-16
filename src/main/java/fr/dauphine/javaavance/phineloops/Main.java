package fr.dauphine.javaavance.phineloops; 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import fr.dauphine.javaavance.phineloops.model.Checker;
import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;
import fr.dauphine.javaavance.phineloops.model.Solver2;
import fr.dauphine.javaavance.phineloops.view.Visualize;
//import javafx.application.Application;
//import javafx.stage.Stage;

public class Main /*extends Application*/  {
    private static String inputFile = null;  
    private static String outputFile = null;
    private static Integer width = -1;
    private static Integer height = -1;
    private static Integer maxcc = -1; 
    

    private static void generate(int width, int height, String outputFile){
	// generate grid and store it to outputFile...
	//... 
    	//FIXME fix the generator
    	try {
        	Game game = new Game(width, height,0);
        	game.generate();
			game.write(outputFile);
			System.out.println(game);
		} catch (FileNotFoundException e) {
			System.out.println("File not found: "+outputFile);
		}
    	
    }

    private static boolean solve(String inputFile, String outputFile){
	// load grid from inputFile, solve it and store result to outputFile...
	// ...
    	Game game = loadFile(inputFile);
    	System.out.println("original game :\n"+game);
    	Solver2 solver = new Solver2(game);
		long startTime = System.currentTimeMillis();
    	Game gameSolved = solver.solve();
    	if(gameSolved == null) {
    		return false;
    	}
    	//long deltaTime = System.currentTimeMillis() - startTime;
    	//System.out.println("time : "+deltaTime+" ms");
    	//System.out.println("\n__________________________\n"+gameSolved);
    	try {
			gameSolved.write(outputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	return true;
 
    }

    private static boolean check(String inputFile){
	// load grid from inputFile and check if it is solved... 
	// ...
    	Game game = loadFile(inputFile);
    	//Checker checker = new Checker(game);
    	boolean isSolution = Checker.check(game);
    	return isSolution;
    	
    }
    
    public static void main(String[] args) {
    	//Application.launch(args);
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

		boolean solved = solve(inputFile, outputFile); 

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
    
    static Game loadFile(String inputFile){
    	File file = new File(inputFile);
    	Game game = null;
    	try {
			FileReader fr = new FileReader(file);
			BufferedReader br=new BufferedReader(fr); 
			String line;
			//First we get the width and height
			int width = Integer.parseInt(br.readLine());
			int height = Integer.parseInt(br.readLine());
			game = new Game(width, height, 1);
			//while((line=br.readLine())!=null) {
				for(int i=0; i<height;i++) {
					for(int j =0; j<width;j++) {
						line = br.readLine();
						Shape shape = Shape.getShapeFromStringId(line, i, j);
						try {
							game.addShape(shape);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				//}	
			}
			br.close();	
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
    	return game;   	
    }
    
/*
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Visualize visu = new Visualize();
		visu.start(primaryStage);
	}*/
	
}
