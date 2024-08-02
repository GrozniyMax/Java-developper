DROP TABLE IF EXISTS word;
DROP TABLE IF EXISTS request;

CREATE TABLE request(
    id BIGSERIAL PRIMARY KEY,
    user_ip inet NOT NULL

);
CREATE TABLE word(
    request_id BIGINT REFERENCES request(id),
    -- Тип VARCHAR без ограничения, т.к.  самое длинное слово сейчас - 189,000 букв
    original_word VARCHAR NOT NULL,
    translated_Word VARCHAR NOT NULL,
    source_language VARCHAR NOT NULL,
    target_language VARCHAR NOT NULL
);