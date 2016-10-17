import java.util.Scanner;

/**
 * Created by LuoLiangchen on 16/9/1.
 */
public class Main {
    public static void main(String[] args) {
        String polygon = "0,0;1,0;2,1;1,1;1,2;2,3;-1,1";
        String position;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            position = scanner.nextLine();
            System.out.println(GeoFencingUtils.isInPolygon(position, polygon) ? "IN" : "OUT");
            System.out.println();
        }
    }

}
