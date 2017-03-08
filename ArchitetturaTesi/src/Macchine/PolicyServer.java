package Macchine;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

import Modelli.*;

public class PolicyServer {
	private ServerSocket s;
	private Socket socket;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private Map<Utente, List<Competenza>> mappaCompetenze;
	private Map<String, Competenza> fileSulServer;
	private Pacchetto pacchettoCorrente;
	private final int PORTA_COMUNICAZIONE_TFE_PS = 4509;
	
	public PolicyServer() throws Exception {
		this.mappaCompetenze = new HashMap<>();
		this.fileSulServer = new HashMap<>();
	}
	
	public static void main(String[] args) throws Exception {
		PolicyServer ps = new PolicyServer();	
	}
	
	//metodo per la verifica delle competenze necessarie per la selezione di un file da parte dell'utente
	public boolean verificaLegittimita() {
		boolean haCompetenza = false;
		Competenza competenzaPacchetto = this.fileSulServer.get(new String(pacchettoCorrente.getFilename()));
		for(Utente u : this.mappaCompetenze.keySet()) {
			if(u.getId().equals(new String(this.pacchettoCorrente.getId()))) {
				for(Competenza c : this.mappaCompetenze.get(u)) {					
					if(c.getCompartment()==competenzaPacchetto.getCompartment()) {
						if(c.getLevel()>=competenzaPacchetto.getLevel()) {
							haCompetenza = true;
							this.pacchettoCorrente.setLevel((byte)c.getLevel());
							this.pacchettoCorrente.setCompartment((byte)c.getCompartment());
						}
					}
					
				}
				
			}
		}
		return haCompetenza;
	}
	
	//metodo per la connessione come server
	public void connectServer(int porta){
        try
        {
            this.s = new ServerSocket(porta);
            this.socket = this.s.accept();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
	
	//metodo per aspettare l'invio di un pacchetto dal client
	public void receivePacchetto(){
		Pacchetto pacchetto;
		try {
			this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
			pacchetto = (Pacchetto)(objectInputStream.readObject());
			this.setPacchettoCorrente(pacchetto);
			this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
			this.objectOutputStream.writeObject("La richiesta è stata presa in carico");			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//metodo per disconnettersi
	public void disconnect(){
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//getter and setter
	
	public void aggiungiUtente(Utente u, List<Competenza> c) {
		this.mappaCompetenze.put(u, c);
	}
	
	public void aggiungiFile(String s, Competenza c) {
		this.fileSulServer.put(s, c);
	}

	public Map<Utente, List<Competenza>> getMappaCompetenze() {
		return mappaCompetenze;
	}

	public void setMappaCompetenze(Map<Utente, List<Competenza>> mappaCompetenze) {
		this.mappaCompetenze = mappaCompetenze;
	}

	public Map<String, Competenza> getFileSulServer() {
		return fileSulServer;
	}

	public void setFileSulServer(Map<String, Competenza> fileSulServer) {
		this.fileSulServer = fileSulServer;
	}

	public Pacchetto getPacchettoCorrente() {
		return pacchettoCorrente;
	}

	public void setPacchettoCorrente(Pacchetto pacchettoCorrente) {
		this.pacchettoCorrente = pacchettoCorrente;
	}
	
}