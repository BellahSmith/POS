Hello! This is my first project that I have made using Java and Google Cloud Vision's API. 
This project is connected to my Google Cloud account, meaning that it will not run unless 
the JSON file is changed in the code and in the environmental variables (IntelliJ).

A quick explanation on how the code works:
The Camera class uses a pre-built API that connects to the user's webcam. 
This uses a motion detector that takes a picture every time it detects motion.
The picture is then processed by the AI model in the ObjectRec class. This class
filters through the descriptions that the AI detected and only returns the fruits and
vegetables that were manually added as strings.
This string is then sent back to the Camera class, which is then added to a list of which updates
the price and the total. 
In the POSWindow class, there are premade empty labels that correspond to the price and the item scans.
When an item is scanned the label list is updated. There is also a "remove item" button which removes the last item scanned
and a "Pay now" button which displays a message showing the final total.
