/**
 * Type enumere des orientations possibles des cotes
 */
public enum Orientation {
		
		N ("Nord"),
		NE ("Nord Ouest"),
		W ("Ouest");
		
		
		public String name = new String();
		
		Orientation (String a) {
			name = a;
		}
		
		public String toString() {
			return name;
		}
	}