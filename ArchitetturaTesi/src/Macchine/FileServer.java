package Macchine;
//import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import Modelli.*;

public class FileServer {
	private PKIServer pki = new PKIServer();
	private ServerSocket s;
	private Socket socket;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private Pacchetto pacchettoCorrente;
	private InetAddress indirizzoTSS = InetAddress.getByName("127.0.0.1");
	private InetAddress indirizzoTFE = InetAddress.getByName("127.0.0.1");
	private final int PORTA_COMUNICAZIONE_FS_TSS = 4513;
	private final int PORTA_COMUNICAZIONE_TFE_FS = 4511;
	private String path = "C:/Users/IBM_ADMIN/Desktop/Tesi/Socketjava/FileSulFS/";
	private Key privateKey = Utils.KeyGeneratorx.loadPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIBYOfzVz7Mud+fsq5nUl6YSg6ackDWNrImC17lLyikbFfXXqs8R9eiiYftr7W/80PtPOYiIXPNTAv/rDJTPAVbtzyCktBGGN8yqu/G/+4LPbh7a7tJexNJOdL3L9YuvcdrCBR4ySuIrMl0/mNyLId0ehuomkV1cVqQnTXdAasA3AgMBAAECgYAxVThc5jai8j6myEMhqyTrcfxmw5Fu9FkYtlGDaQ15BW9IgzClzYqPdDNjeBXZUP1nTAmaYIgSA5uIkWnRnUwu890+eS2q9sUb7Y/iKUJCufBw6GyGhrEIS8k6feazfPZ4J0z+Ih58EFz6us8/6ujA0JtE1YZT8bQaxc3EsBQaMQJBAPCJ58dwCewbvZZYn4CxkzphT7TI/YhbC3DqMXwoyx8fykbMwrdvTBrfdt8sxOJc4sGjuEU7B9090YXCpjwSkU8CQQCImCdZrAJvYErEOqy2CrdKd37EIJ5ddxwmVsnSus0wVmcL8Nm/ABEz3w7oAjasVGHAQ6v7qpCkHjGemY6bhZiZAkEA0bkbupsoO5HQUmcA4x9w4+T2rwTTcXEYX6IB9WRH/eIk5mgfmfpTW9bc+Sc6nrcoCnep8crPR4pfxYV6lUQHJwJAaNwdgUp6SBQs8X11rv6E/SbE6z17NZdFxezkXODIum3qB5GrJ2Se0CpR4Cwq2pqgvB93POkQxtzjDLUndB7YKQJANEGgqtpiINXUyrjSzcvqqEEYZ8mFQzwOSkMiORXk+2H3V1hJp0T+tqyyUGtWlGupSz3ojij7rjH2YAdikeG1cg==");
	private Key simmetricKey;
	
	public FileServer() throws Exception {
		this.pacchettoCorrente = new Pacchetto(0);
	}
	
	public static void main(String[] args) throws Exception {
		FileServer fs = new FileServer();		
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
	
	//metodo che dato il nome del file e una chiave restitusce il file criptato come un array di byte
	public byte[] criptaFile(String nomeFile, Key sk) throws IOException {
		String pathDelFile = this.path+nomeFile;		
		Path path = Paths.get(pathDelFile);
		byte[] data = Files.readAllBytes(path);
		byte[] crypted = Utils.KeyGeneratorx.AESEncrypt(data, sk);
		return crypted;	
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
	public void sconnettiMithDalTSS(){
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
	
	//metodo per l'inserimento del file del pacchetto ricevuto in un nuovo pacchetto contenente il file
	public void inserisciFileNelPacchetto() throws IOException {
		String filePath = this.path+new String(this.pacchettoCorrente.getFilename());
		File fileRichiesto = new File(filePath);
		Pacchetto pacchettoConFile = new Pacchetto((int)fileRichiesto.length());
		this.pacchettoCorrente.copiaPacchetto(pacchettoConFile);
		System.out.println(new String(pacchettoConFile.getFilename()));
		System.out.println(pacchettoConFile.getLevel());
		Path path = Paths.get(filePath);
		byte[] data = Files.readAllBytes(path);
		pacchettoConFile.setFile(data);
		this.setPacchettoCorrente(pacchettoConFile);		
	}
	
	//getter and setter	

	public Pacchetto getPacchettoCorrente() {
		return this.pacchettoCorrente;
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
