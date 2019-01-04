@Description(description = "Metoda zwraca długośc łuku okręgu. 1 arg. to kąt alfa 2 arg. to długość promienia podany w cm. Wynik zwracany jest w [cm^2]")
public class DlugoscLuku implements ICallable
{
	@Override
	public String call(String arg1, String arg2)
	{
		return String.valueOf((Double.parseDouble(arg1)*((2*Math.PI)*Double.parseDouble(arg2)))/360);
	}
}