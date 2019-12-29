package fr.dauphine.javaavance.phineloops.solver.csp;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;

public class InstantiatedAndConnectedPropagator extends Propagator<IntVar> {
	private Shape[] neighbourhood;
	private Game game;

	public InstantiatedAndConnectedPropagator(IntVar[] intVarNeighbourhood, Shape[] neighbourhood, Game game
			) {
		// TODO Auto-generated constructor stub
		super(intVarNeighbourhood);
		this.neighbourhood=neighbourhood;
		this.game=game;
	}

	@Override
	public void propagate(int evtmask) throws ContradictionException {
		// TODO Auto-generated method stub
		Shape auxshape = neighbourhood[0];
		if (vars[0].isInstantiated())
		{
			neighbourhood[0].setOrientation(vars[0].getValue()); //We set this variable to his value because it won't change and now we can filter the domain of his neighbor without backtracking
			for(int k=1;k<neighbourhood.length;k++) 
			{
				int aux=neighbourhood[k].getOrientation();
				for(int orientationk=vars[k].getLB();orientationk<vars[k].getUB();orientationk++)
				{
					neighbourhood[k].setOrientation(orientationk);
					if(!vars[k].isInstantiated() && game.areShapesConnected(neighbourhood[k], neighbourhood[0]) && game.isShapeFullyConnected(neighbourhood[k]))
					{
							vars[k].instantiateTo(orientationk, null);
							break;
					}
				}
				if (!vars[k].isInstantiated()) neighbourhood[k].setOrientation(aux);
			}
		}
		else 
		{
			for(int k=1;k<neighbourhood.length;k++)
			{
				if(vars[k].isInstantiated())
				{
					for(int orientation=vars[0].getLB();orientation<vars[0].getUB();orientation++)
					{
						neighbourhood[0].setOrientation(orientation);
						if(game.lookingButNotConnected(neighbourhood[0], neighbourhood[k]) || game.lookingButNotConnected(neighbourhood[k], neighbourhood[0]))
						{
							vars[0].removeValue(orientation, null);
							if(vars[0].isInstantiated())
							{
								neighbourhood[0].setOrientation(vars[0].getValue());
							}
							else
								neighbourhood[0]=auxshape;
						}
					}
				}
			}
		}
	}

	@Override
	public ESat isEntailed() {
		// TODO Auto-generated method stub
		int tokenNB=0;
		int tokenCM=0;
		int nbVue=1;
		if(vars[0].isInstantiated())
		{
			for(int k=1;k<neighbourhood.length;k++)
			{
				for (int orientation=vars[k].getLB();orientation<=vars[k].getUB();orientation++)
				{
					neighbourhood[k].setOrientation(orientation);
					if(game.areShapesConnected(neighbourhood[0], neighbourhood[k]))
					{
						tokenCM++;
					}
					nbVue=nbVue*neighbourhood[k].getVConnections();
				}
			}
		}
		else
		{
			for(int k=1;k<neighbourhood.length;k++)
			{
				for (int orientation=vars[0].getLB();orientation<=vars[0].getUB();orientation++)
				{
					neighbourhood[0].setOrientation(orientation);
					if(vars[k].isInstantiated() && game.areShapesConnected(neighbourhood[0], neighbourhood[k])) 
					{
						tokenNB++;
						break;
					}
				}
				System.out.println("Je vérifie");
			}
		}
		if (tokenNB==0 && tokenCM==0)
            return ESat.FALSE;
        else if (tokenCM==(neighbourhood.length-1)*nbVue || tokenNB==(neighbourhood.length-1)*neighbourhood[0].getVConnections())
            return ESat.TRUE;
        else
            return ESat.UNDEFINED;
	}
}

