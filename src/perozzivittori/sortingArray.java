package perozzivittori;

import movida.commons.SortingAlgorithm;

public class sortingArray<T extends Comparable<T>> {
	private T[] sortArray;
	
	public sortingArray(T[] a) {
		this.sortArray = a;
	}
	
	public T[] getA() {
		return this.sortArray;
	}
	
	/*
	public void setA(T[] a) {
		this.sortArray = a;
	}
	*/
	
	public void sort(SortingAlgorithm alg) {
		switch (alg) {
			case SelectionSort:	selectionSort();
								break;
								
			case HeapSort:		heapSort();
								break;
								
			default:			System.err.println("SortingAlgorithm or MapImplementation value: invalid");
								System.exit(1);
								break;
		}
	}
	
	/*** SELECTION SORT ***/
	public T[] selectionSort() {
		for (int i=0; i < sortArray.length - 1; i++) {
			int min = i;
			
			for (int j=i+1; j < sortArray.length; j++) {
				if (sortArray[j].compareTo(sortArray[min]) < 0) min = j;
			}
			
			swap(i, min);
		}
		
		return sortArray;
	}
	
	private void swap(int x, int min) {
		T tmp 			= sortArray[x];
		sortArray[x]	= sortArray[min];
		sortArray[min]	= tmp;
	}
	
	/*** HEAP SORT ***/
	public T[] heapSort() {
		T[] A = (T[]) new Comparable[sortArray.length+1];
		System.arraycopy(sortArray, 0, A, 1, sortArray.length);
		T maxE;
		int n = A.length-1; // n = last Heap element index
		heapify(A, n, 1);
		while(n>=1) {
			maxE = findMax(A);
			deleteMax(A, n);
			sortArray[n-1] = maxE;
			n--;
		}
		return sortArray;
	}
	
	private int left(int i) {return 2*i;} 
	private int right(int i) {return 2*i+1;}
	
	private void heapify(T[] A, int n, int i) { // n = last Heap element index 
		if(i > n) return;
		heapify(A, n, left(i));
		heapify(A, n, right(i));
		fixHeap(A, n,i);
	}
	private T findMax(T[] A) {
		return A[1];
	}
	private void fixHeap(T[] A, int n, int i) { // n = last Heap element index 
		int max = left(i); // Sx child
		if(left(i) > n) return;
		if(right(i) <= n && A[left(i)].compareTo(A[right(i)]) < 0)
			max = right(i);
		if(A[i].compareTo(A[max]) < 0) {
			swap(A, i, max);
			fixHeap(A, n, max);
		}
	}
	private void deleteMax(T[] A, int n) {
		if(n <= 0) return;
		A[1] = A[n];
		n--;
		fixHeap(A, n, 1);
	}
	private void swap(T[] A,int i, int max) {
		T tmp 	= A[max];
		A[max] 	= A[i];
		A[i] 	= tmp;
	}
	
}
