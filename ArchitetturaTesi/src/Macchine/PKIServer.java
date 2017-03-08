package Macchine;

import java.security.*;


public class PKIServer {
	private Key publickeyMith = Utils.KeyGeneratorx.loadPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDauTZcKAMItIweRhhQdaQTHZgHq8Cza/fpX4ZW28SgA42zfRrdlQH+9k1cQSTXbgX/UUhTOofv6Dn49BSFzq8ukzv39tVTz/MStyEf2OKjTM0MXtRRvw91ssTjsy816zxlem2bMEL0/pvKdWC8IQBhZtQPWqz6uiaTgNnQQCUjFQIDAQAB");
	private Key publickeyTFE = Utils.KeyGeneratorx.loadPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCd+RKF5MC2jyWEvqeRCiVthfcQ076n0ABr9AThiGGPpvSejmqcqi4NVywWON76wVu57cYhIB2SA3kVExLqMNTT0lQYyUSrJ2vf536oXghxF/K2FU9wvPs7ga39Wc3qbZqYo4VKLkDEptwWGFQ7CScx8of0nUkGea98tlkBxZ0uuQIDAQAB");
	private Key publickeyTSS = Utils.KeyGeneratorx.loadPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLi0I4Nvnj+dONIzD429UXHRrtJF3loqnP099Ghu29uBiuHKeOFQbaCr6GKILMRMgvsyf8Ac/VACgxNWXCHuzKEYAD9uWjhIcA8vrxY4quDSMZEgmu7h8wvgs1T9dWJPUswgwm+wFL7mU8nBFhl+OjLEUje7imwBJDi+lIQK61gwIDAQAB");
	private Key publickeyFS = Utils.KeyGeneratorx.loadPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAWDn81c+zLnfn7KuZ1JemEoOmnJA1jayJgte5S8opGxX116rPEfXoomH7a+1v/ND7TzmIiFzzUwL/6wyUzwFW7c8gpLQRhjfMqrvxv/uCz24e2u7SXsTSTnS9y/WLr3HawgUeMkriKzJdP5jciyHdHobqJpFdXFakJ013QGrANwIDAQAB");
	
	public PKIServer() throws Exception {
		
	}

	public Key getPublickeyMith() {
		return publickeyMith;
	}

	public void setPublickeyMith(Key publickeyMith) {
		this.publickeyMith = publickeyMith;
	}

	public Key getPublickeyTFE() {
		return publickeyTFE;
	}

	public void setPublickeyTFE(Key publickeyTFE) {
		this.publickeyTFE = publickeyTFE;
	}

	public Key getPublickeyTSS() {
		return publickeyTSS;
	}

	public void setPublickeyTSS(Key publickeyTSS) {
		this.publickeyTSS = publickeyTSS;
	}

	public Key getPublickeyFS() {
		return publickeyFS;
	}

	public void setPublickeyFS(Key publickeyFS) {
		this.publickeyFS = publickeyFS;
	}
	
	
}
