package Macchine;
import Modelli.*;
import Utils.*;

import java.security.*;
import java.util.*;
import java.io.*;
import java.net.*;


public class TransitionalSecureServer {
	private PKIServer pki = new PKIServer();
	private ServerSocket s;
	private Socket socket;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	private Pacchetto pacchettoCorrente;
	private String path = "C:/Users/IBM_ADMIN/Desktop/Tesi/Socketjava/FileSulTSS/Livello ";
	private Timer timer = new Timer();
	private InetAddress indirizzoFS = InetAddress.getByName("127.0.0.1");
	private final int PORTA_COMUNICAZIONE_FS_TSS = 4513;
	private TimerTask rimuoviFile;
	private Key privateKey = Utils.KeyGeneratorx.loadPrivateKey("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIuLQjg2+eP5040jMPjb1RcdGu0kXeWiqc/T30aG7b24GK4cp44VBtoKvoYogsxEyC+zJ/wBz9UAKDE1ZcIe7MoRgAP25aOEhwDy+vFjiq4NIxkSCa7uHzC+CzVP11Yk9SzCDCb7AUvuZTycEWGX46MsRSN7uKbAEkOL6UhArrWDAgMBAAECgYEAisGTvDA2dZHC4XHfzWVS06P2VV43HBOORYEJYKjkmCUKLX+CwQ55fLD4WJDJ1vKCGKROGxF2Jvf7/0p4f/meO6weg13DII7wbI0VrsAcpRZz2kZ6PtqllFXeD45fmv3KqZQYCBH4BInscRM1T21ugsY3fHtyxIpHnELB7MrRkyECQQC9j83TSpK1vn9WXwjC7sdXa5GtMy0oWfp+oRTNrqRYNUcteFgzBBk7h+GBl+hcvDKgWMVs3iSx/3gnLAA+z5rxAkEAvHOsJGu/SDSK2GjfOkP8ynJ4H38JXFDGbswHMz7JRzG8TFEKYOSBmacIzjMlOVLc7mwVc0L8qcELBBPEHgVPswJBALg1NGitQSBH/Gosc9EqRKCZMblD0BA0UA4Z7qNpoN7u9zoihmMdCoAlLWjH+8+SOwhC+6ctwC8Q+OvoAF0JVaECQEq6g9SddWiojTZrv7lXJHMTGVdEBO46ibV96o0DpMU6j7Sjnj313v6TIgbCeUxV844IBimCsraN+lRPkkFlUckCQC8VLG8k8spGaPl3p2v3erpMgMdiZz5fuMP37vw6hjnMHSyIlZmHTwPs9VlY0Ucer2OUV4tv+FWYaS+ISd6YvJ4=");
	
	public TransitionalSecureServer() throws Exception{
		this.pacchettoCorrente = new Pacchetto(0);
	}
	
	public static void main(String[] args) throws Exception {
		TransitionalSecureServer tss = new TransitionalSecureServer();
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
	
	public void disconnect(){
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	//metodo per ricevere il file criptato e salvarlo in una partizione sicura dove sarà eliminato
	//dopo il tempo indicato nell'expTime
	public void salvaFileInLocale() throws IOException {
		String livelloPacchetto = Byte.toString(this.pacchettoCorrente.getLevel());
		String nomeFile = new String(this.pacchettoCorrente.getFilename());
		final String filePath = path+livelloPacchetto+"/"+nomeFile;
		System.out.println(filePath);
		FileOutputStream fos = new FileOutputStream(filePath);
		fos.write(this.pacchettoCorrente.getFile());
		fos.close();
		this.pacchettoCorrente.setFile(null);
		this.pacchettoCorrente.setFilepath(filePath.getBytes());
		this.rimuoviFile = new TimerTask() {
			public void run() {
				File file = new File(filePath);
				file.delete();
			}
		};
		this.timer.schedule(this.rimuoviFile, Long.parseLong(new String(this.pacchettoCorrente.getExpTime())));
	}
	

	//getter and setter
	
	public Pacchetto getPacchettoCorrente() {
		return pacchettoCorrente;
	}


	public void setPacchettoCorrente(Pacchetto pacchettoCorrente) {
		this.pacchettoCorrente = pacchettoCorrente;
	}


}
