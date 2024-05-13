package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;


import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Wallet;
import ru.robinhood.TelegramBotRobinHoodCapital.models.xls.DataUserModel;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
public class ShowAllUsers implements Command {

    private final UserController userController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;

    public ShowAllUsers(UserController userController, @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot) {
        this.userController = userController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
    }

    @Override
    public void execute(Message message) {
        Optional<User> userOptional = userController.findByChatId(message.getChatId());

        if (userOptional.isEmpty())
            return;

        User user = userOptional.get();

        if (user.getRole() != Role.ADMIN) {
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    "Команда доступна только администраторам!",
                    null);
            return;
        }

        List<User> users = userController.findByAll();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String fileName = "Список пользователей на %s"
                .formatted(dateTimeFormatter.format(LocalDateTime.now()));

        if (!users.isEmpty()) {
            File file = generateAndCreatedXmlFile(users, fileName);
            robbinHoodTelegramBot.sendDoc(file, fileName, message);
        }


    }


    @SneakyThrows
    private File generateAndCreatedXmlFile(List<User> users, String fileName) {


        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet(fileName);
        File tempFile = File.createTempFile("temp-", ".xls");

        int rowNum = 0;

        Row row = hssfSheet.createRow(rowNum);
        row.createCell(0).setCellValue("ID пользователя");
        row.createCell(1).setCellValue("Имя пользователя");
        row.createCell(2).setCellValue("Имя");
        row.createCell(3).setCellValue("Текущий баланс");
        row.createCell(4).setCellValue("Сумма внесенных денег");
        row.createCell(5).setCellValue("Адрес кошелька");
        row.createCell(6).setCellValue("Пользователей пригласил");
        row.createCell(7).setCellValue("Реферальный код");
        row.createCell(8).setCellValue("Роль");

        HSSFFont font = hssfWorkbook.createFont();
        font.setBold(true);
        font.setColor(Font.COLOR_RED);
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setFont(font);
        for (int i = 0; i < 9; i++) {
            hssfSheet.autoSizeColumn(i);
            row.getCell(i).setCellStyle(style);
        }

        for (User user : users) {
            createSheetHeader(hssfSheet, ++rowNum, createFromUserToDataUserModel(user));
        }

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            hssfWorkbook.write(out);
        }

        return tempFile;
    }

    private static void createSheetHeader(HSSFSheet sheet, int rowNum, DataUserModel dataUserModel) {
        Row row = sheet.createRow(rowNum);

        row.createCell(0).setCellValue(dataUserModel.getId());
        row.createCell(1).setCellValue("@%s".formatted(dataUserModel.getUsername()));
        row.createCell(2).setCellValue(dataUserModel.getName());
        row.createCell(3).setCellValue("%s USD".formatted(dataUserModel.getBalance()));
        row.createCell(4).setCellValue("%s USD".formatted(dataUserModel.getOrigBalance()));
        row.createCell(5).setCellValue(dataUserModel.getAddressWallet());
        row.createCell(6).setCellValue(dataUserModel.getCountInvited());
        row.createCell(7).setCellValue(dataUserModel.getRefCode());
        row.createCell(8).setCellValue(dataUserModel.getRole());
        for (int i = 0; i < 9; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private DataUserModel createFromUserToDataUserModel(User user) {
        Wallet currentWalletUser = user.getWallet();

        String id = String.valueOf(user.getId());
        String username = user.getUsername() == null ? "Не указан" : user.getUsername();
        String name = user.getName();
        String balance;
        String origBalance;
        String addressWallet;
        String countInvited = String.valueOf(userController.findByInvited(user.getChatId()).size());
        String refCode = String.valueOf(user.getChatId());
        String role = user.getRole().toString();

        if (currentWalletUser != null) {
            balance = String.valueOf(((double) currentWalletUser.getBalance()) / 100);
            origBalance = String.valueOf(((double) currentWalletUser.getOrigBalance()) / 100);
            addressWallet = currentWalletUser.getNumberWallet();
        } else {
            balance = "Кошелек не указан";
            origBalance = "Кошелек не указан";
            addressWallet = "Кошелек не указан";
        }

        return DataUserModel.builder()
                .id(id)
                .username(username)
                .name(name)
                .balance(balance)
                .origBalance(origBalance)
                .addressWallet(addressWallet)
                .countInvited(countInvited)
                .refCode(refCode)
                .role(role)
                .build();
    }
}
