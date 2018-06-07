package hsps.services.logic.player;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hsps.services.MqttService;
import hsps.services.exception.NotAValidCardException;
import hsps.services.exception.NotYourTurnException;
import hsps.services.logic.basic.Observer;
import hsps.services.logic.basic.Spiel;
import hsps.services.logic.basic.Stich;
import hsps.services.logic.cards.Karte;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;
import hsps.services.mqtt.Topic;

public class Spieler extends Observer {

	private String name;
	private String ip;
	private List<Stich> gesammelteStiche;
	private Hand hand;
	private int spielerNr;

	public String getName() {
		return name;
	}

	@JsonIgnore()
	private Spiel spiel;
	private Statistik statistik;
	private boolean solo;

	public Spieler( Spiel spiel, String name ) {
		this.spiel = spiel;
		this.name = name;
		gesammelteStiche = new ArrayList<Stich>();
		hand = new Hand();
		statistik = new Statistik();
		spielerNr = spiel.getSpielerNr( this );
	}

	public void addStich( Stich stich ) {
		gesammelteStiche.add( stich );
	}

	public void karteAusgesucht( Karte karte ) throws NotAValidCardException, NotYourTurnException {
		spiel.spielzugAusfuehren( this, karte );
	}

	public List<Stich> getGesammelteStiche() {
		return gesammelteStiche;
	}

	// Berechnung und Rueckgabe der gesammelten Stichpunkte
	public int getStichpunkte() {
		int sum = 0;
		for( Stich s : gesammelteStiche ) {
			sum += s.getPunktezahl();
		}
		return sum;
	}

	public Hand getHand() {
		return hand;
	}

	public boolean isRe() {
		return hand.isRe();
	}

	public boolean isSolo() {
		return solo;
	}

	public void setSolo( boolean solo ) {
		this.solo = solo;
	}

	public Statistik getStatistik() {
		return statistik;
	}

	// Die Update-Methode wird vom Spiel aufgerufen, wenn der Spieler eine Karte
	// aussuchen soll
	@Override
	public synchronized void update() {
		System.out.println(this.getName() + " ist nun an der Reihe!");
		MqttService.publisher.publishData( new Message( MessageType.YourTurn ), Topic.genPlayerTopic( spielerNr ) );
		/*System.out.println( "   Das sind deine Karten, " + this + ":" );
		System.out.print( "      " );
		for( int i = 0; i < getHand().getKarten().size(); i++ ) {
			System.out.print( i + ": " + getHand().getKarten().get( i ) + " - " );
		}
		System.out.println( "" );
		System.out.print( "   Bitte waehle eine Karte aus ... " );
		System.out.print( "Eingabe der Kartennummer: " );
		Scanner s = new Scanner( System.in );
		Karte k = getHand().getKarten().get( s.nextInt() );
		System.out.println( k + " = Ausgesuchte Karte" );
		System.out.println();
		karteAusgesucht( k );*/
	}

	@Override
	public String toString() {
		return name;
	}
}
