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
		//for (int orientation=0;orientation<neighbourhood[0].getDomain().length;orientation++)
		{
			Shape auxshape = neighbourhood[0]; // Useless
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
				//System.out.println(neighbourhood[0].getSymbol());
				//System.out.println(token);
				//System.out.println(neighbourhood[0].getVConnections());
				if (token!=neighbourhood[0].getVConnections())
						vars[0].removeValue(orientation,null);
				neighbourhood[0]=auxshape;
		}
		if (vars[0].isInstantiated()) neighbourhood[0].setOrientation(vars[0].getValue());
	}

	@Override
	public ESat isEntailed() {
		// TODO Auto-generated method stub
		//Maybe fix this
		/*int token=0;
		for (int orientation=vars[0].getLB();orientation<=vars[0].getUB();orientation++)
		{
			neighbourhood[0].setOrientation(orientation);
			for(int v=1;v<neighbourhood.length;v++)
			{
				for(int orientationv=vars[v].getLB();orientationv<vars[v].getUB();orientationv++)
				{
					neighbourhood[v].setOrientation(orientationv);
					if (game.areShapesConnected(neighbourhood[0], neighbourhood[v])) 
						{
							token++;
							break;
						}
				}
			}
		}*/
		int token=0;
		for (int orientation=vars[0].getLB();orientation<=vars[0].getUB();orientation++)
		{
			neighbourhood[0].setOrientation(orientation);
			//System.out.println("Je vérifie");
			if(game.isShapeFullyConnected(neighbourhood[0])) token++;
		}
		
		if (token==0)
            return ESat.FALSE;
        else if (token==vars[0].getRange())
            return ESat.TRUE;
        else
            return ESat.UNDEFINED;
	}

}
	
	
