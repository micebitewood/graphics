public class Triangle
{
	/*
		m stores the three vertices from top to bottom, m[0]:A, m[1]:B, m[2]:C
		D stores the vertice of D, and BD divides the triangle into to pieces
		They are unknown when passed in
		
		If there is a D existing, return true, else return false
	*/
	public static boolean split(int[][] point, int[] m, int[] D, int a, int b, int c)
	{
//--------------------------------------Part 1----------------------------------------------
		//assign m[0]:A, m[1]:B, m[2]:C
		if(point[a][1] <= point[b][1] && point[a][1] <= point[c][1])
		{
			m[0] = a;
			if(point[b][1] <= point[c][1])
			{
				m[1] = b;
				m[2] = c;
			}
			else
			{
				m[1] = c;
				m[2] = b;
			}
		}
		else if(point[b][1] <= point[a][1] && point[b][1] <= point[c][1])
		{
			m[0] = b;
			if(point[a][1] <= point[c][1])
			{
				m[1] = a;
				m[2] = c;
			}
			else
			{
				m[1] = c;
				m[2] = a;
			}
		}
		else
		{
			m[0] = c;
			if(point[a][1] <= point[b][1])
			{
				m[1] = a;
				m[2] = b;
			}
			else
			{
				m[1] = b;
				m[2] = a;
			}
		}
		/*
			xA = point[m[0]][0]
			xB = point[m[1]][0]
			xC = point[m[2]][0]
			yA = point[m[0]][1]
			yB = point[m[1]][1]
			yC = point[m[2]][1]
		*/
		
//----------------------------Part 2------------------------------------

		if(point[m[0]][1] != point[m[1]][1] && point[m[1]][1] != point[m[2]][1])
		{
		
			//xD = xA + (yB - yA) * (xC - xA) / (yC - yA)
			D[0] = point[m[0]][0] + (int)((point[m[1]][1] - point[m[0]][1]) * (point[m[2]][0] - point[m[0]][0]) / (point[m[2]][1] - point[m[0]][1]));
			//yD = yB
			D[1] = point[m[1]][1];
			
			return true;
		}
		return false;
	}
}
