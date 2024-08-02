package com.maxim.tbank.database;

import com.maxim.tbank.database.exception.DatabaseException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
@Log
public class DatabaseManager {

    @Autowired
    private Connection jdbcConnection;



    public void save(RequestDao request) {
        try(PreparedStatement statement = jdbcConnection.prepareStatement("INSERT INTO request(user_ip) values (?::inet) RETURNING id")) {
            statement.setObject(1,request.getIp());
            var result = statement.executeQuery();
            result.next();
            request.setId(result.getLong("id"));
            saveWord(request);
            log.info("Saved request with id " + request.getId());
        } catch (SQLException e) {
            log.warning("Failed to save request with id " + request.getId() + "because " + e.getMessage());
        }
    }

    public Optional<String> searchForTranslation(String text, String sourceLanguage, String targetLanguage) {
        try (PreparedStatement statement = jdbcConnection
                .prepareStatement("SELECT * FROM word WHERE ((source_language=? AND target_language=?) OR (source_language=? AND target_language=?)) AND (original_word=? OR translated_word=?)")){

            statement.setString(1,sourceLanguage);
            statement.setString(2,targetLanguage);
            statement.setString(3,targetLanguage);
            statement.setString(4,sourceLanguage);
            statement.setString(5,text);
            statement.setString(6,text);

            var result = statement.executeQuery();

            String originalWord;
            String translatedWord;
            while (result.next()) {
                originalWord = result.getString("original_word");
                translatedWord = result.getString("translated_word");
                if (originalWord.equals(text)) {
                    return Optional.of(translatedWord);
                } else if (translatedWord.equals(text)) {
                    return Optional.of(originalWord);
                }
            }
        } catch (SQLException e) {
            log.warning("Failed to search for translation with text " + text + "because " + e.getMessage());
        }
        return Optional.empty();

    }

    private void saveWord(RequestDao requestDao){
        try (PreparedStatement statement = jdbcConnection.prepareStatement("INSERT INTO word values (?, ?, ?, ?, ?)")){
            jdbcConnection.setAutoCommit(false);
            for (var entry: requestDao.getTranslation().entrySet()){
                statement.setLong(1, requestDao.getId());
                statement.setString(2, entry.getKey());
                statement.setString(3, entry.getValue());
                statement.setString(4, requestDao.getOriginLanguage());
                statement.setString(5, requestDao.getDestinationLanguage());
                statement.addBatch();
            };
            int[] result = statement.executeBatch();
            jdbcConnection.commit();
            jdbcConnection.setAutoCommit(true);
        } catch (SQLException e) {
            log.warning("Failed to save word because " + e.getMessage());
        }
    }
}
