package com.geekbrains.client;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller implements Initializable {

    private DataInputStream is;
    private DataOutputStream os;
    private Socket socket;
    public TextField input;
    public TextField serverPathField;
    public TextField сlientPathField;
    public TextArea output;
    public TextArea fileListLeft;
    public TextArea fileListRight;

    private String clientPath = "E:\\Java\\2021\\geekbrains\\filemanager\\1\\fileManager\\storage\\Client1";
    private String serverPath = "E:\\Java\\2021\\geekbrains\\filemanager\\1\\fileManager\\storage\\ServerCommonStorage";

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        //input.clear();
        String command = input.getText();
        System.out.println("command:" + command);
        if (command.contains("sendFile")) {
            String fileName = command.split(" ")[1];
            System.out.println("command: sendFile + fileName: " + fileName);
            if (!"".equals(fileName)) {
                sendFile(fileName);
            } else {
                output.appendText("incorrect command, please input correct file Name");
            }
        }
        else {
            System.out.println("send some command");
            os.writeUTF(command);
            input.clear();
        }
    }
/*
    public void sendServerPath(ActionEvent actionEvent) throws IOException {
        fileListLeft.clear();
        os.writeUTF("ServerPath:" + inputServerPath.getText());
        System.out.println("os.writeUTF(\"ServerPath:" + inputServerPath.getText());
    }
 */

    public void getServerPath(ActionEvent actionEvent) throws IOException {
        serverPathField.clear();
        serverPathField.appendText(this.serverPath);
        fileListLeft.clear();

        try {
            System.out.println(serverPath);

            File dir = new File(serverPath);
            File[] arrFiles = dir.listFiles();
            List<File> lst = Arrays.asList(arrFiles);
            if (lst.size() == 0) {
                fileListLeft.appendText("ServerList:" + "(empty folder)" + "\n");
            }
            else {
                for (File filAndDirList : lst) {
                    System.out.println(filAndDirList.getName());
                    fileListLeft.appendText(filAndDirList.getName() + "\n");
                }

            }

        } catch (NullPointerException e) {
            os.writeUTF("Path Not Found, please enter a valid path");
            // break;
        }


    }

    public void getClientPath(ActionEvent actionEvent) throws IOException {
    //    this.clientPath = inputClientPath.getText();
        сlientPathField.clear();
        сlientPathField.appendText(this.clientPath);
        fileListRight.clear();
       // os.writeUTF("ClientPath:" + inputClientPath.getText());
        //command = ServerPath:E:\Java\2021\geekbrains\filemanager\1\fileManager
        try {
            System.out.println(clientPath);

            File dir = new File(clientPath);
            File[] arrFiles = dir.listFiles();
            List<File> lst = Arrays.asList(arrFiles);
            if (lst.size() == 0) {
                fileListRight.appendText("ClientList:" + "(empty folder)" + "\n");
            }
            else {
                for (File filAndDirList : lst) {
                    System.out.println(filAndDirList.getName());
                    fileListRight.appendText(filAndDirList.getName() + "\n");
                }

            }

        } catch (NullPointerException e) {
            os.writeUTF("Path Not Found, please enter a valid path");
            // break;
        }


    }
    public void sendFile(String fileName) {
        try {
            File file = new File(clientPath + "\\" + fileName);
            System.out.println(clientPath + "\\" + fileName);
            long length = file.length();
            byte[] bytes = new byte[16 * 1024];

            InputStream in = new FileInputStream(file);
            os.writeUTF("sendFileFromClient " + fileName);
            int count;
            while ((count = in.read(bytes)) > 0) {
                os.write(bytes, 0, count);
            }
        } catch (NullPointerException e) {
            output.appendText("there`s no file: " + fileName + " in path: " + clientPath + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFile(ActionEvent actionEvent) throws IOException {
        input.clear();
        output.appendText("input file Name, for copy" + "\n");
        //String fileName = input.getText();
        String fileName = "";

        while ("".equals(input.getText())) {
            //waiting input
        }
        System.out.println("input file name: " + fileName);
        if (!"".equals(fileName)) {
            try {
                File file = new File(clientPath + "\\" + fileName);
                System.out.println(clientPath + "\\" + fileName);
                long length = file.length();
                byte[] bytes = new byte[16 * 1024];

                InputStream in = new FileInputStream(file);
                os.writeUTF("switchOnByteMode");
                System.out.println("after switchOnByteMode .....");
                int count;
                while ((count = in.read(bytes)) > 0) {
                    os.write(bytes, 0, count);
                }

            } catch (NullPointerException e) {
                output.appendText("there`s no file: " + fileName + " in path: " + clientPath + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void read() {
        try {
            while (true) {
                String message = is.readUTF();
                String path = message.substring(message.indexOf(":") + 1) + "\n";
                if (message.contains("ServerList")) {
                  //  fileListLeft.clear();
                    fileListLeft.appendText(path);
                } else if (message.contains("ClientList")){
                    fileListRight.appendText(path);;
                } else {
                    output.appendText(message + "\n");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //Socket
            socket = new Socket("localhost", 8188);
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            Thread thread = new Thread(this::read);
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serverPathField(ActionEvent actionEvent) {
    }
}
