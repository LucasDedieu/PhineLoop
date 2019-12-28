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
		
		
		
		/*if (vars[i][j].isInstantiated())
				{
					board[i][j].setOrientation(vars[i][j].getValue());
					Shape[] nb = this.getNeighbourhood(board[i][j]);
					for (int k=1;k<nb.length;k++) // Pour chaque voisins
					{
						for(int orientationk=vars[nb[k].getI()][nb[k].getJ()].getLB();orientationk<vars[nb[k].getI()][nb[k].getJ()].getUB();orientationk++) // Pour chaque orientatins de chaque voisin
						{
							nb[k].setOrientation(orientationk);
							
							//System.out.println("Is "+nb[k]+" already instanciated ?"+vars[nb[k].getI()][nb[k].getJ()].isInstantiated());
							
							if(!vars[nb[k].getI()][nb[k].getJ()].isInstantiated() && game.areShapesConnected(nb[k], board[i][j]) && game.isShapeFullyConnected(nb[k])) 
							{
								try {
									vars[nb[k].getI()][nb[k].getJ()].instantiateTo(orientationk, null);
									board[nb[k].getI()][nb[k].getJ()].setOrientation(orientationk);
								//	System.out.println("Is instanciated");
								} catch (ContradictionException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								//board[i][j].setOrientation(orientationk);
							}
							else if (vars[nb[k].getI()][nb[k].getJ()].isInstantiated());
						}
					}
				}
				else 
				{
					Shape[] nb = this.getNeighbourhood(board[i][j]);
					for (int orientation=vars[i][j].getLB();orientation<vars[i][j].getUB();orientation++)
					{
						board[i][j].setOrientation(orientation);
						for (int k=1;k<nb.length;k++)
						{
							if(vars[nb[k].getI()][nb[k].getJ()].isInstantiated()   && (game.lookingButNotConnected(board[i][j],board[ nb[k].getI()][nb[k].getJ()]) || game.lookingButNotConnected(board[nb[k].getI()][nb[k].getJ()],board[i][j]))) /  /*&& !game.areShapesConnected(board[i][j], board[nb[k].getI()][nb[k].getJ()]) && game.isShapeFullyConnected(board[nb[k].getI()][nb[k].getJ()])  ////*&&  !game.couldConnect(board[i][j], board[nb[k].getI()][nb[k].getJ()]) && il est fully connected)
							{
								try {
									vars[i][j].removeValue(orientation,null);
									if (vars[i][j].isInstantiated()) 
									{
										board[i][j].setOrientation(vars[i][j].getValue());
										
									}
								} catch (ContradictionException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}*/
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

