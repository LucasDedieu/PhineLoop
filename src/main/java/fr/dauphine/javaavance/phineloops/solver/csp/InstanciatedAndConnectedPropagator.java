package fr.dauphine.javaavance.phineloops.solver.csp;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;

public class InstanciatedAndConnectedPropagator extends Propagator<IntVar> {

	public InstanciatedAndConnectedPropagator(IntVar[] intVarNeighbourhood, Shape[] neighbourhood, Game game,
			Shape[][] board) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void propagate(int evtmask) throws ContradictionException {
		// TODO Auto-generated method stub
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
		return null;
	}

}

