package com.example.customspayments.service;

import com.example.customspayments.Entity.Payment;
import com.example.customspayments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class FileService {
    public static int status = 0;   // ничего не производилось

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    public void fileToBase(String fileName) {
        File file = new File("BNK1/OUT/" + fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            char[] buffer = new char[157];

            while ((reader.read(buffer, 0, 157)) != -1) {
// Запись
                char[] bf = {buffer[154], buffer[155], buffer[156]};
                String str = new String(bf);

                status = str.equals("0XA") ? 100 : -100;
                if (status == -100) break;
                // 100: данные соответствуют формату записи, кодировка возможно win-1251
                // -100: данные не соответствуют формату записи, либо кодировка не ASCII

// Record Number
                // так как не МЫ его формируем, а получаем от банка
                // поэтому для нас - это входные данные
                // у нас первичный ключ наш - id, формируется автоматически

                char[] recordNumberChar = Arrays.copyOfRange(buffer, 0, 12);

                // ancii: 48-57
                IntPredicate inRange = j -> j >= 48 && j <= 57;
                status = validRange(recordNumberChar, inRange) ? 1 : -1;

                if (status == -1) break;
                String recordNumber = new String(recordNumberChar);

// Payment ID
                char[] paymentIdChar = Arrays.copyOfRange(buffer, 13, 63);

                // ancii: 32 || 45 || 48-57 || 65-90 || 97-122
                inRange = j -> (j == 32) || (j == 45) || (j >= 48 && j <= 57) || (j >= 65 && j <= 90)
                        || (j >= 97 && j <= 122);
                status = (validRange(paymentIdChar, inRange)
                        && (paymentIdChar[0] != ' ')                // первый не должен быть пробелом
                        && validSpace(paymentIdChar)                // внутри paymentIdChar не должно быть пробелов
                        && validOrder(paymentIdChar)) ?             // 8-4-4-4-12
                        2 : -2;

                if (status == -2) break;
                String paymentId = new String(paymentIdChar);

// Company Name
                // ancii: 32-34 || 44-57 || 64-90 || 97-122 || 1040-1103 || 1025 || 1105

                char[] companyNameChar = Arrays.copyOfRange(buffer, 64, 128);

                inRange = j -> (j >= 32 && j <= 34) || (j >= 44 && j <= 57) || (j >= 64 && j <= 90)
                        || (j >= 97 && j <= 122) || (j >= 1040 && j <= 1103) || (j == 1025) || (j == 1105);
                status = (validRange(companyNameChar, inRange)
                        && (companyNameChar[0] != ' ')) ?           // первый не должен быть пробелом
                        3 : -3;

                if (status == -3) break;
                String companyName = new String(companyNameChar);

// Payer INN
                // ancii: 32 || 48-57
                char[] payerINNChar = Arrays.copyOfRange(buffer, 129, 141);

                inRange = j -> (j == 32) || (j >= 48 && j <= 57);
                status = (validRange(payerINNChar, inRange)
                        && (payerINNChar[0] != ' ')                 // первый не должен быть пробелом
                        && validSpace(payerINNChar)) ?              // внутри payerINN не должно быть пробелов
                        4 : -4;

                if (status == -4) break;
                String payerINN = new String(payerINNChar);

// Amount
                // ancii: 46 || 48-57
                char[] amountChar = Arrays.copyOfRange(buffer, 142, 154);

                inRange = j -> (j == 46) || (j >= 48 && j <= 57);
                status = validRange(amountChar, inRange)
                        && validDecimal(amountChar) ?               // точка должна быть одна и дробная часть - 2 цифры
                        5 : -5;

                if (status == -5) break;
                String amount = new String(amountChar);

                paymentRepository.save(new Payment(recordNumber, paymentId, companyName, payerINN, amount, status));
            }

        } catch (IOException ex) {
            status = -1000;    // ошибка входного потока
            System.out.println(ex.getMessage());
            System.out.println("Файл не загружен");
        }
    }


    private static boolean validRange(char[] buff, IntPredicate condition) {
        long count = IntStream.range(0, buff.length)
                .map(i -> (int) buff[i])
                .filter(condition)
                .count();

        return count == buff.length;
    }

    private static boolean validSpace(char[] buff) {
        String str = new String(buff).trim();
        int index = str.indexOf(' ');

        return index == -1;
    }

    private static boolean validOrder(char[] buff) {
        long result = 0;
        long count = IntStream.range(0, buff.length).filter(i -> buff[i] == '-').count();

        if (count == 4) {
            result = Stream.of(8, 13, 18, 23)
                    .filter(i -> buff[i] == '-')
                    .count();
        }

        return result == 4;
    }

    private static boolean validDecimal(char[] buff) {
        if (buff[buff.length - 1] == '.')
            return false;

        long count = IntStream.range(0, buff.length).filter(i -> buff[i] == '.').count();
        if (count == 1) {
            String[] str = new String(buff).split("\\.");

            return str[1].length() == 2;
        } else {
            return false;
        }
    }
}
