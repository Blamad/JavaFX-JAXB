package controller;

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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import catalog.Catalog;
import catalog.Catalog.Product;
import catalog.Catalog.Product.Price;

public class ViewController implements Initializable {

	@FXML
	private TableView<Observable> tableView;
	
	@FXML
	private TableColumn product,price, currency;
	
	@FXML
	private TextField field1, field2, field3;
	
	//lista obserwowanych wartosci
	final ObservableList<Observable> data = FXCollections.observableArrayList();
	
	//trzymamy caly uklad xmla
	private Catalog catalog;
	
	private Observable selectedItem;
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//Pobranie wartoœci z xmla i za³adowanie do listy
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance("catalog");
			Unmarshaller um = context.createUnmarshaller();
			catalog = (Catalog) um.unmarshal(new FileReader("c:\\pliczek.xml"));
			ArrayList<Product> list = (ArrayList<Product>) catalog.getProduct();
			for(Product it : list)
				data.add(new Observable(it));
		} catch (FileNotFoundException | JAXBException e) {
			e.printStackTrace();
		}
		tableView.setEditable(true);
		product = new TableColumn("Product name");
		product.setMinWidth(195);
		price = new TableColumn("Price");
		price.setMinWidth(85);
		currency = new TableColumn("Currency");
		currency.setMinWidth(75);
		
		
		product.setCellValueFactory(
                new PropertyValueFactory<Observable, String>("product"));
		//Edycja tabeli z miejsca		
		product.setCellFactory(TextFieldTableCell.forTableColumn());
		product.setOnEditCommit(
		    new EventHandler<CellEditEvent<Observable, String>>() {
		        @Override
		        public void handle(CellEditEvent<Observable, String> t) {
		            ((Observable) t.getTableView().getItems().get(
		                t.getTablePosition().getRow())
		                ).setTitle(t.getNewValue());
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
		                ).setPages(t.getNewValue());
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
		                ).setPages(t.getNewValue());
		        }
		    }
		);

		
		tableView.setItems(data);
		tableView.getColumns().clear();
		tableView.getColumns().addAll(product, price, currency);
		
		tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				selectedItem = (Observable)newValue;
			}
		});		
		
	}

	public void addNew() {
		Product Product = new Product();
		Product.setName(field1.getText());
		Product.setPrice(new Price());
		Product.getPrice().setValue(field2.getText());
		Product.getPrice().setCurrency(field3.getText());
		catalog.getProduct().add(Product);
		writeToXML();
		data.add(new Observable(Product));
		field1.clear();
		field2.clear();
	}
	
	public void removeSelected() {
		catalog.getProduct().remove(selectedItem.getProduct());
		data.remove(selectedItem);
		writeToXML();
	}
	
	public void writeToXML() {
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance("catalog");
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(catalog, new File("c:\\pliczek.xml"));
		} catch (JAXBException c) {
			System.out.println(c.toString());
		}
	}
	
}
