package com.basicarch.base.component;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShellResult {
    private final int exitCode;
    private final String stdout;
    private final String stderr;

    public boolean isSuccess() {
        return exitCode == 0;
    }
}
