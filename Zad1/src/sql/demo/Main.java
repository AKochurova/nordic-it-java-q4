package sql.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws Exception{
        Scanner scanner = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите ширину рамки");
        int a = scanner.nextInt();
        System.out.println("Введите длину рамки");
        int b = scanner.nextInt();
        System.out.println("Введите строку");
        String word = reader.readLine();

        //Если длина строки больше ширины рамки выводит "Ошибка"
        if (b < word.length()) {
            System.out.println("Ошибка");
        }
        else {
            //Выводит а строк, состоящих из "*"
            for (int i = 0; i < a; i++) {
                System.out.print("*");
                //Заполняет пробелами все кроме 1го столбца с "*"
                if (i != 0 & i != a - 1) {
                    //Центрирует строку
                    for (int j = 0; j < b - 2; j++) {
                        if (j == (b / 2) - (word.length() / 2)-1 & i == a / 2) {
                            System.out.print(word);

                            j = (b / 2) + (word.length() / 2)-1;


                        }


                        System.out.print(" ");
                    }
                    System.out.print("*");

                }
                //Рисует верхнюю и нижнюю границы
                else {
                    for (int j = 0; j < b - 1; j++) {
                        System.out.print("*");
                    }
                }


                System.out.println("");
            }
        }
    }

}

