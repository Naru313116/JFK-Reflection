@Description(description = "Metoda zwraca potęgę liczby, której podstawą jest 1 arg. Potęgę zaś definiuje wartość w 2arg. ")
public class Potegowanie implements ICallable
{
    @Override
    public String call(String arg1, String arg2)
    {
        return String.valueOf(Math.pow(Double.parseDouble(arg1),Double.parseDouble(arg2)));
    }
}