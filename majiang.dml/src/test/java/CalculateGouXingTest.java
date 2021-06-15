
import com.dml.majiang.player.shoupai.ShoupaiCalculator;
import com.dml.majiang.player.shoupai.gouxing.GouXingCalculator;
import com.dml.majiang.player.shoupai.gouxing.GouXingCalculatorHelper;

public class CalculateGouXingTest {
	public static void main(String[] args) {
		GouXingCalculatorHelper.gouXingCalculator = new GouXingCalculator(17, 3);
		ShoupaiCalculator c = new ShoupaiCalculator();
		int[] paiQuantityArray = new int[] {
		        0, 1, 1, 1, 0, 2, 0, 0, 1,
                0, 1, 0, 0, 0, 0, 0, 0, 0,
                0, 2, 1, 2, 0, 2, 1, 1, 2};
		c.setPaiQuantityArray(paiQuantityArray);
		c.calculateAllGouXing();
	}
}
