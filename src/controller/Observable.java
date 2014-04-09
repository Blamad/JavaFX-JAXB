package controller;

import javafx.beans.property.SimpleStringProperty;
import catalog.Catalog.Product;



public class Observable {
	
	private final SimpleStringProperty name;
	private final SimpleStringProperty price;
	private final SimpleStringProperty currency;
	private Product product;
	
	public Observable(Product product) {
		this.product = product;
		this.name = new SimpleStringProperty(product.getName());
		this.price = new SimpleStringProperty(product.getPrice().getValue());
		this.currency = new SimpleStringProperty(product.getPrice().getCurrency());
	}
	
	public String getName() {
		return name.get();
	}
	
	public void setTitle(String newTitle) {
		name.set(newTitle);
		product.setName(newTitle);
	}
	
	public String getPages() {
		return price.get();
	}
	
	public void setPages(String newPages) {
		price.set(newPages);
		product.getPrice().setValue(newPages);
	}

	public String getCurrency() {
		return currency.get();
	}
	
	public void setCurrency(String newPages) {
		price.set(newPages);
		product.getPrice().setCurrency(newPages);
	}

	
	public Product getProduct() {
		return product;
	}
}
