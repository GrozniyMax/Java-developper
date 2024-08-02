package com.maxim.tbank.translation;

import java.util.Optional;

public interface Language {

    String getCode();

    Optional<String> getName();
}
