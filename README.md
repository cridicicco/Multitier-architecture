# Multitier-architecture
Implementazione e validazione di un'applicazione distribuita per Document Retrieval in ambito Data Center mediante tecniche di sicurezza multilivello

NOTA: il progetto deve essere utilizzato nel seguente modo:
- collegare 5 macchine in ambiente virtualizzato o fisico
- installare e configurare i protocolli per la comunicazione IPsec e TLS
- su ogni macchina va inserita la classe della macchina di riferimento e il package utility


Abstract della tesi:
Lo studio condotto prevede l’implementazione e l’analisi delle prestazioni di un’architettura multilivello distribuita attraverso un ambiente virtuale. L’architettura è stata sviluppata utilizzando tecniche di sicurezza multillivello instaurando uno stack di sicurezza nella pila ISO-OSI tramite i protocolli IPsec e TLS. All’ interno di ogni macchina è stato aggiunto un’ulteriore livello di sicurezza implementando la “Cipher suite B” proposta dalla National Security Agency utilizzando tecniche di crittografia sicure quali: Algoritmi d crittografia simmetrici ed asimmetrici, Funzioni Hash e Firme Digitali tramite le JAVA security APIs. Nell’architettura proposta è stato analizzato un caso d’uso per il download di un file da un utente registrato nel sistema arrivando alla conclusione che l’inserimento dei livelli di sicurezza introduce un ritardo molto elevato nel processare la richiesta ma che può ritenersi accettabile in architetture che trattino dati sensibili e che quindi hanno bisogno di un elevata sicurezza.
