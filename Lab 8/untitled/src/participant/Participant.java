package participant;

import java.io.Serializable;

public class Participant implements Serializable {
    private String name;
    private String family;
    private String organization;
    private String report;
    private String email;
    public Participant(String name, String family, String organization, String report, String email) {
        this.name = name;
        this.family = family;
        this.organization = organization;
        this.report = report;
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public String getFamily() {
        return family;
    }
    public String getOrganization() {
        return organization;
    }
    public String getReport() {
        return report;
    }
    public String getEmail() {
        return email;
    }
    @Override
    public String toString() {
        return "Name: " + name +
                ", Family: " + family +
                ", Organization: " + organization +
                ", Report: " + report +
                ", e-mail: " + email;
    }
}
