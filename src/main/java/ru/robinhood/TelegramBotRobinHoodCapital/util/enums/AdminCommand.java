package ru.robinhood.TelegramBotRobinHoodCapital.util.enums;

public enum AdminCommand {

    AUTH_ADMIN("/authadmin"),
    ADMIN_PANEL("/adminpanel"),
    ADMIN_SEND_VIDEO_ALL("/sendvideo"),
    ADMIN_SEND_PHOTO_ALL("/sendphoto"),
    ADMIN_SEND_MESSAGE_ALL("/sendmessage"),
    CREATE_START_TEXT("/create_start_text"),
    CREATE_START_PHOTO("/create_start_photo"),
    CREATE_START_VIDEO("/create_start_video");



    private final String cmd;

    AdminCommand(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return cmd;
    }

}
