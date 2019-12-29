package fr.dauphine.javaavance.phineloops; 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.sun.corba.se.spi.orbutil.fsm.Input;
import com.sun.org.apache.xerces.internal.impl.dtd.models.CMLeaf;

import fr.dauphine.javaavance.phineloops.checker.Checker;
import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;
import fr.dauphine.javaavance.phineloops.solver.Solver;
import fr.dauphine.javaavance.phineloops.solver.csp.SolverCSP;
import fr.dauphine.javaavance.phineloops.solver.csp.SolverChoco;
import fr.dauphine.javaavance.phineloops.solver.line.SolverLineByLine;
import fr.dauphine.javaavance.phineloops.solver.snail.SolverSnail;
import fr.dauphine.javaavance.phineloops.view.GameVisualizer;

public class Main  {
    private static String inputFile = null;  
    private static String outputFile = null;
    private static Integer width = -1;
    private static Integer height = -1;
    private static Integer maxcc = -1; 
    

    private static void generate(int width,int height, String outputFile){
	// generate grid and store it to outputFile...
	//... 
    	if (maxcc!=-1) {
	    	try {
	        	Game game = new Game(height, width,maxcc);
	        	game.generate(maxcc);
				game.write(outputFile);
				System.out.println(game);
			} catch (FileNotFoundException e) {
				System.out.println("File not found: "+outputFile);
			}
    	}
    	else {
    		try {
            	Game game = new Game(height, width);
            	game.generate();
    			game.write(outputFile);
    			System.out.println(game);
    		}catch (FileNotFoundException e) {
    			System.out.println("File not found: "+outputFile);
    		}
        }
    }
    	

    private static boolean solve(String inputFile, String outputFile, int threads,String method) throws IOException{
	// load grid from inputFile, solve it and store result to outputFile...
	// ...
    	Game game = loadFile(inputFile);
    	if(game == null) {
    		//inputFile is not a valid game file
    		Files.copy(Paths.get(inputFile), Paths.get(outputFile));
    		return false;
    	}
    	//System.out.println("original game :\n"+game);
    	
    	//Check if game is already solved
    	if(Checker.check(game)) {
    		game.write(outputFile);
    		return true;
    	}
    	
    	Solver solver;
    	if(method.equals("snail")) {
    		 solver = new SolverSnail(game);
    	}
    	else if(method.equals("line")) {
    		solver = new SolverLineByLine(game);
    	}
    	else if(method.equals("csp")) {
    		 solver = new SolverCSP(game);
    	}
    	else if(method.equals("choco")) {
    		solver = new SolverChoco(game);
    	}
    	else {
    		 solver = new SolverLineByLine(game);
    	}
    	//Executor exec = Executors.newFixedThreadPool(threads);
    	//CountDownLatch latch = new CountDownLatch(1);
    	long startTime = System.currentTimeMillis();
    	//exec.execute(new LineByLineThread(game, latch ));
    	//try {
		//	latch.await();
		//} catch (InterruptedException e1) {
		//	e1.printStackTrace();
		//}
		
    	//Game gameSolved = ThreadController.getInstance().getSolvedGame();
    	Game gameSolved = solver.solve(threads);

    	if(gameSolved == null) {
    		game.write(outputFile);
    		return false;
    	}
    	long deltaTime = System.currentTimeMillis() - startTime;
    	System.out.println("Total time : "+deltaTime+" ms");
    	//System.out.println("\n__________________________\n"+gameSolved);
    	for(int i =0;i<gameSolved.getHeight();i++) {
			for(int j=0; j<gameSolved.getWidth();j++) {
				Shape shape = gameSolved.getBoard()[i][j];
				shape.setI(i);
				shape.setJ(j);
			}
		}
    	if(Checker.check(gameSolved)) {
    		gameSolved.write(outputFile);
        	return true;
    	}
    	return false;
    }

