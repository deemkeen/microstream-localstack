package de.eemkeen.handler;

public interface MessageHandler<T> {
    void handle(T message);
}
