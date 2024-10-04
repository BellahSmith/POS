package org.example;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjectRec {
    public static void main(String[] args) throws IOException {
        // Path to the image file
        String imagePath = "C:/Users/my pc/IdeaProjects/POS/image.png";
        //credentials required for AI model
        String preresult = detectLabel(imagePath, "C:/Users/my pc/Downloads/posproject-436820-5b238258265d.json\"");

    }

    public static String detectLabel(String imagePath, String credentialsPath) throws IOException {
        StringBuilder result = new StringBuilder();  // Use StringBuilder for appending multiple labels

        // Create a client
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

            // Reads the image file into memory
            ByteString imgBytes = ByteString.readFrom(new FileInputStream(imagePath));

            // Builds the image annotation request
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            List<AnnotateImageRequest> requests = new ArrayList<>();
            requests.add(request);

            // Performs label detection on the image file
            List<AnnotateImageResponse> responses = vision.batchAnnotateImages(requests).getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s%n", res.getError().getMessage());
                    return "";
                }

                // Process each label description
                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    String description = annotation.getDescription();

                    /* Switch based on detected labels
                    AI model tends to detect specific fruits or vegetables,
                     therefore, using a switch case to simplify labels.
                     Ex: Scanned description Meyer lemon's result will be "Lemon"
                    printing label mainly for debugging*/
                    switch (description) {
                        case "Banana":
                            System.out.println("Detected Banana");
                            result.append("Banana ");
                            break;
                        case "Apple", "Carmine":
                            System.out.println("Detected Apple");
                            result = new StringBuilder("Apple");
                            break;
                        case "Lemon", "Sweet lemon", "Meyer lemon", "Citron", "Citric Acid":
                            System.out.println("Detected Lemon");
                            result = new StringBuilder("Lemon");
                            break;
                        case "Tomato", "Bush tomato", "Cherry tomato", "Peach", "Plum tomato":
                            System.out.println("Detected Tomato");
                            result = new StringBuilder("Tomato");
                            break;
                        case "Onion", "Yellow onion":
                            System.out.println("Detected Onion");
                            result = new StringBuilder("Onion");
                            break;
                        case "Potato":
                            System.out.println("Detected Potato");
                            result.append("Potato");
                            break;
                        case "Squash", "Brussels sprout":
                            System.out.println("Detected Squash");
                            result = new StringBuilder("Squash");;
                            break;
                        default:
                            System.out.println("Detected: " + description);
                            break;
                    }
                }
            }
        }

        return result.toString().trim(); // Return the detected labels as a single string
    }
}
