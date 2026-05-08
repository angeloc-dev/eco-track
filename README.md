# EcoTrack 🌱

**EcoTrack** è una piattaforma web full-stack progettata per promuovere abitudini ecologiche, incentivare la sostenibilità e costruire una comunità attiva nel rispetto dell'ambiente attraverso un sistema di sfide ed eventi.

## 🎯 Obiettivo del Progetto
EcoTrack mira a sensibilizzare e responsabilizzare gli utenti verso comportamenti più sostenibili (riduzione dei rifiuti, gestione ottimizzata delle risorse). Il sistema motiva un cambiamento comportamentale positivo e misurabile attraverso la condivisione di buone pratiche ecologiche, supportato da un motore di **gamification** integrato.

## 🛠️ Stack Tecnologico

- **Frontend:** React (Interfacce modulari basate su componenti)
- **Backend:** Java, Spring Boot (Architettura API REST)
- **ORM & Data Management:** JPA, Hibernate

## ✨ Funzionalità Principali

- **Motore di Gamification:** Profili utente dinamici dotati di livelli e punteggi. Il completamento di sfide ecologiche individuali sblocca riconoscimenti (*Badge*), mentre il successo nelle attività collettive conferisce premi ai gruppi (*Trofei*).
- **Gestione Gruppi e Aggregazione:** Gli utenti possono aggregarsi in entità strutturate (Famiglie, Scuole, Aziende, Comunità).
- **Eventi e Sotto-attività:** Creazione e partecipazione a eventi ecologici comunitari, frammentabili in task specifici per aumentare l'interattività.
- **Role-Based Access Control (RBAC):**
  - *Utente Standard:* Tracciamento delle proprie attività, partecipazione alle sfide e visualizzazione delle bacheche di gruppo.
  - *Amministratore:* Diritti di gestione per l'organizzazione di nuovi eventi e il coordinamento delle sotto-attività dei gruppi amministrati.

## 💡 Sfide Tecniche e Ottimizzazioni
Lo sviluppo di EcoTrack ha richiesto focus su architetture pulite e manutenibili:
- **Gestione avanzata dello Stato (React):** Con l'aumento della complessità della UI, l'architettura dei componenti è stata curata per gestire in modo efficiente gli stati (locali e globali) e le dipendenze degli effetti (`useEffect`), prevenendo memory leak e garantendo la scalabilità del frontend.
- **Integrazione Database (Spring Boot):** Configurazione e ottimizzazione del layer di persistenza tramite **JPA e Hibernate**, con particolare attenzione alla corretta gestione delle dipendenze e delle relazioni complesse tra entità (Utenti, Gruppi, Eventi, Premi).

## 🚀 Come avviare il progetto in locale

### Prerequisiti
- Node.js e npm
- Java JDK 17+ e Maven
- Database relazionale (es. MySQL/MariaDB)

### Avvio Frontend (React)
```bash
cd frontend
npm install
npm start


### Avvio Backend (Spring Boot)
```bash
cd backend
mvn spring-boot:run

(Assicurati di aver configurato correttamente i parametri di connessione al database nel file application.properties)

👨‍💻 Autore
Angelo Cannella - Full Stack Engineer
https://github.com/angeloc-dev/




