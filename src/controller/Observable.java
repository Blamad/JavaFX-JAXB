package controller;

import javafx.beans.property.SimpleStringProperty;
import catalog.ProductCatalog.Product;



public class Observable {
	
	private final SimpleStringProperty name;
	private final SimpleStringProperty price;
	private final SimpleStringProperty currency;
	private final Product prod;
	
	public Observable(Product product) {
		this.name = new SimpleStringProperty(product.getName());
		this.price = new SimpleStringProperty(String.valueOf(product.getPrice().getValue()));
		this.currency = new SimpleStringProperty(product.getPrice().getCurrency());
		this.prod = product;
	}
	
	public String getName() {
		return name.get();
	}
	
	public void setName(String newName) {
		name.set(newName);
		prod.setName(newName);
	}
	
	public String getPrice() {
		return price.get();
	}
	
	public void setPrice(String newPrice) {
		price.set(newPrice);
		prod.getPrice().setValue(Byte.valueOf(newPrice));
	}
	
	public String getCurrency() {
		return currency.get();
	}
	
	public void setCurrency(String newCurrency) {
		currency.set(newCurrency);
		prod.getPrice().setCurrency(newCurrency);
	}
	
	public Product getProduct() {
		return prod;
	}
}
