package kiul.kiulduelsv2;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseMethods {

    public static Component parseHexToMiniMessage(String message) {

        Component component = parseHexColorHTML(message);
        return component;
    }
    public static String parseLegacyHexToMiniMessage(String message) {
        String text = message;

        // Define the regular expression pattern
        String pattern = "§x(§[0-9a-fA-F]){6}";

        // Compile the pattern
        Pattern regexPattern = Pattern.compile(pattern);

        // Create a matcher object
        Matcher matcher = regexPattern.matcher(text);

        // StringBuffer to store the modified text
        StringBuffer modifiedText = new StringBuffer();

        // Iterate through matches and replace them
        while (matcher.find()) {
            // Extract hex values
            String hexValues = matcher.group().substring(2);
            // Remove leading "§" and concatenate to form hex color

            String hexColor = "<#" + hexValues.replaceAll("§","") + ">";
            // Append the replacement to the modified text

            matcher.appendReplacement(modifiedText, hexColor);
        }

        // Append the remaining part of the text after the last match
        matcher.appendTail(modifiedText);
        // Print the modified text
        String finalText = modifiedText.toString();
        finalText = parseMinecraftColorCodeToMiniMessage(finalText);
        return finalText;
    }

    // Method to replace legacy chat format with hex code

    // Method to replace Minecraft color code &7 with hex code
    public static String parseMinecraftColorCodeToMiniMessage(String text) {
        String pattern = "([&§][a-f0-9])|([&§][klnor])";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(text);
        StringBuffer modifiedText = new StringBuffer();
        while (matcher.find()) {
            String match = matcher.group().toLowerCase();
            String replacement;
            switch (match.charAt(1)) { // Check the second character of the match
                case 'a':
                    replacement = "<#55FF55>";
                    break;
                case 'b':
                    replacement = "<#55FFFF>";
                    break;
                case 'c':
                    replacement = "<#FF5555>";
                    break;
                case 'd':
                    replacement = "<#FF55FF>";
                    break;
                case 'e':
                    replacement = "<#FFFF55>";
                    break;
                case 'f':
                    replacement = "<#FFFFFF>";
                    break;
                case 'k':
                    replacement = "<obf>";
                    break;
                case 'l':
                    replacement = "<bold>";
                    break;
                case 'm':
                    replacement = "<st>";
                    break;
                case 'n':
                    replacement = "<u>";
                    break;
                case 'o':
                    replacement = "<i>";
                    break;
                case 'r':
                    replacement = "<reset>";
                    break;
                case '0':
                    replacement = "<#000000>";
                    break;
                case '1':
                    replacement = "<#0000AA>";
                    break;
                case '2':
                    replacement = "<#00AA00>";
                    break;
                case '3':
                    replacement = "<#00AAAA>";
                    break;
                case '4':
                    replacement = "<#AA0000>";
                    break;
                case '5':
                    replacement = "<#AA00AA>";
                    break;
                case '6':
                    replacement = "<#FFAA00>";
                    break;
                case '7':
                    replacement = "<#AAAAAA>";
                    break;
                case '8':
                    replacement = "<#555555>";
                    break;
                case '9':
                    replacement = "<#5555FF>";
                    break;
                default:
                    replacement = "s";
                    break;
            }
            matcher.appendReplacement(modifiedText, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(modifiedText);
        // Define the hex code corresponding to Minecraft color code &7

        // Replace Minecraft color code &7 with hex code
        return modifiedText.toString();
    }

    public static Component parseHexColorHTML(String message) {
        Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})([^<]*)");
        Matcher matcher = pattern.matcher(message);

        ComponentBuilder builder = Component.text();
        int lastEnd = 0;

        while (matcher.find()) {
            // Append text before the hex color tag
            builder.append(Component.text(message.substring(lastEnd, matcher.start())));

            // Extract hex color and text
            String hexColor = matcher.group(1);
            String text = matcher.group(2);

            // Append colored text
            builder.append(Component.text(text).color(TextColor.fromHexString("#" + hexColor)));

            lastEnd = matcher.end();
        }

        // Append remaining text after the last hex color tag
        if (lastEnd < message.length()) {
            builder.append(Component.text(message.substring(lastEnd)));
        }
        return builder.build();
    }
    public static String parseHexColor(String message) {
        // Pattern to capture hex color code
        ArrayList<String> codes = new ArrayList<>();
        Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        StringBuffer modifiedText = new StringBuffer();

        while (matcher.find()) {
            // Extract hex color
            String hexColor = matcher.group(1);

            // Create replacement string
            String replacement = "<#" + hexColor + ">";
            codes.add(replacement);

            // Append replacement to modifiedText
            matcher.appendReplacement(modifiedText, Matcher.quoteReplacement(replacement));
        }

        // Append remaining text after the last hex color tag
        matcher.appendTail(modifiedText);
        return modifiedText.toString();
    }
    public static String getFinalColor(String message) {
        // Pattern to capture hex color code
        ArrayList<String> codes = new ArrayList<>();
        Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        StringBuffer modifiedText = new StringBuffer();
        String finalColor = "<#FFFFFF>";

        while (matcher.find()) {
            // Extract hex color
            String hexColor = matcher.group(1);

            // Create replacement string
             finalColor = "<#" + hexColor + ">";

        }

        // Append remaining text after the last hex color tag

        return finalColor;
    }
}
