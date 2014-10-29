package org.metacsp.spatial.geometry;


public class Manifold {



		public Polygon A;
		public Polygon B;
		public float penetration;
		public final Vec2 normal = new Vec2();
		public final Vec2[] contacts = { new Vec2(), new Vec2() };
		public int contactCount;


		public Manifold( Polygon a, Polygon b )
		{
			A = a;
			B = b;
			this.initialize();
		}

		public void solve()
		{
			CollisionPolygonPolygon cpp = new CollisionPolygonPolygon();
			cpp.handleCollision( this, A, B );			
		}

		public void initialize()
		{
			for (int i = 0; i < contactCount; ++i)
			{
				// Calculate radii from COM to contact
				// Vec2 ra = contacts[i] - A->position;
				// Vec2 rb = contacts[i] - B->position;
				Vec2 ra = contacts[i].sub( A.getPosition() );
				Vec2 rb = contacts[i].sub( B.getPosition() );
			}
		}



		public void positionalCorrection()
		{
			// const real k_slop = 0.05f; // Penetration allowance
			// const real percent = 0.4f; // Penetration percentage to correct
			// Vec2 correction = (std::max( penetration - k_slop, 0.0f ) / (A->im +
			// B->im)) * normal * percent;
			// A->position -= correction * A->im;
			// B->position += correction * B->im;

//			float correction = StrictMath.max( penetration - ImpulseMath.PENETRATION_ALLOWANCE, 0.0f ) / (A.invMass + B.invMass) * ImpulseMath.PENETRATION_CORRETION;
//			float correction = StrictMath.max( penetration - ImpulseMath.PENETRATION_ALLOWANCE, 0.0f );
			float correction = penetration;
//			if(A.incident) 
				A.getPosition().addsi( normal, -(correction * (float)1.0)  );
//			if(B.incident) 
//				B.position.addsi( normal, correction * ((float)1.0) );			
//			A.position.addsi( normal, -A.invMass * correction );
//			B.position.addsi( normal, B.invMass * correction );
		}

}
