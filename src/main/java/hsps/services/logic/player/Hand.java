package hsps.services.logic.player;

import hsps.services.logic.basic.Symbolik;
import hsps.services.logic.cards.Farbwert;
import hsps.services.logic.cards.Karte;

import java.util.LinkedList;
import java.util.List;

public class Hand {

    private boolean re;
    private List<Karte> karten;

    public Hand() {
        resetKarten();
    }

    public void resetKarten() {
        karten = new LinkedList<Karte>();
    }

    public void removeKarte(Karte karte) {
        karten.remove(karte);
    }

    public void addKarte(Karte karte) {
        if (karte.getSymbolik() == Symbolik.DAME && karte.getFarbwert() == Farbwert.KREUZ) re = true;
        karten.add(karte);
    }

    public boolean isRe() {
        return re;
    }

    public List<Karte> getKarten() {
        return karten;
    }

    @Override
    public String toString() {
        String s = "[";
        boolean first = true;
        for (Karte k : karten) {
            if (first)
                first = false;
            else
                s += ", ";
            s += k;
        }
        s += "]";
        return s;
    }
}
