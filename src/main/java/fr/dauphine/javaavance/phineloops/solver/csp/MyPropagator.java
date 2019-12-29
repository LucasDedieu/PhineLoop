package fr.dauphine.javaavance.phineloops.solver.csp;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;

public class MyPropagator extends Propagator<IntVar>{
	private Game game;
	private Shape[] neighbourhood;
	

	public MyPropagator(IntVar[] intVarNeighbourhood, Shape[] neighbourhood, Game game) {
		super(intVarNeighbourhood);
		this.game = game;
		this.neighbourhood = neighbourhood;
	}
	
	@Override
	public void propagate(int evtmask) throws ContradictionException {
		// TODO Auto-generated method stud 
		for (int orientation=vars[0].getLB();orientation<=vars[0].getUB();orientation++) //For each orientations of the current piece 
		{
			Shape auxshape = neighbourhood[0]; 
			int token=0; //token to count how many times the neighbor of the piece in his current orientation can connect with it (should be equals to his number of connection if the orientation is feasible) 
			neighbourhood[0].setOrientation(orientation); 
			for (int v=1;v<neighbourhood.length;v++) //Now for each neighbor ...
			{ 
				int auxorientation=neighbourhood[v].getOrientation();
				for (int orientationVoisin=vars[v].getLB();orientationVoisin<=vars[v].getUB();orientationVoisin++)//... We turn it (change orientation) until it meets a connection with the current piece 								vars[auxneighbor.get(v).getI()][auxneighbor.get(v).getJ()].getUB()
				{
					neighbourhood[v].setOrientation(orientationVoisin);
					if(game.areShapesConnected(neighbourhood[0],neighbourhood[v]))
						{
								token++;
								break;
						}
				}
				neighbourhood[v].setOrientation(auxorientation);
			}
				if (token!=neighbourhood[0].getVConnections())
						vars[0].removeValue(orientation,null); //If it did not meet his neighbor enough time in this orientation we remove it 
				neighbourhood[0]=auxshape;
		}
		if (vars[0].isInstantiated()) neighbourhood[0].setOrientation(vars[0].getValue());
	}

	@Override
	public ESat isEntailed() {
		// TODO Auto-generated method stub
		//We check if all the value of the domain correspond to a solution
		int token=0;
		for (int orientation=vars[0].getLB();orientation<=vars[0].getUB();orientation++)
		{
			neighbourhood[0].setOrientation(orientation);
			//We turn the shape in all of his orientation and mark if it has meet a neighbor 
			if(game.isShapeFullyConnected(neighbourhood[0])) token++;
		}
		if (token==0) //Never met a neighbor 
            return ESat.FALSE;
        else if (token==vars[0].getRange()) //Always met a neighbor
            return ESat.TRUE;
        else
            return ESat.UNDEFINED;
	}

}
	
	
