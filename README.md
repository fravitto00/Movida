# Movida

## Gruppo
Il gruppo di lavoro Ã¨ formato da:
- Perozzi Marco | marco.perozzi2@studio.unibo.it | 0000933854
- Vittori Francesco | francesco.vittori5@studio.unibo.it | 0000923487

## Ambito di lavoro
Lo sviluppo dell'applicativo Ã¨ avvenuto su sistema operativo *Windows*. L'IDE scelto per la scrittura del codice Ã¨ *Eclipse*. La versione di Java utilizzata Ã¨ *jdk-9.0.1* e la codifica dei caratteri Ã¨ *UTF-8* (inclusione di tutti i caratteri accentati nella normalizzazione delle stringhe)

## Strutture Dati
Abbiamo iniziato lo sviluppo delle strutture dati utilizzando Java Generics e quindi l'interfaccia Comparable. Notando perÃ² che nei metodi dalle interfacce, implementati in MovidaCore, title e name fossero di tipo String abbiamo optato per l'utilizzo di questo tipo. In aggiunta, all'interno del BTree vengono utilizzati degli array per i record e i figli di ciascun nodo, per efficienza. Un tipo di dato Generics non avrebbe permesso l'allocazione della memoria per degli array.

Per quanto riguarda, invece, il tipo di dato dell'elemento abbiamo optato per la scelta "generica" del tipo Object.

Le due strutture dati, BTree e Array Ordinato, vengono aggiornate contemporaneamente durante il run-time, per quanto riguarda l'aggiunta o la cancellazione di record. Per tutte le altre operazioni viene utilizzata la struttura scelta tramite costruttore o setMap.
Inoltre, implementano l'interfaccia Dizionario che definisce i tre metodi di: ricerca, inserimento e cancellazione di un record.

**Array Ordinato**: la scelta Ã¨ stata quella di utilizzare come struttura un array statico, con lo svantaggio di doverlo ridimensionare ad ogni operazione di modifica: insert, delete. L'alternativa era implementare la struttura come ArrayList. Quest'ultimo permette l'alterazione dinamica della sua struttura ma allo stesso tempo alloca piÃ¹ spazio di quello realmente necessario. Il vantaggio della struttura statica dell'array si misura in termini di allocazione in memoria.

## Algoritmi di Ordinamento
Gli algoritmi di ordinamento sono stati implementati come metodi di una classe dedicata sortingArray, di cui un'istanza viene creata e istanziata al bisogno nei metodi di MovidaCore, indicando il tipo di dato che l'array deve contenere e riordinare, tramite Generics. L'algoritmo utilizzato Ã¨ definito dal costruttore o dal metodo setSort.

## Estensione Grafo
Abbiamo implementato il grafo tramite lista di adiacenza, implementata tramite un HashMap con chiavi di tipo Person (attori) e come elementi liste di tipo Collaboration (classe utilizzata come archi).

Ogni vertice ha una lista degli archi ad esso adiacenti, perciÃ² uno stesso arco Ã¨ inserito nella lista di entrambi i vertici che collega. Quando questo spigolo viene creato Ã¨ istanziato un oggetto Collaboration e la stessa istanza Ã¨ aggiunta a entrambe le liste. In questo modo ogni operazione eseguita su un arco Ã¨ duplicata anche per l'istanza di quello stesso arco memorizzata nella lista di adiacenza del suo vertice opposto.

## Maximum Spanning Tree
La questione era quella di emulare una struttura Union Find per controllare che 2 nodi del grafo, collegati dall'arco in atto di verifica, non appartenessero giÃ  allo stesso albero (facendo quindi parte di insiemi diversi). La struttura Ã¨ stata costruita come una lista di liste, dove le liste interne rappresentano gli insiemi di vertici del grafo giÃ  collegati tra loro, in modo diretto o indiretto. In avvio del suddetto metodo, ogni nodo appartiene a un insieme contenente solo se stesso, e in corso di esecuzione queste liste interne vanno fondendosi fino ad arrivare ad un unico insieme contenente tutti i nodi interpellati. 

## Modifica Classi movida.commons
Le uniche due classi a cui sono state apportate modifiche sono Collaboration e Person: nella prima sono stati aggiunti metodi per l'aggiunta, la cancellazione di un Movie e per la restituzione del numero di Movie contenuti; nella seconda è stata effettuata la sovrascrittura del metodo equals di Object, e con sé hashCode, per effettuare confronti corretti tra oggetti con gli stessi valori ma hashCode diversi, dati da una nuova istanziazione dell'oggetto da confrontare con quelli presenti nelle strutture dati/grafo.
