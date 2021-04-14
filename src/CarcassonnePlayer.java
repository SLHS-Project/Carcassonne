/*
 * TODO
 * -Takes care of Meeples
 * -Keep track of individual score
 * -Serves as identification for Game class
 */
public class CarcassonnePlayer {
	private int score;
	
		public CarcassonnePlayer()
		{
			score=0;
		}
	
		public void addScore(int num)
		{
			score+=num;
		}

}
