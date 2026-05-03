# HNB Tečajni Konverter
Java desktop aplikacija za konverziju valuta koristeći službene tečajeve Hrvatske narodne banke (HNB).

Opis

Ova aplikacija dohvaća aktualne tečajeve s HNB API-ja i omogućuje jednostavnu konverziju između eura i stranih valuta putem grafičkog sučelja.

Tehnologije

Java
Swing (GUI)
HNB API (https://api.hnb.hr/tecajn-eur/v3)
⚙️ Funkcionalnosti
Dohvat aktualnih tečajeva
Konverzija:
EUR → strana valuta
strana valuta → EUR
Prikaz srednjeg tečaja
Osvježavanje podataka
Jednostavno i pregledno GUI sučelje

Struktura projekta
KonverterValute/
│
├── Main.java              # Pokretanje aplikacije
├── ConverterGUI.java      # Grafičko sučelje
├── Konvertiranje.java     # Dohvat i obrada API podataka
└── Valuta.java            # Model klase za valutu

Pokretanje
Kloniraj repozitorij:
git clone https://github.com/lskolj/Currency-converter.git
Otvori projekt u IDE-u (npr. IntelliJ / Eclipse)
Pokreni Main.java

API

Podaci se dohvaćaju s:

https://api.hnb.hr/tecajn-eur/v3

!! Napomene
Aplikacija zahtijeva internet vezu
Ako API nije dostupan, prikazat će se greška
Parsiranje JSON-a je ručno implementirano (bez biblioteka)

Moguća poboljšanja
Dodati JSON biblioteku (npr. Gson ili Jackson)
Spremanje povijesti konverzija
Bolji dizajn UI-a
Validacija unosa u realnom vremenu
