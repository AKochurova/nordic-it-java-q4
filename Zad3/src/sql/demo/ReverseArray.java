package sql.demo;

public class ReverseArray {

    public static void main(String[] args) {
	int[] arr = new int[8];
	for (int i = 0; i<arr.length; i++){
	    arr[i] = i;
        System.out.print(arr[i]);
    }
        System.out.println("");

	arr = mas(arr);
	for (int i = 0; i<arr.length; i++){

	    System.out.print(arr[i]);
	}

    }

    public static int[] mas(int[] m){
        int [] arr2 = new int[m.length];
        int n = 0;
        for (int i = m.length-1; i>0; i--){
            arr2[n] = i;
            n++;
        }
        return arr2;

    }
}
