package Macchine;
import Modelli.Pacchetto;
import Modelli.Utente;

import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Timestamp;
import java.util.*;
import java.io.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MYTH {
	private ServerSocket s;
	private Socket socket;
	private PKIServer pki = new PKIServer();
	private final String MESSAGGIO_BENVENUTO = "Benvenuti nell'architettura MITH!";
	private final String localPath = "C:/Users/Cristian/Desktop/Tesi/Socketjava/FileSulMITH/";
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	private Utente utenteCorrente;
	private List<Utente> utentiRegistrati;
	private Pacchetto pacchettoCorrente;
	private List<String> listaFile;
	private InetAddress indirizzoTFE = InetAddress.getByName("127.0.0.1");
	private InetAddress indirizzoTSS = InetAddress.getByName("127.0.0.1");
	private final int PORTA_COMUNICAZIONE_TFE_MYTH = 4507;
	private Key privateKey = Utils.KeyGeneratorx.loadPrivateKey("MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANq5NlwoAwi0jB5GGFB1pBMdmAerwLNr9+lfhlbbxKADjbN9Gt2VAf72TVxBJNduBf9RSFM6h+/oOfj0FIXOry6TO/f21VPP8xK3IR/Y4qNMzQxe1FG/D3WyxOOzLzXrPGV6bZswQvT+m8p1YLwhAGFm1A9arPq6JpOA2dBAJSMVAgMBAAECgYEAu5eY0QecOo4nkxuJZRDQ7Xg3WXTsrLZQhb9hSAPrq1YA5c3KCfvwH03+Yb6Zq49441dCkeekiZYCisxFAdKYGeMjzoJpiFxcOSwcY8NT0J+2RUIiR9Gt1yqRvKg+3KGhZBiTAKx7vdVrd+qLSP9/gcmnkBQX+OKLZ4O8XMLnn8ECQQDz+Hk/RfqaoO1vBYD6Ic4hx4IUnLXAE54Hjfj1ATgoA9ub5sal9AU1JWxMcyNyFpxytgU23CC6Ww2PPGf+xC7lAkEA5YIOZjMMphcpz9KkFhPtsiEajwu+8BQi8UxtL7lLLAgod+WNWjBbVaV5BsKFz4jEjVOo/taScAh4aEt3WiOwcQJAZ63hgPUxQcNAA91X6XSyUAKkEjRivMTZdKZt9VjWT5MMId3Z766bA/HqazbHCnX3eHxIDP9RXUDSq1tlaoxezQJBAJPHfawS3OcpbRPTZzKMHHahWC+ZeZWu8Zz8ACZFooC0tBIXrj/PeVAZn2dZ471xLFZv8xY8Zchbu+Q0C6Lv7hECQQDW9mMRfFMjGEGeKsV2b1wbCwW4IxG2bHpltOpxaxPHXa8wRJvj52Gl5zQ9KspftQ8+pVE22gmOXsXXh+u5eGjl");
	private Key simmetricKey;

	public MYTH() throws Exception  {
		System.out.println(MESSAGGIO_BENVENUTO);
		this.utentiRegistrati = new ArrayList<>();
		this.listaFile = new ArrayList<>();
		this.pacchettoCorrente = new Pacchetto(0);
	}	

	public static void main(String[] args) throws Exception {
		MYTH myth = new MYTH();
		myth.connectServer(myth.PORTA_COMUNICAZIONE_TFE_MYTH);
		myth.receivePacchetto();
		myth.disconnect();
	}
	
	//metodo per permettere il loginUtente
	public void logInUtente() throws NullPointerException {
		try {
			System.out.println("Inserisci un Username");
			Scanner sc = new Scanner(System.in);
			String user = sc.nextLine();			
			System.out.println("Inserisci una Password");
			Scanner sc2 = new Scanner(System.in);
			String password = sc2.nextLine();		
			System.out.println("");
			Utente u = verificaCredenziali(user, password);
			System.out.println("L'utente " +u.getNome()+" "+ u.getCognome() + " è connesso");
			System.out.println("");
			String nomeCognome = u.getNome()+" "+u.getCognome();
			this.pacchettoCorrente.setUser(nomeCognome.getBytes());
			this.pacchettoCorrente.setId(u.getId().getBytes());		
		}
		catch (NullPointerException e) {
			System.out.println("Username o Password errati");
			System.exit(1);
		}
	}
	
	//metodo per decrittare un file in locale
	public void decriptaFile(String nomeFile, Key sk) throws IOException {
		File fileCriptato = new File(this.localPath+nomeFile);
		Path path = Paths.get(this.localPath+nomeFile);
		byte[] data = Files.readAllBytes(path);		
		byte[] decryptedFile = Utils.KeyGeneratorx.AESDecrypt(data, sk);
		FileOutputStream fos2 = new FileOutputStream(this.localPath+nomeFile);
		fos2.write(decryptedFile);
		fos2.close();	
	}

	//metodo per la registrazione di un utente
	public void registraUtente(Utente u) {
		System.out.println("Ciao "+u.getNome()+" "+u.getCognome());
		System.out.println("Inserisci un Username");
		Scanner sc = new Scanner(System.in);
		String user = sc.next();
		u.setUser(user);
		System.out.println("Inserisci una Password");
		Scanner sc2 = new Scanner(System.in);
		String password = sc2.next();	
		u.setPassword(password);
		System.out.println("Operazione completata con successo!");		
		System.out.println("");
		this.utentiRegistrati.add(u);
	}
	
	//metodo per la verifica di user e password
	public Utente verificaCredenziali(String user, String password) {
		for(Utente u : this.utentiRegistrati) {
			if(u.getUser().equals(user)) {
				if(u.getPassword().equals(password)) {
					return u;
				}
			}
		}
		return null;
	}

	//metodo che permette all'utente di selezionare un file
	public String selezionaFile() {
		String trovato = "";
		System.out.println("Questa è la lista dei file disponibili :");
		for(String s : this.listaFile) {
			System.out.println(s);
		}
		Scanner sc = new Scanner(System.in);
		String file = sc.nextLine();
		sc.close();
		if(this.listaFile.contains(file)) {
			System.out.println("File selezionato : " +file);
			trovato = file;
			this.pacchettoCorrente.setFilename(trovato.getBytes());
		}
		else 
			System.out.println("Il file richiesto non esiste");
		return trovato;
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

	//metodo per la connessione come client
	public void connectClient(InetAddress indirizzo, int porta) {
		try
		{	
			this.socket = new Socket(indirizzo, porta);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	//metodo per la disconnessione 
	public void disconnect(){
		try {
			this.socket.close();
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
			String s = (String)this.objectInputStream.readObject();
			System.out.println(s);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	//metodo per scaricare il file da remoto
	public void ottieniFile() throws IOException {
		String remotePath = "//"+this.indirizzoTSS.getHostAddress()+"/"+new String(this.pacchettoCorrente.getFilepath());				
		String nomeFile = new String(this.pacchettoCorrente.getFilename());
		String localPath = this.localPath+"\\"+nomeFile;
		System.out.println(remotePath);
		Path path = Paths.get(remotePath);
		byte[] data = Files.readAllBytes(path);
		FileOutputStream fos = new FileOutputStream(localPath);
		fos.write(data);
		fos.close();
	}
	
	//getter and setter

	public void aggiungiFile(String file) {
		this.listaFile.add(file);
	}

	public Pacchetto getPacchettoCorrente() {
		return pacchettoCorrente;
	}

	public void setPacchettoCorrente(Pacchetto pacchettoCorrente) {
		this.pacchettoCorrente = pacchettoCorrente;
	}

	public Utente getUtenteCorrente() {
		return utenteCorrente;
	}

	public void setUtenteCorrente(Utente utenteCorrente) {
		this.utenteCorrente = utenteCorrente;
	}

	public List<Utente> getUtentiRegistrati() {
		return utentiRegistrati;
	}

	public void setUtentiRegistrati(List<Utente> utentiRegistrati) {
		this.utentiRegistrati = utentiRegistrati;
	}

	public List<String> getListaFile() {
		return listaFile;
	}

	public void setListaFile(List<String> listaFile) {
		this.listaFile = listaFile;
	}

	public Key getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(Key privateKey) {
		this.privateKey = privateKey;
	}

}
