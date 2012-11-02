package time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import framework.BinaryConstraint;
import framework.Constraint;

/**
 * This class provides the fundamental building block for {@link TemporalAssertion}s.  All such assertions (i.e.,
 * {@link SimpleDistanceConstraint}s and {@link TemporalDistanceConstraint}s) are represented as {@link SimpleDistanceConstraint}s
 * in the {@link APSPSolver} temporal reasoner. This class should only be used to create low-level
 * temporal constraints between {@link TimePoint}s for direct insertion into an {@link APSPSolver}. 
 * @version 1.0
 */
public class SimpleDistanceConstraint extends BinaryConstraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1177765583299694096L;

	public SimpleDistanceConstraint() { }

	//lower and upper bound of the active constraint [min,max]
	private long minimum;
	private long maximum;

	//Imposed intervals
	//Four should be enough in most cases
	private final List<Bounds> bs = new ArrayList<Bounds>(4);

	//Utility methods

	/**
	 * Compare two constraints.
	 * @param obj The constraint to compare with this constraint.
	 * @return True iff the two constraints have equal minimum/maximum distances and source/destination {@link TimePoint}s. 
	 */
	@Override
	public boolean equals (Object obj)
	{return (obj instanceof SimpleDistanceConstraint) &&
		this.getFrom() == ((SimpleDistanceConstraint) obj).getFrom() &&
		this.getTo() == ((SimpleDistanceConstraint) obj).getTo() &&
		minimum == ((SimpleDistanceConstraint) obj).minimum &&
		maximum == ((SimpleDistanceConstraint) obj).maximum;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(new Object[]{this.getFrom(), this.getTo()});
	}

	//Print methods
	/**
	 * Get a String representation of this constraint.
	 * @return A String describing this constraint.
	 */
	/*
	public String toString() {
		return "(" + this.getFrom() + ") --["+APSPSolver.printLong(minimum)+","+APSPSolver.printLong(maximum)+"]--> (" + this.getTo() + ")";
	}
	 */
	
	/**
	 * Draw a graphical representation of this constraint.  This method is NOT implemented.
	 * @return True iff the constraint was be drawn successfully.
	 */
	public boolean draw(){
		return false;
	}

	//Access methods

	/**
	 * Get the minimum time lag between the two {@link TimePoint}s involved in this constraint.
	 * @return The minimum time lag between the two {@link TimePoint}s involved in this constraint.
	 */
	public long getMinimum(){
		return minimum;
	}

	/**
	 * Set the minimum time lag between the two {@link TimePoint}s involved in this constraint.
	 * @param newVal The minimum time lag between the two {@link TimePoint}s involved in this constraint.
	 */
	public void setMinimum(long newVal){
		minimum = newVal;
	}

	/**
	 * Get the maximum time lag between the two {@link TimePoint}s involved in this constraint.
	 * @return The minimum time lag between the two {@link TimePoint}s involved in this constraint.
	 */
	public long getMaximum(){
		return maximum;
	}

	/**
	 * Set the minimum time lag between the two {@link TimePoint}s involved in this constraint.
	 * @param newVal The minimum time lag between the two {@link TimePoint}s involved in this constraint.
	 */
	public void setMaximum(long newVal){
		maximum = newVal;
	}

	/**
	 * Get the current constraint counter, expressing the number of [lb,ub] intervals added between the two {@link TimePoint}s
	 * (including the active constraint). 
	 * @return The number of [lb,ub] intervals added between the two {@link TimePoint}s
	 * (including the active constraint).
	 */
	public int getCounter()
	{
		return bs.size();
	}

	/**
	 * Add an [lb,ub] interval between the two {@link TimePoint}s of this constraint.
	 * @param i The interval to add.
	 * @return True if the interval was added, throws error if {@link #MAX_EDGES} was reached. 
	 */
	public boolean addInterval(Bounds i) {
		if(i.max < this.minimum || i.min > this.maximum) {
			return false;
		}
		bs.add(i);
		return true;
	}

	/**
	 * Remove an [lb,ub] interval between the two {@link TimePoint}s of this constraint.
	 * @param i The interval to remove.
	 * @return {@code true} if the interval was removed, {@code false} if this was
	 * an attempt to remove the active constraint.
	 */
	public boolean removeInterval(Bounds i) {
		
		if(bs.remove(i)) {
			Bounds intersection = new Bounds(0, APSPSolver.INF);
			
			for(Bounds toIntersect : bs) {
				//intersection = intervalIntersect(intersection, toIntersect);//intervalIntersect(intersection, inter);
				intersection = intersection.intersect(toIntersect);
			}
			
//			if(intersection.stop < intersection.start) {
//				//intersection = new Interval(null);
//				throw new Error("This should not happen");
//			}
			
			minimum = intersection.min;
			maximum = intersection.max;
			
			return true;
		}
		return false;
	}
	
