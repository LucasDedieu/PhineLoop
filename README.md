# Phineloops
This is a java implementation for the game Infinity Loops.

## Resume of the original game
Infinity Loop is a captivating mobile game that challenges players to connect various shapes and patterns in a continuous loop. With its minimalist design and intuitive gameplay, it offers a relaxing and addictive experience. The goal is to rotate and align the pieces until they form a complete loop without any loose ends. Featuring hundreds of levels with increasing complexity, Infinity Loop provides a perfect blend of puzzle-solving and creativity. It offers a unique and satisfying puzzle-solving experience that keeps players engaged and entertained. With its accessible mechanics and endless possibilities, Infinity Loop is a must-play game for puzzle enthusiasts of all ages.

# Build
<ul>
<li>Clone the repository</li>
<li>Run : mvn package</li>
</ul>

# Usage 

<p><strong>java -jar projet.jar [options...]</strong></br> The following options are avalaible and perform a specific task for the game: </p>  
      
      --generate hxw --output file [--nbcc c] : Generate a grid of size height x width.
                                      You can specify a maximal number of c connected component with --nbcc
                                      
      --check file : Check whether the grid in file is solved.
      
      --solve file --output filesolved [--method chosen_method] : Solve the grid with the line solver and stores it in filesolved 
                                      You can specify a solver to use in chosen_method ("line"(default solver) or "snail" or "csp")
                                      
      --gui file --dimension hxw : Run with the graphic user interface.
      


# Using the graphic user interface 

<p> When playing a game you can move the shapes by clicking on them, you also have access to the list of solvers avalaible that will solve the game for you. You also have an option to regenerate another grid. When a grid is solved, you'll get a notification.</p>
      

# Authors 

<p>
Thierno Bah Moussa</br>
Lucas Dedieu </br>
Alban Tiacoh </p>


