package com.geekbrains.client;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.geekbrains.model.AbstractMessage;

import com.geekbrains.model.FileMessage;
import com.geekbrains.model.FileRequest;
import com.geekbrains.model.FilesList;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller implements Initializable {

    public ListView<String> clientFiles;
    public ListView<String> serverFiles;
    private Path baseDir;
    private ObjectDecoderInputStream is;
    private ObjectEncoderOutputStream os;
    @FXML
    private TextField сlientPathField;
    @FXML
    private TextField serverPathField;

    private void read() {
        try {
            while (true) {

                AbstractMessage msg = (AbstractMessage) is.readObject();
                switch (msg.getMessageType()) {
                    case FILE:
                        FileMessage fileMessage = (FileMessage) msg;
                        Files.write(
                                baseDir.resolve(fileMessage.getFileName()),
                                fileMessage.getBytes()
                        );
                        Platform.runLater(() -> fillClientView(getFileNames()));
                        break;
                    case FILES_LIST:
                        FilesList files = (FilesList) msg;
                        Platform.runLater(() -> fillServerView(files.getFiles()));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillServerView(List<String> list) {
        serverFiles.getItems().clear();
        serverFiles.getItems().addAll(list);
    }

    private void fillClientView(List<String> list) {
        clientFiles.getItems().clear();
        clientFiles.getItems().addAll(list);
    }

    private List<FileInfo> getClientFiles() throws IOException {
        return Files.list(baseDir)
                .map(FileInfo::new)
                .collect(Collectors.toList());
    }

    private List<String> getFileNames() {
        try {
            return Files.list(baseDir)
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //baseDir = Paths.get(System.getProperty("user.home"));
            baseDir = Paths.get(System.getProperty("user.dir")).resolve("storage").resolve("Client1");
            clientFiles.getItems().addAll(getFileNames());
            System.out.println("baseDir is: " + baseDir);
            сlientPathField.appendText(String.valueOf(baseDir));

            clientFiles.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    String file = clientFiles.getSelectionModel().getSelectedItem();
                    Path path = baseDir.resolve(file);
                    if (Files.isDirectory(path)) {
                        baseDir = path;
                        fillClientView(getFileNames());
                    }
                }
            });

            Socket socket = new Socket("localhost", 8189);
            os = new ObjectEncoderOutputStream(socket.getOutputStream());
            is = new ObjectDecoderInputStream(socket.getInputStream());
            Thread thread = new Thread(this::read);
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void upload(ActionEvent actionEvent) throws IOException {
        String file = clientFiles.getSelectionModel().getSelectedItem();
        Path filePath = baseDir.resolve(file);
        os.writeObject(new FileMessage(filePath));
    }

    public void download(ActionEvent actionEvent) throws IOException {
        String file = serverFiles.getSelectionModel().getSelectedItem();
        os.writeObject(new FileRequest(file));
    }

    public TextField input;
    public TextArea output;

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        os.writeUTF(input.getText());
    }

    public void sendFile(ActionEvent actionEvent) throws IOException {
    }

    public void getServerPath(ActionEvent actionEvent) throws IOException  {

    }

    public void getClientPath(ActionEvent actionEvent) throws IOException {
        Platform.runLater(() -> fillClientView(getFileNames()));
    }
}
