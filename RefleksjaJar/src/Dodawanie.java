@Description(description = "Metoda zwraca sume dwóch liczb")
public class Dodawanie implements ICallable
{
	@Override
	public String call(String arg1, String arg2)
	{
		return String.valueOf(Double.parseDouble(arg1)+Double.parseDouble(arg2));
	}

}
