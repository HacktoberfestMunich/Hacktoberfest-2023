import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Random;

public class ColorSpectrum {

    private static final String host = "127.0.0.1";
    private static final int port = 1234;
    private static final List<String> colors = List.of("00ff00", "e28211", "ff0000", "99b259", "0000ff");
    private static final int xOffset = 10;
    private static final int yOffset = 10;
    private static final int cubeSize = 5;

    public static void main(String[] args) throws IOException, InterruptedException {

        try (final Socket s = new Socket(host, port)) {

            System.out.println("Connected to Pixelflut server.");
            final PrintWriter writer = new PrintWriter(s.getOutputStream());
            final InputStreamReader streamReader = new InputStreamReader(s.getInputStream());
            final BufferedReader reader = new BufferedReader(streamReader);

            writer.println("SIZE");
            writer.flush();
            final String[] answer = reader.readLine().split(" ");
            System.out.printf("Detected size: %s x %s%n", answer[1], answer[2]);

            int xFieldWidth = (Integer.parseInt(answer[1]) / 3) / 2;
            int yFieldWidth = (Integer.parseInt(answer[2]) / 3) / 2;

             System.out.printf("Our field size: %s x %s%n", xFieldWidth, yFieldWidth);

            System.out.println("Painting...");
            StringBuilder out = new StringBuilder();
            for (int x = 725; x < 725 + xFieldWidth; x++) {
                Random random = new Random();
                int nextInt = random.nextInt(0xffffff + 1);
                String colorCode = String.format("#%06x", nextInt);
                for (int y = 5; y < 5 + yFieldWidth; y++) {
                    out.append(String.format("PX %d %d %s\n", x, y, colorCode.replace("#", "")));
                    System.out.printf("Paining pixel at %s x %s%n", x, y);
                }
            }
            writer.print(out);
            writer.flush();
        }
    }
}
