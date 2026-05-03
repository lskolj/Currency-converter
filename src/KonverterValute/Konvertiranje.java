package KonverterValute;

import java.io.*;
import java.net.*;
import java.util.*;
public class Konvertiranje {

    private static final String API_URL = "https://api.hnb.hr/tecajn-eur/v3";

    public List<Valuta> dohvatiTecajeve() throws IOException {
        String json = dohvatiJSON();
        return parsingJSON(json);
    }

    private String dohvatiJSON() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection veza = (HttpURLConnection) url.openConnection();
        veza.setRequestMethod("GET");
        veza.setConnectTimeout(8000);
        veza.setReadTimeout(8000);

        BufferedReader citac = new BufferedReader(
                new InputStreamReader(veza.getInputStream(), "UTF-8"));

        StringBuilder sb = new StringBuilder();
        String linija;
        while ((linija = citac.readLine()) != null){
            sb.append(linija);
        }
        citac.close();
        return sb.toString();
    }

    private List<Valuta> parsingJSON(String json) {
        List<Valuta> lista = new ArrayList<>();
        String[] zapisi = json.split("\\{");

        for(String zapis : zapisi) {
            if(!zapis.contains("sifra_valute")) continue;

            String valuta = izvuci(zapis, "valuta");
            String drzava = izvuci(zapis,"drzava");
            String tecaj = izvuci(zapis,"srednji_tecaj");

            if(valuta == null || drzava == null || tecaj == null) continue;

            double tecajIznos = Double.parseDouble(tecaj.replace(",","."));
            lista.add(new Valuta(valuta,drzava,tecajIznos));
        }
        return lista;
    }

    private String izvuci(String json, String kljuc) {
        String trazeni ="\"" + kljuc + "\"";
        int index = json.indexOf(trazeni);
        if(index < 0) return null;

        int pocetak = json.indexOf("\"", index + trazeni.length()+1);
        if(pocetak < 0) return null;

        int kraj = json.indexOf("\"",pocetak+1);
        if(kraj < 0) return null;

        return json.substring(pocetak + 1,kraj).trim();
    }
}
