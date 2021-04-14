/*
 * TODO
 * -Takes care of Meeples
 * -Keep track of individual score
 * -Serves as identification for Game class
 */
import tile.Meeple;

public class CarcassonnePlayer {
	private int score;
	private String name;
	private Meeple[] meeples;
	
		public CarcassonnePlayer()
		{
			score=0;
			meeples=new Meeple[8];
		}
		
		private void assignMeeples()
		{
				for(int i=0; i<meeples.length; i++)
					meeples[i]=new Meeple(name);
		}
	
		public void addScore(int num)
		{
			score+=num;
		}
		
		public String getName()
		{
			return name;
		}

}
