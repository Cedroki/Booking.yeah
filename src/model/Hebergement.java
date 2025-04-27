package model;

public class Hebergement {
    private int id;
    private String nom;
    private String adresse;
    private String description;
    private String photos;
    private double prix;
    private String categorie;
    private String fourchette;
    private String ville;

    // 🔥 NOUVEAUX CHAMPS
    private boolean wifi;
    private boolean piscine;
    private boolean parking;
    private boolean climatisation;
    private boolean restaurant;
    private boolean roomService;
    private boolean spa;
    private boolean animauxAcceptes;
    private boolean vueMer;
    private boolean salleDeSport;

    // 🔵 CONSTRUCTEURS

    public Hebergement() {
        // Constructeur vide utile pour initialisations
    }

    public Hebergement(int id, String nom, String adresse, String description, String photos, double prix,
                       String categorie, String fourchette, String ville,
                       boolean wifi, boolean piscine, boolean parking, boolean climatisation,
                       boolean restaurant, boolean roomService, boolean spa, boolean animauxAcceptes,
                       boolean vueMer, boolean salleDeSport) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.description = description;
        this.photos = photos;
        this.prix = prix;
        this.categorie = categorie;
        this.fourchette = fourchette;
        this.ville = ville;
        this.wifi = wifi;
        this.piscine = piscine;
        this.parking = parking;
        this.climatisation = climatisation;
        this.restaurant = restaurant;
        this.roomService = roomService;
        this.spa = spa;
        this.animauxAcceptes = animauxAcceptes;
        this.vueMer = vueMer;
        this.salleDeSport = salleDeSport;
    }

    // Constructeur sans ID (insertion)
    public Hebergement(String nom, String adresse, String description, String photos, double prix,
                       String categorie, String fourchette, String ville,
                       boolean wifi, boolean piscine, boolean parking, boolean climatisation,
                       boolean restaurant, boolean roomService, boolean spa, boolean animauxAcceptes,
                       boolean vueMer, boolean salleDeSport) {
        this.nom = nom;
        this.adresse = adresse;
        this.description = description;
        this.photos = photos;
        this.prix = prix;
        this.categorie = categorie;
        this.fourchette = fourchette;
        this.ville = ville;
        this.wifi = wifi;
        this.piscine = piscine;
        this.parking = parking;
        this.climatisation = climatisation;
        this.restaurant = restaurant;
        this.roomService = roomService;
        this.spa = spa;
        this.animauxAcceptes = animauxAcceptes;
        this.vueMer = vueMer;
        this.salleDeSport = salleDeSport;
    }

    // 🔵 GETTERS ET SETTERS

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPhotos() { return photos; }
    public void setPhotos(String photos) { this.photos = photos; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public String getFourchette() { return fourchette; }
    public void setFourchette(String fourchette) { this.fourchette = fourchette; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public boolean isWifi() { return wifi; }
    public void setWifi(boolean wifi) { this.wifi = wifi; }

    public boolean isPiscine() { return piscine; }
    public void setPiscine(boolean piscine) { this.piscine = piscine; }

    public boolean isParking() { return parking; }
    public void setParking(boolean parking) { this.parking = parking; }

    public boolean isClimatisation() { return climatisation; }
    public void setClimatisation(boolean climatisation) { this.climatisation = climatisation; }

    public boolean isRestaurant() { return restaurant; }
    public void setRestaurant(boolean restaurant) { this.restaurant = restaurant; }

    public boolean isRoomService() { return roomService; }
    public void setRoomService(boolean roomService) { this.roomService = roomService; }

    public boolean isSpa() { return spa; }
    public void setSpa(boolean spa) { this.spa = spa; }

    public boolean isAnimauxAcceptes() { return animauxAcceptes; }
    public void setAnimauxAcceptes(boolean animauxAcceptes) { this.animauxAcceptes = animauxAcceptes; }

    public boolean isVueMer() { return vueMer; }
    public void setVueMer(boolean vueMer) { this.vueMer = vueMer; }

    public boolean isSalleDeSport() { return salleDeSport; }
    public void setSalleDeSport(boolean salleDeSport) { this.salleDeSport = salleDeSport; }

    // 🔵 toString()
    @Override
    public String toString() {
        return "Hebergement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", description='" + description + '\'' +
                ", photos='" + photos + '\'' +
                ", prix=" + prix +
                ", categorie='" + categorie + '\'' +
                ", fourchette='" + fourchette + '\'' +
                ", ville='" + ville + '\'' +
                ", wifi=" + wifi +
                ", piscine=" + piscine +
                ", parking=" + parking +
                ", climatisation=" + climatisation +
                ", restaurant=" + restaurant +
                ", roomService=" + roomService +
                ", spa=" + spa +
                ", animauxAcceptes=" + animauxAcceptes +
                ", vueMer=" + vueMer +
                ", salleDeSport=" + salleDeSport +
                '}';
    }
}
