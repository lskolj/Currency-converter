package KonverterValute;

public class Valuta {

    private String valuta; //USD
    private String drzava; //USA
    private double tecaj; //tecaj prema EUR

    public Valuta(String valuta, String drzava, double tecaj){
        this.valuta = valuta;
        this.drzava = drzava;
        this.tecaj = tecaj;
    }

    public String getValuta() { return valuta;}
    public String getDrzava() {return drzava;}
    public double getTecaj() {return tecaj;}

    @Override
    public String toString() {
        return valuta  + "("+ drzava+")";
    }
}
