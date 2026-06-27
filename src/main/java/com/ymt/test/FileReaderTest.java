package com.ymt.test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.github.mertakdut.exception.OutOfPagesException;
import com.github.mertakdut.exception.ReadingException;

public class FileReaderTest {
    public static void main(String[] args) throws IOException, ReadingException, OutOfPagesException {
        String dir = "library";
    
        List<String> fileSet = new ArrayList<>();
    
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileSet.add(path.toString());
                }
            }
        }
        System.out.println(fileSet.get(0));

        //Reader reader = new Reader();
        //reader.setFullContent(fileSet.get(0));
        //reader.setIsIncludingTextContent(true);
        //System.out.println(reader.readSection(50).getSectionTextContent());
    }
}
