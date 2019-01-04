@Description(description = "Metoda zwraca większą z dwóch podanych wartości")
public class Max implements ICallable
{
	@Override
	public String call(String arg1, String arg2)
	{
			return String.valueOf(Math.max(Double.parseDouble(arg1),Double.parseDouble(arg2)));
	}
}