package transport.core;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class TitreTransport implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int id;
    protected LocalDateTime dateAchat;
    protected double prix;

    public TitreTransport(int id, LocalDateTime dateAchat) {
        this.id = id;
        this.dateAchat = dateAchat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(LocalDateTime dateAchat) {
        this.dateAchat = dateAchat;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public abstract boolean isValid(LocalDateTime date);

}
