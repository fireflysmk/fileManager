package com.geekbrains.server;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Handler implements Runnable {

    private final DataInputStream is;
    private final DataOutputStream os;

    private OutputStream localOut;

    private String serverPath = "E:\\Java\\2021\\geekbrains\\filemanager\\1\\fileManager\\storage\\ServerCommonStorage";
    private String ClientPath;

    public Handler(Socket socket) throws IOException {
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
        System.out.println("Client accepted...");
    }




    @Override
    public void run() {
        try {
            String command = "";
            boolean readByteMode = false;
            while (true) {
             //
                if (!readByteMode) {
                    command = is.readUTF().trim();
                    System.out.println("received command: " + command);
                }

                if (command.contains("ServerPath")) {
                    //command = ServerPath:E:\Java\2021\geekbrains\filemanager\1\fileManager
                    try {
                        String path  = command.substring(command.indexOf(":") + 1);
                        System.out.println(path);

                        File dir = new File(path);
                        File[] arrFiles = dir.listFiles();
                        List<File> lst = Arrays.asList(arrFiles);
                        if (lst.size() == 0) {
                            System.out.println("ServerList:" + "(empty folder)");
                            os.writeUTF("ServerList:" + "(empty folder)");
                        }else {
                            for (File filAndDirList : lst) {
                                System.out.println(filAndDirList.getName());
                                os.writeUTF("ServerList:" + filAndDirList.getName());
                            }
                            System.out.println("ServerPath is: " + path);
                        }

                    } catch (NullPointerException e) {
                       os.writeUTF("Path Not Found, please enter a valid path");
                      // break;
                    }
                   // os.writeUTF("ServerPath is: " + path);
                   // break;
                }
                else if (command.contains("ClientPath")) {
                    //command = ServerPath:E:\Java\2021\geekbrains\filemanager\1\fileManager
                    try {
                        String path  = command.substring(command.indexOf(":") + 1);
                        System.out.println(path);

                        File dir = new File(path);
                        File[] arrFiles = dir.listFiles();
                        List<File> lst = Arrays.asList(arrFiles);
                        if (lst.size() == 0) {
                            os.writeUTF("ClientList:" + "(empty folder)");
                        }
                        else {
                            for (File filAndDirList : lst) {
                                System.out.println(filAndDirList.getName());
                                os.writeUTF("ClientList:" + filAndDirList.getName());
                            }
                            System.out.println("ClientPath is: " + path);
                        }

                    } catch (NullPointerException e) {
                        os.writeUTF("Path Not Found, please enter a valid path");
                        // break;
                    }
                    // os.writeUTF("ServerPath is: " + path);
                    // break;
                }

                else if (command.contains("getFile")) {
                    os.writeUTF("Enter file name...");
                    String fileName = is.readUTF();
                    os.writeUTF("File: " + fileName);
                  //  break;
                }
                else if (command.contains("putFile")) {
                    os.writeUTF("Enter file name");
                    String fileName = is.readUTF();
                    os.writeUTF("Enter file size");
                    long size = is.readLong();
                    os.writeUTF("Upload: file " + fileName + " uploaded, size: " + size);
                 //   break;
                }
                else if (command.contains("getListFiles")) {
                    os.writeUTF("List: {File1, File2, File3}");
                  //  break;
                }
                else  if (command.contains("sendFileFromClient") || readByteMode) {

                    // command example:
                    // "sendFileFromClient someFilename"
                    String fileName = command.split(" ")[1];
                    System.out.println("fileName from client:" + fileName);
                    if (readByteMode) {
                        System.out.println("readByteMode ON");
                        try {
                            localOut = new FileOutputStream(this.serverPath + "\\" + fileName);

                        } catch (Exception ex) {
                            System.out.println("File not found. ");
                        }
                        byte[] bytes = new byte[16*1024];

                        int count;

                        while ((count = is.read(bytes)) > 0) {
                            localOut.write(bytes, 0, count);
                        }
                        readByteMode = false;
                        System.out.println("readByteMode: false");
                    } else {
                        readByteMode = true;
                        System.out.println("readByteMode: true");
                    }

                }
                os.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
