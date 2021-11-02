package com.example.nationalpetition.config.validator;

import com.opencsv.CSVReader;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class BadWordValidator implements ConstraintValidator<BadWord, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader("forbidden_list.csv"));
            List<String> badWord = new ArrayList<>();
            String[] line;
            while((line = csvReader.readNext()) != null) {
                badWord.add(line[0]);
            }
            boolean badWordMatch = badWord.stream().anyMatch(word -> value.matches(String.format(".*%s.*", word)));
            return !badWordMatch;
        } catch (Exception e) {
            throw new ValidationException("file을 읽는데 애러가 발생했습니다.");
        }
    }

}