    private static boolean check(String inputFile){
	// load grid from inputFile and check if it is solved... 
	// ...
    	Game game = loadFile(inputFile);
    	//Checker checker = new Checker(game);
    	boolean isSolution = Checker.check(game);
    	return isSolution;
    	
    }
   

    
    public static void main(String[] args) throws IOException {
    	//Application.launch(args);
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        
        options.addOption("g", "generate ", true, "Generate a grid of size height x width.");
        options.addOption("c", "check", true, "Check whether the grid in <arg> is solved.");
        
        options.addOption("s", "solve", true, "Solve the grid stored in <arg>.");   
        options.addOption("o", "output", true, "Store the generated or solved grid in <arg>. (Use only with --generate and --solve.)");
        options.addOption("t", "threads", true, "Maximum number of solver threads. (Use only with --solve.)");
        options.addOption("m", "method", true, "Specify what method should be use for solver (line or snail or csp). (Use only with --solve.)");
        options.addOption("x", "nbcc", true, "Maximum number of connected components. (Use only with --generate.)");
        options.addOption("G", "gui", false, "Run with the graphic user interface.");
        options.addOption("d", "dimension", true, "Specify the dimension of the game (Use only with --solve.)");
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
			if( cmd.hasOption( "x" ) ){
				maxcc = Integer.parseInt(cmd.getOptionValue( "x" ));
			} 
			generate(width, height, outputFile);
	    }
	    else if(cmd.hasOption( "G" ) ){
	    	inputFile = cmd.getOptionValue( "G" );
	    	boolean inputfileValid = false;
	    	if(inputFile!=null) {
	    		Game game = loadFile(inputFile);
	    		if(game!=null) {
	    			inputfileValid =true;
	    			new GameVisualizer(new Game(game));
	    		}
	    	}
	    	if(cmd.hasOption("d") && !inputfileValid) {
	    		String[] gridformat = cmd.getOptionValue( "d" ).split("x");
				width = Integer.parseInt(gridformat[0]);
				height = Integer.parseInt(gridformat[1]);
				Game game = new Game(height, width);
				game.generate();
				new GameVisualizer(new Game(game));
	    	}
	    	else {
	    		throw new ParseException("Missing or invalid input file argument and no --dimension hxw argument"); 
	    	}
	    }
	    else if( cmd.hasOption( "s" ) ) {
			System.out.println("Running phineloops solver.");
			inputFile = cmd.getOptionValue( "s" );
			if(! cmd.hasOption("o")) throw new ParseException("Missing mandatory --output argument.");      
			outputFile = cmd.getOptionValue( "o" );
			int threads =0;
			String method = "line";
			if(cmd.hasOption("t")) {
				threads= Integer.parseInt(cmd.getOptionValue( "t" ));
			}
			if(cmd.hasOption("m")) {
				method= cmd.getOptionValue( "m" );
			}
			boolean solved = solve(inputFile, outputFile, threads,method ); 
	
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
		if(!cmd.hasOption( "G")){
			System.exit(0); // exit with success         
		}
    }
    
    
    static Game loadFile(String inputFile){
    	File file = new File(inputFile);
    	Game game = null;
    	int nb = 0;
    	try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr); 
			String line;
			//First we get the width and height
			try {
			height = Integer.parseInt(br.readLine());
			width = Integer.parseInt(br.readLine());
			}
			catch(NumberFormatException e) {
				br.close();
				return null;
			}
			if(height<0 || width<0) {
				br.close();	
				return null;
			}	
			game = new Game(height, width, 1);
			//while((line=br.readLine())!=null) {
				for(int i=0; i<height;i++) {
					for(int j =0; j<width;j++) {
						line = br.readLine();
						Shape shape = Shape.getShapeFromStringId(line, i, j);
						if(shape == null) {
							br.close();	
							return null;
						}
						nb++;
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
    	if(nb != height*width) {
    		//Wrong file
    		return null;
    	}
    	return game;   	
    }
}
