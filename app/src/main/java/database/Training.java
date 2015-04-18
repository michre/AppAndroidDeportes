package database;

public class Training {

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String place;
    private String team;
    private String id;



    public Training( String date, String place, String team, String id) {
        super();
        this.date = date;
        this.place = place;
        this.team = team;
        this.id = id;
    }
}
