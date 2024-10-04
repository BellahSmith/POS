package org.example;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import javax.imageio.ImageIO;
import java.util.Vector;

import static org.example.ObjectRec.detectLabel;

/**
 * Detect motion. This example demonstrates how to use the built-in motion detector
 * and motion listener to fire motion events.
 */
public class Camera implements WebcamMotionListener {

    private static final long COOLDOWN_PERIOD_MS = 2000; // 2 seconds cooldown
    public String result;   // String to store dynamic labels
    private boolean canCapture = true; // Flag to check if capturing is allowed
    private Webcam webcam;
    static Vector<String> items = new Vector<>(); // Vector that stores items scanned
    static Vector<Double> prices = new Vector<>(); //Vector that stores item's prices
    static Double total = 0.0; //Double that stores updating total

    public Camera() {
        webcam = Webcam.getDefault();
        if (webcam == null) {
            System.err.println("No webcam detected");
            System.exit(1);
        }

        WebcamMotionDetector detector = new WebcamMotionDetector(webcam);
        detector.setInterval(200); // Motion check interval (200ms)
        detector.addMotionListener(this);
        detector.start();

    }

    @Override
    public void motionDetected(WebcamMotionEvent wme) {

        if (canCapture) {
            canCapture = false; // Disable capturing until cooldown period is over

            // Open the webcam, capture an image, and save it
            if (webcam.isOpen()) {
                BufferedImage image = webcam.getImage();
                try {
                    //save and override motion detected image
                    File imageFile = new File("image.png");
                    ImageIO.write(image, "PNG", imageFile);

                    // Perform label detection
                    String preresult = detectLabel(
                            imageFile.getAbsolutePath(),
                            "C:/Users/my pc/Downloads/posproject-436820-5b238258265d.json");

                    // Updating prices list and total

                    /* Preresult will be empty if it is not one of the fruits or
                     vegetables in ObjectRec class. Therefore, if it's not empty,
                     preresult set it equal to results and add it to items vector */
                    if (!preresult.isEmpty()) {
                        result = preresult;
                        items.add(result);
                        switch (result){
                            case "Banana":
                                prices.add(0.57);
                                total += 0.57;
                                break;
                            case "Apple":
                                prices.add(1.38);
                                total += 1.38;
                                break;
                            case "Lemon":
                                prices.add(0.89);
                                total += 0.89;
                                break;
                            case "Tomato":
                                prices.add(0.53);
                                total += 0.53;
                                break;
                            case "Onion":
                                prices.add(1.23);
                                total += 1.23;
                                break;
                            case "Potato":
                                prices.add(1.65);
                                total += 1.65;
                                break;
                            case "Squash":
                                prices.add(2.40);
                                total+= 2.40;
                                break;
                            default:
                                break;
                        }
                    } else {
                        System.out.println("No label detected.");
                    }

                } catch (IOException e) {
                    System.err.println("Failed to save image: " + e.getMessage());
                }
            } else {
                System.err.println("Webcam is not open");
            }

            /* TimerTask used for cooldown period, mainly to
            reduce frequency of items being scanned twice */

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    canCapture = true; // Re-enable capturing
                    timer.cancel(); // Cancel the timer
                }
            }, COOLDOWN_PERIOD_MS); // 2 Second cooldown

        }
    }

    public static void main(String[] args) throws IOException {
        //Credentials required for AI model
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "C:/Users/my pc/Downloads/posproject-436820-5b238258265d.json");

        new Camera();
        System.in.read();
        // Keep program open and display detected items
    }
}



