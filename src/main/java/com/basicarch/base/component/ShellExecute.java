package com.basicarch.base.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ShellExecute {

    private static final long DEFAULT_TIMEOUT_SECONDS = 60;

    public ShellResult execute(List<String> command) {
        return execute(command, DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * @param command        커맨드
     * @param timeoutSeconds 대기시간
     * @return
     */
    public ShellResult execute(List<String> command, long timeoutSeconds) {
        log.info("Shell execute: {}", command);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(false);

            Process process = processBuilder.start();

            StringBuilder stdout = new StringBuilder();
            StringBuilder stderr = new StringBuilder();

            Thread stdoutThread = streamThread(process.getInputStream(), stdout);
            Thread stderrThread = streamThread(process.getErrorStream(), stderr);

            stdoutThread.start();
            stderrThread.start();

            boolean completed = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            stdoutThread.join(1000);
            stderrThread.join(1000);

            // 타임아웃 초과 시 프로세스 강제 종료
            if (!completed) {
                process.destroyForcibly();
                return ShellResult.builder()
                        .exitCode(-1)
                        .stdout(stdout.toString())
                        .stderr(String.format("프로세스 타임아웃 %s 초", timeoutSeconds))
                        .build();
            }

            int exitCode = process.exitValue();
            if (exitCode != 0) {
                log.error("Shell failed (exit={}): {}", exitCode, command);
            } else {
                log.info("Shell success: {}", command);
            }

            return ShellResult.builder()
                    .exitCode(exitCode)
                    .stdout(stdout.toString())
                    .stderr(stderr.toString())
                    .build();

        } catch (IOException e) {
            log.error("Shell IOException: {}", command, e);
            return ShellResult.builder()
                    .exitCode(-1)
                    .stdout("")
                    .stderr(e.getMessage())
                    .build();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Shell InterruptedException: {}", command, e);
            return ShellResult.builder()
                    .exitCode(-1)
                    .stdout("")
                    .stderr(e.getMessage())
                    .build();
        }
    }

    private Thread streamThread(InputStream inputStream, StringBuilder buffer) {
        return new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("[Shell] {}", line);
                    buffer.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                log.error("[Shell] stream read error", e);
            }
        });
    }
}
