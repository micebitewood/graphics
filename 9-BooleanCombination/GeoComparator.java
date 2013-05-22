/*
	Assignment 9
	
	comparator for priorityqueue
*/
import java.util.*;
public class GeoComparator implements Comparator<Object>
{
	//if num == 0, compare root[0]
	//if num == 1, compare root[1]
	private int num;
	public GeoComparator(int num)
	{
		this.num = num;
	}
	public int compare(Object g1, Object g2)
	{
		double r1 = ((Geometry)g1).getRoot()[num];
		double r2 = ((Geometry)g2).getRoot()[num];

		return r1 > r2 ? 1 : r1 == r2 ? 0 : -1;
	}
}
