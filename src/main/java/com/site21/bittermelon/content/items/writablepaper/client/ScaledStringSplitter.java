package com.site21.bittermelon.content.items.writablepaper.client;

import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ScaledStringSplitter {
    private final StringSplitter splitter;

    public ScaledStringSplitter(@NotNull Font font) {
        this.splitter = font.getSplitter();
    }

    /**
     * Splits text content into lines with scaling support, calling a consumer for each line segment.
     *
     * @param content the text content to split
     * @param maxWidth the maximum width in pixels before wrapping is required
     * @param style the text style to use for width calculations
     * @param withNewLines whether to include newline characters in the end positions
     * @param consumer callback that receives (style, startPos, endPos, scale) for each line segment
     */
    public void splitLinesWithScaling(@NotNull String content, int maxWidth, Style style, boolean withNewLines,
                                      ScaledLinePosConsumer consumer) {
        // Handle empty content
        if (content.isEmpty()) {
            consumer.accept(style, 0, 0, 1.0f);
            return;
        }

        // Split content into lines at newline characters
        String[] lines = content.split("\n", -1);
        int currentPos = 0;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            boolean hasNewLine = i < lines.length - 1; // Last line has no trailing newline

            float scale = 1.0f;
            String processedLine = line;
            int prefixLength = 0;

            // Parse size multiplier prefixes
            if (line.startsWith("++")) {
                scale = 1.5f;
                processedLine = line.substring(2);
                prefixLength = 2;
            } else if (line.startsWith("--")) {
                scale = 0.5f;
                processedLine = line.substring(2);
                prefixLength = 2;
            } else if (line.startsWith("+")) {
                scale = 1.25f;
                processedLine = line.substring(1);
                prefixLength = 1;
            } else if (line.startsWith("-")) {
                scale = 0.75f;
                processedLine = line.substring(1);
                prefixLength = 1;
            }

            // Calculate width limit based on scaling factor
            int adjustedMaxWidth = (int) (maxWidth / scale);

            // Check if line fits without wrapping
            if (processedLine.isEmpty() || splitter.stringWidth(processedLine) <= adjustedMaxWidth) {
                int endPos = currentPos + line.length();
                // Add newline to end position if needed
                if (hasNewLine && withNewLines) endPos++;
                consumer.accept(style, currentPos, endPos, scale);
                currentPos = endPos;
                // Move past newline even if not including it
                if (hasNewLine && !withNewLines) currentPos++;
            } else {
                // Line is too long, need to wrap it
                List<Integer> breakPoints = new ArrayList<>();
                breakPoints.add(0); // Start of line

                // Find where to break the line
                int lastBreak = 0;
                while (lastBreak < processedLine.length()) {
                    int nextBreak = splitter.findLineBreak(
                            processedLine.substring(lastBreak),
                            adjustedMaxWidth,
                            style
                    );
                    if (nextBreak <= 0) break; // No more breaks found

                    int absoluteBreak = lastBreak + nextBreak;

                    // Skip spaces at the break point
                    while (absoluteBreak > lastBreak &&
                            absoluteBreak < processedLine.length() &&
                            processedLine.charAt(absoluteBreak) == ' ') {
                        absoluteBreak++;
                    }

                    lastBreak = absoluteBreak;
                    breakPoints.add(lastBreak);
                }

                // Add end of line if there's leftover text
                if (lastBreak < processedLine.length()) {
                    breakPoints.add(processedLine.length());
                }

                // Call consumer for each wrapped line piece
                for (int j = 0; j < breakPoints.size() - 1; j++) {
                    int lineStart = currentPos + prefixLength + breakPoints.get(j);
                    int lineEnd = currentPos + prefixLength + breakPoints.get(j + 1);

                    // Add newline to last piece if needed
                    boolean isLastSubline = j == breakPoints.size() - 2;
                    if (isLastSubline && hasNewLine && withNewLines) {
                        lineEnd++;
                    }

                    consumer.accept(style, lineStart, lineEnd, scale);
                }

                // Move past the whole original line
                currentPos += line.length();
                if (hasNewLine) currentPos++;
            }
        }
    }

    @FunctionalInterface
    public interface ScaledLinePosConsumer {
        void accept(Style style, int startPos, int endPos, float scale);
    }
}
