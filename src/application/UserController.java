package application;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import javax.imageio.ImageIO;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


public class UserController {

	String pathIni = "";
	double X;
	double Y;
	String text = "";

	private int iterator = -1;

	private ArrayList<AnchorPane> anchorPaneList = new ArrayList<>();

	@FXML
	private Button backButton;
	@FXML
	private Button nextButton;
	@FXML
	private Button finnishButton;
	@FXML
	private TabPane tabPane;
	@FXML
	private ImageView imagee;

	// eventsen för knapparna
	@FXML
	public void onBack(ActionEvent event) {		
		tabPane.getSelectionModel().selectPrevious();			

	}

	@FXML
	public void onClose(ActionEvent event) {
		Platform.exit();		
	}

	@FXML
	public void onNext(ActionEvent event) {		
		tabPane.getSelectionModel().selectNext();

	}

	@FXML
	public void onFinnish(ActionEvent event) throws BadElementException {
		try {			
			WritableImage wi = new WritableImage(919, 682);
			SnapshotParameters  snapshott = new SnapshotParameters();     

			//---------
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Provresultat.pdf"));
			document.open();

			for(int  i = 0; i < tabPane.getTabs().size(); i++  ){
				tabPane.getSelectionModel().select(i);
				WritableImage snapshot = tabPane.snapshot(snapshott, wi);

				File output = new File("snapshot" + new Date().getTime() + ".png");

				ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);


				document.add(new Paragraph(""));

				//Add Image				
				Image image1 = Image.getInstance(output.getPath());
				document.newPage();
				//Fixed Positioning
				image1.setAbsolutePosition(0f, 100f);
				//Scale to new height and new width of image
				image1.scaleAbsolute(700, 600);
				//Add to document
				document.add(image1);


			}
			document.close();
			writer.close();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("Ditt prov har sparats!");
			alert.showAndWait();
		
		}

		catch (Exception e) {   
			e.printStackTrace();
		}
	}


	@FXML
	public void onFileOpen(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Ini Files", "*.ini"));
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			tabPane.getTabs().clear();
			selectedFile.getPath();
			pathIni = selectedFile.toString();
			readFromTextFile();

		}
	}

	public void readFromTextFile() {

		try (Scanner sc = new Scanner(new File(pathIni))) {
			while (sc.hasNextLine()) {
				String name = sc.nextLine();

				// Sends information to create a combobox on the specified
				// coordinates in the textfile

				if (name.equals("checkbox")) {
					if (sc.hasNextDouble());
					
					{						
						X = sc.nextDouble();						
						Y = sc.nextDouble();						
						text = sc.next();
					}
					AnchorPane pane;					
					pane = anchorPaneList.get(iterator);
					pane.getChildren().add(createCheckbox(X, Y));

				}				
				// Sends information to create a textfield on the specified
				// coordinates in the textfile

				if (name.equals("textfield")) {
					if (sc.hasNextDouble());
					{
						if (sc.hasNextLine());
						X = sc.nextDouble();
						Y = sc.nextDouble();
						text = sc.next();						
					}
					AnchorPane pane;										 
					pane = anchorPaneList.get(iterator);
					pane.getChildren().add(createTextField(X, Y));
				}
				// Sends information to create a textarea on the specified
				// coordinates in the textfile
				if (name.equals("textarea")) {
					if (sc.hasNextDouble());					
					{						
						X = sc.nextDouble();
						Y = sc.nextDouble();
						text = sc.next();						
					}                    
					AnchorPane pane;										 
					pane = anchorPaneList.get(iterator);
					pane.getChildren().add(createTextArea(X, Y));					   					   
				}			
				if (name.equals("textarea2")) {
					if (sc.hasNextDouble());				
					{						
						X = sc.nextDouble();
						Y = sc.nextDouble();						
					}

					AnchorPane pane;										 
					pane = anchorPaneList.get(iterator);
					pane.getChildren().add(createTextArea2(X, Y));

				}

				if (name.equals("tab")) {

					iterator++;
					AnchorPane pane = new AnchorPane();
					anchorPaneList.add(pane);

					text = sc.next();
					text = text.replace("_", " ");
					Tab tab = new Tab(text, pane);			
					tabPane.getTabs().add(tab);

				}				
			}			
		} 

		catch (FileNotFoundException e) {

		}
	}


	// Creates checkbox
	public CheckBox createCheckbox(double x, double y) {	
		text = text.replace("_", " ");
		CheckBox cb = new CheckBox(text);
		cb.setLayoutX(x);
		cb.setLayoutY(y);
		return cb;

	}

	// Creates textfield
	public TextField createTextField(double x, double y) {
		text = text.replace("_", " ");
		TextField tf = new TextField(text);
		tf.setEditable(false);
		tf.setPrefSize(705, 20);
		tf.setLayoutX(x);
		tf.setLayoutY(y);
		return tf;	

	}

	// Creates textarea
	public TextArea createTextArea(double x, double y) {
		text = text.replace("_", " ");
		TextArea ta = new TextArea(text);
		ta.setEditable(false);
		ta.setPrefSize(705, 135);
		ta.setLayoutX(x);
		ta.setLayoutY(y);
		return ta;

	}

	public TextArea createTextArea2(double x, double y) {
		TextArea ta2 = new TextArea();
		ta2.setPrefSize(705, 135);
		ta2.setLayoutX(x);
		ta2.setLayoutY(y);
		return ta2;

	}

}

