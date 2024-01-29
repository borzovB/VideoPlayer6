import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class VideoPlayerApp extends Application {

    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private File selectedVideoFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Создаем кнопки
        Button openButton = new Button("Открыть видео");
        Button playButton = new Button("Запустить видео");
        Button stopButton = new Button("Стоп");

        // Создаем объект MediaView
        mediaView = new MediaView();

        // Создаем вертикальный контейнер для размещения кнопок и MediaView
        VBox vbox = new VBox(openButton, playButton, stopButton, mediaView);

        // Назначаем обработчики событий кнопок
        openButton.setOnAction(e -> openVideo(primaryStage));
        playButton.setOnAction(e -> playVideo());
        stopButton.setOnAction(e -> stopVideo());

        // Создаем сцену и добавляем на нее VBox
        Scene scene = new Scene(vbox, 800, 600);

        // Устанавливаем сцену для primaryStage
        primaryStage.setScene(scene);

        // Настройка primaryStage
        primaryStage.setTitle("Видео плеер");
        primaryStage.show();
    }

    private void openVideo(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Видеофайлы", "*.mp4", "*.flv", "*.avi"));
        selectedVideoFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedVideoFile != null) {
            Media media = new Media(selectedVideoFile.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
        }
    }

    private void playVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    private void stopVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void stop() {
        // Сохраняем загруженное видео в папку "storage" перед закрытием приложения
        if (selectedVideoFile != null) {
            File storageFolder = new File("storage");
            storageFolder.mkdirs();
            File destination = new File(storageFolder, selectedVideoFile.getName());

            try {
                Files.copy(selectedVideoFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Освобождаем ресурсы MediaPlayer перед закрытием приложения
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
    }
}
