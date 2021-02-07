# Movida
## Gruppo
Il gruppo di lavoro è formato da:
- Perozzi Marco | (marco.perozzi2@studio.unibo.it) | (0000933854)
- Vittori Francesco | francesco.vittori5@studio.unibo.it | 0000923487
## Strutture Dati
Abbiamo iniziato lo sviluppo delle strutture dati utilizzando Java Generics e quindi l'interfaccia Comparable.
Notando però che nei metodi dalle interfacce, implementati in MovidaCore, title e name erano di tipo String
abbiamo optato per l'utilizzo di questo tipo. In aggiunta, all'interno del BTree vengono utilizzati degli array
per i record e i figli di ciascun nodo, per efficienza. Un tipo di dato Generics non avrebbe permesso l'allocazione
della memoria per degli array.

Per quanto riguarda, invece, il tipo di dato dell'elemento abbiamo optato per la scelta "generica" del tipo Object.

Le due strutture dati, BTree e Array Ordinato, vengono aggiornate contemporaneamente durante il run-time, ... 

## Estensione Grafo
Abbiamo implementato il grafo tramite lista di adiacenza, implementata tramite un HashMap con chiavi di tipo Person (attori) e
come elementi liste di tipo Collaboration (classe utilizzata come archi).
