package designpatterns.builder;

import java.time.LocalDate;

public class TestBuilder {
    public static void main(String[] args) {

        Client client = null;
        Adresse adresse = null;
        Location loc = null;

        try{
            client = new Client.ClientBuilder()
                    .setId(1)
                    .setNom("Lorfèvre")
                    .setPrenom("Arthur")
                    .setTel("0477444444")
                    .build();

            adresse = new Adresse.AdresseBuilder()
                    .setId(1)
                    .setNum("1")
                    .setRue("test")
                    .setLocalite("Test")
                    .setCp(5555)
                    .build();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        try {
            loc = new Location.LocationBuilder()
                    .setId(1)
                    .setDate(LocalDate.now())
                    .setClient(client)
                    .setAdrDepart(adresse)
                    .setKmTotal(12)
                    .build(true);
        }catch (Exception e){
            System.out.println("Ce message ne sera pas affiché");
        }

        try {
            loc = new Location.LocationBuilder()
                    .setId(1)
                    .setDate(LocalDate.now())
                    .setClient(null)
                    .setAdrDepart(adresse)
                    .setKmTotal(12)
                    .build(true);
        }catch (Exception e){
            System.out.println("ici, le client est null");
        }

        try {
            loc = new Location.LocationBuilder()
                    .setId(1)
                    .setDate(LocalDate.now())
                    .setClient(client)
                    .setAdrDepart(null)
                    .setKmTotal(12)
                    .build(true);
        }catch (Exception e){
            System.out.println("ici, l'adresse est null");
        }

        try {
            loc = new Location.LocationBuilder()
                    .setId(1)
                    .setDate(null)
                    .setClient(client)
                    .setAdrDepart(adresse)
                    .setKmTotal(12)
                    .build(true);
        }catch (Exception e){
            System.out.println("ici, la date est null");
        }

        try {
            loc = new Location.LocationBuilder()
                    .setId(1)
                    .setDate(null)
                    .setClient(client)
                    .setAdrDepart(adresse)
                    .setKmTotal(12)
                    .build(true);
        }catch (Exception e){
            System.out.println("ici, la date est null");
        }

        try {
            loc = new Location.LocationBuilder()
                    .setId(1)
                    .setDate(LocalDate.now().minusDays(1))
                    .setClient(client)
                    .setAdrDepart(adresse)
                    .setKmTotal(12)
                    .build(true);
        }catch (Exception e){
            System.out.println("ici, la date est avant aujourd'hui");
        }

        try {
            loc = new Location.LocationBuilder()
                    .setId(1)
                    .setDate(LocalDate.now().minusDays(1))
                    .setClient(client)
                    .setAdrDepart(adresse)
                    .setKmTotal(-5)
                    .build(true);
        }catch (Exception e){
            System.out.println("ici, le km total est négatif");
        }

    }
}
