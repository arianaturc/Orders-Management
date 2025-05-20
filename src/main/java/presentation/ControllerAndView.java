package presentation;

import businessLogic.ClientBLL;
import businessLogic.LogBLL;
import businessLogic.OrderBLL;
import businessLogic.ProductBLL;
import dataModel.Bill;
import dataModel.Client;
import dataModel.Orders;
import dataModel.Product;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.DateUtil;
import util.TableHelper;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


public class ControllerAndView {

    private BorderPane root;

    private TableView<Client> clientTableView;
    private ObservableList<Client> clientList;

    private ClientBLL clientBLL = new ClientBLL();

    private TextField clientNameField;
    private TextField clientEmailField;
    private TextField clientAddressField;
    private TextField clientAgeField;

    private HBox clientButtonsBox;
    private HBox clientInputForm;
    private Button addClientButton;
    private Button clientInputOkButton;
    private Button clientInputCancelButton;

    private TableView<Product> productTableView;
    private ObservableList<Product> productList;
    private ProductBLL productBLL = new ProductBLL();

    private TextField productNameField;
    private TextField productPriceField;
    private TextField productQuantityField;

    private HBox productButtonsBox;
    private HBox productInputForm;
    private Button addProductButton;
    private Button productInputOkButton;
    private Button productInputCancelButton;

    private Button showClientsButton;
    private Button showProductsButton;
    private HBox topButtonsBox;

    private Button showOrdersButton;
    private TableView<Orders> orderTableView;
    private ObservableList<Orders> orderList;
    private Button addOrderButton;
    private HBox orderButtonsBox;
    private final OrderBLL orderBLL = new OrderBLL();

    private Button billButton;

    public ControllerAndView() {
        clientList = FXCollections.observableArrayList(clientBLL.findAllClients());
        productList = FXCollections.observableArrayList(productBLL.findAllProducts());
        initView();
        loadClients();
    }

    private void initView() {
        root = new BorderPane();
        initClientsPanel();
        initProductsPanel();

        showClientsButton = new Button("Clients");
        showProductsButton = new Button("Products");
        billButton = new Button("Bills");
        topButtonsBox = new HBox(10, showClientsButton, showProductsButton);

        topButtonsBox.setPadding(new Insets(10));

        showClientsButton.setOnAction(e -> showClientsPanel());
        showProductsButton.setOnAction(e -> showProductsPanel());

        root.setTop(topButtonsBox);
        showClientsPanel();

        showOrdersButton = new Button("Orders");
        topButtonsBox.getChildren().add(showOrdersButton);

        topButtonsBox.getChildren().add(billButton);
        billButton.setOnAction(e -> showBill());

        showOrdersButton.setOnAction(e -> showOrdersPanel());
    }

    // ===== CLIENTS PANEL =====
    private void initClientsPanel() {
        clientTableView = new TableView<>();
        clientTableView.setEditable(true);

        TableHelper.populateTableView(clientTableView, clientList, Client.class);

        addClientButton = new Button("Add Client");
        Button editClientButton = new Button("Edit Selected");
        Button deleteClientButton = new Button("Delete Selected");

        addClientButton.setOnAction(e -> showAddClientDialog());
        editClientButton.setOnAction(e -> {
            Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();
            if (selectedClient != null) {
                showEditClientDialog(selectedClient);
            } else {
                showAlert("Select a row to edit.");
            }
        });

        deleteClientButton.setOnAction(e -> deleteSelectedClient());

        clientButtonsBox = new HBox(10, addClientButton, editClientButton, deleteClientButton);
        clientButtonsBox.setPadding(new Insets(10));

        clientNameField = new TextField();
        clientNameField.setPromptText("Name");
        clientEmailField = new TextField();
        clientEmailField.setPromptText("Email");
        clientAddressField = new TextField();
        clientAddressField.setPromptText("Address");
        clientAgeField = new TextField();
        clientAgeField.setPromptText("Age");

        clientInputOkButton = new Button("OK");
        clientInputCancelButton = new Button("Cancel");
        clientInputOkButton.setOnAction(e -> addClientFromInputForm());
        clientInputCancelButton.setOnAction(e -> hideClientInputForm());

        clientInputForm = new HBox(10, clientNameField, clientEmailField, clientAddressField, clientAgeField,
                clientInputOkButton, clientInputCancelButton);
        clientInputForm.setPadding(new Insets(10));
        clientInputForm.setVisible(false);
    }

