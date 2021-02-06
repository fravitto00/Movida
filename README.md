# Movida

## Strutture Dati
Abbiamo iniziato lo sviluppo delle strutture dati utilizzando Java Generics e quindi l'interfaccia Comparable.
Notando però che nei metodi dalle interfacce, implementati in MovidaCore, title e name erano di tipo String
abbiamo optato per l'utilizzo di questo tipo. In aggiunta, all'interno del BTree vengono utilizzati degli array
per i record e i figli di ciascun nodo, per efficienza. Un tipo di dato Generics non avrebbe permesso l'allocazione
della memoria per degli array.

Per quanto riguarda, invece, il tipo di dato dell'elemento abbiamo optato per la scelta "generica" del tipo Object.

## Estensione Grafo
/**
	* I vertici sono rappresentati dagli attori e gli archi da Collaboration
	*/
