package controllers;

import database.BookingDAO;
import model.Booking;
import utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingController {

    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeCombo;
    @FXML private Spinner<Integer> guestsSpinner;
    @FXML private TextArea specialRequestsArea;
    @FXML private Button bookButton;
    @FXML private Button backButton;
    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> dateColumn;
    @FXML private TableColumn<Booking, String> timeColumn;
    @FXML private TableColumn<Booking, Integer> guestsColumn;
    @FXML private TableColumn<Booking, String> statusColumn;

    @FXML
    private void initialize() {
        setupTimeCombo();
        setupGuestsSpinner();
        setupDatePicker();
        loadUserBookings();
    }

    private void setupTimeCombo() {
        // Добавить временные слоты с 10:00 до 22:00
        for (int hour = 10; hour <= 22; hour++) {
            timeCombo.getItems().add(String.format("%02d:00", hour));
            if (hour < 22) {
                timeCombo.getItems().add(String.format("%02d:30", hour));
            }
        }
        timeCombo.setValue("12:00");
    }

    private void setupGuestsSpinner() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 2);
        guestsSpinner.setValueFactory(valueFactory);
    }

    private void setupDatePicker() {
        // Запретить выбор прошедших дат
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        datePicker.setValue(LocalDate.now().plusDays(1));
    }

    private void loadUserBookings() {
        int userId = SessionManager.getInstance().getCurrentUser().getUserId();
        var bookings = BookingDAO.getUserBookings(userId);

        // Настройка колонок таблицы
        dateColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getBookingDate().toString()));
        timeColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getBookingTime().toString()));
        guestsColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getGuestsCount()));
        statusColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(translateStatus(data.getValue().getStatus())));

        bookingsTable.setItems(bookings);
    }

    private String translateStatus(String status) {
        switch (status) {
            case "pending": return "Ожидает подтверждения";
            case "confirmed": return "Подтверждено";
            case "rejected": return "Отклонено";
            case "cancelled": return "Отменено";
            default: return status;
        }
    }

    @FXML
    private void handleBooking() {
        LocalDate date = datePicker.getValue();
        String timeStr = timeCombo.getValue();
        int guests = guestsSpinner.getValue();
        String specialRequests = specialRequestsArea.getText().trim();

        if (date == null || timeStr == null) {
            showAlert(Alert.AlertType.WARNING, "Заполните все поля", "Пожалуйста, выберите дату и время");
            return;
        }

        LocalTime time = LocalTime.parse(timeStr);

        Booking booking = new Booking();
        booking.setUserId(SessionManager.getInstance().getCurrentUser().getUserId());
        booking.setBookingDate(date);
        booking.setBookingTime(time);
        booking.setGuestsCount(guests);
        booking.setSpecialRequests(specialRequests.isEmpty() ? null : specialRequests);

        if (BookingDAO.createBooking(booking)) {
            showAlert(Alert.AlertType.INFORMATION, "Бронирование создано",
                    "Ваше бронирование отправлено на подтверждение администратору");
            clearFields();
            loadUserBookings();
        } else {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось создать бронирование");
        }
    }

    private void clearFields() {
        datePicker.setValue(LocalDate.now().plusDays(1));
        timeCombo.setValue("12:00");
        guestsSpinner.getValueFactory().setValue(2);
        specialRequestsArea.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBack() {
        loadScene("MainPage.fxml", "Главная страница");
    }

    private void loadScene(String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/" + fxmlFile));
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/green-theme.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
