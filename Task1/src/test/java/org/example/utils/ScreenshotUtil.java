package org.example.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScreenshotUtil {

    public static void saveScreenshot(WebDriver driver, String fileName) throws IOException {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path dest = Paths.get("src/test/resources/screenshots", fileName + ".png");
        Files.createDirectories(dest.getParent());
        Files.copy(scrFile.toPath(), dest);
    }
}