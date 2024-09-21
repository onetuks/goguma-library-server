package com.onetuks.libraryinjector.csv;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class CsvParser {

  private static final char CSV_SEPARATOR = ';';

  public List<CsvBookData> parse(MultipartFile csvFile) {
    List<CsvBookData> bookDatas = new ArrayList<>();
    try (CSVReader reader =
        new CSVReaderBuilder(
                new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))
            .withCSVParser(new CSVParserBuilder().withSeparator(CSV_SEPARATOR).build())
            .build()) {
      String[] line;
      reader.readNext(); // 첫 번째 줄은 헤더이므로 읽어서 버림
      while ((line = reader.readNext()) != null) {
        if (line[0].isBlank()) {
          break;
        }

        CsvBookData csvBookData =
            new CsvBookData(line[0], line[1], line[2], line[3], line[5], line[6]);
        bookDatas.add(csvBookData);
      }
    } catch (Exception e) {
      log.warn("CSV 파일을 읽는 중 오류가 발생했습니다.", e);
    }

    return bookDatas;
  }
}
