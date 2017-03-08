package Modelli;
import java.io.*;

public class Pacchetto implements Serializable {
	private byte[] user = new byte[30];
	private byte[] id = new byte[16];
	private byte[] service = new byte[2];
	private byte[] filename = new byte[255];
	private byte[] nonce = new byte[16];
	private byte[] timestamp = new byte[18];
	private byte[] expTime = new byte[8];
	private byte[] digest = new byte[172];
	private byte[] filepath = new byte[4096];
	private byte[] fileEncryptionKey = new byte[172];
	private byte level;
	private byte compartment;
	private byte[] file;
	
	public Pacchetto(int grandezzaFile) {
		this.file = new byte[grandezzaFile];
	}
	
	public void aggiornaTimestampPacchetto() {
		String tempoVecchio = new String(this.getTimestamp());
		String tempoArrivo = new Double((System.currentTimeMillis()-Double.parseDouble(tempoVecchio))).toString();
		this.setTimestamp(tempoArrivo.getBytes());		
	}
	
	public void copiaPacchetto(Pacchetto p) {
		p.setUser(this.getUser());
		p.setId(this.getId());
		p.setService(this.getService());
		p.setFilename(this.getFilename());
		p.setNonce(this.getNonce());
		p.setTimestamp(this.getTimestamp());
		p.setExpTime(this.getExpTime());
		p.setDigest(this.getDigest());
		p.setFilepath(this.getFilepath());
		p.setFileEncryptionKey(this.getFileEncryptionKey());
		p.setLevel(this.getLevel());
		p.setCompartment(this.getCompartment());		
	}

	public byte[] getUser() {
		return user;
	}

	public void setUser(byte[] user) {
		this.user = user;
	}

	public byte[] getId() {
		return id;
	}

	public void setId(byte[] id) {
		this.id = id;
	}

	public byte[] getService() {
		return service;
	}

	public void setService(byte[] service) {
		this.service = service;
	}

	public byte[] getFilename() {
		return filename;
	}

	public void setFilename(byte[] filename) {
		this.filename = filename;
	}

	public byte[] getNonce() {
		return nonce;
	}

	public void setNonce(byte[] nonce) {
		this.nonce = nonce;
	}

	public byte[] getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(byte[] timestamp) {
		this.timestamp = timestamp;
	}

	public byte[] getExpTime() {
		return expTime;
	}

	public void setExpTime(byte[] expTime) {
		this.expTime = expTime;
	}

	public byte[] getDigest() {
		return digest;
	}

	public void setDigest(byte[] digest) {
		this.digest = digest;
	}

	public byte[] getFilepath() {
		return filepath;
	}

	public void setFilepath(byte[] filepath) {
		this.filepath = filepath;
	}

	public byte[] getFileEncryptionKey() {
		return fileEncryptionKey;
	}

	public void setFileEncryptionKey(byte[] fileEncryptionKey) {
		this.fileEncryptionKey = fileEncryptionKey;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public byte getCompartment() {
		return compartment;
	}

	public void setCompartment(byte compartment) {
		this.compartment = compartment;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}	
	
}
