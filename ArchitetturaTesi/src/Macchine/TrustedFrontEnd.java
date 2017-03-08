package Macchine;
import java.net.*;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.io.*;

import Modelli.*;

public class TrustedFrontEnd {
	private PKIServer pki = new PKIServer();
	private ServerSocket s;
	private Socket socket;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private Pacchetto pacchettoCorrente;
	private Key privateKey = Utils.KeyGeneratorx.loadPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ35EoXkwLaPJYS+p5EKJW2F9xDTvqfQAGv0BOGIYY+m9J6OapyqLg1XLBY43vrBW7ntxiEgHZIDeRUTEuow1NPSVBjJRKsna9/nfqheCHEX8rYVT3C8+zuBrf1ZzeptmpijhUouQMSm3BYYVDsJJzHyh/SdSQZ5r3y2WQHFnS65AgMBAAECgYEAkM6ffyMI0I/5WWyXF+oQEwyJROCE1TljGZIO41pr7A2kELAF5GRPYKU24YvyQjP6zD28r+LLmGwckwfv/8MRY0VLG3A0fLjvjvXnJKBIVEiQWKXvqCHKWoLXK4MeqYvruXTeVb1M2s/nf41eK9KGlWmmhyIRY5ahVtfFDox+0DECQQDSaR2SHIgZBk0/dyk76XKhALY3WlJIoyqWPBtKTPZb/4Tp2d5ibLQuLAEQCc80BtiIROa1GIH8yhAotsjmwAhFAkEAwDNjCqswxHYZSpuEKgNlKLv4E3qHSBIlDcAAHDxhnv7xt53VVYfyo4ndUOEqUenpMip4uTS98pybxub6Plm15QJAWEzSoxGkL9RMMLkiLclfWJ/C2GHoLCGFarzYuwqnTJ5jufu7btStnL7Mx11Gfk/tKOq5YwBg9KQbuepTTzsBfQJAEIOZy+3fl9HJ5IYuOenmxDQ6YXoSq5ebhW3s1IA/pwiivjMahGnU6EZNNMjT+QvPFJdfnhIxC+p23AU2lSriHQJABtQeKKaF/3fFJ/28iSuL86rWUWj/1c3e3bCpDJEqBqY+DjZvspGG6vqOmMy878jk6iMmFExYGv8io+HACMMSIw==");
	private Key simmetricKey;
	private InetAddress indirizzoPS = InetAddress.getByName("127.0.0.1");
	private InetAddress indirizzoMYTH = InetAddress.getByName("127.0.0.1");
	private InetAddress indirizzoFS = InetAddress.getByName("127.0.0.1");
	private final int PORTA_COMUNICAZIONE_TFE_MYTH = 4507;
	private final int PORTA_COMUNICAZIONE_TFE_PS = 4509;
	private final int PORTA_COMUNICAZIONE_TFE_FS = 4511;

	public TrustedFrontEnd() throws Exception {
		this.pacchettoCorrente = new Pacchetto(0);
	}

	public static void main(String[] args) throws Exception {
		TrustedFrontEnd tfe = new TrustedFrontEnd();
		tfe.connectClient(tfe.indirizzoMYTH, tfe.PORTA_COMUNICAZIONE_TFE_MYTH);
		tfe.sendPacchetto();
		tfe.disconnect();
	}
	
	
	
	//metodo per la connessione come client
	public void connectClient(InetAddress indirizzo, int porta){
        try
        {
            this.socket = new Socket(indirizzo, porta);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
	
	//metodo per disconnettersi
	public void disconnect(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	//metodo per l'invio di un pacchetto al server
	public void sendPacchetto() {
		try {
			this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
			this.objectOutputStream.writeObject(this.pacchettoCorrente);
			this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
			Object s = this.objectInputStream.readObject();
			System.out.println(s);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//metodo per la generazione della chiave simmetrica
	public void generaChiaveSimmetrica() throws NoSuchAlgorithmException {
		Key sk = Utils.KeyGeneratorx.generateAESSimmetricKey();
		this.simmetricKey = sk;		
	}

	
	//metodo per la connessione da server
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
		
	//metodo per la ricezione di un pacchetto dal client
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
	
	//getter and setter
	
	public Pacchetto getPacchettoCorrente() {
		return pacchettoCorrente;
	}

	public void setPacchettoCorrente(Pacchetto pacchettoCorrente) {
		this.pacchettoCorrente = pacchettoCorrente;
	}

	public Key getSimmetricKey() {
		return simmetricKey;
	}

	public void setSimmetricKey(Key simmetricKey) {
		this.simmetricKey = simmetricKey;
	}




}
