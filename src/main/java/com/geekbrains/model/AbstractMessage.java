package com.geekbrains.model;

import java.io.Serializable;

public interface AbstractMessage extends Serializable {

    MessageType getMessageType();

}
