package it.uniroma2.dicii.ispw.supportdesk.dao.file;

import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

final class CsvFileStore {

    private CsvFileStore() {}

    static List<String> readLines(String filePath) throws DAOException {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return lines;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) lines.add(line);
            }
        } catch (IOException e) {
            throw new DAOException("Errore lettura file: " + filePath, e);
        }
        return lines;
    }

    static void writeLines(String filePath, List<String> lines) throws DAOException {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, false), StandardCharsets.UTF_8))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new DAOException("Errore scrittura file: " + filePath, e);
        }
    }

    static void appendLine(String filePath, String line) throws DAOException {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, true), StandardCharsets.UTF_8))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            throw new DAOException("Errore append file: " + filePath, e);
        }
    }
}
