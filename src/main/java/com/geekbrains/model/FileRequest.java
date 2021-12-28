package com.geekbrains.model;

import lombok.Data;

//import lombok.Data;
@Data
public class FileRequest implements AbstractMessage {

    public String getFileName() {
        return fileName;
    }

    private final String fileName;

    public FileRequest(String file) {
        this.fileName = file;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.FILE_REQUEST;
    }
}
