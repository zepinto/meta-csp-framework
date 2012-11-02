package multi.activity;
import java.util.Vector;

import multi.allenInterval.AllenIntervalConstraint;
import multi.allenInterval.AllenIntervalNetworkSolver;
import symbols.SymbolicValueConstraint;
import symbols.SymbolicVariableConstraintSolver;
import utility.UI.PlotActivityNetworkGantt;
import framework.ConstraintNetwork;
import framework.ConstraintSolver;
import framework.Variable;
import framework.multi.MultiConstraintSolver;

public class ActivityNetworkSolver extends MultiConstraintSolver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4961558508886363042L;
	protected int IDs = 0;
	protected long origin;
	
	/**
	 * @return the origin
	 */
	public long getOrigin() {
		return origin;
	}

	/**
	 * @return the horizon
	 */
	public long getHorizon() {
		return horizon;
	}

	private long horizon;
	
	public ActivityNetworkSolver(long origin, long horizon) {
		super(new Class[] {AllenIntervalConstraint.class, SymbolicValueConstraint.class}, new Class[]{Activity.class}, createConstraintSolvers(origin,horizon,500));
		this.origin = origin;
		this.horizon = horizon;
	}
	
	public ActivityNetworkSolver(long origin, long horizon, int numActivities) {
		super(new Class[] {AllenIntervalConstraint.class, SymbolicValueConstraint.class}, new Class[]{Activity.class}, createConstraintSolvers(origin,horizon,numActivities));
		this.origin = origin;
		this.horizon = horizon;
	}

	private static ConstraintSolver[] createConstraintSolvers(long origin, long horizon, int numActivities) {
		ConstraintSolver[] ret = new ConstraintSolver[] {new AllenIntervalNetworkSolver(origin, horizon, numActivities), new SymbolicVariableConstraintSolver()};
		return ret;
	}
	
	/**
	 * Draw all activities on Gantt chart 
	 */
	public void drawAsGantt() {
		new PlotActivityNetworkGantt(this, null, "Activity Network Gantt");
	}
	
	/**
	 * Draw selected variables on Gantt chart
	 * @param selectedVariableNames Only variable/components in this {@link Vector} will be plotted 
	 */
	public void drawAsGantt( Vector<String> selectedVariableNames ) {
		new PlotActivityNetworkGantt(this, selectedVariableNames, "Activity Network Gantt");
	}
	
	/**
	 * Draw variables matching this {@link String} on Gantt chart
	 * @param varsMatchingThis {@link String} that must be contained in a variable to be plotted
	 */
	public void drawAsGantt( String varsMatchingThis ) {
		Vector<String> selectedVariables = new Vector<String>();
		for ( Variable v : this.getVariables() ) {
			if ( v.getComponent().contains(varsMatchingThis)) {
				selectedVariables.add(v.getComponent());
			}
		}
		new PlotActivityNetworkGantt(this, selectedVariables, "Activity Network Gantt");
	}
	
	@Override
	protected ConstraintNetwork createConstraintNetwork() {
		return new ActivityNetwork(this);
	}

	@Override
	protected Variable createVariableSub() {
		return new Activity(this, IDs++, this.constraintSolvers);
	}

	@Override
	protected Variable[] createVariablesSub(int num) {
		Variable[] ret = new Variable[num];
		for (int i = 0; i < num; i++)
			ret[i] = new Activity(this, IDs++, this.constraintSolvers); 
		return ret;
	}

	@Override
	public boolean propagate() {
		// For now, does nothing...
		return true;
	}

	@Override
	protected void removeVariableSub(Variable v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void removeVariablesSub(Variable[] v) {
		// TODO Auto-generated method stub

	}

}
