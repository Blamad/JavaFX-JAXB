package controller;

import java.awt.print.Book;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import catalog.ProductCatalog;
import catalog.ProductCatalog.Product;
import catalog.ProductCatalog.Product.Price;

public class ViewController implements Initializable {

	@FXML
	private TableView<Observable> tableView;
	
	@FXML
	private TableColumn name,price,currency;
	
	@FXML
	private TextField field1, field2, field3;
	
	//lista obserwowanych wartosci
	final ObservableList<Observable> data = FXCollections.observableArrayList();
	
	//trzymamy caly uklad xmla
	private ProductCatalog catalog;
	
	private Observable selectedItem;
	
	private String sciezkaDostepu = "src\\catalog\\pliczek.xml";
	private String schemaLocation = "src\\catalog\\baseSchema.xsd";
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//Pobranie wartoœci z xmla i za³adowanie do listy
		load();
		
		tableView.setEditable(true);
		name = new TableColumn("Product Name");
		name.setMinWidth(195);
		price = new TableColumn("Price");
		price.setMinWidth(85);
		currency = new TableColumn("Currency");
		currency.setMinWidth(64);
		
		name.setCellValueFactory(
                new PropertyValueFactory<Observable, String>("name"));
		//Edycja tabeli z miejsca		
		name.setCellFactory(TextFieldTableCell.forTableColumn());
		name.setOnEditCommit(
		    new EventHandler<CellEditEvent<Observable, String>>() {
		        @Override
		        public void handle(CellEditEvent<Observable, String> t) {
		            ((Observable) t.getTableView().getItems().get(
		                t.getTablePosition().getRow())
		                ).setName(t.getNewValue());
		        }
		    }
		);

		price.setCellValueFactory( 
				new PropertyValueFactory<Observable, String>("price"));
		
		//Edycja tabeli z miejsca		
		price.setCellFactory(TextFieldTableCell.forTableColumn());
		price.setOnEditCommit(
		    new EventHandler<CellEditEvent<Observable, String>>() {
		        @Override
		        public void handle(CellEditEvent<Observable, String> t) {
		            ((Observable) t.getTableView().getItems().get(
		                t.getTablePosition().getRow())
		                ).setPrice(t.getNewValue());
		        }
		    }
		);

		currency.setCellValueFactory( 
				new PropertyValueFactory<Observable, String>("currency"));
		
		//Edycja tabeli z miejsca		
		currency.setCellFactory(TextFieldTableCell.forTableColumn());
		currency.setOnEditCommit(
		    new EventHandler<CellEditEvent<Observable, String>>() {
		        @Override
		        public void handle(CellEditEvent<Observable, String> t) {
		            ((Observable) t.getTableView().getItems().get(
		                t.getTablePosition().getRow())
		                ).setCurrency(t.getNewValue());
		        }
		    }
		);


		
		tableView.setItems(data);
		tableView.getColumns().clear();
		tableView.getColumns().addAll(name, price, currency);
		
		tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				selectedItem = (Observable)newValue;
			}
		});		
		
	}

	public void load() {
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance("catalog");
			
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
		    Schema schema = null;
			try {
				schema = sf.newSchema(new File(schemaLocation));
			} catch (SAXException e) {
				System.out.println(e.toString());
			} 
		    
			Unmarshaller um = context.createUnmarshaller();
			um.setSchema(schema);
			um.setEventHandler(new MyValidationHandler());
			catalog = (ProductCatalog) um.unmarshal(new FileReader(sciezkaDostepu));
			ArrayList<Product> list = (ArrayList<Product>) catalog.getProduct();
			for(Product it : list)
				data.add(new Observable(it));
		} catch (FileNotFoundException | JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		writeToXML();
		data.clear();
	}
	
	public void addNew() {
		Product product = new Product();
		product.setName(field1.getText());
		product.setPrice(new Price());
		product.getPrice().setValue(Byte.parseByte(field2.getText()));
		product.getPrice().setCurrency(field3.getText());
		catalog.getProduct().add(product);
		data.add(new Observable(product));
		field1.clear();
		field2.clear();
		field3.clear();
	}
	
	public void removeSelected() {
		catalog.getProduct().remove(selectedItem.getProduct());
		data.remove(selectedItem);
	}
	
	public void writeToXML() {
		
		JAXBContext context = null;
		try {			
			context = JAXBContext.newInstance("catalog");
			
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
	        Schema schema = null;
			try {
				schema = sf.newSchema(new File(schemaLocation));
			} catch (SAXException e) {
				System.out.println(e.toString());
			} 
	        Marshaller marshaller = context.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.setSchema(schema);
	        marshaller.setEventHandler(new MyValidationHandler());
		
			marshaller.marshal(catalog, new File(sciezkaDostepu));
		} catch (JAXBException c) {
			System.out.println(c.toString());
		}
	}
}
