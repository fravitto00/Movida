# Movida
## Gruppo
Il gruppo di lavoro è formato da:
- Perozzi Marco | marco.perozzi2@studio.unibo.it | (0000933854)
- Vittori Francesco | francesco.vittori5@studio.unibo.it | 0000923487
## Strutture Dati
Abbiamo iniziato lo sviluppo delle strutture dati utilizzando Java Generics e quindi l'interfaccia Comparable.
Notando però che nei metodi dalle interfacce, implementati in MovidaCore, title e name erano di tipo String
abbiamo optato per l'utilizzo di questo tipo. In aggiunta, all'interno del BTree vengono utilizzati degli array
per i record e i figli di ciascun nodo, per efficienza. Un tipo di dato Generics non avrebbe permesso l'allocazione
della memoria per degli array.

Per quanto riguarda, invece, il tipo di dato dell'elemento abbiamo optato per la scelta "generica" del tipo Object.

Le due strutture dati, BTree e Array Ordinato, vengono aggiornate contemporaneamente durante il run-time, nella fase di upload dei dati dal file.

**Array Ordinato**: la scelta è stata quella di utilizzare come struttura un array statico, con lo svantaggio di doverlo ridimensionare ad ogni operazione di modifica: insert, delete. L'alternativa era implementare la struttura come ArrayList. Quest'ultimo permette l'alterazione dinamica della sua struttura ma allo stesso tempo alloca più spazio di quello realmente necessario. Il vantaggio della struttura statica dell'array si misura in termini di allocazione in memoria 

## Algoritmi di Ordinamento
Gli algoritmi di ordinamento sono stati implementati tramite una classe dedicata sortingArray, di cui un'istanza viene creata e istanziata al bisogno nei metodi di MovidaCore, indicando il tipo di dato che l'array deve contenere e riordinare.

## Estensione Grafo
Abbiamo implementato il grafo tramite lista di adiacenza, implementata tramite un HashMap con chiavi di tipo Person (attori) e
come elementi liste di tipo Collaboration (classe utilizzata come archi).