//	public boolean removeInterval(Interval i) {
//		if (counter == 1) {
//			bs[0] = null;
//			counter--;
//			return true;
//		}
//
//		int index = 0;
//		while (index < counter) {
//			if (!bs[index].equals(i)) index++; 
//			else break;
//		}
//
//		if (index == counter) return false;
//
//		if (index == counter-1) {
//			bs[index] = null;
//			counter--;
//		} 
//		else {
//			for (int j = index; j < counter;j++) bs[j] = bs[j+1];
//			bs[counter] = null;
//			counter--;
//		}
//
//		//only 1 edge left
//		if (counter == 1) {
//			minimum = bs[0].start;
//			maximum = bs[0].stop;
//		}
//		//more edges
//		else {
//			Interval inters = bs[0];
//			for (int j = 1; j < counter; j++) inters = intervalIntersect(inters,bs[j]);
//			minimum = inters.start;
//			maximum = inters.stop;
//		}
//		
//		return true;	 
//	}


	/*
	@Override
	public boolean isCompatible(Variable v) {
		return (v instanceof TimePoint);
	}
	*/
	
	@Override
	public String getEdgeLabel() {
		return "["+APSPSolver.printLong(minimum)+","+APSPSolver.printLong(maximum)+"]";
	}
	
	//Private functions
//	public static Interval intervalIntersect(Interval a, Interval b)
//	{
//		Interval ret = new Interval(null);
//		
//		ret.start = Math.max(a.start, b.start);
//		ret.stop = Math.min(a.stop, b.stop);
//		
//		return ret;
//	}

	@Override
	public SimpleDistanceConstraint clone() {
		SimpleDistanceConstraint sdc = new SimpleDistanceConstraint();
		sdc.setFrom(this.getFrom());
		sdc.setTo(this.getTo());
		sdc.setMinimum(this.getMinimum());
		sdc.setMaximum(this.getMaximum());
		return sdc;
	}
	
	/**
	 * Inverts the constraint if its lower bound is negative.
	 * At the time of writing negative constraints could not be used, e.g. constraints
	 * whose {@link #minimum} and {@link #maximum} values are negative.
	 * If this becomes possible in the future, this function will have little use. 
	 * (Since all constraint can't necessarily be expressed with positive bounds then.)
	 * @return A normalized copy the original constraint.
	 */
	public final SimpleDistanceConstraint normalize() {
		if(this.getMinimum() < 0) {
			return this.invert();
		} else {
			return this.clone();
		}
	}
	
	/**
	 * Inverts the <em>from</em> and <em>to</em> reference of this constraints and updates 
	 * the {@link #minimum} and {@link #maximum} bounds accordingly.
	 * @return an inverted copy of this constraint.
	 */
	public final SimpleDistanceConstraint invert() {
		SimpleDistanceConstraint newConstraint = new SimpleDistanceConstraint();
		newConstraint.setMinimum(-this.getMaximum());
		newConstraint.setMaximum(-this.getMinimum());
		newConstraint.setFrom(this.getTo());
		newConstraint.setTo(this.getFrom());
		return newConstraint;
	}

	@Override
	public boolean isEquivalent(Constraint c) {
		return c.equals(this);
	}

}