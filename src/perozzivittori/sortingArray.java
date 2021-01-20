package perozzivittori;

public class sortingArray<T extends Comparable<T>> {
	private T[] sortArray;
	
	public sortingArray(T[] a) {
		sortArray = a;
	}
	
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
}