    private void showEditClientDialog(Client client) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit Client");

        TextField nameField = new TextField(client.getName());
        TextField emailField = new TextField(client.getEmail());
        TextField addressField = new TextField(client.getAddress());
        TextField ageField = new TextField(String.valueOf(client.getAge()));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.addRow(0, new Label("Name:"), nameField);
        grid.addRow(1, new Label("Email:"), emailField);
        grid.addRow(2, new Label("Address:"), addressField);
        grid.addRow(3, new Label("Age:"), ageField);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        HBox buttons = new HBox(10, saveButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox vbox = new VBox(10, grid, buttons);
        vbox.setPadding(new Insets(10));

        saveButton.setOnAction(e -> {
            try {

                String newName = nameField.getText().trim();
                String newEmail = emailField.getText().trim();
                String newAddress = addressField.getText().trim();
                int newAge = Integer.parseInt(ageField.getText().trim());

                if (newName.isEmpty() || newEmail.isEmpty()) {
                    showAlert("Name and Email cannot be empty.");
                    return;
                }

                client.setName(newName);
                client.setEmail(newEmail);
                client.setAddress(newAddress);
                client.setAge(newAge);

                clientBLL.updateClient(client);

                clientTableView.refresh();
                dialog.close();
            } catch (NumberFormatException ex) {
                showAlert("Age must be a valid number.");
            } catch (IllegalArgumentException ex) {
                showAlert("Invalid input: " + ex.getMessage());
            } catch (Exception ex) {
                showAlert("Error updating client: " + ex.getMessage());
            }
        });

        cancelButton.setOnAction(e -> dialog.close());

        Scene scene = new Scene(vbox);
        dialog.setScene(scene);
        dialog.showAndWait();
    }



    private void loadClients() {
        clientList = FXCollections.observableArrayList(clientBLL.findAllClients());
        clientTableView.setItems(clientList);
    }

    private void showClientsPanel() {
        VBox clientPanel = new VBox(10, clientTableView, clientButtonsBox, clientInputForm);
        clientPanel.setPadding(new Insets(10));
        VBox.setVgrow(clientTableView, Priority.ALWAYS);
        root.setCenter(clientPanel);
        loadClients();
    }

    private void showAddClientDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Client");
        dialog.initModality(Modality.APPLICATION_MODAL);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        TextField ageField = new TextField();
        ageField.setPromptText("Age");

        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");

        HBox buttons = new HBox(10, okButton, cancelButton);
        buttons.setPadding(new Insets(10));

