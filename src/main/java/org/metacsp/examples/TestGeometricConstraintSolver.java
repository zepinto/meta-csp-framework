package org.metacsp.examples;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import org.metacsp.framework.Variable;
import org.metacsp.spatial.geometry.GeometricConstraint;
import org.metacsp.spatial.geometry.GeometricConstraintSolver;
import org.metacsp.spatial.geometry.Polygon;
import org.metacsp.spatial.geometry.Vec2;


public class TestGeometricConstraintSolver {

	/**
	 * 
	 */
	public static void main(String[] args) {

		GeometricConstraintSolver solver = new GeometricConstraintSolver();
		Vector<Vector<Vec2>> toPlots = new Vector<Vector<Vec2>>();
		Variable[] vars = solver.createVariables(3);
		
		Polygon p1 = (Polygon)vars[0];
		Vector<Vec2> vecs1 = new Vector<Vec2>();
		vecs1.add(new Vec2(100,87));
		vecs1.add(new Vec2(60,30));
		vecs1.add(new Vec2(220,60));
		vecs1.add(new Vec2(180,120));
		p1.setDomain(vecs1.toArray(new Vec2[vecs1.size()]));
		
		Polygon p2 = (Polygon)vars[1];		
		Vector<Vec2> vecs = new Vector<Vec2>();
		vecs.add(new Vec2(180,90));
		vecs.add(new Vec2(100,350));
		vecs.add(new Vec2(340,350));
		vecs.add(new Vec2(290,125));
		p2.setDomain(vecs.toArray(new Vec2[vecs.size()]));

		
		Polygon p3 = (Polygon)vars[2];		
		Vector<Vec2> vecs2 = new Vector<Vec2>();
		vecs2.add(new Vec2(180,190));
		vecs2.add(new Vec2(100,50));
		vecs2.add(new Vec2(240,138));
		vecs2.add(new Vec2(190,225));
		p3.setDomain(vecs2.toArray(new Vec2[vecs2.size()]));
		
		//adding polygon for visualization
		toPlots.add(p1.getFullSpaceRepresentation());
		toPlots.add(p2.getFullSpaceRepresentation());
		toPlots.add(p3.getFullSpaceRepresentation());
		
		
		//adding constraint
		GeometricConstraint dc = new GeometricConstraint(GeometricConstraint.Type.DC);
		dc.setFrom(p1);
		dc.setTo(p2);

		GeometricConstraint inside = new GeometricConstraint(GeometricConstraint.Type.INSIDE);
		inside.setFrom(p1);
		inside.setTo(p2);
		
		GeometricConstraint dc1 = new GeometricConstraint(GeometricConstraint.Type.DC);
		dc1.setFrom(p3);
		dc1.setTo(p2);
				
		GeometricConstraint inside1 = new GeometricConstraint(GeometricConstraint.Type.INSIDE);
		inside1.setFrom(p3);
		inside1.setTo(p1);
		
//		solver.addConstraints(new Constraint[]{dc, inside});
		
		solver.addConstraint(inside);
		solver.addConstraint(dc1);
		solver.addConstraint(inside1);

		solver.removeConstraint(inside1);
//		solver.addConstraint(dc);
		
		
		
		toPlots.add(p1.getFullSpaceRepresentation());
		toPlots.add(p3.getFullSpaceRepresentation());

		String PATH = "/home/iran/Desktop/";
//		String PATH = "../../";
		BufferedWriter initPlot = null;
		String initLayoutPlot = "";
		initLayoutPlot = GeometricConstraintSolver.drawPolygons(toPlots, 500);
		try{
			
			initPlot = new BufferedWriter(new FileWriter(PATH+ "shift"+".dat", false));
			initPlot.write(initLayoutPlot);
			initPlot.newLine();
			initPlot.flush();
		}				
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}