        VBox layout = new VBox(10, nameField, emailField, addressField, ageField, buttons);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout);
        dialog.setScene(scene);

        okButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();
            String ageText = ageField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || address.isEmpty() || ageText.isEmpty()) {
                showAlert("Please fill all fields.");
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException ex) {
                showAlert("Age must be a valid number.");
                return;
            }

            Client client = new Client();
            client.setName(name);
            client.setEmail(email);
            client.setAddress(address);
            client.setAge(age);

            try {
                Client inserted = clientBLL.insertClient(client);
                if (inserted != null) {
                    clientList.add(inserted);
                    dialog.close();
                } else {
                    showAlert("Failed to add client.");
                }
            } catch (IllegalArgumentException ex) {
                showAlert(ex.getMessage());
            }
        });

        cancelButton.setOnAction(e -> dialog.close());
        dialog.showAndWait();
    }

    private void addClientFromInputForm() {
        String name = clientNameField.getText().trim();
        String email = clientEmailField.getText().trim();
        String address = clientAddressField.getText().trim();
        String ageText = clientAgeField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || address.isEmpty() || ageText.isEmpty()) {
            showAlert("Please fill all fields.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            showAlert("Age must be a valid number.");
            return;
        }

        Client client = new Client();
        client.setName(name);
        client.setEmail(email);
        client.setAddress(address);
        client.setAge(age);

        Client inserted = clientBLL.insertClient(client);
        if (inserted != null) {
            clientList.add(inserted);
            hideClientInputForm();
        } else {
            showAlert("Failed to add client.");
        }
    }

    private void hideClientInputForm() {
        clientInputForm.setVisible(false);
        clientButtonsBox.setDisable(false);
        clearClientInputFields();
    }

    private void clearClientInputFields() {
        clientNameField.clear();
        clientEmailField.clear();
        clientAddressField.clear();
        clientAgeField.clear();
    }

    private void deleteSelectedClient() {
        Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert("Please select a client to delete.");
            return;
        }

        clientBLL.deleteClient(selectedClient.getId());
        clientList.remove(selectedClient);
    }

    // ===== PRODUCTS PANEL =====
    private void initProductsPanel() {
        productTableView = new TableView<>();
        productTableView.setEditable(true);

        TableHelper.populateTableView(productTableView, productList, Product.class);

        addProductButton = new Button("Add Product");
        Button editProductButton = new Button("Edit Selected");
        Button deleteProductButton = new Button("Delete Selected");

        addProductButton.setOnAction(e -> showAddProductDialog());

        editProductButton.setOnAction(e -> {
            Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                showEditProductDialog(selectedProduct);
            } else {
                showAlert("Select a row to edit.");
            }
        });

        deleteProductButton.setOnAction(e -> deleteSelectedProduct());

        productButtonsBox = new HBox(10, addProductButton, editProductButton, deleteProductButton);
        productButtonsBox.setPadding(new Insets(10));

        productNameField = new TextField();
        productNameField.setPromptText("Name");
        productPriceField = new TextField();
        productPriceField.setPromptText("Price");
        productQuantityField = new TextField();
        productQuantityField.setPromptText("Quantity");

        productInputOkButton = new Button("OK");
        productInputCancelButton = new Button("Cancel");
        productInputOkButton.setOnAction(e -> addProductFromInputForm());
        productInputCancelButton.setOnAction(e -> hideProductInputForm());

        productInputForm = new HBox(10, productNameField, productPriceField, productQuantityField,
                productInputOkButton, productInputCancelButton);
        productInputForm.setPadding(new Insets(10));
        productInputForm.setVisible(false);
    }

    private void showEditProductDialog(Product product) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit Product");

        TextField nameField = new TextField(product.getProduct_name());
        TextField priceField = new TextField(String.valueOf(product.getPrice()));
        TextField quantityField = new TextField(String.valueOf(product.getStock()));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.addRow(0, new Label("Name:"), nameField);
        grid.addRow(1, new Label("Price:"), priceField);
        grid.addRow(2, new Label("Quantity:"), quantityField);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        HBox buttons = new HBox(10, saveButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox vbox = new VBox(10, grid, buttons);
        vbox.setPadding(new Insets(10));

        saveButton.setOnAction(e -> {
            try {
                String newName = nameField.getText().trim();
                double newPrice = Double.parseDouble(priceField.getText().trim());
                int newQuantity = Integer.parseInt(quantityField.getText().trim());

                if (newName.isEmpty()) {
                    showAlert("Name cannot be empty.");
                    return;
                }

                product.setProduct_name(newName);
                product.setPrice(newPrice);
                product.setStock(newQuantity);

                productBLL.updateProduct(product);

                productTableView.refresh();
                dialog.close();
            } catch (NumberFormatException ex) {
                showAlert("Price and Quantity must be valid numbers.");
            } catch (Exception ex) {
                showAlert("Error updating product: " + ex.getMessage());
            }
        });

        cancelButton.setOnAction(e -> dialog.close());

        Scene scene = new Scene(vbox);
        dialog.setScene(scene);
        dialog.showAndWait();
    }


    private void loadProducts() {
        productList = FXCollections.observableArrayList(productBLL.findAllProducts());
        productTableView.setItems(productList);
    }

    private void showProductsPanel() {
        VBox productPanel = new VBox(10, productTableView, productButtonsBox, productInputForm);
        productPanel.setPadding(new Insets(10));
        root.setCenter(productPanel);
        loadProducts();
    }

    private void showAddProductDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Product");
        dialog.initModality(Modality.APPLICATION_MODAL);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");

        HBox buttons = new HBox(10, okButton, cancelButton);
        buttons.setPadding(new Insets(10));

        VBox layout = new VBox(10, nameField, priceField, quantityField, buttons);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout);
        dialog.setScene(scene);

        okButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String priceText = priceField.getText().trim();
            String quantityText = quantityField.getText().trim();

            if (name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                showAlert("Please fill all fields.");
                return;
            }

            double price;
            int quantity;
            try {
                price = Double.parseDouble(priceText);
                quantity = Integer.parseInt(quantityText);
            } catch (NumberFormatException ex) {
                showAlert("Price must be a number and Quantity must be an integer.");
                return;
            }

            Product product = new Product();
            product.setProduct_name(name);
            product.setPrice(price);
            product.setStock(quantity);

            Product inserted = productBLL.insertProduct(product);
            if (inserted != null) {
                productList.add(inserted);
                dialog.close();
            } else {
                showAlert("Failed to add product.");
            }
        });

        cancelButton.setOnAction(e -> dialog.close());
        dialog.showAndWait();
    }

    private void addProductFromInputForm() {
        String name = productNameField.getText().trim();
        String priceText = productPriceField.getText().trim();
        String quantityText = productQuantityField.getText().trim();

        if (name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
            showAlert("Please fill all fields.");
            return;
        }

        double price;
        int quantity;
        try {
            price = Double.parseDouble(priceText);
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            showAlert("Price must be a number and Quantity must be an integer.");
            return;
        }

        Product product = new Product();
        product.setProduct_name(name);
        product.setPrice(price);
        product.setStock(quantity);

        Product inserted = productBLL.insertProduct(product);
        if (inserted != null) {
            productList.add(inserted);
            hideProductInputForm();
        } else {
            showAlert("Failed to add product.");
        }
    }

    private void hideProductInputForm() {
        productInputForm.setVisible(false);
        productButtonsBox.setDisable(false);
        clearProductInputFields();
    }

    private void clearProductInputFields() {
        productNameField.clear();
        productPriceField.clear();
        productQuantityField.clear();
    }

    private void deleteSelectedProduct() {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert("Please select a product to delete.");
            return;
        }
        productBLL.deleteProduct(selectedProduct.getId());
        productList.remove(selectedProduct);
    }

    // ====== ORDERS ======
    private void initOrdersPanel() {
        orderTableView = new TableView<>();
        orderList = FXCollections.observableArrayList(orderBLL.findAllOrders());
        orderTableView.setItems(orderList);

        TableColumn<Orders, Integer> orderIdCol = new TableColumn<>("ID");
        orderIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        TableColumn<Orders, String> clientCol = new TableColumn<>("Client");
        clientCol.setCellValueFactory(cellData -> {
            int clientId = cellData.getValue().getClient_id();
            Client client = clientBLL.findClientById(clientId);
            String clientName = client != null ? client.getName() : "Unknown";
            return new SimpleStringProperty(clientName);
        });

        TableColumn<Orders, String> productCol = new TableColumn<>("Product");
        productCol.setCellValueFactory(cellData -> {
            int productId = cellData.getValue().getProduct_id();
            Product product = productBLL.findProductById(productId);
            String productName = product != null ? product.getProduct_name() : "Unknown";
            return new SimpleStringProperty(productName);
        });

        TableColumn<Orders, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

        TableColumn<Orders, String> dateCol = new TableColumn<>("Order Date");
        dateCol.setCellValueFactory(cellData -> {
            java.sql.Date orderDate = (java.sql.Date) cellData.getValue().getOrder_date();
            String formattedDate = DateUtil.toString(orderDate);
            return new SimpleStringProperty(formattedDate);
        });

        orderTableView.getColumns().addAll(orderIdCol, clientCol, productCol, quantityCol, dateCol);

        addOrderButton = new Button("Add New Order");
        addOrderButton.setOnAction(e -> showAddOrderDialog());
        orderButtonsBox = new HBox(10, addOrderButton);
        orderButtonsBox.setPadding(new Insets(10));
    }

    private void showOrdersPanel() {
        initOrdersPanel();
        VBox orderPanel = new VBox(10, orderTableView, orderButtonsBox);
        orderPanel.setPadding(new Insets(10));
        root.setCenter(orderPanel);
    }

    private void showAddOrderDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Order");
        dialog.initModality(Modality.APPLICATION_MODAL);

        ComboBox<Client> clientComboBox = new ComboBox<>(clientList);
        clientComboBox.setPromptText("Select Client");

        ComboBox<Product> productComboBox = new ComboBox<>(productList);
        productComboBox.setPromptText("Select Product");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Order Date");

        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");

        HBox buttons = new HBox(10, okButton, cancelButton);
        buttons.setPadding(new Insets(10));

        VBox layout = new VBox(10, clientComboBox, productComboBox, quantityField, datePicker, buttons);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout);
        dialog.setScene(scene);

        okButton.setOnAction(e -> {
            Client client = clientComboBox.getValue();
            Product product = productComboBox.getValue();
            String quantityText = quantityField.getText().trim();
            LocalDate selectedDate = datePicker.getValue();

            if (client == null || product == null || quantityText.isEmpty() || selectedDate == null) {
                showAlert("Please fill all fields.");
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityText);
            } catch (NumberFormatException ex) {
                showAlert("Quantity must be a number.");
                return;
            }

            try {
                Orders newOrder = orderBLL.placeOrder(client.getId(), product.getId(), quantity);
                Date utilDate = java.sql.Date.valueOf(selectedDate);
                newOrder.setOrder_date(utilDate);

                orderList.add(newOrder);
                productList.setAll(productBLL.findAllProducts());

                showBillDialog(client, product, quantity, utilDate, product.getPrice() * quantity);

                dialog.close();
            } catch (RuntimeException ex) {
                showAlert(ex.getMessage());
            }
        });
        cancelButton.setOnAction(e -> dialog.close());
        dialog.showAndWait();
    }

    private void showBillDialog(Client client, Product product, int quantity, Date date, double totalPrice) {
        Stage billStage = new Stage();
        billStage.setTitle("Bill");


        StringBuilder billBuilder = new StringBuilder();
        billBuilder.append("******************************\n");
        billBuilder.append("              BILL            \n");
        billBuilder.append("******************************\n");
        billBuilder.append(String.format("Date:        %s\n", new java.text.SimpleDateFormat("dd-MM-yyyy").format(date)));
        billBuilder.append(String.format("Client:      %s\n", client.getName()));
        billBuilder.append(String.format("Product:     %s\n", product.getProduct_name()));
        billBuilder.append(String.format("Quantity:    %d\n", quantity));
        billBuilder.append(String.format("Unit Price:  %.2f\n", product.getPrice()));
        billBuilder.append("------------------------------\n");
        billBuilder.append(String.format("Total:       %.2f\n", totalPrice));
        billBuilder.append("******************************\n");
        billBuilder.append("     Thank you for your order!\n");
        billBuilder.append("******************************");

        Label billLabel = new Label(billBuilder.toString());
        billLabel.setStyle("-fx-font-family: 'monospaced'; -fx-font-size: 16px;");
        billLabel.setAlignment(Pos.TOP_LEFT);

        VBox layout = new VBox(billLabel);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER_LEFT);

        Scene scene = new Scene(layout, 500, 400);
        billStage.setScene(scene);
        billStage.initModality(Modality.APPLICATION_MODAL);
        billStage.showAndWait();
    }

    // ====== LOG ======
    private void showBill() {
        LogBLL logBLL = new LogBLL();
        List<Bill> logs = logBLL.findAllProducts();

        if (logs == null || logs.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Bills");
            alert.setHeaderText("No bills found.");
            alert.setContentText("The log table is empty.");
            alert.showAndWait();
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("===== Bill Summary =====\n\n");

        for (Bill log : logs) {
            sb.append("Order ID     : ").append(log.order_id()).append("\n");
            sb.append("Client Name  : ").append(log.client_name()).append("\n");
            sb.append("Product Name : ").append(log.product_name()).append("\n");
            sb.append("Quantity     : ").append(log.quantity()).append("\n");
            sb.append("Order Date   : ").append(log.order_date()).append("\n");
            sb.append("Total Price  : $").append(String.format("%.2f", log.price())).append("\n");
            sb.append("------------------------------\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bills");
        alert.setHeaderText("Bills:");
        alert.setContentText(sb.toString());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }



    // ====== UTILS ======
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BorderPane getView() {
        return root;
    }
}
